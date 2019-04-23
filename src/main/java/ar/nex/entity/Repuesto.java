package ar.nex.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "ped_repuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repuesto.findAll", query = "SELECT r FROM Repuesto r"),
    @NamedQuery(name = "Repuesto.findByIdRepuesto", query = "SELECT r FROM Repuesto r WHERE r.idRepuesto = :idRepuesto"),
    @NamedQuery(name = "Repuesto.findByCodigo", query = "SELECT r FROM Repuesto r WHERE r.codigo = :codigo"),
    @NamedQuery(name = "Repuesto.findByDescripcion", query = "SELECT r FROM Repuesto r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "Repuesto.findByMarca", query = "SELECT r FROM Repuesto r WHERE r.marca = :marca"),
    @NamedQuery(name = "Repuesto.findByInfo", query = "SELECT r FROM Repuesto r WHERE r.info = :info")})
public class Repuesto implements Serializable {

    @OneToMany(mappedBy = "repuesto")
    private List<Pedido> pedidoList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_repuesto")
    private Long idRepuesto;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stock")
    private Double stock;
    @Column(name = "info")
    private String info;

    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "marca")
    private String marca;

    @Column(name = "parte")
    private String parte;

    @ManyToMany(mappedBy = "repuestoList")
    private List<Empresa> empresaList;

    @ManyToMany(mappedBy = "repuestoList")
    private List<EquipoModelo> equipoModeloList;

    public Repuesto() {
        empresaList = new ArrayList<>();
        equipoModeloList = new ArrayList<>();
        
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
        return this.codigo + " - " + this.descripcion;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @XmlTransient
    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    @XmlTransient
    public List<EquipoModelo> getEquipoModeloList() {
        return equipoModeloList;
    }

    public void setEquipoModeloList(List<EquipoModelo> equipoModeloList) {
        this.equipoModeloList = equipoModeloList;
    }

    public String listaModelo() {
        String str = "";
        List<EquipoModelo> list = this.getEquipoModeloList();
        for (EquipoModelo object : list) {
            str = str + " / " + object.getNombre();
        }
        return str;
    }

    public String listaProvedor() {
        String str = "";
        List<Empresa> list = this.getEmpresaList();
        for (Empresa object : list) {
            str = str + " / " + object.getNombre();
        }
        return str;
    }

    public Pedido getLastPedido(){
        int i = this.getPedidoList().size();        
        if(i >= 1){
            return getPedidoList().get(i-1);
        }
        return new Pedido();
    }
    
    public String listaPedido() {
        String str = "N7N";
        int i = this.getPedidoList().size();        
        if(i >= 1){
            str = getPedidoList().get(i-1).pedidoStringFull();
        }
        return str;
    }

    public StringProperty equipo_solo() {
        StringProperty equipo_solo = new SimpleStringProperty("NN");        
        if (equipoModeloList.size() >= 1) {
            equipo_solo = new SimpleStringProperty(equipoModeloList.get(0).getNombre());
        }
        return equipo_solo;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

}
