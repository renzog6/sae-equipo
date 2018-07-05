/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.service;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Empresa;
import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.service.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        if (pedido.getRepuestoList() == null) {
            pedido.setRepuestoList(new ArrayList<Repuesto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = pedido.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                pedido.setEmpresa(empresa);
            }
            List<Repuesto> attachedRepuestoList = new ArrayList<Repuesto>();
            for (Repuesto repuestoListRepuestoToAttach : pedido.getRepuestoList()) {
                repuestoListRepuestoToAttach = em.getReference(repuestoListRepuestoToAttach.getClass(), repuestoListRepuestoToAttach.getIdRepuesto());
                attachedRepuestoList.add(repuestoListRepuestoToAttach);
            }
            pedido.setRepuestoList(attachedRepuestoList);
            em.persist(pedido);
            if (empresa != null) {
                empresa.getPedidoList().add(pedido);
                empresa = em.merge(empresa);
            }
            for (Repuesto repuestoListRepuesto : pedido.getRepuestoList()) {
                repuestoListRepuesto.getPedidoList().add(pedido);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdPedido());
            Empresa empresaOld = persistentPedido.getEmpresa();
            Empresa empresaNew = pedido.getEmpresa();
            List<Repuesto> repuestoListOld = persistentPedido.getRepuestoList();
            List<Repuesto> repuestoListNew = pedido.getRepuestoList();
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                pedido.setEmpresa(empresaNew);
            }
            List<Repuesto> attachedRepuestoListNew = new ArrayList<Repuesto>();
            for (Repuesto repuestoListNewRepuestoToAttach : repuestoListNew) {
                repuestoListNewRepuestoToAttach = em.getReference(repuestoListNewRepuestoToAttach.getClass(), repuestoListNewRepuestoToAttach.getIdRepuesto());
                attachedRepuestoListNew.add(repuestoListNewRepuestoToAttach);
            }
            repuestoListNew = attachedRepuestoListNew;
            pedido.setRepuestoList(repuestoListNew);
            pedido = em.merge(pedido);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getPedidoList().remove(pedido);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getPedidoList().add(pedido);
                empresaNew = em.merge(empresaNew);
            }
            for (Repuesto repuestoListOldRepuesto : repuestoListOld) {
                if (!repuestoListNew.contains(repuestoListOldRepuesto)) {
                    repuestoListOldRepuesto.getPedidoList().remove(pedido);
                    repuestoListOldRepuesto = em.merge(repuestoListOldRepuesto);
                }
            }
            for (Repuesto repuestoListNewRepuesto : repuestoListNew) {
                if (!repuestoListOld.contains(repuestoListNewRepuesto)) {
                    repuestoListNewRepuesto.getPedidoList().add(pedido);
                    repuestoListNewRepuesto = em.merge(repuestoListNewRepuesto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = pedido.getIdPedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            Empresa empresa = pedido.getEmpresa();
            if (empresa != null) {
                empresa.getPedidoList().remove(pedido);
                empresa = em.merge(empresa);
            }
            List<Repuesto> repuestoList = pedido.getRepuestoList();
            for (Repuesto repuestoListRepuesto : repuestoList) {
                repuestoListRepuesto.getPedidoList().remove(pedido);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pedido findPedido(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
