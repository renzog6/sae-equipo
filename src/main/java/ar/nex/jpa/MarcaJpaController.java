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
import ar.nex.entity.Item;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.Equipo;
import ar.nex.entity.Marca;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class MarcaJpaController implements Serializable {

    public MarcaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Marca marca) {
        if (marca.getItemList() == null) {
            marca.setItemList(new ArrayList<Item>());
        }
        if (marca.getEquipoList() == null) {
            marca.setEquipoList(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : marca.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getIdItem());
                attachedItemList.add(itemListItemToAttach);
            }
            marca.setItemList(attachedItemList);
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : marca.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            marca.setEquipoList(attachedEquipoList);
            em.persist(marca);
            for (Item itemListItem : marca.getItemList()) {
                itemListItem.getMarcaList().add(marca);
                itemListItem = em.merge(itemListItem);
            }
            for (Equipo equipoListEquipo : marca.getEquipoList()) {
                Marca oldMarcaOfEquipoListEquipo = equipoListEquipo.getMarca();
                equipoListEquipo.setMarca(marca);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldMarcaOfEquipoListEquipo != null) {
                    oldMarcaOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldMarcaOfEquipoListEquipo = em.merge(oldMarcaOfEquipoListEquipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Marca marca) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca persistentMarca = em.find(Marca.class, marca.getIdMarca());
            List<Item> itemListOld = persistentMarca.getItemList();
            List<Item> itemListNew = marca.getItemList();
            List<Equipo> equipoListOld = persistentMarca.getEquipoList();
            List<Equipo> equipoListNew = marca.getEquipoList();
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getIdItem());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            marca.setItemList(itemListNew);
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            marca.setEquipoList(equipoListNew);
            marca = em.merge(marca);
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    itemListOldItem.getMarcaList().remove(marca);
                    itemListOldItem = em.merge(itemListOldItem);
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    itemListNewItem.getMarcaList().add(marca);
                    itemListNewItem = em.merge(itemListNewItem);
                }
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setMarca(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Marca oldMarcaOfEquipoListNewEquipo = equipoListNewEquipo.getMarca();
                    equipoListNewEquipo.setMarca(marca);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldMarcaOfEquipoListNewEquipo != null && !oldMarcaOfEquipoListNewEquipo.equals(marca)) {
                        oldMarcaOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldMarcaOfEquipoListNewEquipo = em.merge(oldMarcaOfEquipoListNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = marca.getIdMarca();
                if (findMarca(id) == null) {
                    throw new NonexistentEntityException("The marca with id " + id + " no longer exists.");
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
            Marca marca;
            try {
                marca = em.getReference(Marca.class, id);
                marca.getIdMarca();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The marca with id " + id + " no longer exists.", enfe);
            }
            List<Item> itemList = marca.getItemList();
            for (Item itemListItem : itemList) {
                itemListItem.getMarcaList().remove(marca);
                itemListItem = em.merge(itemListItem);
            }
            List<Equipo> equipoList = marca.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setMarca(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            em.remove(marca);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Marca> findMarcaEntities() {
        return findMarcaEntities(true, -1, -1);
    }

    public List<Marca> findMarcaEntities(int maxResults, int firstResult) {
        return findMarcaEntities(false, maxResults, firstResult);
    }

    private List<Marca> findMarcaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Marca.class));
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

    public Marca findMarca(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Marca.class, id);
        } finally {
            em.close();
        }
    }

    public int getMarcaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Marca> rt = cq.from(Marca.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
