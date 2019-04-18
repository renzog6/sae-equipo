package ar.nex.equipo;

import ar.nex.entity.Equipo;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class EquipoController implements Initializable {

    @FXML
    private ComboBox filtroEmpresa;
    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    ObservableList<Equipo> data = FXCollections.observableArrayList();
    FilteredList<Equipo> filteredData = new FilteredList<>(data);
    Equipo select;

    @FXML
    private TableView<Equipo> table;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colCategoria;
    @FXML
    private TableColumn<?, ?> colTipo;
    @FXML
    private TableColumn<?, ?> colModelo;
    @FXML
    private TableColumn<?, ?> colAnio;
    @FXML
    private TableColumn<?, ?> colChasis;
    @FXML
    private TableColumn<?, ?> colMotor;
    @FXML
    private TableColumn<?, ?> colPatente;
    @FXML
    private TableColumn<?, ?> colOtro;
    @FXML
    private TableColumn colAccion;

    private EquipoService srvEquipo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        initSevice();
        initFiltro();
        loadData(0);
    }

    public void clearAll() {
        System.out.println("ar.nex.equipo.EquipoController.clearAll()");
        data.clear();
        searchBox.clear();
        select = null;
    }

    public void initTable() {
        System.out.println("ar.nex.equipo.EquipoController.initTable()");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colChasis.setCellValueFactory(new PropertyValueFactory<>("chasis"));
        colMotor.setCellValueFactory(new PropertyValueFactory<>("motor"));
        colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
        colOtro.setCellValueFactory(new PropertyValueFactory<>("otro"));
        initCellAccion();
    }

    public void initCellAccion() {
        colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
        Callback<TableColumn<Equipo, String>, TableCell<Equipo, String>> cellFactory
                = //
                (final TableColumn<Equipo, String> param) -> {
                    final TableCell<Equipo, String> cell = new TableCell<Equipo, String>() {

                final Button btn = new Button("+");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            Equipo itemSelect = getTableView().getItems().get(getIndex());
                            //Equipo newEquipo = dialog.addToEquipo(repuesto);
                            //srvEquipo.create(newEquipo);
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    return cell;
                };
        colAccion.setCellFactory(cellFactory);
    }

    public void initSevice() {
        System.out.println("ar.nex.equipo.EquipoController.initSevices()");
        srvEquipo = new EquipoService();
    }

    public void initFiltro() {
        ObservableList list = FXCollections.observableArrayList(EmpresaSelect.values());
        filtroEmpresa.getItems().addAll(list);
        filtroEmpresa.getSelectionModel().select(0);
    }

    public void loadData(long id) {
        System.out.println("ar.nex.equipo.EquipoController.loadData()");
        clearAll();

        List<Equipo> lst = srvEquipo.getEquipo().findEquipoEntities();
        for (Equipo item : lst) {            
            if ((item.getEmpresa().getIdEmpresa() == id) || (id == 0)) {
                data.add(item);
            }
        }
        table.setItems(data);
    }

    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Add(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoAdd.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Nuevo Equipo");
            stage.setScene(new Scene(root1));
            stage.showAndWait();
            loadData(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Update(ActionEvent event) {
    }

    @FXML
    private void Delete(ActionEvent event) {
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

    @FXML
    private void filtroEmpresa() {
        EmpresaSelect empresa = (EmpresaSelect) filtroEmpresa.getSelectionModel().getSelectedItem();
        System.out.println("ar.nex.equipo.EquipoController.cambiarEmpresa()" + empresa);
        loadData(empresa.getId());
    }
}
