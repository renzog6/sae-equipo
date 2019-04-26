package ar.nex.util;

import ar.nex.entity.Equipo;
import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.entity.Marca;
import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.EquipoCategoriaJpaController;
import ar.nex.jpa.EquipoCompraVentaJpaController;
import ar.nex.jpa.EquipoJpaController;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.EquipoTipoJpaController;
import ar.nex.jpa.MarcaJpaController;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public final class JpaService {

    private final EntityManagerFactory factory;

    private final EquipoJpaController srvEquipo;

    private final EquipoCategoriaJpaController srvCategoria;

    private final EquipoModeloJpaController srvModelo;

    private final EquipoTipoJpaController srvTipo;

    private final MarcaJpaController srvMarca;

    private final EquipoCompraVentaJpaController srvCompraVenta;

    private final EmpresaJpaController srvEmpresa;

    public JpaService() {
        this.factory = Persistence.createEntityManagerFactory("SaeFxPU");

        this.srvEquipo = null;
        this.srvCategoria = null; 
        this.srvCompraVenta = new EquipoCompraVentaJpaController(factory);
        this.srvModelo = new EquipoModeloJpaController(factory);
        this.srvTipo = new EquipoTipoJpaController(factory);
        this.srvMarca = new MarcaJpaController(factory);
        this.srvEmpresa = new EmpresaJpaController(factory);

    }

    public EquipoJpaController getEquipo() {
        if (srvEquipo != null) {
            return srvEquipo;
        } else {
            return new EquipoJpaController(factory);
        }
    }

    public EquipoCategoriaJpaController getCategoria() {
        if (srvCategoria != null) {
            return srvCategoria;
        } else {
            return new EquipoCategoriaJpaController(factory);
        }
    }

    public EquipoCompraVentaJpaController getCompraVenta() {
        return srvCompraVenta;
    }

    public EquipoModeloJpaController getModelo() {
        return srvModelo;
    }

    public EquipoTipoJpaController getTipo() {
        return srvTipo;
    }

    public MarcaJpaController getMarca() {
        return srvMarca;
    }

    public EmpresaJpaController getEmpresa() {
        return srvEmpresa;
    }

    public ObservableList<Equipo> getListEquipo() {
        List<Equipo> lst = srvEquipo.findEquipoEntities();
        ObservableList<Equipo> data = FXCollections.observableArrayList();
        for (Equipo item : lst) {
            data.add(item);
        }
        return data;
    }

    public ObservableList<EquipoCategoria> getListEquipoCategoria() {
        List<EquipoCategoria> lst = srvCategoria.findEquipoCategoriaEntities();
        ObservableList<EquipoCategoria> data = FXCollections.observableArrayList();
        for (EquipoCategoria item : lst) {
            data.add(item);
        }
        return data;
    }

    public ObservableList<EquipoModelo> getListEquipoModelo() {
        List<EquipoModelo> lst = srvModelo.findEquipoModeloEntities();
        ObservableList<EquipoModelo> data = FXCollections.observableArrayList();
        for (EquipoModelo item : lst) {
            data.add(item);
        }
        return data;
    }

    public ObservableList<EquipoTipo> getListEquipoTipo() {
        List<EquipoTipo> lst = srvTipo.findEquipoTipoEntities();
        ObservableList<EquipoTipo> data = FXCollections.observableArrayList();
        for (EquipoTipo item : lst) {
            data.add(item);
        }
        return data;
    }

    public ObservableList<Marca> getListMarca() {
        List<Marca> lst = srvMarca.findMarcaEntities();
        ObservableList<Marca> data = FXCollections.observableArrayList();
        for (Marca item : lst) {
            data.add(item);
        }
        return data;
    }

}
