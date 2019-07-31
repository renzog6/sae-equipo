package ar.nex.pedido;

import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.entity.equipo.Pedido;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.equipo.EquipoController;
import ar.nex.equipo.util.DialogController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.repuesto.RepuestoPedidoDialogController;
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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
    @FXML
    private Label lblInfo;

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
    private TableColumn<Pedido, String> colCodigo;
    @FXML
    private TableColumn<Pedido, String> colDescripcion;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<?, ?> colProveedor;
    @FXML
    private TableColumn colEstado;
//    @FXML
//    private TableColumn<Pedido, Date> colFLlego;    
    @FXML
    private TableColumn colInfo;

    @FXML
    private ContextMenu menuTable;

    private EntityManagerFactory factory;
    private PedidoJpaController jpaPedido;
    private RepuestoJpaController jpaRepuesto;

    DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");

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
        initMenuTable();
        initService();
        loadData(EstadoPedido.PENDIENTE);
        initFiltroEstado();

    }

    private void initMenuTable() {

        MenuItem item = new MenuItem("Llego...");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pedidoSelect != null) {
                    reciboPedido();
                }
            }
        });
        menuTable.getItems().addAll(item);

        item = new MenuItem("Volver a Pedir");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pedidoSelect != null) {
                    volverAPedir();
                }
            }
        });
        menuTable.getItems().addAll(item);

        item = new MenuItem("Devolver");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pedidoSelect != null) {
                    devolverPedido();
                }
            }
        });
        menuTable.getItems().addAll(item);
    }

    private void clearAll() {
        data.clear();
        searchBox.clear();
        pedidoSelect = null;
    }

    private String listaModelo(Repuesto r) {
        String list = "-";
        try {
            if (!r.getModeloList().isEmpty() && r.getModeloList() != null) {
                if (r.getModeloList().size() >= 3) {
                    list = "[ Varios ]";
                } else {
                    EquipoModelo item = r.getModeloList().get(0);
                    list = item.getTipo().getNombre() + " " + item.getNombre();
                    if (r.getModeloList().size() >= 2) {
                        item = r.getModeloList().get(1);
                        list = list + " / " + item.getTipo().getNombre() + " " + item.getNombre();
                    }
                }
            }
        } catch (Exception e) {
            list = "NN";
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

        colCodigo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getRepuesto().getCodigo());
            }
        });

        colDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getRepuesto().getDescripcion());
            }
        });

        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));

        //   initCellAccion();
        initCellFecha();
        initCellEstado();

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        PedidoTableUtils.installCopyPasteHandler(table);

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
                        setText(fd.format(item));
                        setGraphic(null);
                    }
                }
            };
                    return cell;
                };

        colFecha.setCellFactory(cellFactory);

//        colFLlego.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
//        Callback<TableColumn<Pedido, Date>, TableCell<Pedido, Date>> cellFactoryLlego
//                = //
//                (final TableColumn<Pedido, Date> param) -> {
//                    final TableCell<Pedido, Date> cell = new TableCell<Pedido, Date>() {
//
//                @Override
//                public void updateItem(Date item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || (item == null)) {
//                        setGraphic(null);
//                        setText(null);
//                    } else {                        
//                        setText(fd.format(item));
//                        setGraphic(null);
//                    }
//                }
//            };
//                    return cell;
//                };
//
//        colFLlego.setCellFactory(cellFactoryLlego);
    }

    private void initCellEstado() {
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

    private void initService() {
        System.out.println("ar.nex.util.PedidoController.initService()");
        factory = Persistence.createEntityManagerFactory("SaeFxPU");
        jpaPedido = new PedidoJpaController(factory);
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

    private void loadData(EstadoPedido estado) {
        try {
            clearAll();

            List<Pedido> lst = jpaPedido.findPedidoEntities();
            lst.forEach((item) -> {
                if ((item.getEstado() == estado.getValue()) || (estado == EstadoPedido.TODOS)) {
                    data.add(item);
                }
            });
            table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search() {
        try {
            searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Pedido>) (Pedido item) -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (item.getRepuesto().getCodigo().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (item.getEmpresa().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (item.getRepuesto().getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Pedido> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        try {
            pedidoSelect = (Pedido) table.getSelectionModel().getSelectedItem();
            lblInfo.setText("Info: " + pedidoSelect.getRepuesto().getInfo());
            if (pedidoSelect.getRepuesto().getPedidoList().size() >= 1) {
                lblCompra.setText(ultimaCompra(pedidoSelect.getRepuesto().getPedidoList().get(pedidoSelect.getRepuesto().getPedidoList().size() - 1)));
            } else {
                lblCompra.setText("Ultima compra: sin registro");
            }
        } catch (Exception e) {
            System.out.println(e);
            lblCompra.setText("Ultima compra: sin registro");
        }
    }

    private String ultimaCompra(Pedido p) {
        String str = "Ultima compra: ";
        str += fd.format(p.getFechaInicio()) + " - ";
        str += p.getEmpresa().getNombre() + " - ";
        str += "Llego: " + fd.format(p.getFechaFin());
        return str;
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

    private void volverAPedir() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/repuesto/RepuestoPedidoDialog.fxml"));
            RepuestoPedidoDialogController controller = new RepuestoPedidoDialogController(pedidoSelect.getRepuesto());
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);

            stage.showAndWait();
            this.loadData(EstadoPedido.PENDIENTE);

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    private void devolverPedido() {
        try {
            if (DialogController.confirmDialog("Seguro que desea Cancelar el Pedido???")) {
                if (pedidoSelect.getEstado() == EstadoPedido.COMPLETO.getValue()) {
                    pedidoSelect.getRepuesto().setStock(pedidoSelect.getRepuesto().getStock() - pedidoSelect.getCantidad());
                    jpaRepuesto = new RepuestoJpaController(factory);
                    jpaRepuesto.edit(pedidoSelect.getRepuesto());
                    jpaRepuesto = null;
                }

                pedidoSelect.setEstado(EstadoPedido.CANCELADO.getValue());
                jpaPedido.edit(pedidoSelect);

                filtroEstado();
            }
        } catch (Exception e) {
        }
    }

}
