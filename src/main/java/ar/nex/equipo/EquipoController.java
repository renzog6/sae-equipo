package ar.nex.equipo;

import ar.nex.entity.equipo.Equipo;
import ar.nex.equipo.util.DialogController;
import ar.nex.equipo.util.EquipoToExel;
import ar.nex.service.JpaService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public EquipoController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/equipo/Equipo.fxml"));
            root.setStyle("/css/equipo.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private ComboBox filtroEmpresa;
    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    ObservableList<Equipo> data = FXCollections.observableArrayList();
    FilteredList<Equipo> filteredData = new FilteredList<>(data);
    Equipo equipoSelect;

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

    @FXML
    private MenuButton mbMenu;

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initMenu();
        initTable();
        initSevice();
        initFiltro();
        loadData(0);

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());
    }

    private void initMenu() {
        MenuItem item = new MenuItem("[- Exportar a Excel -]");
        item.setOnAction(e -> export());
        mbMenu.getItems().add(item);
        item = new MenuItem("Pedidos        ");
        mbMenu.getItems().add(item);
        item = new MenuItem("Stock Repuestos");
        mbMenu.getItems().add(item);
    }

    public void clearAll() {
        data.clear();
        searchBox.clear();
        equipoSelect = null;
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
                            //jpaEquipo.create(newEquipo);
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
        jpa = new JpaService();
    }

    public void initFiltro() {
        ObservableList list = FXCollections.observableArrayList(EmpresaSelect.values());
        filtroEmpresa.getItems().addAll(list);
        filtroEmpresa.getSelectionModel().select(0);
    }

    public void loadData(long id) {
        try {
            clearAll();
            List<Equipo> lst = jpa.getEquipo().findEquipoEntities();
            for (Equipo item : lst) {
                if ((item.getEmpresa().getIdEmpresa() == id) || (id == 0)) {
                    data.add(item);
                }
            }
            table.setItems(data);
        } catch (Exception e) {
            clearAll();
            DialogController.showException(e);
        }

    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Equipo>) user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getModelo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getTipo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getCategoria().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    private void add() {
        equipoSelect = new Equipo();
        edit();
    }

    private void edit() {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/equipo/EquipoDialog.fxml"));
            EquipoDialogController controller = new EquipoDialogController(equipoSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            this.loadData(0);

        } catch (IOException e) {
            System.err.print(e);
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
        try {
            Equipo item = (Equipo) table.getSelectionModel().getSelectedItem();
            equipoSelect = jpa.getEquipo().findEquipo(item.getIdEquipo());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void filtroEmpresa() {
        EmpresaSelect empresa = (EmpresaSelect) filtroEmpresa.getSelectionModel().getSelectedItem();
        loadData(empresa.getId());
    }

    private void export(){
        new EquipoToExel().export(data,  filtroEmpresa.getSelectionModel().getSelectedItem().toString());
    }
}
