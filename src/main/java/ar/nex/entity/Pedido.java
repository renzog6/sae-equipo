package ar.nex.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "ped_pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByIdPedido", query = "SELECT p FROM Pedido p WHERE p.idPedido = :idPedido"),
    @NamedQuery(name = "Pedido.findByCantidad", query = "SELECT p FROM Pedido p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "Pedido.findByEstado", query = "SELECT p FROM Pedido p WHERE p.estado = :estado"),
    @NamedQuery(name = "Pedido.findByFechaFin", query = "SELECT p FROM Pedido p WHERE p.fechaFin = :fechaFin"),
    @NamedQuery(name = "Pedido.findByFechaInicio", query = "SELECT p FROM Pedido p WHERE p.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Pedido.findByInfo", query = "SELECT p FROM Pedido p WHERE p.info = :info")})
public class Pedido implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Double cantidad;

    @JoinColumn(name = "repuesto", referencedColumnName = "id_repuesto")
    @ManyToOne
    private Repuesto repuesto;

    @Column(name = "info")
    private String info;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pedido")
    private Long idPedido;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;

    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa empresa;

    public Pedido() {
    }

    public Pedido(Repuesto r) {
        this.repuesto = r;
    }

    public Pedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }


    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Pedido[ idPedido=" + idPedido + " ]";
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String pedidoStringFull() {
        DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
        return fd.format(this.fechaInicio) + " - " + this.cantidad + " - " + this.empresa.getNombre();
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

      public StringProperty equipo_solo() {
        StringProperty equipo_solo = new SimpleStringProperty("NN");        
        if (repuesto != null && repuesto.getEquipoModeloList().size() >= 1) {
            equipo_solo = new SimpleStringProperty(repuesto.getEquipoModeloList().get(0).toString());
        }
        return equipo_solo;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}
