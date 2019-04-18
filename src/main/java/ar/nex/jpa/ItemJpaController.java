/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.Item;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Marca;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        if (item.getMarcaList() == null) {
            item.setMarcaList(new ArrayList<Marca>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Marca> attachedMarcaList = new ArrayList<Marca>();
            for (Marca marcaListMarcaToAttach : item.getMarcaList()) {
                marcaListMarcaToAttach = em.getReference(marcaListMarcaToAttach.getClass(), marcaListMarcaToAttach.getIdMarca());
                attachedMarcaList.add(marcaListMarcaToAttach);
            }
            item.setMarcaList(attachedMarcaList);
            em.persist(item);
            for (Marca marcaListMarca : item.getMarcaList()) {
                marcaListMarca.getItemList().add(item);
                marcaListMarca = em.merge(marcaListMarca);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getIdItem());
            List<Marca> marcaListOld = persistentItem.getMarcaList();
            List<Marca> marcaListNew = item.getMarcaList();
            List<Marca> attachedMarcaListNew = new ArrayList<Marca>();
            for (Marca marcaListNewMarcaToAttach : marcaListNew) {
                marcaListNewMarcaToAttach = em.getReference(marcaListNewMarcaToAttach.getClass(), marcaListNewMarcaToAttach.getIdMarca());
                attachedMarcaListNew.add(marcaListNewMarcaToAttach);
            }
            marcaListNew = attachedMarcaListNew;
            item.setMarcaList(marcaListNew);
            item = em.merge(item);
            for (Marca marcaListOldMarca : marcaListOld) {
                if (!marcaListNew.contains(marcaListOldMarca)) {
                    marcaListOldMarca.getItemList().remove(item);
                    marcaListOldMarca = em.merge(marcaListOldMarca);
                }
            }
            for (Marca marcaListNewMarca : marcaListNew) {
                if (!marcaListOld.contains(marcaListNewMarca)) {
                    marcaListNewMarca.getItemList().add(item);
                    marcaListNewMarca = em.merge(marcaListNewMarca);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = item.getIdItem();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getIdItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<Marca> marcaList = item.getMarcaList();
            for (Marca marcaListMarca : marcaList) {
                marcaListMarca.getItemList().remove(item);
                marcaListMarca = em.merge(marcaListMarca);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
