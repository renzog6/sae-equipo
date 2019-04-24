package ar.nex.equipo;

import ar.nex.util.MarcaDialogController;
import ar.nex.entity.Empresa;
import ar.nex.entity.Equipo;
import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.EquipoCompraVenta;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.entity.Marca;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EquipoDialogController implements Initializable {

    public EquipoDialogController(Equipo e) {
        this.equipo = e;
    }

    @FXML
    private ComboBox filtroEmpresa;
    @FXML
    private TextField boxCategoria;
    @FXML
    private TextField boxModelo;
    @FXML
    private TextField boxColor;
    @FXML
    private TextField boxOtro;
    @FXML
    private TextField boxAnio;
    @FXML
    private TextField boxChasis;
    @FXML
    private TextField boxMotor;
    @FXML
    private TextField boxPatente;
    @FXML
    private TextField boxTipo;
    @FXML
    private TextField boxFechaCompra;
    @FXML
    private TextField boxVendedor;
    @FXML
    private TextField boxValorCompra;
    @FXML
    private TextField boxMarca;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAddMarca;
    @FXML
    private Button btnAddCategoria;
    @FXML
    private Button btnAddTipo;
    @FXML
    private Button btnAddModelo;

    private EquipoService service;

    private final Equipo equipo;

    /**
     * Initializes the controll
     *
     * @param url class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        service = new EquipoService();

        loadDataCategoria();
        loadDataTipo();
        loadDataModelo();
        loadDataMarca();

        initFiltro();

        initControls();
    }

    public void initFiltro() {
        ObservableList list = FXCollections.observableArrayList(EmpresaSelect.values());
        list.add(0, "Elegir Empresa");
        filtroEmpresa.getItems().addAll(list);
        filtroEmpresa.getSelectionModel().select(0);
    }

    private void initControls() {
        System.out.println("ar.nex.equipo.EquipoAddController.initControls()");
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            btnAddCategoria.setOnAction(e -> addCategoria(e));
            btnAddTipo.setOnAction(e -> addTipo(e));
            btnAddModelo.setOnAction(e -> addModelo(e));
            btnAddMarca.setOnAction(e -> addMarca(e));

            categoriaSelect = equipo.getCategoria() != null ? equipo.getCategoria() : new EquipoCategoria("NN");
            boxCategoria.setText(categoriaSelect.getNombre());

            tipoSelect = equipo.getTipo();
            boxTipo.setText(tipoSelect.getNombre());

            modeloSelect = equipo.getModelo();
            boxModelo.setText(modeloSelect.getNombre());

            marcaSelect = equipo.getMarca();
            boxMarca.setText(marcaSelect.getNombre());

            boxColor.setText(equipo.getColor());
            boxOtro.setText(equipo.getOtro());
            boxAnio.setText(equipo.getAnio());
            boxChasis.setText(equipo.getChasis());
            boxMotor.setText(equipo.getMotor());
            boxPatente.setText(equipo.getPatente());

            boxFechaCompra.setText("-");
            boxVendedor.setText("-");
            boxValorCompra.setText("0.0");

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {

            Empresa emp = service.getEmpresa().findEmpresa(idEmp);

            EquipoCompraVenta cv = new EquipoCompraVenta();//service.getCompraVenta().findEquipoCompraVenta(Long.valueOf(1));
            Date fechaCompra = new Date();
            cv.setFechaCompra(fechaCompra);
            cv.setVendedor(boxVendedor.getText());
            cv.setValorCompra(Double.valueOf(boxValorCompra.getText()));
            service.getCompraVenta().create(cv);

            equipo.setEmpresa(emp);
            equipo.setAnio(boxAnio.getText());
            equipo.setChasis(boxChasis.getText());
            equipo.setMotor(boxMotor.getText());
            equipo.setPatente(boxPatente.getText());
            equipo.setColor(boxColor.getText());
            equipo.setOtro(boxOtro.getText());

            equipo.setCategoria(categoriaSelect);
            equipo.setTipo(tipoSelect);
            equipo.setModelo(modeloSelect);
            equipo.setMarca(marcaSelect);

            equipo.setCompraVenta(cv);

            if (equipo.getIdEquipo() != null) {
                service.getEquipo().edit(equipo);
            } else {
                service.getEquipo().create(equipo);
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }

    private EquipoCategoria categoriaSelect;

    private final ObservableList<EquipoCategoria> dataCategoria = FXCollections.observableArrayList();

    private void loadDataCategoria() {
        try {
            this.dataCategoria.clear();
            List<EquipoCategoria> lst = service.getCategoria().findEquipoCategoriaEntities();
            lst.forEach((item) -> {
                this.dataCategoria.add(item);
            });

            AutoCompletionBinding<EquipoCategoria> autoCategoria = TextFields.bindAutoCompletion(boxCategoria, dataCategoria);

            autoCategoria.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoCategoria> event) -> {
                        categoriaSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addCategoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoCategoriaDialog.fxml"));
            EquipoCategoriaDialogController controller = new EquipoCategoriaDialogController(new EquipoCategoria());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 1);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private EquipoTipo tipoSelect;

    private final ObservableList<EquipoTipo> dataTipo = FXCollections.observableArrayList();

    private void loadDataTipo() {
        try {
            this.dataTipo.clear();
            List<EquipoTipo> lst = service.getTipo().findEquipoTipoEntities();
            lst.forEach((item) -> {
                this.dataTipo.add(item);
            });

            AutoCompletionBinding<EquipoTipo> autoTipo = TextFields.bindAutoCompletion(boxTipo, dataTipo);

            autoTipo.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoTipo> event) -> {
                        tipoSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addTipo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoTipoDialog.fxml"));
            EquipoTipoDialogController controller = new EquipoTipoDialogController(new EquipoTipo());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 1);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private EquipoModelo modeloSelect;

    private final ObservableList<EquipoModelo> dataModelo = FXCollections.observableArrayList();

    private void loadDataModelo() {
        try {
            this.dataModelo.clear();
            List<EquipoModelo> lst = service.getModelo().findEquipoModeloEntities();
            lst.forEach((item) -> {
                this.dataModelo.add(item);
            });

            AutoCompletionBinding<EquipoModelo> autoModelo = TextFields.bindAutoCompletion(boxModelo, dataModelo);

            autoModelo.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoModelo> event) -> {
                        modeloSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addModelo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/ModeloDialog.fxml"));
            ModeloDialogController controller = new ModeloDialogController(new EquipoModelo());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 1);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private Marca marcaSelect;

    private final ObservableList<Marca> dataMarca = FXCollections.observableArrayList();

    private void loadDataMarca() {
        try {
            this.dataMarca.clear();
            List<Marca> lst = service.getMarca().findMarcaEntities();
            lst.forEach((item) -> {
                this.dataMarca.add(item);
            });

            AutoCompletionBinding<Marca> autoMarca = TextFields.bindAutoCompletion(boxMarca, dataMarca);

            autoMarca.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<Marca> event) -> {
                        marcaSelect = event.getCompletion();
                    }
            );
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void addMarca(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/util/MarcaDialog.fxml"));
            MarcaDialogController controller = new MarcaDialogController(new Marca());
            loader.setController(controller);

            showDialog(new Scene(loader.load()), 1);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private long idEmp;

    @FXML
    private void selectEmpresa() {
        EmpresaSelect empresa = (EmpresaSelect) filtroEmpresa.getSelectionModel().getSelectedItem();
        idEmp = empresa.getId();
    }

    private void showDialog(Scene scene, int i) {
        System.out.println("ar.nex.equipo.EquipoAddController.showDialog()");
        try {
            Stage dialog = new Stage();

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

            switch (i) {
                case 1:
                    loadDataModelo();
                    break;

            }

        } catch (Exception e) {
            System.err.print(e);
        }
    }

}