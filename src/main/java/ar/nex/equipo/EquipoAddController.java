package ar.nex.equipo;

import ar.nex.entity.Empresa;
import ar.nex.entity.Equipo;
import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.EquipoCompraVenta;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.entity.Marca;
import ar.nex.pedido.RepuestoDialogController;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class EquipoAddController implements Initializable {

    @FXML
    private ComboBox filtroEmpresa;

    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
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
    private Label lblCategoria;
    @FXML
    private Label lblTipo;
    @FXML
    private Label label;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblColor;
    @FXML
    private Label lblAnio;
    @FXML
    private Label lblChasis;
    @FXML
    private Label lblMotor;
    @FXML
    private Label lblPatente;
    @FXML
    private Label lblOtro;
    @FXML
    private Label lblValorCompra;
    @FXML
    private Label lblVendedor;
    @FXML
    private Label lblFechaCompra;
    @FXML
    private TextField boxMarca;

    @FXML
    private Button btnSelectMarca;
    @FXML
    private Button btnSelectCategoria;
    @FXML
    private Button btnSelectTipo;
    @FXML
    private Button btnAddModelo;
    @FXML
    private Label lblAnio1;

    private EquipoService service;

    private Long idCat = -1L, idMod, idTip, idMar, idEmp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        service = new EquipoService();

        boxCategoria.setText("-");
        boxModelo.setText("-");
        boxColor.setText("-");
        boxOtro.setText("-");
        boxAnio.setText("-");
        boxChasis.setText("-");
        boxMotor.setText("-");
        boxPatente.setText("-");
        boxTipo.setText("-");
        boxFechaCompra.setText("-");
        boxVendedor.setText("-");
        boxValorCompra.setText("0.0");

        loadDataModelo();

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

            btnAddModelo.setOnAction(e -> addModelo(e));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void addEquipo(ActionEvent event) {

        EquipoCategoria c = service.getCategoria().findEquipoCategoria(idCat);

        EquipoTipo t = service.getTipo().findEquipoTipo(idTip);

        //EquipoModelo m = service.getModelo().findEquipoModelo(idMod);

        Marca ma = service.getMarca().findMarca(idMar);

        Empresa emp = service.getEmpresa().findEmpresa(idEmp);

        EquipoCompraVenta cv = new EquipoCompraVenta();//service.getCompraVenta().findEquipoCompraVenta(Long.valueOf(1));
        Date fechaCompra = new Date();
        cv.setFechaCompra(fechaCompra);
        cv.setVendedor(boxVendedor.getText());
        cv.setValorCompra(Double.valueOf(boxValorCompra.getText()));
        service.getCompraVenta().create(cv);

        Equipo e = new Equipo();
        e.setEmpresa(emp);
        e.setAnio(boxAnio.getText());
        e.setChasis(boxChasis.getText());
        e.setMotor(boxMotor.getText());
        e.setPatente(boxPatente.getText());
        e.setColor(boxColor.getText());
        e.setOtro(boxOtro.getText());

        e.setCategoria(c);
        e.setModelo(modeloSelect);
        e.setTipo(t);
        e.setMarca(ma);
        e.setCompraVenta(cv);

        try {
            service.getEquipo().create(e);
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void selectCategoria(ActionEvent event) {
        idCat = service.selectCategoria();
        boxCategoria.setText(service.getCategoria().findEquipoCategoria(idCat).getNombre());
    }

    @FXML
    private void selectTipo(ActionEvent event) {
        idTip = service.selectTipo();
        boxTipo.setText(service.getTipo().findEquipoTipo(idTip).getNombre());
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

    @FXML
    private void selectMarca(ActionEvent event) {
        idMar = service.selectMarca();
        boxMarca.setText(service.getMarca().findMarca(idMar).getNombre());
    }

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
