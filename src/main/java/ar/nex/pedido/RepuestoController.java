package ar.nex.pedido;

import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.service.PedidoJpaController;
import ar.nex.service.RepuestoJpaController;
import ar.nex.util.DialogController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    private DialogController dialog;

    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private TextField boxCodigo;
    @FXML
    private TextField boxDescripcion;
    @FXML
    private TextField boxMarca;
    @FXML
    private TextField boxObservacion;
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    ObservableList<Repuesto> data = FXCollections.observableArrayList();
    FilteredList<Repuesto> filteredData = new FilteredList<>(data);
    Repuesto select;

    @FXML
    private TableView<Repuesto> table;
    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colObeservacion;
    @FXML
    private TableColumn colAccion;

    private EntityManagerFactory factory;
    private RepuestoJpaController service;
    private PedidoJpaController srvPedido;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.repuesto.RepuestoController.initialize()");
        initTable();
        initService();
        loadData();
    }

    public void clearAll() {
        System.out.println("ar.nex.util.RepuestoController.clearAll()");
        data.clear();
        boxCodigo.clear();
        boxDescripcion.clear();
        boxMarca.clear();
        boxObservacion.clear();
        searchBox.clear();
        select = null;
    }

    public void initTable() {
        System.out.println("ar.nex.util.RepuestoController.initTable()");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colObeservacion.setCellValueFactory(new PropertyValueFactory<>("obsercacion"));        
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
                            Repuesto repuesto = getTableView().getItems().get(getIndex());
                            Pedido newPedido = dialog.addToPedido(repuesto);                            
                            srvPedido.create(newPedido);
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
        System.out.println("ar.nex.util.RepuestoController.initService()");
        factory = Persistence.createEntityManagerFactory("SaeFxPU");
        service = new RepuestoJpaController(factory);
        srvPedido = new PedidoJpaController(factory);
    }

    public void loadData() {
        System.out.println("ar.nex.util.RepuestoController.loadData()");
        clearAll();

        List<Repuesto> lst = service.findRepuestoEntities();
        for (Repuesto item : lst) {
            data.add(item);
            table.setItems(data);
        }
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
    private void Add(ActionEvent event) {
        System.out.println("ar.nex.util.RepuestoController.Add()");
        try {
            Repuesto item = new Repuesto();
            item.setCodigo(boxCodigo.getText());
            item.setDescripcion(boxDescripcion.getText());
            item.setMarca(boxMarca.getText());
            item.setObsercacion(boxObservacion.getText());

            service.create(item);

        } catch (Exception e) {
            System.out.println(e);
        }
        dialog.showSuccess("New Added Successfully!!!");
        loadData();
    }

    @FXML
    private void Update(ActionEvent event) {
        System.out.println("ar.nex.util.RepuestoController.Update()");
        try {
            String msg = "Confirma Actualizar " + select.toString() + " a " + boxCodigo.getText();
            if (dialog.confirmDialog(msg)) {
                select.setCodigo(boxCodigo.getText());
                select.setDescripcion(boxDescripcion.getText());
                select.setMarca(boxMarca.getText());
                select.setObsercacion(boxObservacion.getText());
                service.edit(select);
                dialog.showSuccess("Se Actualizo Correctamente!!!");
            } else {
                boxCodigo.clear();
                boxDescripcion.clear();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        loadData();

    }

    @FXML
    private void Delete(ActionEvent event) {
        try {
            if (dialog.confirmDialog("Confimar Eliminar : " + select.toString() + "???")) {
                service.destroy(select.getIdRepuesto());
                dialog.showSuccess("Se Elimino Correctamente!!!");
            } else {
                boxCodigo.clear();
                boxDescripcion.clear();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadData();
    }

    @FXML
    private void selectMarca(ActionEvent event) {
        boxMarca.setText(dialog.selectMarca());
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        System.out.println("ar.nex.util.RepuestoController.showOnClick()");
        try {
            Repuesto item = (Repuesto) table.getSelectionModel().getSelectedItem();
            select = service.findRepuesto(item.getIdRepuesto());

            boxCodigo.setText(item.getCodigo());
            boxDescripcion.setText(item.getDescripcion());
            boxMarca.setText(item.getMarca());
            boxObservacion.setText(item.getObsercacion());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void AddToPedido(ActionEvent event) {
        //
    }
}
