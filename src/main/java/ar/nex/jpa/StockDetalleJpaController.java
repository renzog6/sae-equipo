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
import ar.nex.entity.Equipo;
import ar.nex.entity.Repuesto;
import ar.nex.entity.StockDetalle;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class StockDetalleJpaController implements Serializable {

    public StockDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StockDetalle stockDetalle) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo = stockDetalle.getEquipo();
            if (equipo != null) {
                equipo = em.getReference(equipo.getClass(), equipo.getIdEquipo());
                stockDetalle.setEquipo(equipo);
            }
            Repuesto repuesto = stockDetalle.getRepuesto();
            if (repuesto != null) {
                repuesto = em.getReference(repuesto.getClass(), repuesto.getIdRepuesto());
                stockDetalle.setRepuesto(repuesto);
            }
            em.persist(stockDetalle);
            if (equipo != null) {
                equipo.getStockDetalleList().add(stockDetalle);
                equipo = em.merge(equipo);
            }
            if (repuesto != null) {
                repuesto.getStockDetalleList().add(stockDetalle);
                repuesto = em.merge(repuesto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStockDetalle(stockDetalle.getIdStock()) != null) {
                throw new PreexistingEntityException("StockDetalle " + stockDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StockDetalle stockDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StockDetalle persistentStockDetalle = em.find(StockDetalle.class, stockDetalle.getIdStock());
            Equipo equipoOld = persistentStockDetalle.getEquipo();
            Equipo equipoNew = stockDetalle.getEquipo();
            Repuesto repuestoOld = persistentStockDetalle.getRepuesto();
            Repuesto repuestoNew = stockDetalle.getRepuesto();
            if (equipoNew != null) {
                equipoNew = em.getReference(equipoNew.getClass(), equipoNew.getIdEquipo());
                stockDetalle.setEquipo(equipoNew);
            }
            if (repuestoNew != null) {
                repuestoNew = em.getReference(repuestoNew.getClass(), repuestoNew.getIdRepuesto());
                stockDetalle.setRepuesto(repuestoNew);
            }
            stockDetalle = em.merge(stockDetalle);
            if (equipoOld != null && !equipoOld.equals(equipoNew)) {
                equipoOld.getStockDetalleList().remove(stockDetalle);
                equipoOld = em.merge(equipoOld);
            }
            if (equipoNew != null && !equipoNew.equals(equipoOld)) {
                equipoNew.getStockDetalleList().add(stockDetalle);
                equipoNew = em.merge(equipoNew);
            }
            if (repuestoOld != null && !repuestoOld.equals(repuestoNew)) {
                repuestoOld.getStockDetalleList().remove(stockDetalle);
                repuestoOld = em.merge(repuestoOld);
            }
            if (repuestoNew != null && !repuestoNew.equals(repuestoOld)) {
                repuestoNew.getStockDetalleList().add(stockDetalle);
                repuestoNew = em.merge(repuestoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = stockDetalle.getIdStock();
                if (findStockDetalle(id) == null) {
                    throw new NonexistentEntityException("The stockDetalle with id " + id + " no longer exists.");
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
            StockDetalle stockDetalle;
            try {
                stockDetalle = em.getReference(StockDetalle.class, id);
                stockDetalle.getIdStock();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stockDetalle with id " + id + " no longer exists.", enfe);
            }
            Equipo equipo = stockDetalle.getEquipo();
            if (equipo != null) {
                equipo.getStockDetalleList().remove(stockDetalle);
                equipo = em.merge(equipo);
            }
            Repuesto repuesto = stockDetalle.getRepuesto();
            if (repuesto != null) {
                repuesto.getStockDetalleList().remove(stockDetalle);
                repuesto = em.merge(repuesto);
            }
            em.remove(stockDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StockDetalle> findStockDetalleEntities() {
        return findStockDetalleEntities(true, -1, -1);
    }

    public List<StockDetalle> findStockDetalleEntities(int maxResults, int firstResult) {
        return findStockDetalleEntities(false, maxResults, firstResult);
    }

    private List<StockDetalle> findStockDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StockDetalle.class));
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

    public StockDetalle findStockDetalle(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StockDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StockDetalle> rt = cq.from(StockDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
