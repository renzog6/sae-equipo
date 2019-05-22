package ar.nex.repuesto;

import ar.nex.entity.Empresa;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Repuesto;
import ar.nex.equipo.EquipoController;
import ar.nex.util.EquipoUtils;
import ar.nex.jpa.RepuestoJpaController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoController implements Initializable {

    public RepuestoController() {
        this.filteredData = new FilteredList<>(data);
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/repuesto/Repuesto.fxml"));
            root.setStyle("/fxml/repuesto/Repuesto.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    private EquipoUtils dlg;

    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAddModelo;

    private final ObservableList<Repuesto> data = FXCollections.observableArrayList();
    private final FilteredList<Repuesto> filteredData;
    private Repuesto selectRepuesto;

    @FXML
    private TableView<Repuesto> table;
    @FXML
    private TableColumn<Repuesto, String> colEquipos;
    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colParte;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn colAccion;

    @FXML
    private Label lblModelo;
    @FXML
    private Label lblPedido;
    @FXML
    private Label lblCompra;

    private EntityManagerFactory factory;
    private RepuestoJpaController jpaService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.repuesto.RepuestoController.initialize()");

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());

        btnAddModelo.setOnAction(e -> this.addModelo());

        initTable();
        initService();
        loadData();
    }

    public void clearAll() {
        System.out.println("ar.nex.util.RepuestoController.clearAll()");
        data.clear();
        searchBox.clear();
        selectRepuesto = null;
    }

    public void initTable() {
        System.out.println("ar.nex.util.RepuestoController.initTable()");
        colEquipos.setCellValueFactory(new Callback<CellDataFeatures<Repuesto, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Repuesto, String> data) {
                return new SimpleStringProperty(listaModelo(data.getValue()));
            }
        }
        );

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(
                new PropertyValueFactory<>("descripcion"));
        colParte.setCellValueFactory(
                new PropertyValueFactory<>("parte"));
        colInfo.setCellValueFactory(
                new PropertyValueFactory<>("info"));
        colMarca.setCellValueFactory(
                new PropertyValueFactory<>("marca"));
        colStock.setCellValueFactory(
                new PropertyValueFactory<>("stock"));

        initCellAccion();
    }

    public void initCellAccion() {
        colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
        Callback<TableColumn<Repuesto, String>, TableCell<Repuesto, String>> cellFactory
                = //
                (final TableColumn<Repuesto, String> param) -> {
                    final TableCell<Repuesto, String> cell = new TableCell<Repuesto, String>() {

                final Button btn = new Button("+");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            selectRepuesto = getTableView().getItems().get(getIndex());
                            addToPedido();
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

    public void initService() {
        factory = Persistence.createEntityManagerFactory("SaeFxPU");
        jpaService = new RepuestoJpaController(factory);
    }

    public void loadData() {
        System.out.println("ar.nex.util.RepuestoController.loadData()");
        clearAll();
        selectRepuesto = null;
        List<Repuesto> lst = jpaService.findRepuestoEntities();
        lst.forEach((item) -> {
            data.add(item);
        });
        table.setItems(data);
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Repuesto>) user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getCodigo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getParte().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Repuesto> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Delete(ActionEvent event) {
        try {
            if (dlg.confirmDialog("Confimar Eliminar : " + selectRepuesto.toString() + "???")) {
                jpaService.destroy(selectRepuesto.getIdRepuesto());
                dlg.showSuccess("Se Elimino Correctamente!!!");
            }
//            } else {
//                boxCodigo.clear();
//                boxDescripcion.clear();
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadData();
    }

    @FXML
    private void selectRepuestoMarca(ActionEvent event) {
        // boxMarca.setText(dialog.selectRepuestoMarca());
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        System.out.println("ar.nex.util.RepuestoController.showOnClick()");
        try {
            Repuesto item = (Repuesto) table.getSelectionModel().getSelectedItem();
            selectRepuesto = jpaService.findRepuesto(item.getIdRepuesto());

            lblModelo.setText(listaModelo(selectRepuesto));
            lblPedido.setText(listaProvedor(selectRepuesto));
            if (selectRepuesto.getPedidoList().size() >= 1) {
                lblCompra.setText("Ultima compra: " + selectRepuesto.getPedidoList().get(selectRepuesto.getPedidoList().size() - 1).toString());
            } else {
                lblCompra.setText("Ultima compra: sin registro");
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private String listaModelo(Repuesto r) {
        String list = null;
        if (!r.getModeloList().isEmpty()) {
            for (EquipoModelo item : r.getModeloList()) {
                if (list == null) {
                    list = item.getNombre();
                } else {
                    list = list + " / " + item.getNombre();
                }
            }
        }
        return list;
    }

    private String listaProvedor(Repuesto r) {
        String list = null;
        if (!r.getEmpresaList().isEmpty()) {
            for (Empresa item : r.getEmpresaList()) {
                if (list == null) {
                    list = item.getNombre();
                } else {
                    list = list + " / " + item.getNombre();
                }
            }
        }
        return list;
    }

    @FXML
    private void addToPedido() {
        System.out.println("ar.nex.pedido.RepuestoController.AddToPedido()");
        try {
            Stage dlg = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoPedidoDialog.fxml"));
            RepuestoPedidoDialogController controller = new RepuestoPedidoDialogController(selectRepuesto);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dlg.setScene(scene);
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.resizableProperty().setValue(Boolean.FALSE);

            dlg.showAndWait();
            this.loadData();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public void add() {
        selectRepuesto = new Repuesto();
        edit();
    }

    public void edit() {
        System.out.println("ar.nex.pedido.RepuestoController.edit()");
        try {
            Stage dlg = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoDialog.fxml"));
            RepuestoDialogController controller = new RepuestoDialogController(selectRepuesto);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dlg.setScene(scene);
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.resizableProperty().setValue(Boolean.FALSE);

            dlg.showAndWait();
            this.loadData();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public void addModelo() {
        try {
            Stage dlg = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoEquipoModeloDialog.fxml"));
            RepuestoEquipoModeloDialogController controller = new RepuestoEquipoModeloDialogController(selectRepuesto);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dlg.setScene(scene);
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.resizableProperty().setValue(Boolean.FALSE);

            dlg.showAndWait();
            this.loadData();

        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
