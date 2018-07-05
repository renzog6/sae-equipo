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
import ar.nex.entity.Provincia;
import ar.nex.entity.Direccion;
import ar.nex.entity.Localidad;
import ar.nex.service.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class LocalidadJpaController implements Serializable {

    public LocalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localidad localidad) {
        if (localidad.getDireccionList() == null) {
            localidad.setDireccionList(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia provinciaId = localidad.getProvinciaId();
            if (provinciaId != null) {
                provinciaId = em.getReference(provinciaId.getClass(), provinciaId.getIdProvincia());
                localidad.setProvinciaId(provinciaId);
            }
            List<Direccion> attachedDireccionList = new ArrayList<Direccion>();
            for (Direccion direccionListDireccionToAttach : localidad.getDireccionList()) {
                direccionListDireccionToAttach = em.getReference(direccionListDireccionToAttach.getClass(), direccionListDireccionToAttach.getIdDireccion());
                attachedDireccionList.add(direccionListDireccionToAttach);
            }
            localidad.setDireccionList(attachedDireccionList);
            em.persist(localidad);
            if (provinciaId != null) {
                provinciaId.getLocalidadList().add(localidad);
                provinciaId = em.merge(provinciaId);
            }
            for (Direccion direccionListDireccion : localidad.getDireccionList()) {
                Localidad oldLocalidadIdOfDireccionListDireccion = direccionListDireccion.getLocalidadId();
                direccionListDireccion.setLocalidadId(localidad);
                direccionListDireccion = em.merge(direccionListDireccion);
                if (oldLocalidadIdOfDireccionListDireccion != null) {
                    oldLocalidadIdOfDireccionListDireccion.getDireccionList().remove(direccionListDireccion);
                    oldLocalidadIdOfDireccionListDireccion = em.merge(oldLocalidadIdOfDireccionListDireccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localidad localidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad persistentLocalidad = em.find(Localidad.class, localidad.getIdLocalidad());
            Provincia provinciaIdOld = persistentLocalidad.getProvinciaId();
            Provincia provinciaIdNew = localidad.getProvinciaId();
            List<Direccion> direccionListOld = persistentLocalidad.getDireccionList();
            List<Direccion> direccionListNew = localidad.getDireccionList();
            if (provinciaIdNew != null) {
                provinciaIdNew = em.getReference(provinciaIdNew.getClass(), provinciaIdNew.getIdProvincia());
                localidad.setProvinciaId(provinciaIdNew);
            }
            List<Direccion> attachedDireccionListNew = new ArrayList<Direccion>();
            for (Direccion direccionListNewDireccionToAttach : direccionListNew) {
                direccionListNewDireccionToAttach = em.getReference(direccionListNewDireccionToAttach.getClass(), direccionListNewDireccionToAttach.getIdDireccion());
                attachedDireccionListNew.add(direccionListNewDireccionToAttach);
            }
            direccionListNew = attachedDireccionListNew;
            localidad.setDireccionList(direccionListNew);
            localidad = em.merge(localidad);
            if (provinciaIdOld != null && !provinciaIdOld.equals(provinciaIdNew)) {
                provinciaIdOld.getLocalidadList().remove(localidad);
                provinciaIdOld = em.merge(provinciaIdOld);
            }
            if (provinciaIdNew != null && !provinciaIdNew.equals(provinciaIdOld)) {
                provinciaIdNew.getLocalidadList().add(localidad);
                provinciaIdNew = em.merge(provinciaIdNew);
            }
            for (Direccion direccionListOldDireccion : direccionListOld) {
                if (!direccionListNew.contains(direccionListOldDireccion)) {
                    direccionListOldDireccion.setLocalidadId(null);
                    direccionListOldDireccion = em.merge(direccionListOldDireccion);
                }
            }
            for (Direccion direccionListNewDireccion : direccionListNew) {
                if (!direccionListOld.contains(direccionListNewDireccion)) {
                    Localidad oldLocalidadIdOfDireccionListNewDireccion = direccionListNewDireccion.getLocalidadId();
                    direccionListNewDireccion.setLocalidadId(localidad);
                    direccionListNewDireccion = em.merge(direccionListNewDireccion);
                    if (oldLocalidadIdOfDireccionListNewDireccion != null && !oldLocalidadIdOfDireccionListNewDireccion.equals(localidad)) {
                        oldLocalidadIdOfDireccionListNewDireccion.getDireccionList().remove(direccionListNewDireccion);
                        oldLocalidadIdOfDireccionListNewDireccion = em.merge(oldLocalidadIdOfDireccionListNewDireccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = localidad.getIdLocalidad();
                if (findLocalidad(id) == null) {
                    throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.");
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
            Localidad localidad;
            try {
                localidad = em.getReference(Localidad.class, id);
                localidad.getIdLocalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.", enfe);
            }
            Provincia provinciaId = localidad.getProvinciaId();
            if (provinciaId != null) {
                provinciaId.getLocalidadList().remove(localidad);
                provinciaId = em.merge(provinciaId);
            }
            List<Direccion> direccionList = localidad.getDireccionList();
            for (Direccion direccionListDireccion : direccionList) {
                direccionListDireccion.setLocalidadId(null);
                direccionListDireccion = em.merge(direccionListDireccion);
            }
            em.remove(localidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localidad> findLocalidadEntities() {
        return findLocalidadEntities(true, -1, -1);
    }

    public List<Localidad> findLocalidadEntities(int maxResults, int firstResult) {
        return findLocalidadEntities(false, maxResults, firstResult);
    }

    private List<Localidad> findLocalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidad.class));
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

    public Localidad findLocalidad(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidad> rt = cq.from(Localidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
