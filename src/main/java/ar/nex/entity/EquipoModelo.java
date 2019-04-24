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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "eq_modelo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipoModelo.findAll", query = "SELECT e FROM EquipoModelo e")
    , @NamedQuery(name = "EquipoModelo.findByIdModelo", query = "SELECT e FROM EquipoModelo e WHERE e.idModelo = :idModelo")
    , @NamedQuery(name = "EquipoModelo.findByNombre", query = "SELECT e FROM EquipoModelo e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EquipoModelo.findByDescripcion", query = "SELECT e FROM EquipoModelo e WHERE e.descripcion = :descripcion")})
public class EquipoModelo implements Serializable {

    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    @ManyToOne
    private EquipoTipo idTipo;

    @JoinTable(name = "ped_repuesto_eq_modelo", joinColumns = {
        @JoinColumn(name = "id_modelo", referencedColumnName = "id_modelo")}, inverseJoinColumns = {
        @JoinColumn(name = "id_repuesto", referencedColumnName = "id_repuesto")})
    @ManyToMany
    private List<Repuesto> repuestoList;

    @Column(name = "anio")
    private Integer anio;
    @Column(name = "info")
    private String info;


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_modelo")
    private Long idModelo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "modelo")
    private List<Equipo> equipoList;

    public EquipoModelo() {
    }

    public EquipoModelo(Long idModelo) {
        this.idModelo = idModelo;
    }

    public Long getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Long idModelo) {
        this.idModelo = idModelo;
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
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModelo != null ? idModelo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoModelo)) {
            return false;
        }
        EquipoModelo other = (EquipoModelo) object;
        if ((this.idModelo == null && other.idModelo != null) || (this.idModelo != null && !this.idModelo.equals(other.idModelo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @XmlTransient
    public List<Repuesto> getRepuestoList() {
        return repuestoList;
    }

    public void setRepuestoList(List<Repuesto> repuestoList) {
        this.repuestoList = repuestoList;
    }

    public EquipoTipo getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(EquipoTipo idTipo) {
        this.idTipo = idTipo;
    }


}
