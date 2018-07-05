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
@Table(name = "rubro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rubro.findAll", query = "SELECT r FROM Rubro r")
    , @NamedQuery(name = "Rubro.findByIdRubro", query = "SELECT r FROM Rubro r WHERE r.idRubro = :idRubro")
    , @NamedQuery(name = "Rubro.findByNombre", query = "SELECT r FROM Rubro r WHERE r.nombre = :nombre")
    , @NamedQuery(name = "Rubro.findByDescripcion", query = "SELECT r FROM Rubro r WHERE r.descripcion = :descripcion")})
public class Rubro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_rubro")
    private Long idRubro;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToMany(mappedBy = "rubroList")
    private List<Empresa> empresaList;

    public Rubro() {
    }

    public Rubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRubro != null ? idRubro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubro)) {
            return false;
        }
        Rubro other = (Rubro) object;
        if ((this.idRubro == null && other.idRubro != null) || (this.idRubro != null && !this.idRubro.equals(other.idRubro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Rubro[ idRubro=" + idRubro + " ]";
    }
    
}
