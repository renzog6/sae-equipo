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
import ar.nex.entity.Equipo;
import ar.nex.entity.Repuesto;
import ar.nex.entity.StockDetalle;
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
        if (repuesto.getEquipoList() == null) {
            repuesto.setEquipoList(new ArrayList<Equipo>());
        }
        if (repuesto.getStockDetalleList() == null) {
            repuesto.setStockDetalleList(new ArrayList<StockDetalle>());
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
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : repuesto.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            repuesto.setEquipoList(attachedEquipoList);
            List<StockDetalle> attachedStockDetalleList = new ArrayList<StockDetalle>();
            for (StockDetalle stockDetalleListStockDetalleToAttach : repuesto.getStockDetalleList()) {
                stockDetalleListStockDetalleToAttach = em.getReference(stockDetalleListStockDetalleToAttach.getClass(), stockDetalleListStockDetalleToAttach.getIdStock());
                attachedStockDetalleList.add(stockDetalleListStockDetalleToAttach);
            }
            repuesto.setStockDetalleList(attachedStockDetalleList);
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
            for (Equipo equipoListEquipo : repuesto.getEquipoList()) {
                equipoListEquipo.getRepuestoList().add(repuesto);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            for (StockDetalle stockDetalleListStockDetalle : repuesto.getStockDetalleList()) {
                Repuesto oldRepuestoOfStockDetalleListStockDetalle = stockDetalleListStockDetalle.getRepuesto();
                stockDetalleListStockDetalle.setRepuesto(repuesto);
                stockDetalleListStockDetalle = em.merge(stockDetalleListStockDetalle);
                if (oldRepuestoOfStockDetalleListStockDetalle != null) {
                    oldRepuestoOfStockDetalleListStockDetalle.getStockDetalleList().remove(stockDetalleListStockDetalle);
                    oldRepuestoOfStockDetalleListStockDetalle = em.merge(oldRepuestoOfStockDetalleListStockDetalle);
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
            List<Equipo> equipoListOld = persistentRepuesto.getEquipoList();
            List<Equipo> equipoListNew = repuesto.getEquipoList();
            List<StockDetalle> stockDetalleListOld = persistentRepuesto.getStockDetalleList();
            List<StockDetalle> stockDetalleListNew = repuesto.getStockDetalleList();
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
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            repuesto.setEquipoList(equipoListNew);
            List<StockDetalle> attachedStockDetalleListNew = new ArrayList<StockDetalle>();
            for (StockDetalle stockDetalleListNewStockDetalleToAttach : stockDetalleListNew) {
                stockDetalleListNewStockDetalleToAttach = em.getReference(stockDetalleListNewStockDetalleToAttach.getClass(), stockDetalleListNewStockDetalleToAttach.getIdStock());
                attachedStockDetalleListNew.add(stockDetalleListNewStockDetalleToAttach);
            }
            stockDetalleListNew = attachedStockDetalleListNew;
            repuesto.setStockDetalleList(stockDetalleListNew);
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
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.getRepuestoList().remove(repuesto);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    equipoListNewEquipo.getRepuestoList().add(repuesto);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                }
            }
            for (StockDetalle stockDetalleListOldStockDetalle : stockDetalleListOld) {
                if (!stockDetalleListNew.contains(stockDetalleListOldStockDetalle)) {
                    stockDetalleListOldStockDetalle.setRepuesto(null);
                    stockDetalleListOldStockDetalle = em.merge(stockDetalleListOldStockDetalle);
                }
            }
            for (StockDetalle stockDetalleListNewStockDetalle : stockDetalleListNew) {
                if (!stockDetalleListOld.contains(stockDetalleListNewStockDetalle)) {
                    Repuesto oldRepuestoOfStockDetalleListNewStockDetalle = stockDetalleListNewStockDetalle.getRepuesto();
                    stockDetalleListNewStockDetalle.setRepuesto(repuesto);
                    stockDetalleListNewStockDetalle = em.merge(stockDetalleListNewStockDetalle);
                    if (oldRepuestoOfStockDetalleListNewStockDetalle != null && !oldRepuestoOfStockDetalleListNewStockDetalle.equals(repuesto)) {
                        oldRepuestoOfStockDetalleListNewStockDetalle.getStockDetalleList().remove(stockDetalleListNewStockDetalle);
                        oldRepuestoOfStockDetalleListNewStockDetalle = em.merge(oldRepuestoOfStockDetalleListNewStockDetalle);
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
            List<Equipo> equipoList = repuesto.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.getRepuestoList().remove(repuesto);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<StockDetalle> stockDetalleList = repuesto.getStockDetalleList();
            for (StockDetalle stockDetalleListStockDetalle : stockDetalleList) {
                stockDetalleListStockDetalle.setRepuesto(null);
                stockDetalleListStockDetalle = em.merge(stockDetalleListStockDetalle);
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
