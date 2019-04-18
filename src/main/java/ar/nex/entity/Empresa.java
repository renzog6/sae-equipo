/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "empresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e")
    , @NamedQuery(name = "Empresa.findByIdEmpresa", query = "SELECT e FROM Empresa e WHERE e.idEmpresa = :idEmpresa")
    , @NamedQuery(name = "Empresa.findByCuit", query = "SELECT e FROM Empresa e WHERE e.cuit = :cuit")
    , @NamedQuery(name = "Empresa.findByNombre", query = "SELECT e FROM Empresa e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Empresa.findByObservacion", query = "SELECT e FROM Empresa e WHERE e.observacion = :observacion")
    , @NamedQuery(name = "Empresa.findByRazonSocial", query = "SELECT e FROM Empresa e WHERE e.razonSocial = :razonSocial")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Long idEmpresa;
    @Column(name = "cuit")
    private String cuit;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "razon_social")
    private String razonSocial;

    @JoinTable(name = "empresa_rubro", joinColumns = {
        @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")}, inverseJoinColumns = {
        @JoinColumn(name = "id_rubro", referencedColumnName = "id_rubro")})
    @ManyToMany
    private List<Rubro> rubroList;

    @ManyToMany(mappedBy = "empresaList")
    private List<Contacto> contactoList;

    @ManyToMany(mappedBy = "empresaList")
    private List<Repuesto> repuestoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Equipo> equipoList;

    @OneToMany(mappedBy = "empresa")
    private List<Pedido> pedidoList;

    @JoinColumn(name = "direccion_id", referencedColumnName = "id_direccion")
    @OneToOne(optional = false)
    private Direccion direccion;

    public Empresa() {
    }

    public Empresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @XmlTransient
    public List<Rubro> getRubroList() {
        return rubroList;
    }

    public void setRubroList(List<Rubro> rubroList) {
        this.rubroList = rubroList;
    }

    @XmlTransient
    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @XmlTransient
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @XmlTransient
    public List<Repuesto> getRepuestoList() {
        return repuestoList;
    }

    public void setRepuestoList(List<Repuesto> repuestoList) {
        this.repuestoList = repuestoList;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
