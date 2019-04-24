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
@Table(name = "eq_tipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipoTipo.findAll", query = "SELECT e FROM EquipoTipo e")
    , @NamedQuery(name = "EquipoTipo.findByIdTipo", query = "SELECT e FROM EquipoTipo e WHERE e.idTipo = :idTipo")
    , @NamedQuery(name = "EquipoTipo.findByNombre", query = "SELECT e FROM EquipoTipo e WHERE e.nombre = :nombre")})
public class EquipoTipo implements Serializable {

    @OneToMany(mappedBy = "idTipo")
    private List<EquipoModelo> equipoModeloList;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private EquipoCategoria idCategoria;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo")
    private Long idTipo;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "tipo")
    private List<Equipo> equipoList;

    public EquipoTipo() {
    }

    public EquipoTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoTipo)) {
            return false;
        }
        EquipoTipo other = (EquipoTipo) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @XmlTransient
    public List<EquipoModelo> getEquipoModeloList() {
        return equipoModeloList;
    }

    public void setEquipoModeloList(List<EquipoModelo> equipoModeloList) {
        this.equipoModeloList = equipoModeloList;
    }

    public EquipoCategoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(EquipoCategoria idCategoria) {
        this.idCategoria = idCategoria;
    }
    
}
