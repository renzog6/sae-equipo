package ar.nex.pedido;

import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.util.DialogController;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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

    private DialogController dialog;

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

    private ObservableList<Repuesto> data = FXCollections.observableArrayList();
    private FilteredList<Repuesto> filteredData = new FilteredList<>(data);
    private Repuesto select;
    
        @FXML
    private TableView<Repuesto> table;
    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn colAccion;

    private EntityManagerFactory factory;
    private RepuestoJpaController jpaService;
    private PedidoJpaController srvPedido;
    
    private Repuesto repuesto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.repuesto.RepuestoController.initialize()");

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());

        initTable();
        initService();
        loadData();
    }

    public void clearAll() {
        System.out.println("ar.nex.util.RepuestoController.clearAll()");
        data.clear();
        searchBox.clear();
        select = null;
    }

    public void initTable() {
        System.out.println("ar.nex.util.RepuestoController.initTable()");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

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
        jpaService = new RepuestoJpaController(factory);
        srvPedido = new PedidoJpaController(factory);
    }

    public void loadData() {
        System.out.println("ar.nex.util.RepuestoController.loadData()");
        clearAll();

        List<Repuesto> lst = jpaService.findRepuestoEntities();
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
//            item.setCodigo(boxCodigo.getText());
//            item.setDescripcion(boxDescripcion.getText());
//            item.setMarca(boxMarca.getText());
//            item.setInfo(boxInfo.getText());

            jpaService.create(item);

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
            String msg = "Confirma Actualizar " + select.toString() + " a ";
//            if (dialog.confirmDialog(msg)) {
//                select.setCodigo(boxCodigo.getText());
//                select.setDescripcion(boxDescripcion.getText());
//                select.setMarca(boxMarca.getText());
//                select.setInfo(boxInfo.getText());
//                jpaService.edit(select);
//                dialog.showSuccess("Se Actualizo Correctamente!!!");
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
    private void Delete(ActionEvent event) {
        try {
            if (dialog.confirmDialog("Confimar Eliminar : " + select.toString() + "???")) {
                jpaService.destroy(select.getIdRepuesto());
                dialog.showSuccess("Se Elimino Correctamente!!!");
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
    private void selectMarca(ActionEvent event) {
        // boxMarca.setText(dialog.selectMarca());
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        System.out.println("ar.nex.util.RepuestoController.showOnClick()");
        try {
            Repuesto item = (Repuesto) table.getSelectionModel().getSelectedItem();
            select = jpaService.findRepuesto(item.getIdRepuesto());

//            boxCodigo.setText(item.getCodigo());
//            boxDescripcion.setText(item.getDescripcion());
//            boxMarca.setText(item.getMarca());
//            boxInfo.setText(item.getInfo());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void AddToPedido(ActionEvent event) {
        //
    }

    
    public void add() {
        repuesto = new Repuesto();
        edit();
    }

    public void edit() {
        System.out.println("ar.nex.pedido.RepuestoController.edit()");
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RepuestoDialog.fxml"));
            RepuestoDialogController controller = new RepuestoDialogController(repuesto);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
