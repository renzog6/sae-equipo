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
import ar.nex.entity.Direccion;
import ar.nex.entity.Rubro;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.Contacto;
import ar.nex.entity.Empresa;
import ar.nex.entity.Equipo;
import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.jpa.exceptions.IllegalOrphanException;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) throws IllegalOrphanException {
        if (empresa.getRubroList() == null) {
            empresa.setRubroList(new ArrayList<Rubro>());
        }
        if (empresa.getContactoList() == null) {
            empresa.setContactoList(new ArrayList<Contacto>());
        }
        if (empresa.getEquipoList() == null) {
            empresa.setEquipoList(new ArrayList<Equipo>());
        }
        if (empresa.getPedidoList() == null) {
            empresa.setPedidoList(new ArrayList<Pedido>());
        }
        if (empresa.getRepuestoList() == null) {
            empresa.setRepuestoList(new ArrayList<Repuesto>());
        }
        List<String> illegalOrphanMessages = null;
        Direccion direccionOrphanCheck = empresa.getDireccion();
        if (direccionOrphanCheck != null) {
            Empresa oldEmpresaOfDireccion = direccionOrphanCheck.getEmpresa();
            if (oldEmpresaOfDireccion != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Direccion " + direccionOrphanCheck + " already has an item of type Empresa whose direccion column cannot be null. Please make another selection for the direccion field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion = empresa.getDireccion();
            if (direccion != null) {
                direccion = em.getReference(direccion.getClass(), direccion.getIdDireccion());
                empresa.setDireccion(direccion);
            }
            List<Rubro> attachedRubroList = new ArrayList<Rubro>();
            for (Rubro rubroListRubroToAttach : empresa.getRubroList()) {
                rubroListRubroToAttach = em.getReference(rubroListRubroToAttach.getClass(), rubroListRubroToAttach.getIdRubro());
                attachedRubroList.add(rubroListRubroToAttach);
            }
            empresa.setRubroList(attachedRubroList);
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : empresa.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            empresa.setContactoList(attachedContactoList);
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : empresa.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            empresa.setEquipoList(attachedEquipoList);
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : empresa.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getIdPedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            empresa.setPedidoList(attachedPedidoList);
            List<Repuesto> attachedRepuestoList = new ArrayList<Repuesto>();
            for (Repuesto repuestoListRepuestoToAttach : empresa.getRepuestoList()) {
                repuestoListRepuestoToAttach = em.getReference(repuestoListRepuestoToAttach.getClass(), repuestoListRepuestoToAttach.getIdRepuesto());
                attachedRepuestoList.add(repuestoListRepuestoToAttach);
            }
            empresa.setRepuestoList(attachedRepuestoList);
            em.persist(empresa);
            if (direccion != null) {
                direccion.setEmpresa(empresa);
                direccion = em.merge(direccion);
            }
            for (Rubro rubroListRubro : empresa.getRubroList()) {
                rubroListRubro.getEmpresaList().add(empresa);
                rubroListRubro = em.merge(rubroListRubro);
            }
            for (Contacto contactoListContacto : empresa.getContactoList()) {
                contactoListContacto.getEmpresaList().add(empresa);
                contactoListContacto = em.merge(contactoListContacto);
            }
            for (Equipo equipoListEquipo : empresa.getEquipoList()) {
                Empresa oldEmpresaOfEquipoListEquipo = equipoListEquipo.getEmpresa();
                equipoListEquipo.setEmpresa(empresa);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldEmpresaOfEquipoListEquipo != null) {
                    oldEmpresaOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldEmpresaOfEquipoListEquipo = em.merge(oldEmpresaOfEquipoListEquipo);
                }
            }
            for (Pedido pedidoListPedido : empresa.getPedidoList()) {
                Empresa oldEmpresaOfPedidoListPedido = pedidoListPedido.getEmpresa();
                pedidoListPedido.setEmpresa(empresa);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldEmpresaOfPedidoListPedido != null) {
                    oldEmpresaOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldEmpresaOfPedidoListPedido = em.merge(oldEmpresaOfPedidoListPedido);
                }
            }
            for (Repuesto repuestoListRepuesto : empresa.getRepuestoList()) {
                repuestoListRepuesto.getEmpresaList().add(empresa);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            Direccion direccionOld = persistentEmpresa.getDireccion();
            Direccion direccionNew = empresa.getDireccion();
            List<Rubro> rubroListOld = persistentEmpresa.getRubroList();
            List<Rubro> rubroListNew = empresa.getRubroList();
            List<Contacto> contactoListOld = persistentEmpresa.getContactoList();
            List<Contacto> contactoListNew = empresa.getContactoList();
            List<Equipo> equipoListOld = persistentEmpresa.getEquipoList();
            List<Equipo> equipoListNew = empresa.getEquipoList();
            List<Pedido> pedidoListOld = persistentEmpresa.getPedidoList();
            List<Pedido> pedidoListNew = empresa.getPedidoList();
            List<Repuesto> repuestoListOld = persistentEmpresa.getRepuestoList();
            List<Repuesto> repuestoListNew = empresa.getRepuestoList();
            List<String> illegalOrphanMessages = null;
            if (direccionNew != null && !direccionNew.equals(direccionOld)) {
                Empresa oldEmpresaOfDireccion = direccionNew.getEmpresa();
                if (oldEmpresaOfDireccion != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Direccion " + direccionNew + " already has an item of type Empresa whose direccion column cannot be null. Please make another selection for the direccion field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (direccionNew != null) {
                direccionNew = em.getReference(direccionNew.getClass(), direccionNew.getIdDireccion());
                empresa.setDireccion(direccionNew);
            }
            List<Rubro> attachedRubroListNew = new ArrayList<Rubro>();
            for (Rubro rubroListNewRubroToAttach : rubroListNew) {
                rubroListNewRubroToAttach = em.getReference(rubroListNewRubroToAttach.getClass(), rubroListNewRubroToAttach.getIdRubro());
                attachedRubroListNew.add(rubroListNewRubroToAttach);
            }
            rubroListNew = attachedRubroListNew;
            empresa.setRubroList(rubroListNew);
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            empresa.setContactoList(contactoListNew);
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            empresa.setEquipoList(equipoListNew);
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getIdPedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            empresa.setPedidoList(pedidoListNew);
            List<Repuesto> attachedRepuestoListNew = new ArrayList<Repuesto>();
            for (Repuesto repuestoListNewRepuestoToAttach : repuestoListNew) {
                repuestoListNewRepuestoToAttach = em.getReference(repuestoListNewRepuestoToAttach.getClass(), repuestoListNewRepuestoToAttach.getIdRepuesto());
                attachedRepuestoListNew.add(repuestoListNewRepuestoToAttach);
            }
            repuestoListNew = attachedRepuestoListNew;
            empresa.setRepuestoList(repuestoListNew);
            empresa = em.merge(empresa);
            if (direccionOld != null && !direccionOld.equals(direccionNew)) {
                direccionOld.setEmpresa(null);
                direccionOld = em.merge(direccionOld);
            }
            if (direccionNew != null && !direccionNew.equals(direccionOld)) {
                direccionNew.setEmpresa(empresa);
                direccionNew = em.merge(direccionNew);
            }
            for (Rubro rubroListOldRubro : rubroListOld) {
                if (!rubroListNew.contains(rubroListOldRubro)) {
                    rubroListOldRubro.getEmpresaList().remove(empresa);
                    rubroListOldRubro = em.merge(rubroListOldRubro);
                }
            }
            for (Rubro rubroListNewRubro : rubroListNew) {
                if (!rubroListOld.contains(rubroListNewRubro)) {
                    rubroListNewRubro.getEmpresaList().add(empresa);
                    rubroListNewRubro = em.merge(rubroListNewRubro);
                }
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getEmpresaList().remove(empresa);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getEmpresaList().add(empresa);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                }
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setEmpresa(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Empresa oldEmpresaOfEquipoListNewEquipo = equipoListNewEquipo.getEmpresa();
                    equipoListNewEquipo.setEmpresa(empresa);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldEmpresaOfEquipoListNewEquipo != null && !oldEmpresaOfEquipoListNewEquipo.equals(empresa)) {
                        oldEmpresaOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldEmpresaOfEquipoListNewEquipo = em.merge(oldEmpresaOfEquipoListNewEquipo);
                    }
                }
            }
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    pedidoListOldPedido.setEmpresa(null);
                    pedidoListOldPedido = em.merge(pedidoListOldPedido);
                }
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Empresa oldEmpresaOfPedidoListNewPedido = pedidoListNewPedido.getEmpresa();
                    pedidoListNewPedido.setEmpresa(empresa);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldEmpresaOfPedidoListNewPedido != null && !oldEmpresaOfPedidoListNewPedido.equals(empresa)) {
                        oldEmpresaOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldEmpresaOfPedidoListNewPedido = em.merge(oldEmpresaOfPedidoListNewPedido);
                    }
                }
            }
            for (Repuesto repuestoListOldRepuesto : repuestoListOld) {
                if (!repuestoListNew.contains(repuestoListOldRepuesto)) {
                    repuestoListOldRepuesto.getEmpresaList().remove(empresa);
                    repuestoListOldRepuesto = em.merge(repuestoListOldRepuesto);
                }
            }
            for (Repuesto repuestoListNewRepuesto : repuestoListNew) {
                if (!repuestoListOld.contains(repuestoListNewRepuesto)) {
                    repuestoListNewRepuesto.getEmpresaList().add(empresa);
                    repuestoListNewRepuesto = em.merge(repuestoListNewRepuesto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            Direccion direccion = empresa.getDireccion();
            if (direccion != null) {
                direccion.setEmpresa(null);
                direccion = em.merge(direccion);
            }
            List<Rubro> rubroList = empresa.getRubroList();
            for (Rubro rubroListRubro : rubroList) {
                rubroListRubro.getEmpresaList().remove(empresa);
                rubroListRubro = em.merge(rubroListRubro);
            }
            List<Contacto> contactoList = empresa.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getEmpresaList().remove(empresa);
                contactoListContacto = em.merge(contactoListContacto);
            }
            List<Equipo> equipoList = empresa.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setEmpresa(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<Pedido> pedidoList = empresa.getPedidoList();
            for (Pedido pedidoListPedido : pedidoList) {
                pedidoListPedido.setEmpresa(null);
                pedidoListPedido = em.merge(pedidoListPedido);
            }
            List<Repuesto> repuestoList = empresa.getRepuestoList();
            for (Repuesto repuestoListRepuesto : repuestoList) {
                repuestoListRepuesto.getEmpresaList().remove(empresa);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
