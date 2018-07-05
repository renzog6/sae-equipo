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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "cer_silo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CerealSilo.findAll", query = "SELECT c FROM CerealSilo c")
    , @NamedQuery(name = "CerealSilo.findByIdSilo", query = "SELECT c FROM CerealSilo c WHERE c.idSilo = :idSilo")
    , @NamedQuery(name = "CerealSilo.findByCampania", query = "SELECT c FROM CerealSilo c WHERE c.campania = :campania")
    , @NamedQuery(name = "CerealSilo.findByEspecie", query = "SELECT c FROM CerealSilo c WHERE c.especie = :especie")
    , @NamedQuery(name = "CerealSilo.findByEstado", query = "SELECT c FROM CerealSilo c WHERE c.estado = :estado")
    , @NamedQuery(name = "CerealSilo.findByNombre", query = "SELECT c FROM CerealSilo c WHERE c.nombre = :nombre")})
public class CerealSilo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_silo")
    private Long idSilo;
    @Column(name = "campania")
    private String campania;
    @Column(name = "especie")
    private String especie;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "nombre")
    private String nombre;

    public CerealSilo() {
    }

    public CerealSilo(Long idSilo) {
        this.idSilo = idSilo;
    }

    public Long getIdSilo() {
        return idSilo;
    }

    public void setIdSilo(Long idSilo) {
        this.idSilo = idSilo;
    }

    public String getCampania() {
        return campania;
    }

    public void setCampania(String campania) {
        this.campania = campania;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSilo != null ? idSilo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CerealSilo)) {
            return false;
        }
        CerealSilo other = (CerealSilo) object;
        if ((this.idSilo == null && other.idSilo != null) || (this.idSilo != null && !this.idSilo.equals(other.idSilo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.CerealSilo[ idSilo=" + idSilo + " ]";
    }
    
}
