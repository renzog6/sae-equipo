/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "eq_equipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipo.findAll", query = "SELECT e FROM Equipo e")
    , @NamedQuery(name = "Equipo.findByIdEquipo", query = "SELECT e FROM Equipo e WHERE e.idEquipo = :idEquipo")
    , @NamedQuery(name = "Equipo.findByAnio", query = "SELECT e FROM Equipo e WHERE e.anio = :anio")
    , @NamedQuery(name = "Equipo.findByChasis", query = "SELECT e FROM Equipo e WHERE e.chasis = :chasis")
    , @NamedQuery(name = "Equipo.findByMotor", query = "SELECT e FROM Equipo e WHERE e.motor = :motor")
    , @NamedQuery(name = "Equipo.findByPatente", query = "SELECT e FROM Equipo e WHERE e.patente = :patente")
    , @NamedQuery(name = "Equipo.findByColor", query = "SELECT e FROM Equipo e WHERE e.color = :color")
    , @NamedQuery(name = "Equipo.findByOtro", query = "SELECT e FROM Equipo e WHERE e.otro = :otro")})
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_equipo")
    private Long idEquipo;
    @Column(name = "anio")
    private String anio;
    @Column(name = "chasis")
    private String chasis;
    @Column(name = "motor")
    private String motor;
    @Column(name = "patente")
    private String patente;
    @Column(name = "color")
    private String color;
    @Column(name = "otro")
    private String otro;
    @JoinColumn(name = "categoria", referencedColumnName = "id_categoria")
    @ManyToOne
    private EquipoCategoria categoria;
    @JoinColumn(name = "compra_venta", referencedColumnName = "id_compra_venta")
    @ManyToOne
    private EquipoCompraVenta compraVenta;
    
    @JoinColumn(name = "empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa empresa;
    
    @JoinColumn(name = "marca", referencedColumnName = "id_marca")
    @ManyToOne
    private Marca marca;
    @JoinColumn(name = "modelo", referencedColumnName = "id_modelo")
    @ManyToOne
    private EquipoModelo modelo;
    @JoinColumn(name = "tipo", referencedColumnName = "id_tipo")
    @ManyToOne
    private EquipoTipo tipo;

    public Equipo() {
    }

    public Equipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOtro() {
        return otro;
    }

    public void setOtro(String otro) {
        this.otro = otro;
    }

    public EquipoCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(EquipoCategoria categoria) {
        this.categoria = categoria;
    }

    public EquipoCompraVenta getCompraVenta() {
        return compraVenta;
    }

    public void setCompraVenta(EquipoCompraVenta compraVenta) {
        this.compraVenta = compraVenta;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public EquipoModelo getModelo() {
        return modelo;
    }

    public void setModelo(EquipoModelo modelo) {
        this.modelo = modelo;
    }

    public EquipoTipo getTipo() {
        return tipo;
    }

    public void setTipo(EquipoTipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEquipo != null ? idEquipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.idEquipo == null && other.idEquipo != null) || (this.idEquipo != null && !this.idEquipo.equals(other.idEquipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.Equipo[ idEquipo=" + idEquipo + " ]";
    }
    
}
