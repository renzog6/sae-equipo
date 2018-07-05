/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.service;

import ar.nex.entity.CerealSilo;
import ar.nex.service.exceptions.NonexistentEntityException;
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
public class CerealSiloJpaController implements Serializable {

    public CerealSiloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CerealSilo cerealSilo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cerealSilo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CerealSilo cerealSilo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cerealSilo = em.merge(cerealSilo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cerealSilo.getIdSilo();
                if (findCerealSilo(id) == null) {
                    throw new NonexistentEntityException("The cerealSilo with id " + id + " no longer exists.");
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
            CerealSilo cerealSilo;
            try {
                cerealSilo = em.getReference(CerealSilo.class, id);
                cerealSilo.getIdSilo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cerealSilo with id " + id + " no longer exists.", enfe);
            }
            em.remove(cerealSilo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CerealSilo> findCerealSiloEntities() {
        return findCerealSiloEntities(true, -1, -1);
    }

    public List<CerealSilo> findCerealSiloEntities(int maxResults, int firstResult) {
        return findCerealSiloEntities(false, maxResults, firstResult);
    }

    private List<CerealSilo> findCerealSiloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CerealSilo.class));
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

    public CerealSilo findCerealSilo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CerealSilo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCerealSiloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CerealSilo> rt = cq.from(CerealSilo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
