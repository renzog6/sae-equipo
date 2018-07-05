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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "dir_localidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidad.findAll", query = "SELECT l FROM Localidad l")
    , @NamedQuery(name = "Localidad.findByIdLocalidad", query = "SELECT l FROM Localidad l WHERE l.idLocalidad = :idLocalidad")
    , @NamedQuery(name = "Localidad.findByNombre", query = "SELECT l FROM Localidad l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "Localidad.findByCodigoPostal", query = "SELECT l FROM Localidad l WHERE l.codigoPostal = :codigoPostal")})
public class Localidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_localidad")
    private Long idLocalidad;
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "codigo_postal")
    private int codigoPostal;
    @JoinColumn(name = "provincia_id", referencedColumnName = "id_provincia")
    @ManyToOne
    private Provincia provinciaId;
    @OneToMany(mappedBy = "localidadId")
    private List<Direccion> direccionList;

    public Localidad() {
    }

    public Localidad(Long idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public Localidad(Long idLocalidad, int codigoPostal) {
        this.idLocalidad = idLocalidad;
        this.codigoPostal = codigoPostal;
    }

    public Long getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(Long idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Provincia getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Provincia provinciaId) {
        this.provinciaId = provinciaId;
    }

    @XmlTransient
    public List<Direccion> getDireccionList() {
        return direccionList;
    }

    public void setDireccionList(List<Direccion> direccionList) {
        this.direccionList = direccionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocalidad != null ? idLocalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localidad)) {
            return false;
        }
        Localidad other = (Localidad) object;
        if ((this.idLocalidad == null && other.idLocalidad != null) || (this.idLocalidad != null && !this.idLocalidad.equals(other.idLocalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Localidad[ idLocalidad=" + idLocalidad + " ]";
    }
    
}
