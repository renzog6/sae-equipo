package ar.nex.equipo;

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
public final class EquipoService {

    private EntityManagerFactory factory;

    private EquipoJpaController srvEquipo;

    private EquipoCategoriaJpaController srvCategoria;

    private EquipoModeloJpaController srvModelo;

    private EquipoTipoJpaController srvTipo;

    private MarcaJpaController srvMarca;

    private EquipoCompraVentaJpaController srvCompraVenta;

    private EmpresaJpaController srvEmpresa;

    public EquipoService() {
        System.out.println("ar.nex.equipo.EquipoService.<init>()");
        this.initService(Persistence.createEntityManagerFactory("SaeFxPU"));
    }

    public EquipoService(EntityManagerFactory factory) {
        System.out.println("ar.nex.equipo.EquipoService.<init>()");
        this.initService(factory);
    }

    public void initService(EntityManagerFactory factory) {
        System.out.println("ar.nex.equipo.EquipoService.initService()");
        this.srvEquipo = new EquipoJpaController(factory);
        this.srvCategoria = new EquipoCategoriaJpaController(factory);
        this.srvCompraVenta = new EquipoCompraVentaJpaController(factory);
        this.srvModelo = new EquipoModeloJpaController(factory);
        this.srvTipo = new EquipoTipoJpaController(factory);
        this.srvMarca = new MarcaJpaController(factory);
        this.srvEmpresa = new EmpresaJpaController(factory);
    }

    public EquipoJpaController getEquipo() {
        return srvEquipo;
    }

    public EquipoCategoriaJpaController getCategoria() {
        return srvCategoria;
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

    public Long selectCategoria() {
        ChoiceDialog<EquipoCategoria> dialog;
        dialog = new ChoiceDialog<>(getListEquipoCategoria().get(0), getListEquipoCategoria());

        dialog.setTitle("Equipo");
        dialog.setContentText("Categoria:");

        Optional<EquipoCategoria> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get().getIdCategoria();
        }
        return Long.valueOf(0);
    }

    public Long selectTipo() {
        ChoiceDialog<EquipoTipo> dialog;
        dialog = new ChoiceDialog<>(getListEquipoTipo().get(0), getListEquipoTipo());

        dialog.setTitle("Tipo de Equipo");
        dialog.setContentText("Tipo:");

        Optional<EquipoTipo> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get().getIdTipo();
        }
        return Long.valueOf(0);
    }

    public Long selectModelo() {
        ChoiceDialog<EquipoModelo> dialog;
        dialog = new ChoiceDialog<>(getListEquipoModelo().get(0), getListEquipoModelo());

        dialog.setTitle("Equipo");
        dialog.setContentText("Modelo:");

        Optional<EquipoModelo> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get().getIdModelo();
        }
        return Long.valueOf(0);
    }

    public Long selectMarca() {
        ChoiceDialog<Marca> dialog;
        dialog = new ChoiceDialog<>(getListMarca().get(0), getListMarca());

        dialog.setTitle("Equipo");
        dialog.setContentText("Marca:");

        Optional<Marca> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get().getIdMarca();
        }
        return Long.valueOf(0);
    }

}
