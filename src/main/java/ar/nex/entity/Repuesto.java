/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "ped_repuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repuesto.findAll", query = "SELECT r FROM Repuesto r")
    , @NamedQuery(name = "Repuesto.findByIdRepuesto", query = "SELECT r FROM Repuesto r WHERE r.idRepuesto = :idRepuesto")
    , @NamedQuery(name = "Repuesto.findByCodigo", query = "SELECT r FROM Repuesto r WHERE r.codigo = :codigo")
    , @NamedQuery(name = "Repuesto.findByDescripcion", query = "SELECT r FROM Repuesto r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "Repuesto.findByMarca", query = "SELECT r FROM Repuesto r WHERE r.marca = :marca")
    , @NamedQuery(name = "Repuesto.findByObsercacion", query = "SELECT r FROM Repuesto r WHERE r.obsercacion = :obsercacion")})
public class Repuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_repuesto")
    private Long idRepuesto;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "marca")
    private String marca;
    @Column(name = "obsercacion")
    private String obsercacion;
    @ManyToMany(mappedBy = "repuestoList")
    private List<Pedido> pedidoList;

    public Repuesto() {
    }

    public Repuesto(Long idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public Long getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Long idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getObsercacion() {
        return obsercacion;
    }

    public void setObsercacion(String obsercacion) {
        this.obsercacion = obsercacion;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRepuesto != null ? idRepuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Repuesto)) {
            return false;
        }
        Repuesto other = (Repuesto) object;
        if ((this.idRepuesto == null && other.idRepuesto != null) || (this.idRepuesto != null && !this.idRepuesto.equals(other.idRepuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Repuesto[ idRepuesto=" + idRepuesto + " ]";
    }
    
}
