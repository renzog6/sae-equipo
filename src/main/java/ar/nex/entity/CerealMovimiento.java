/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "cer_movimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CerealMovimiento.findAll", query = "SELECT c FROM CerealMovimiento c")
    , @NamedQuery(name = "CerealMovimiento.findByIdMovimiento", query = "SELECT c FROM CerealMovimiento c WHERE c.idMovimiento = :idMovimiento")
    , @NamedQuery(name = "CerealMovimiento.findByDestino", query = "SELECT c FROM CerealMovimiento c WHERE c.destino = :destino")
    , @NamedQuery(name = "CerealMovimiento.findByFecha", query = "SELECT c FROM CerealMovimiento c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "CerealMovimiento.findByOrigen", query = "SELECT c FROM CerealMovimiento c WHERE c.origen = :origen")
    , @NamedQuery(name = "CerealMovimiento.findByTotal", query = "SELECT c FROM CerealMovimiento c WHERE c.total = :total")
    , @NamedQuery(name = "CerealMovimiento.findByUbicacion", query = "SELECT c FROM CerealMovimiento c WHERE c.ubicacion = :ubicacion")})
public class CerealMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_movimiento")
    private Long idMovimiento;
    @Column(name = "destino")
    private String destino;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "origen")
    private String origen;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private Double total;
    @Column(name = "ubicacion")
    private String ubicacion;

    public CerealMovimiento() {
    }

    public CerealMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimiento != null ? idMovimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CerealMovimiento)) {
            return false;
        }
        CerealMovimiento other = (CerealMovimiento) object;
        if ((this.idMovimiento == null && other.idMovimiento != null) || (this.idMovimiento != null && !this.idMovimiento.equals(other.idMovimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.CerealMovimiento[ idMovimiento=" + idMovimiento + " ]";
    }
    
}
