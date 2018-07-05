package ar.nex.equipo;

import ar.nex.entity.Empresa;
import ar.nex.entity.Equipo;
import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.EquipoCompraVenta;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.entity.Marca;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private EquipoService service;

    private Long idCat = -1L, idMod, idTip, idMar, idEmp;

    @FXML
    private Button btnSelectCategoria;
    @FXML
    private Button btnSelectTipo;
    @FXML
    private Button btnSelectModelo;
    @FXML
    private Label lblAnio1;

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

        initFiltro();
    }

    public void initFiltro() {
        ObservableList list = FXCollections.observableArrayList(EmpresaSelect.values());
        list.add(0, "Elegir Empresa");
        filtroEmpresa.getItems().addAll(list);
        filtroEmpresa.getSelectionModel().select(0);
    }

    @FXML
    private void addEquipo(ActionEvent event) {

        EquipoCategoria c = service.getCategoria().findEquipoCategoria(idCat);

        EquipoTipo t = service.getTipo().findEquipoTipo(idTip);

        EquipoModelo m = service.getModelo().findEquipoModelo(idMod);

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
        e.setModelo(m);
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
    private void Cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();

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

    @FXML
    private void selectModelo(ActionEvent event) {
        idMod = service.selectModelo();
        boxModelo.setText(service.getModelo().findEquipoModelo(idMod).getNombre());
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
}
