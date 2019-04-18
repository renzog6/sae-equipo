package ar.nex.pedido;

import ar.nex.entity.Pedido;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
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
import javafx.scene.control.ComboBox;
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

import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.RepuestoJpaController;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PedidoController implements Initializable {

    @FXML
    private ComboBox filtroEstado;
    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;

    ObservableList<Pedido> data = FXCollections.observableArrayList();
    FilteredList<Pedido> filteredData = new FilteredList<>(data);
    Pedido select;

    @FXML
    private TableView<Pedido> table;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn<?, ?> colRepuesto;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<?, ?> colProveedor;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<?, ?> colObeservacion;
    @FXML
    private TableColumn colAccion;

    private EntityManagerFactory factory;
    private PedidoJpaController srvPedido;
    private RepuestoJpaController srvRepuesto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.repuesto.PedidoController.initialize()");
        initTable();
        initService();
        loadData();

        ObservableList list = FXCollections.observableArrayList(EstadoPedido.values());
        list.add(0, "Todos");
        filtroEstado.getItems().addAll(list);
        filtroEstado.getSelectionModel().select(1);
    }

    public void clearAll() {
        System.out.println("ar.nex.util.PedidoController.clearAll()");
        data.clear();
        searchBox.clear();
        select = null;
    }

    public void initTable() {
        System.out.println("ar.nex.util.PedidoController.initTable()");
        colRepuesto.setCellValueFactory(new PropertyValueFactory<>("repuesto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colObeservacion.setCellValueFactory(new PropertyValueFactory<>("info"));
        initCellAccion();
        initCellFecha();
    }

    public void initCellAccion() {
        colAccion.setCellValueFactory(new PropertyValueFactory<>("Accion"));
        Callback<TableColumn<Pedido, String>, TableCell<Pedido, String>> cellFactory
                = //
                (final TableColumn<Pedido, String> param) -> {
                    final TableCell<Pedido, String> cell = new TableCell<Pedido, String>() {

                final Button btn = new Button("+");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            Pedido repuesto = getTableView().getItems().get(getIndex());
                            //Pedido newPedido = dialog.addToPedido(repuesto);
                            //srvPedido.create(newPedido);
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

    public void initCellFecha() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        Callback<TableColumn<Pedido, Date>, TableCell<Pedido, Date>> cellFactory
                = //
                (final TableColumn<Pedido, Date> param) -> {
                    final TableCell<Pedido, Date> cell = new TableCell<Pedido, Date>() {

                @Override
                public void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || (item == null)) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
                        setText(fd.format(item));
                        setGraphic(null);
                    }
                }
            };
                    return cell;
                };

        colFecha.setCellFactory(cellFactory);
    }

    public void initService() {
        System.out.println("ar.nex.util.PedidoController.initService()");
        factory = Persistence.createEntityManagerFactory("SaeFxPU");
        srvPedido = new PedidoJpaController(factory);
        srvPedido = new PedidoJpaController(factory);
    }

    public void loadData() {
        System.out.println("ar.nex.util.PedidoController.loadData()");
        clearAll();

        List<Pedido> lst = srvPedido.findPedidoEntities();
        for (Pedido item : lst) {
            data.add(item);
            table.setItems(data);
        }
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Pedido>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
//                if (item.getRepuesto().getCodigo().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                } else if (item.getEmpresa().getNombre().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                }
                if (item.getEmpresa().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Pedido> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

}
