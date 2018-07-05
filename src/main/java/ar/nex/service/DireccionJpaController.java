/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.service;

import ar.nex.entity.Direccion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Localidad;
import ar.nex.entity.Empresa;
import ar.nex.service.exceptions.IllegalOrphanException;
import ar.nex.service.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class DireccionJpaController implements Serializable {

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad localidadId = direccion.getLocalidadId();
            if (localidadId != null) {
                localidadId = em.getReference(localidadId.getClass(), localidadId.getIdLocalidad());
                direccion.setLocalidadId(localidadId);
            }
            Empresa empresa = direccion.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                direccion.setEmpresa(empresa);
            }
            em.persist(direccion);
            if (localidadId != null) {
                localidadId.getDireccionList().add(direccion);
                localidadId = em.merge(localidadId);
            }
            if (empresa != null) {
                Direccion oldDireccionOfEmpresa = empresa.getDireccion();
                if (oldDireccionOfEmpresa != null) {
                    oldDireccionOfEmpresa.setEmpresa(null);
                    oldDireccionOfEmpresa = em.merge(oldDireccionOfEmpresa);
                }
                empresa.setDireccion(direccion);
                empresa = em.merge(empresa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getIdDireccion());
            Localidad localidadIdOld = persistentDireccion.getLocalidadId();
            Localidad localidadIdNew = direccion.getLocalidadId();
            Empresa empresaOld = persistentDireccion.getEmpresa();
            Empresa empresaNew = direccion.getEmpresa();
            List<String> illegalOrphanMessages = null;
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Empresa " + empresaOld + " since its direccion field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (localidadIdNew != null) {
                localidadIdNew = em.getReference(localidadIdNew.getClass(), localidadIdNew.getIdLocalidad());
                direccion.setLocalidadId(localidadIdNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                direccion.setEmpresa(empresaNew);
            }
            direccion = em.merge(direccion);
            if (localidadIdOld != null && !localidadIdOld.equals(localidadIdNew)) {
                localidadIdOld.getDireccionList().remove(direccion);
                localidadIdOld = em.merge(localidadIdOld);
            }
            if (localidadIdNew != null && !localidadIdNew.equals(localidadIdOld)) {
                localidadIdNew.getDireccionList().add(direccion);
                localidadIdNew = em.merge(localidadIdNew);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                Direccion oldDireccionOfEmpresa = empresaNew.getDireccion();
                if (oldDireccionOfEmpresa != null) {
                    oldDireccionOfEmpresa.setEmpresa(null);
                    oldDireccionOfEmpresa = em.merge(oldDireccionOfEmpresa);
                }
                empresaNew.setDireccion(direccion);
                empresaNew = em.merge(empresaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = direccion.getIdDireccion();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getIdDireccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Empresa empresaOrphanCheck = direccion.getEmpresa();
            if (empresaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direccion (" + direccion + ") cannot be destroyed since the Empresa " + empresaOrphanCheck + " in its empresa field has a non-nullable direccion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Localidad localidadId = direccion.getLocalidadId();
            if (localidadId != null) {
                localidadId.getDireccionList().remove(direccion);
                localidadId = em.merge(localidadId);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
