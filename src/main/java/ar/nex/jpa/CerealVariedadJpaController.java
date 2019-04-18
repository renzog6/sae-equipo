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
import ar.nex.entity.CerealEspecie;
import ar.nex.entity.CerealVariedad;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class CerealVariedadJpaController implements Serializable {

    public CerealVariedadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CerealVariedad cerealVariedad) {
        if (cerealVariedad.getCerealEspecieList() == null) {
            cerealVariedad.setCerealEspecieList(new ArrayList<CerealEspecie>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CerealEspecie> attachedCerealEspecieList = new ArrayList<CerealEspecie>();
            for (CerealEspecie cerealEspecieListCerealEspecieToAttach : cerealVariedad.getCerealEspecieList()) {
                cerealEspecieListCerealEspecieToAttach = em.getReference(cerealEspecieListCerealEspecieToAttach.getClass(), cerealEspecieListCerealEspecieToAttach.getIdEspecie());
                attachedCerealEspecieList.add(cerealEspecieListCerealEspecieToAttach);
            }
            cerealVariedad.setCerealEspecieList(attachedCerealEspecieList);
            em.persist(cerealVariedad);
            for (CerealEspecie cerealEspecieListCerealEspecie : cerealVariedad.getCerealEspecieList()) {
                cerealEspecieListCerealEspecie.getCerealVariedadList().add(cerealVariedad);
                cerealEspecieListCerealEspecie = em.merge(cerealEspecieListCerealEspecie);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CerealVariedad cerealVariedad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CerealVariedad persistentCerealVariedad = em.find(CerealVariedad.class, cerealVariedad.getIdVariedead());
            List<CerealEspecie> cerealEspecieListOld = persistentCerealVariedad.getCerealEspecieList();
            List<CerealEspecie> cerealEspecieListNew = cerealVariedad.getCerealEspecieList();
            List<CerealEspecie> attachedCerealEspecieListNew = new ArrayList<CerealEspecie>();
            for (CerealEspecie cerealEspecieListNewCerealEspecieToAttach : cerealEspecieListNew) {
                cerealEspecieListNewCerealEspecieToAttach = em.getReference(cerealEspecieListNewCerealEspecieToAttach.getClass(), cerealEspecieListNewCerealEspecieToAttach.getIdEspecie());
                attachedCerealEspecieListNew.add(cerealEspecieListNewCerealEspecieToAttach);
            }
            cerealEspecieListNew = attachedCerealEspecieListNew;
            cerealVariedad.setCerealEspecieList(cerealEspecieListNew);
            cerealVariedad = em.merge(cerealVariedad);
            for (CerealEspecie cerealEspecieListOldCerealEspecie : cerealEspecieListOld) {
                if (!cerealEspecieListNew.contains(cerealEspecieListOldCerealEspecie)) {
                    cerealEspecieListOldCerealEspecie.getCerealVariedadList().remove(cerealVariedad);
                    cerealEspecieListOldCerealEspecie = em.merge(cerealEspecieListOldCerealEspecie);
                }
            }
            for (CerealEspecie cerealEspecieListNewCerealEspecie : cerealEspecieListNew) {
                if (!cerealEspecieListOld.contains(cerealEspecieListNewCerealEspecie)) {
                    cerealEspecieListNewCerealEspecie.getCerealVariedadList().add(cerealVariedad);
                    cerealEspecieListNewCerealEspecie = em.merge(cerealEspecieListNewCerealEspecie);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cerealVariedad.getIdVariedead();
                if (findCerealVariedad(id) == null) {
                    throw new NonexistentEntityException("The cerealVariedad with id " + id + " no longer exists.");
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
            CerealVariedad cerealVariedad;
            try {
                cerealVariedad = em.getReference(CerealVariedad.class, id);
                cerealVariedad.getIdVariedead();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cerealVariedad with id " + id + " no longer exists.", enfe);
            }
            List<CerealEspecie> cerealEspecieList = cerealVariedad.getCerealEspecieList();
            for (CerealEspecie cerealEspecieListCerealEspecie : cerealEspecieList) {
                cerealEspecieListCerealEspecie.getCerealVariedadList().remove(cerealVariedad);
                cerealEspecieListCerealEspecie = em.merge(cerealEspecieListCerealEspecie);
            }
            em.remove(cerealVariedad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CerealVariedad> findCerealVariedadEntities() {
        return findCerealVariedadEntities(true, -1, -1);
    }

    public List<CerealVariedad> findCerealVariedadEntities(int maxResults, int firstResult) {
        return findCerealVariedadEntities(false, maxResults, firstResult);
    }

    private List<CerealVariedad> findCerealVariedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CerealVariedad.class));
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

    public CerealVariedad findCerealVariedad(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CerealVariedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCerealVariedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CerealVariedad> rt = cq.from(CerealVariedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
