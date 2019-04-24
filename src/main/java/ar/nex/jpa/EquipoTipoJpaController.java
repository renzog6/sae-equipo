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
import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.Equipo;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoTipoJpaController implements Serializable {

    public EquipoTipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoTipo equipoTipo) {
        if (equipoTipo.getEquipoList() == null) {
            equipoTipo.setEquipoList(new ArrayList<Equipo>());
        }
        if (equipoTipo.getEquipoModeloList() == null) {
            equipoTipo.setEquipoModeloList(new ArrayList<EquipoModelo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoCategoria idCategoria = equipoTipo.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                equipoTipo.setIdCategoria(idCategoria);
            }
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : equipoTipo.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            equipoTipo.setEquipoList(attachedEquipoList);
            List<EquipoModelo> attachedEquipoModeloList = new ArrayList<EquipoModelo>();
            for (EquipoModelo equipoModeloListEquipoModeloToAttach : equipoTipo.getEquipoModeloList()) {
                equipoModeloListEquipoModeloToAttach = em.getReference(equipoModeloListEquipoModeloToAttach.getClass(), equipoModeloListEquipoModeloToAttach.getIdModelo());
                attachedEquipoModeloList.add(equipoModeloListEquipoModeloToAttach);
            }
            equipoTipo.setEquipoModeloList(attachedEquipoModeloList);
            em.persist(equipoTipo);
            if (idCategoria != null) {
                idCategoria.getEquipoTipoList().add(equipoTipo);
                idCategoria = em.merge(idCategoria);
            }
            for (Equipo equipoListEquipo : equipoTipo.getEquipoList()) {
                EquipoTipo oldTipoOfEquipoListEquipo = equipoListEquipo.getTipo();
                equipoListEquipo.setTipo(equipoTipo);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldTipoOfEquipoListEquipo != null) {
                    oldTipoOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldTipoOfEquipoListEquipo = em.merge(oldTipoOfEquipoListEquipo);
                }
            }
            for (EquipoModelo equipoModeloListEquipoModelo : equipoTipo.getEquipoModeloList()) {
                EquipoTipo oldIdTipoOfEquipoModeloListEquipoModelo = equipoModeloListEquipoModelo.getIdTipo();
                equipoModeloListEquipoModelo.setIdTipo(equipoTipo);
                equipoModeloListEquipoModelo = em.merge(equipoModeloListEquipoModelo);
                if (oldIdTipoOfEquipoModeloListEquipoModelo != null) {
                    oldIdTipoOfEquipoModeloListEquipoModelo.getEquipoModeloList().remove(equipoModeloListEquipoModelo);
                    oldIdTipoOfEquipoModeloListEquipoModelo = em.merge(oldIdTipoOfEquipoModeloListEquipoModelo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoTipo equipoTipo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoTipo persistentEquipoTipo = em.find(EquipoTipo.class, equipoTipo.getIdTipo());
            EquipoCategoria idCategoriaOld = persistentEquipoTipo.getIdCategoria();
            EquipoCategoria idCategoriaNew = equipoTipo.getIdCategoria();
            List<Equipo> equipoListOld = persistentEquipoTipo.getEquipoList();
            List<Equipo> equipoListNew = equipoTipo.getEquipoList();
            List<EquipoModelo> equipoModeloListOld = persistentEquipoTipo.getEquipoModeloList();
            List<EquipoModelo> equipoModeloListNew = equipoTipo.getEquipoModeloList();
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                equipoTipo.setIdCategoria(idCategoriaNew);
            }
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            equipoTipo.setEquipoList(equipoListNew);
            List<EquipoModelo> attachedEquipoModeloListNew = new ArrayList<EquipoModelo>();
            for (EquipoModelo equipoModeloListNewEquipoModeloToAttach : equipoModeloListNew) {
                equipoModeloListNewEquipoModeloToAttach = em.getReference(equipoModeloListNewEquipoModeloToAttach.getClass(), equipoModeloListNewEquipoModeloToAttach.getIdModelo());
                attachedEquipoModeloListNew.add(equipoModeloListNewEquipoModeloToAttach);
            }
            equipoModeloListNew = attachedEquipoModeloListNew;
            equipoTipo.setEquipoModeloList(equipoModeloListNew);
            equipoTipo = em.merge(equipoTipo);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getEquipoTipoList().remove(equipoTipo);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getEquipoTipoList().add(equipoTipo);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setTipo(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    EquipoTipo oldTipoOfEquipoListNewEquipo = equipoListNewEquipo.getTipo();
                    equipoListNewEquipo.setTipo(equipoTipo);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldTipoOfEquipoListNewEquipo != null && !oldTipoOfEquipoListNewEquipo.equals(equipoTipo)) {
                        oldTipoOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldTipoOfEquipoListNewEquipo = em.merge(oldTipoOfEquipoListNewEquipo);
                    }
                }
            }
            for (EquipoModelo equipoModeloListOldEquipoModelo : equipoModeloListOld) {
                if (!equipoModeloListNew.contains(equipoModeloListOldEquipoModelo)) {
                    equipoModeloListOldEquipoModelo.setIdTipo(null);
                    equipoModeloListOldEquipoModelo = em.merge(equipoModeloListOldEquipoModelo);
                }
            }
            for (EquipoModelo equipoModeloListNewEquipoModelo : equipoModeloListNew) {
                if (!equipoModeloListOld.contains(equipoModeloListNewEquipoModelo)) {
                    EquipoTipo oldIdTipoOfEquipoModeloListNewEquipoModelo = equipoModeloListNewEquipoModelo.getIdTipo();
                    equipoModeloListNewEquipoModelo.setIdTipo(equipoTipo);
                    equipoModeloListNewEquipoModelo = em.merge(equipoModeloListNewEquipoModelo);
                    if (oldIdTipoOfEquipoModeloListNewEquipoModelo != null && !oldIdTipoOfEquipoModeloListNewEquipoModelo.equals(equipoTipo)) {
                        oldIdTipoOfEquipoModeloListNewEquipoModelo.getEquipoModeloList().remove(equipoModeloListNewEquipoModelo);
                        oldIdTipoOfEquipoModeloListNewEquipoModelo = em.merge(oldIdTipoOfEquipoModeloListNewEquipoModelo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoTipo.getIdTipo();
                if (findEquipoTipo(id) == null) {
                    throw new NonexistentEntityException("The equipoTipo with id " + id + " no longer exists.");
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
            EquipoTipo equipoTipo;
            try {
                equipoTipo = em.getReference(EquipoTipo.class, id);
                equipoTipo.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoTipo with id " + id + " no longer exists.", enfe);
            }
            EquipoCategoria idCategoria = equipoTipo.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getEquipoTipoList().remove(equipoTipo);
                idCategoria = em.merge(idCategoria);
            }
            List<Equipo> equipoList = equipoTipo.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setTipo(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<EquipoModelo> equipoModeloList = equipoTipo.getEquipoModeloList();
            for (EquipoModelo equipoModeloListEquipoModelo : equipoModeloList) {
                equipoModeloListEquipoModelo.setIdTipo(null);
                equipoModeloListEquipoModelo = em.merge(equipoModeloListEquipoModelo);
            }
            em.remove(equipoTipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoTipo> findEquipoTipoEntities() {
        return findEquipoTipoEntities(true, -1, -1);
    }

    public List<EquipoTipo> findEquipoTipoEntities(int maxResults, int firstResult) {
        return findEquipoTipoEntities(false, maxResults, firstResult);
    }

    private List<EquipoTipo> findEquipoTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoTipo.class));
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

    public EquipoTipo findEquipoTipo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoTipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoTipo> rt = cq.from(EquipoTipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
