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
import ar.nex.entity.EquipoCompraVenta;
import ar.nex.entity.Empresa;
import ar.nex.entity.Equipo;
import ar.nex.entity.Marca;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.entity.Repuesto;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.StockDetalle;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoJpaController implements Serializable {

    public EquipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Equipo equipo) {
        if (equipo.getRepuestoList() == null) {
            equipo.setRepuestoList(new ArrayList<Repuesto>());
        }
        if (equipo.getStockDetalleList() == null) {
            equipo.setStockDetalleList(new ArrayList<StockDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoCategoria categoria = equipo.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getIdCategoria());
                equipo.setCategoria(categoria);
            }
            EquipoCompraVenta compraVenta = equipo.getCompraVenta();
            if (compraVenta != null) {
                compraVenta = em.getReference(compraVenta.getClass(), compraVenta.getIdCompraVenta());
                equipo.setCompraVenta(compraVenta);
            }
            Empresa empresa = equipo.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                equipo.setEmpresa(empresa);
            }
            Marca marca = equipo.getMarca();
            if (marca != null) {
                marca = em.getReference(marca.getClass(), marca.getIdMarca());
                equipo.setMarca(marca);
            }
            EquipoModelo modelo = equipo.getModelo();
            if (modelo != null) {
                modelo = em.getReference(modelo.getClass(), modelo.getIdModelo());
                equipo.setModelo(modelo);
            }
            EquipoTipo tipo = equipo.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getIdTipo());
                equipo.setTipo(tipo);
            }
            List<Repuesto> attachedRepuestoList = new ArrayList<Repuesto>();
            for (Repuesto repuestoListRepuestoToAttach : equipo.getRepuestoList()) {
                repuestoListRepuestoToAttach = em.getReference(repuestoListRepuestoToAttach.getClass(), repuestoListRepuestoToAttach.getIdRepuesto());
                attachedRepuestoList.add(repuestoListRepuestoToAttach);
            }
            equipo.setRepuestoList(attachedRepuestoList);
            List<StockDetalle> attachedStockDetalleList = new ArrayList<StockDetalle>();
            for (StockDetalle stockDetalleListStockDetalleToAttach : equipo.getStockDetalleList()) {
                stockDetalleListStockDetalleToAttach = em.getReference(stockDetalleListStockDetalleToAttach.getClass(), stockDetalleListStockDetalleToAttach.getIdStock());
                attachedStockDetalleList.add(stockDetalleListStockDetalleToAttach);
            }
            equipo.setStockDetalleList(attachedStockDetalleList);
            em.persist(equipo);
            if (categoria != null) {
                categoria.getEquipoList().add(equipo);
                categoria = em.merge(categoria);
            }
            if (compraVenta != null) {
                compraVenta.getEquipoList().add(equipo);
                compraVenta = em.merge(compraVenta);
            }
            if (empresa != null) {
                empresa.getEquipoList().add(equipo);
                empresa = em.merge(empresa);
            }
            if (marca != null) {
                marca.getEquipoList().add(equipo);
                marca = em.merge(marca);
            }
            if (modelo != null) {
                modelo.getEquipoList().add(equipo);
                modelo = em.merge(modelo);
            }
            if (tipo != null) {
                tipo.getEquipoList().add(equipo);
                tipo = em.merge(tipo);
            }
            for (Repuesto repuestoListRepuesto : equipo.getRepuestoList()) {
                repuestoListRepuesto.getEquipoList().add(equipo);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            for (StockDetalle stockDetalleListStockDetalle : equipo.getStockDetalleList()) {
                Equipo oldEquipoOfStockDetalleListStockDetalle = stockDetalleListStockDetalle.getEquipo();
                stockDetalleListStockDetalle.setEquipo(equipo);
                stockDetalleListStockDetalle = em.merge(stockDetalleListStockDetalle);
                if (oldEquipoOfStockDetalleListStockDetalle != null) {
                    oldEquipoOfStockDetalleListStockDetalle.getStockDetalleList().remove(stockDetalleListStockDetalle);
                    oldEquipoOfStockDetalleListStockDetalle = em.merge(oldEquipoOfStockDetalleListStockDetalle);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Equipo equipo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo persistentEquipo = em.find(Equipo.class, equipo.getIdEquipo());
            EquipoCategoria categoriaOld = persistentEquipo.getCategoria();
            EquipoCategoria categoriaNew = equipo.getCategoria();
            EquipoCompraVenta compraVentaOld = persistentEquipo.getCompraVenta();
            EquipoCompraVenta compraVentaNew = equipo.getCompraVenta();
            Empresa empresaOld = persistentEquipo.getEmpresa();
            Empresa empresaNew = equipo.getEmpresa();
            Marca marcaOld = persistentEquipo.getMarca();
            Marca marcaNew = equipo.getMarca();
            EquipoModelo modeloOld = persistentEquipo.getModelo();
            EquipoModelo modeloNew = equipo.getModelo();
            EquipoTipo tipoOld = persistentEquipo.getTipo();
            EquipoTipo tipoNew = equipo.getTipo();
            List<Repuesto> repuestoListOld = persistentEquipo.getRepuestoList();
            List<Repuesto> repuestoListNew = equipo.getRepuestoList();
            List<StockDetalle> stockDetalleListOld = persistentEquipo.getStockDetalleList();
            List<StockDetalle> stockDetalleListNew = equipo.getStockDetalleList();
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getIdCategoria());
                equipo.setCategoria(categoriaNew);
            }
            if (compraVentaNew != null) {
                compraVentaNew = em.getReference(compraVentaNew.getClass(), compraVentaNew.getIdCompraVenta());
                equipo.setCompraVenta(compraVentaNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                equipo.setEmpresa(empresaNew);
            }
            if (marcaNew != null) {
                marcaNew = em.getReference(marcaNew.getClass(), marcaNew.getIdMarca());
                equipo.setMarca(marcaNew);
            }
            if (modeloNew != null) {
                modeloNew = em.getReference(modeloNew.getClass(), modeloNew.getIdModelo());
                equipo.setModelo(modeloNew);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getIdTipo());
                equipo.setTipo(tipoNew);
            }
            List<Repuesto> attachedRepuestoListNew = new ArrayList<Repuesto>();
            for (Repuesto repuestoListNewRepuestoToAttach : repuestoListNew) {
                repuestoListNewRepuestoToAttach = em.getReference(repuestoListNewRepuestoToAttach.getClass(), repuestoListNewRepuestoToAttach.getIdRepuesto());
                attachedRepuestoListNew.add(repuestoListNewRepuestoToAttach);
            }
            repuestoListNew = attachedRepuestoListNew;
            equipo.setRepuestoList(repuestoListNew);
            List<StockDetalle> attachedStockDetalleListNew = new ArrayList<StockDetalle>();
            for (StockDetalle stockDetalleListNewStockDetalleToAttach : stockDetalleListNew) {
                stockDetalleListNewStockDetalleToAttach = em.getReference(stockDetalleListNewStockDetalleToAttach.getClass(), stockDetalleListNewStockDetalleToAttach.getIdStock());
                attachedStockDetalleListNew.add(stockDetalleListNewStockDetalleToAttach);
            }
            stockDetalleListNew = attachedStockDetalleListNew;
            equipo.setStockDetalleList(stockDetalleListNew);
            equipo = em.merge(equipo);
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getEquipoList().remove(equipo);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getEquipoList().add(equipo);
                categoriaNew = em.merge(categoriaNew);
            }
            if (compraVentaOld != null && !compraVentaOld.equals(compraVentaNew)) {
                compraVentaOld.getEquipoList().remove(equipo);
                compraVentaOld = em.merge(compraVentaOld);
            }
            if (compraVentaNew != null && !compraVentaNew.equals(compraVentaOld)) {
                compraVentaNew.getEquipoList().add(equipo);
                compraVentaNew = em.merge(compraVentaNew);
            }
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getEquipoList().remove(equipo);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getEquipoList().add(equipo);
                empresaNew = em.merge(empresaNew);
            }
            if (marcaOld != null && !marcaOld.equals(marcaNew)) {
                marcaOld.getEquipoList().remove(equipo);
                marcaOld = em.merge(marcaOld);
            }
            if (marcaNew != null && !marcaNew.equals(marcaOld)) {
                marcaNew.getEquipoList().add(equipo);
                marcaNew = em.merge(marcaNew);
            }
            if (modeloOld != null && !modeloOld.equals(modeloNew)) {
                modeloOld.getEquipoList().remove(equipo);
                modeloOld = em.merge(modeloOld);
            }
            if (modeloNew != null && !modeloNew.equals(modeloOld)) {
                modeloNew.getEquipoList().add(equipo);
                modeloNew = em.merge(modeloNew);
            }
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getEquipoList().remove(equipo);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getEquipoList().add(equipo);
                tipoNew = em.merge(tipoNew);
            }
            for (Repuesto repuestoListOldRepuesto : repuestoListOld) {
                if (!repuestoListNew.contains(repuestoListOldRepuesto)) {
                    repuestoListOldRepuesto.getEquipoList().remove(equipo);
                    repuestoListOldRepuesto = em.merge(repuestoListOldRepuesto);
                }
            }
            for (Repuesto repuestoListNewRepuesto : repuestoListNew) {
                if (!repuestoListOld.contains(repuestoListNewRepuesto)) {
                    repuestoListNewRepuesto.getEquipoList().add(equipo);
                    repuestoListNewRepuesto = em.merge(repuestoListNewRepuesto);
                }
            }
            for (StockDetalle stockDetalleListOldStockDetalle : stockDetalleListOld) {
                if (!stockDetalleListNew.contains(stockDetalleListOldStockDetalle)) {
                    stockDetalleListOldStockDetalle.setEquipo(null);
                    stockDetalleListOldStockDetalle = em.merge(stockDetalleListOldStockDetalle);
                }
            }
            for (StockDetalle stockDetalleListNewStockDetalle : stockDetalleListNew) {
                if (!stockDetalleListOld.contains(stockDetalleListNewStockDetalle)) {
                    Equipo oldEquipoOfStockDetalleListNewStockDetalle = stockDetalleListNewStockDetalle.getEquipo();
                    stockDetalleListNewStockDetalle.setEquipo(equipo);
                    stockDetalleListNewStockDetalle = em.merge(stockDetalleListNewStockDetalle);
                    if (oldEquipoOfStockDetalleListNewStockDetalle != null && !oldEquipoOfStockDetalleListNewStockDetalle.equals(equipo)) {
                        oldEquipoOfStockDetalleListNewStockDetalle.getStockDetalleList().remove(stockDetalleListNewStockDetalle);
                        oldEquipoOfStockDetalleListNewStockDetalle = em.merge(oldEquipoOfStockDetalleListNewStockDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipo.getIdEquipo();
                if (findEquipo(id) == null) {
                    throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.");
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
            Equipo equipo;
            try {
                equipo = em.getReference(Equipo.class, id);
                equipo.getIdEquipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.", enfe);
            }
            EquipoCategoria categoria = equipo.getCategoria();
            if (categoria != null) {
                categoria.getEquipoList().remove(equipo);
                categoria = em.merge(categoria);
            }
            EquipoCompraVenta compraVenta = equipo.getCompraVenta();
            if (compraVenta != null) {
                compraVenta.getEquipoList().remove(equipo);
                compraVenta = em.merge(compraVenta);
            }
            Empresa empresa = equipo.getEmpresa();
            if (empresa != null) {
                empresa.getEquipoList().remove(equipo);
                empresa = em.merge(empresa);
            }
            Marca marca = equipo.getMarca();
            if (marca != null) {
                marca.getEquipoList().remove(equipo);
                marca = em.merge(marca);
            }
            EquipoModelo modelo = equipo.getModelo();
            if (modelo != null) {
                modelo.getEquipoList().remove(equipo);
                modelo = em.merge(modelo);
            }
            EquipoTipo tipo = equipo.getTipo();
            if (tipo != null) {
                tipo.getEquipoList().remove(equipo);
                tipo = em.merge(tipo);
            }
            List<Repuesto> repuestoList = equipo.getRepuestoList();
            for (Repuesto repuestoListRepuesto : repuestoList) {
                repuestoListRepuesto.getEquipoList().remove(equipo);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            List<StockDetalle> stockDetalleList = equipo.getStockDetalleList();
            for (StockDetalle stockDetalleListStockDetalle : stockDetalleList) {
                stockDetalleListStockDetalle.setEquipo(null);
                stockDetalleListStockDetalle = em.merge(stockDetalleListStockDetalle);
            }
            em.remove(equipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Equipo> findEquipoEntities() {
        return findEquipoEntities(true, -1, -1);
    }

    public List<Equipo> findEquipoEntities(int maxResults, int firstResult) {
        return findEquipoEntities(false, maxResults, firstResult);
    }

    private List<Equipo> findEquipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Equipo.class));
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

    public Equipo findEquipo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Equipo> rt = cq.from(Equipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
