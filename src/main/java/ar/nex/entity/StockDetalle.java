package ar.nex.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ped_stock_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockDetalle.findAll", query = "SELECT s FROM StockDetalle s"),
    @NamedQuery(name = "StockDetalle.findByIdStock", query = "SELECT s FROM StockDetalle s WHERE s.idStock = :idStock"),
    @NamedQuery(name = "StockDetalle.findByFecha", query = "SELECT s FROM StockDetalle s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "StockDetalle.findByDetalle", query = "SELECT s FROM StockDetalle s WHERE s.detalle = :detalle"),
    @NamedQuery(name = "StockDetalle.findByUsuario", query = "SELECT s FROM StockDetalle s WHERE s.usuario = :usuario"),
    @NamedQuery(name = "StockDetalle.findByInfo", query = "SELECT s FROM StockDetalle s WHERE s.info = :info")})
public class StockDetalle implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Double cantidad;
    @Column(name = "estado")
    private Integer estado;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_stock")
    private Long idStock;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Column(name = "info")
    private String info;
    @JoinColumn(name = "equipo", referencedColumnName = "id_equipo")
    @ManyToOne
    private Equipo equipo;
    @JoinColumn(name = "repuesto", referencedColumnName = "id_repuesto")
    @ManyToOne
    private Repuesto repuesto;

    public StockDetalle() {
    }

    public StockDetalle(Date fecha) {
        this.fecha = fecha;
    }
    
    public StockDetalle(Long idStock) {
        this.idStock = idStock;
    }

    public Long getIdStock() {
        return idStock;
    }

    public void setIdStock(Long idStock) {
        this.idStock = idStock;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStock != null ? idStock.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDetalle)) {
            return false;
        }
        StockDetalle other = (StockDetalle) object;
        if ((this.idStock == null && other.idStock != null) || (this.idStock != null && !this.idStock.equals(other.idStock))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.StockDetalle[ idStock=" + idStock + " ]";
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    
}
