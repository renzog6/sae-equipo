/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Empresa;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class RepuestoJpaController implements Serializable {

    public RepuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Repuesto repuesto) {
        if (repuesto.getEmpresaList() == null) {
            repuesto.setEmpresaList(new ArrayList<Empresa>());
        }
        if (repuesto.getEquipoModeloList() == null) {
            repuesto.setEquipoModeloList(new ArrayList<EquipoModelo>());
        }
        if (repuesto.getPedidoList() == null) {
            repuesto.setPedidoList(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : repuesto.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getIdEmpresa());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            repuesto.setEmpresaList(attachedEmpresaList);
            List<EquipoModelo> attachedEquipoModeloList = new ArrayList<EquipoModelo>();
            for (EquipoModelo equipoModeloListEquipoModeloToAttach : repuesto.getEquipoModeloList()) {
                equipoModeloListEquipoModeloToAttach = em.getReference(equipoModeloListEquipoModeloToAttach.getClass(), equipoModeloListEquipoModeloToAttach.getIdModelo());
                attachedEquipoModeloList.add(equipoModeloListEquipoModeloToAttach);
            }
            repuesto.setEquipoModeloList(attachedEquipoModeloList);
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : repuesto.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getIdPedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            repuesto.setPedidoList(attachedPedidoList);
            em.persist(repuesto);
            for (Empresa empresaListEmpresa : repuesto.getEmpresaList()) {
                empresaListEmpresa.getRepuestoList().add(repuesto);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            for (EquipoModelo equipoModeloListEquipoModelo : repuesto.getEquipoModeloList()) {
                equipoModeloListEquipoModelo.getRepuestoList().add(repuesto);
                equipoModeloListEquipoModelo = em.merge(equipoModeloListEquipoModelo);
            }
            for (Pedido pedidoListPedido : repuesto.getPedidoList()) {
                Repuesto oldRepuestoOfPedidoListPedido = pedidoListPedido.getRepuesto();
                pedidoListPedido.setRepuesto(repuesto);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldRepuestoOfPedidoListPedido != null) {
                    oldRepuestoOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldRepuestoOfPedidoListPedido = em.merge(oldRepuestoOfPedidoListPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Repuesto repuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Repuesto persistentRepuesto = em.find(Repuesto.class, repuesto.getIdRepuesto());
            List<Empresa> empresaListOld = persistentRepuesto.getEmpresaList();
            List<Empresa> empresaListNew = repuesto.getEmpresaList();
            List<EquipoModelo> equipoModeloListOld = persistentRepuesto.getEquipoModeloList();
            List<EquipoModelo> equipoModeloListNew = repuesto.getEquipoModeloList();
            List<Pedido> pedidoListOld = persistentRepuesto.getPedidoList();
            List<Pedido> pedidoListNew = repuesto.getPedidoList();
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getIdEmpresa());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            repuesto.setEmpresaList(empresaListNew);
            List<EquipoModelo> attachedEquipoModeloListNew = new ArrayList<EquipoModelo>();
            for (EquipoModelo equipoModeloListNewEquipoModeloToAttach : equipoModeloListNew) {
                equipoModeloListNewEquipoModeloToAttach = em.getReference(equipoModeloListNewEquipoModeloToAttach.getClass(), equipoModeloListNewEquipoModeloToAttach.getIdModelo());
                attachedEquipoModeloListNew.add(equipoModeloListNewEquipoModeloToAttach);
            }
            equipoModeloListNew = attachedEquipoModeloListNew;
            repuesto.setEquipoModeloList(equipoModeloListNew);
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getIdPedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            repuesto.setPedidoList(pedidoListNew);
            repuesto = em.merge(repuesto);
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    empresaListOldEmpresa.getRepuestoList().remove(repuesto);
                    empresaListOldEmpresa = em.merge(empresaListOldEmpresa);
                }
            }
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    empresaListNewEmpresa.getRepuestoList().add(repuesto);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                }
            }
            for (EquipoModelo equipoModeloListOldEquipoModelo : equipoModeloListOld) {
                if (!equipoModeloListNew.contains(equipoModeloListOldEquipoModelo)) {
                    equipoModeloListOldEquipoModelo.getRepuestoList().remove(repuesto);
                    equipoModeloListOldEquipoModelo = em.merge(equipoModeloListOldEquipoModelo);
                }
            }
            for (EquipoModelo equipoModeloListNewEquipoModelo : equipoModeloListNew) {
                if (!equipoModeloListOld.contains(equipoModeloListNewEquipoModelo)) {
                    equipoModeloListNewEquipoModelo.getRepuestoList().add(repuesto);
                    equipoModeloListNewEquipoModelo = em.merge(equipoModeloListNewEquipoModelo);
                }
            }
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    pedidoListOldPedido.setRepuesto(null);
                    pedidoListOldPedido = em.merge(pedidoListOldPedido);
                }
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Repuesto oldRepuestoOfPedidoListNewPedido = pedidoListNewPedido.getRepuesto();
                    pedidoListNewPedido.setRepuesto(repuesto);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldRepuestoOfPedidoListNewPedido != null && !oldRepuestoOfPedidoListNewPedido.equals(repuesto)) {
                        oldRepuestoOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldRepuestoOfPedidoListNewPedido = em.merge(oldRepuestoOfPedidoListNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = repuesto.getIdRepuesto();
                if (findRepuesto(id) == null) {
                    throw new NonexistentEntityException("The repuesto with id " + id + " no longer exists.");
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
            Repuesto repuesto;
            try {
                repuesto = em.getReference(Repuesto.class, id);
                repuesto.getIdRepuesto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The repuesto with id " + id + " no longer exists.", enfe);
            }
            List<Empresa> empresaList = repuesto.getEmpresaList();
            for (Empresa empresaListEmpresa : empresaList) {
                empresaListEmpresa.getRepuestoList().remove(repuesto);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            List<EquipoModelo> equipoModeloList = repuesto.getEquipoModeloList();
            for (EquipoModelo equipoModeloListEquipoModelo : equipoModeloList) {
                equipoModeloListEquipoModelo.getRepuestoList().remove(repuesto);
                equipoModeloListEquipoModelo = em.merge(equipoModeloListEquipoModelo);
            }
            List<Pedido> pedidoList = repuesto.getPedidoList();
            for (Pedido pedidoListPedido : pedidoList) {
                pedidoListPedido.setRepuesto(null);
                pedidoListPedido = em.merge(pedidoListPedido);
            }
            em.remove(repuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Repuesto> findRepuestoEntities() {
        return findRepuestoEntities(true, -1, -1);
    }

    public List<Repuesto> findRepuestoEntities(int maxResults, int firstResult) {
        return findRepuestoEntities(false, maxResults, firstResult);
    }

    private List<Repuesto> findRepuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Repuesto.class));
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

    public Repuesto findRepuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Repuesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getRepuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Repuesto> rt = cq.from(Repuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
