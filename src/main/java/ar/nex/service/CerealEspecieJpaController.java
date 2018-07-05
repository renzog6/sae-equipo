/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.service;

import ar.nex.entity.CerealEspecie;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.CerealVariedad;
import ar.nex.service.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class CerealEspecieJpaController implements Serializable {

    public CerealEspecieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CerealEspecie cerealEspecie) {
        if (cerealEspecie.getCerealVariedadList() == null) {
            cerealEspecie.setCerealVariedadList(new ArrayList<CerealVariedad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CerealVariedad> attachedCerealVariedadList = new ArrayList<CerealVariedad>();
            for (CerealVariedad cerealVariedadListCerealVariedadToAttach : cerealEspecie.getCerealVariedadList()) {
                cerealVariedadListCerealVariedadToAttach = em.getReference(cerealVariedadListCerealVariedadToAttach.getClass(), cerealVariedadListCerealVariedadToAttach.getIdVariedead());
                attachedCerealVariedadList.add(cerealVariedadListCerealVariedadToAttach);
            }
            cerealEspecie.setCerealVariedadList(attachedCerealVariedadList);
            em.persist(cerealEspecie);
            for (CerealVariedad cerealVariedadListCerealVariedad : cerealEspecie.getCerealVariedadList()) {
                cerealVariedadListCerealVariedad.getCerealEspecieList().add(cerealEspecie);
                cerealVariedadListCerealVariedad = em.merge(cerealVariedadListCerealVariedad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CerealEspecie cerealEspecie) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CerealEspecie persistentCerealEspecie = em.find(CerealEspecie.class, cerealEspecie.getIdEspecie());
            List<CerealVariedad> cerealVariedadListOld = persistentCerealEspecie.getCerealVariedadList();
            List<CerealVariedad> cerealVariedadListNew = cerealEspecie.getCerealVariedadList();
            List<CerealVariedad> attachedCerealVariedadListNew = new ArrayList<CerealVariedad>();
            for (CerealVariedad cerealVariedadListNewCerealVariedadToAttach : cerealVariedadListNew) {
                cerealVariedadListNewCerealVariedadToAttach = em.getReference(cerealVariedadListNewCerealVariedadToAttach.getClass(), cerealVariedadListNewCerealVariedadToAttach.getIdVariedead());
                attachedCerealVariedadListNew.add(cerealVariedadListNewCerealVariedadToAttach);
            }
            cerealVariedadListNew = attachedCerealVariedadListNew;
            cerealEspecie.setCerealVariedadList(cerealVariedadListNew);
            cerealEspecie = em.merge(cerealEspecie);
            for (CerealVariedad cerealVariedadListOldCerealVariedad : cerealVariedadListOld) {
                if (!cerealVariedadListNew.contains(cerealVariedadListOldCerealVariedad)) {
                    cerealVariedadListOldCerealVariedad.getCerealEspecieList().remove(cerealEspecie);
                    cerealVariedadListOldCerealVariedad = em.merge(cerealVariedadListOldCerealVariedad);
                }
            }
            for (CerealVariedad cerealVariedadListNewCerealVariedad : cerealVariedadListNew) {
                if (!cerealVariedadListOld.contains(cerealVariedadListNewCerealVariedad)) {
                    cerealVariedadListNewCerealVariedad.getCerealEspecieList().add(cerealEspecie);
                    cerealVariedadListNewCerealVariedad = em.merge(cerealVariedadListNewCerealVariedad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cerealEspecie.getIdEspecie();
                if (findCerealEspecie(id) == null) {
                    throw new NonexistentEntityException("The cerealEspecie with id " + id + " no longer exists.");
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
            CerealEspecie cerealEspecie;
            try {
                cerealEspecie = em.getReference(CerealEspecie.class, id);
                cerealEspecie.getIdEspecie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cerealEspecie with id " + id + " no longer exists.", enfe);
            }
            List<CerealVariedad> cerealVariedadList = cerealEspecie.getCerealVariedadList();
            for (CerealVariedad cerealVariedadListCerealVariedad : cerealVariedadList) {
                cerealVariedadListCerealVariedad.getCerealEspecieList().remove(cerealEspecie);
                cerealVariedadListCerealVariedad = em.merge(cerealVariedadListCerealVariedad);
            }
            em.remove(cerealEspecie);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CerealEspecie> findCerealEspecieEntities() {
        return findCerealEspecieEntities(true, -1, -1);
    }

    public List<CerealEspecie> findCerealEspecieEntities(int maxResults, int firstResult) {
        return findCerealEspecieEntities(false, maxResults, firstResult);
    }

    private List<CerealEspecie> findCerealEspecieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CerealEspecie.class));
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

    public CerealEspecie findCerealEspecie(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CerealEspecie.class, id);
        } finally {
            em.close();
        }
    }

    public int getCerealEspecieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CerealEspecie> rt = cq.from(CerealEspecie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
