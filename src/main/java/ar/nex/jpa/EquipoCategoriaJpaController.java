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
import ar.nex.entity.EquipoCategoria;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.EquipoTipo;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoCategoriaJpaController implements Serializable {

    public EquipoCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoCategoria equipoCategoria) {
        if (equipoCategoria.getEquipoList() == null) {
            equipoCategoria.setEquipoList(new ArrayList<Equipo>());
        }
        if (equipoCategoria.getEquipoTipoList() == null) {
            equipoCategoria.setEquipoTipoList(new ArrayList<EquipoTipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : equipoCategoria.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            equipoCategoria.setEquipoList(attachedEquipoList);
            List<EquipoTipo> attachedEquipoTipoList = new ArrayList<EquipoTipo>();
            for (EquipoTipo equipoTipoListEquipoTipoToAttach : equipoCategoria.getEquipoTipoList()) {
                equipoTipoListEquipoTipoToAttach = em.getReference(equipoTipoListEquipoTipoToAttach.getClass(), equipoTipoListEquipoTipoToAttach.getIdTipo());
                attachedEquipoTipoList.add(equipoTipoListEquipoTipoToAttach);
            }
            equipoCategoria.setEquipoTipoList(attachedEquipoTipoList);
            em.persist(equipoCategoria);
            for (Equipo equipoListEquipo : equipoCategoria.getEquipoList()) {
                EquipoCategoria oldCategoriaOfEquipoListEquipo = equipoListEquipo.getCategoria();
                equipoListEquipo.setCategoria(equipoCategoria);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldCategoriaOfEquipoListEquipo != null) {
                    oldCategoriaOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldCategoriaOfEquipoListEquipo = em.merge(oldCategoriaOfEquipoListEquipo);
                }
            }
            for (EquipoTipo equipoTipoListEquipoTipo : equipoCategoria.getEquipoTipoList()) {
                EquipoCategoria oldIdCategoriaOfEquipoTipoListEquipoTipo = equipoTipoListEquipoTipo.getIdCategoria();
                equipoTipoListEquipoTipo.setIdCategoria(equipoCategoria);
                equipoTipoListEquipoTipo = em.merge(equipoTipoListEquipoTipo);
                if (oldIdCategoriaOfEquipoTipoListEquipoTipo != null) {
                    oldIdCategoriaOfEquipoTipoListEquipoTipo.getEquipoTipoList().remove(equipoTipoListEquipoTipo);
                    oldIdCategoriaOfEquipoTipoListEquipoTipo = em.merge(oldIdCategoriaOfEquipoTipoListEquipoTipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoCategoria equipoCategoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoCategoria persistentEquipoCategoria = em.find(EquipoCategoria.class, equipoCategoria.getIdCategoria());
            List<Equipo> equipoListOld = persistentEquipoCategoria.getEquipoList();
            List<Equipo> equipoListNew = equipoCategoria.getEquipoList();
            List<EquipoTipo> equipoTipoListOld = persistentEquipoCategoria.getEquipoTipoList();
            List<EquipoTipo> equipoTipoListNew = equipoCategoria.getEquipoTipoList();
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            equipoCategoria.setEquipoList(equipoListNew);
            List<EquipoTipo> attachedEquipoTipoListNew = new ArrayList<EquipoTipo>();
            for (EquipoTipo equipoTipoListNewEquipoTipoToAttach : equipoTipoListNew) {
                equipoTipoListNewEquipoTipoToAttach = em.getReference(equipoTipoListNewEquipoTipoToAttach.getClass(), equipoTipoListNewEquipoTipoToAttach.getIdTipo());
                attachedEquipoTipoListNew.add(equipoTipoListNewEquipoTipoToAttach);
            }
            equipoTipoListNew = attachedEquipoTipoListNew;
            equipoCategoria.setEquipoTipoList(equipoTipoListNew);
            equipoCategoria = em.merge(equipoCategoria);
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setCategoria(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    EquipoCategoria oldCategoriaOfEquipoListNewEquipo = equipoListNewEquipo.getCategoria();
                    equipoListNewEquipo.setCategoria(equipoCategoria);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldCategoriaOfEquipoListNewEquipo != null && !oldCategoriaOfEquipoListNewEquipo.equals(equipoCategoria)) {
                        oldCategoriaOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldCategoriaOfEquipoListNewEquipo = em.merge(oldCategoriaOfEquipoListNewEquipo);
                    }
                }
            }
            for (EquipoTipo equipoTipoListOldEquipoTipo : equipoTipoListOld) {
                if (!equipoTipoListNew.contains(equipoTipoListOldEquipoTipo)) {
                    equipoTipoListOldEquipoTipo.setIdCategoria(null);
                    equipoTipoListOldEquipoTipo = em.merge(equipoTipoListOldEquipoTipo);
                }
            }
            for (EquipoTipo equipoTipoListNewEquipoTipo : equipoTipoListNew) {
                if (!equipoTipoListOld.contains(equipoTipoListNewEquipoTipo)) {
                    EquipoCategoria oldIdCategoriaOfEquipoTipoListNewEquipoTipo = equipoTipoListNewEquipoTipo.getIdCategoria();
                    equipoTipoListNewEquipoTipo.setIdCategoria(equipoCategoria);
                    equipoTipoListNewEquipoTipo = em.merge(equipoTipoListNewEquipoTipo);
                    if (oldIdCategoriaOfEquipoTipoListNewEquipoTipo != null && !oldIdCategoriaOfEquipoTipoListNewEquipoTipo.equals(equipoCategoria)) {
                        oldIdCategoriaOfEquipoTipoListNewEquipoTipo.getEquipoTipoList().remove(equipoTipoListNewEquipoTipo);
                        oldIdCategoriaOfEquipoTipoListNewEquipoTipo = em.merge(oldIdCategoriaOfEquipoTipoListNewEquipoTipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoCategoria.getIdCategoria();
                if (findEquipoCategoria(id) == null) {
                    throw new NonexistentEntityException("The equipoCategoria with id " + id + " no longer exists.");
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
            EquipoCategoria equipoCategoria;
            try {
                equipoCategoria = em.getReference(EquipoCategoria.class, id);
                equipoCategoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoCategoria with id " + id + " no longer exists.", enfe);
            }
            List<Equipo> equipoList = equipoCategoria.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setCategoria(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<EquipoTipo> equipoTipoList = equipoCategoria.getEquipoTipoList();
            for (EquipoTipo equipoTipoListEquipoTipo : equipoTipoList) {
                equipoTipoListEquipoTipo.setIdCategoria(null);
                equipoTipoListEquipoTipo = em.merge(equipoTipoListEquipoTipo);
            }
            em.remove(equipoCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoCategoria> findEquipoCategoriaEntities() {
        return findEquipoCategoriaEntities(true, -1, -1);
    }

    public List<EquipoCategoria> findEquipoCategoriaEntities(int maxResults, int firstResult) {
        return findEquipoCategoriaEntities(false, maxResults, firstResult);
    }

    private List<EquipoCategoria> findEquipoCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoCategoria.class));
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

    public EquipoCategoria findEquipoCategoria(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoCategoria> rt = cq.from(EquipoCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
