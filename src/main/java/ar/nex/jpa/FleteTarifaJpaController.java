/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.FleteTarifa;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Renzo
 */
public class FleteTarifaJpaController implements Serializable {

    public FleteTarifaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FleteTarifa fleteTarifa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(fleteTarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FleteTarifa fleteTarifa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            fleteTarifa = em.merge(fleteTarifa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fleteTarifa.getIdTarifa();
                if (findFleteTarifa(id) == null) {
                    throw new NonexistentEntityException("The fleteTarifa with id " + id + " no longer exists.");
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
            FleteTarifa fleteTarifa;
            try {
                fleteTarifa = em.getReference(FleteTarifa.class, id);
                fleteTarifa.getIdTarifa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fleteTarifa with id " + id + " no longer exists.", enfe);
            }
            em.remove(fleteTarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FleteTarifa> findFleteTarifaEntities() {
        return findFleteTarifaEntities(true, -1, -1);
    }

    public List<FleteTarifa> findFleteTarifaEntities(int maxResults, int firstResult) {
        return findFleteTarifaEntities(false, maxResults, firstResult);
    }

    private List<FleteTarifa> findFleteTarifaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FleteTarifa.class));
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

    public FleteTarifa findFleteTarifa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FleteTarifa.class, id);
        } finally {
            em.close();
        }
    }

    public int getFleteTarifaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FleteTarifa> rt = cq.from(FleteTarifa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}