package ar.nex.pedido;

import ar.nex.entity.Equipo;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.equipo.EquipoController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.RepuestoJpaController;
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

import ar.nex.util.DialogController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PedidoController implements Initializable {

    public PedidoController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/pedido/Pedido.fxml"));
            root.setStyle("/fxml/pedido/Pedido.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private ComboBox filtroEstado;
    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    
    @FXML
    private Label lblCompra;

    private final ObservableList<Pedido> data = FXCollections.observableArrayList();
    private final FilteredList<Pedido> filteredData = new FilteredList<>(data);
    private Pedido pedidoSelect;

    @FXML
    private TableView<Pedido> table;
    @FXML
    private TableColumn<Pedido, Date> colFecha;
    @FXML
    private TableColumn<Pedido, String> colEquipo;
    @FXML
    private TableColumn<?, ?> colRepuesto;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<?, ?> colProveedor;
    @FXML
    private TableColumn colEstado;
    @FXML
    private TableColumn<?, ?> colObeservacion;
    @FXML
    private TableColumn colAccion;

    private EntityManagerFactory factory;
    private PedidoJpaController srvPedido;
    private RepuestoJpaController srvRepuesto;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.repuesto.PedidoController.initialize()");
        initTable();
        initService();
        loadData(EstadoPedido.PENDIENTE);
        initFiltroEstado();
    }

    public void clearAll() {
        data.clear();
        searchBox.clear();
        pedidoSelect = null;
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
      
    public void initTable() {
        colEquipo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> data) {              
                return new SimpleStringProperty(listaModelo(data.getValue().getRepuesto()));
            }
        });
        colRepuesto.setCellValueFactory(new PropertyValueFactory<>("repuesto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colObeservacion.setCellValueFactory(new PropertyValueFactory<>("info"));
        initCellAccion();
        initCellFecha();
        initCellEstado();

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        PedidoTableUtils.installCopyPasteHandler(table);

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
                            pedidoSelect = getTableView().getItems().get(getIndex());
                            reciboPedido();
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

    public void initCellEstado() {
        try {
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            Callback<TableColumn<Pedido, Integer>, TableCell<Pedido, Integer>> cellFactory
                    = //
                    (final TableColumn<Pedido, Integer> param) -> {
                        final TableCell<Pedido, Integer> cell = new TableCell<Pedido, Integer>() {

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || (item == null)) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            switch (item) {
                                case 1:
                                    setGraphic(new ImageView(new Image(getClass().getResource("/img/warning_32.png").toString())));
                                    break;
                                case 2:
                                    setGraphic(new ImageView(new Image(getClass().getResource("/img/tick_32.png").toString())));
                                    break;
                                case 3:
                                    setGraphic(new ImageView(new Image(getClass().getResource("/img/block_32.png").toString())));
                                    break;
                                default:
                                    setGraphic(null);
                            }

                        }
                    }
                };
                        return cell;
                    };

            colEstado.setCellFactory(cellFactory);
        } catch (Exception ex) {
            DialogController.showException(ex);
        }
    }

    public void initService() {
        System.out.println("ar.nex.util.PedidoController.initService()");
        factory = Persistence.createEntityManagerFactory("SaeFxPU");
        srvPedido = new PedidoJpaController(factory);
        srvPedido = new PedidoJpaController(factory);
    }

    private void initFiltroEstado() {
        if (filtroEstado.getItems().isEmpty()) {
            ObservableList list = FXCollections.observableArrayList(EstadoPedido.values());
            filtroEstado.getItems().addAll(list);
            filtroEstado.getSelectionModel().select(1);
        }
    }

    @FXML
    private void filtroEstado() {
        loadData((EstadoPedido) filtroEstado.getSelectionModel().getSelectedItem());
    }

    public void loadData(EstadoPedido estado) {
        clearAll();

        List<Pedido> lst = srvPedido.findPedidoEntities();
        lst.forEach((item) -> {
            if ((item.getEstado() == estado.getValue()) || (estado == EstadoPedido.TODOS)) {
                data.add(item);
            }
        });
        table.setItems(data);
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Pedido>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getRepuesto().getCodigo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                //if (item.getEmpresa().getNombre().toLowerCase().contains(lowerCaseFilter)) {
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
           try {
            Pedido item = (Pedido) table.getSelectionModel().getSelectedItem();            
            if (item.getRepuesto().getPedidoList().size() >= 1) {
                lblCompra.setText("Ultima compra: " + item.getRepuesto().getPedidoList().get(item.getRepuesto().getPedidoList().size() - 1).toString());
            } else {
                lblCompra.setText("Ultima compra: sin registro");
            }
        } catch (Exception e) {
            System.out.println(e);
            
        }
    }

    private void reciboPedido() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pedido/PedidoReciboDialog.fxml"));
            PedidoReciboDialogController controller = new PedidoReciboDialogController(pedidoSelect);
            loader.setController(controller);

            Stage dialog = new Stage();
            dialog.setScene(new Scene(loader.load()));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);
            dialog.showAndWait();

            this.initFiltroEstado();
            this.loadData(EstadoPedido.PENDIENTE);
        } catch (IOException e) {
            System.err.print(e);
        }
    }
}
