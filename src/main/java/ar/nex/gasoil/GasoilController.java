package ar.nex.gasoil;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.gasto.Gasoil;
import ar.nex.service.JpaService;
import ar.nex.util.DateUtils;
import ar.nex.util.DialogController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class GasoilController implements Initializable {

    public GasoilController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/gasoil/GasoilList.fxml"));
            root.setStyle("/fxml/gasoil/Gasoil.css");
        } catch (IOException ex) {
            Logger.getLogger(GasoilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpGasoil;
    @FXML
    private TextField searchBox;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;

    @FXML
    private MenuButton mbMenu;

    @FXML
    private HBox vhTanque;

    private BarChart bcGasoil;

    private final ObservableList<Gasoil> data = FXCollections.observableArrayList();
    private final FilteredList<Gasoil> filteredData = new FilteredList<>(data);
    private Gasoil gasoilSelect;
    @FXML
    private TableView<Gasoil> table;
    @FXML
    private TableColumn<Gasoil, String> colFecha;
    @FXML
    private TableColumn<Gasoil, String> colEquipo;
    @FXML
    private TableColumn<Gasoil, String> colMovimiento;
    @FXML
    private TableColumn<Gasoil, String> colLitros;
    @FXML
    private TableColumn<Gasoil, String> colStock;
    @FXML
    private TableColumn<?, ?> colInfo;

    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            btnAdd.setOnAction(e -> add());
            btnEdit.setOnAction(e -> edit());
            btnUpdate.setOnAction(e -> loadData(dpDesde.getValue(), dpHasta.getValue()));
            startTask();
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }

    private void startTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jpa = new JpaService();
                initMenu();
                initFecha();
                iniTable();
                loadData(listGasoil(dpDesde.getValue(), dpHasta.getValue()));
                initTanque();
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void initMenu() {
        MenuItem item = new MenuItem("[ Update Stock ]");
        item.setOnAction(e -> updateStock());
        mbMenu.getItems().add(item);
    }

    private void initFecha() {
        LocalDate hoy = LocalDate.now();
        dpDesde.setValue(hoy.plusDays(-10));
        dpHasta.setValue(hoy);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (dpDesde.getValue().compareTo(dpHasta.getValue()) > 0) {
                    dpDesde.setValue(dpHasta.getValue());
                }
            }
        };

        dpDesde.setOnAction(event);
        dpHasta.setOnAction(event);
    }

    private void iniTable() {
        //colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasoil, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasoil, String> p) {
                return new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy").format(p.getValue().getFecha()));
            }
        });

        colEquipo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasoil, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasoil, String> param) {
                return new SimpleStringProperty(param.getValue().getEquipo().toString());
            }
        });

        colMovimiento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasoil, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasoil, String> p) {
                if (p.getValue().getMovimineto() == 0) {
                    return new SimpleStringProperty(GasoilMovimiento.CARGA.getNombre());
                } else {
                    return new SimpleStringProperty(GasoilMovimiento.DESCARDA.getNombre());
                }
            }
        });

        //colLitros.setCellValueFactory(new PropertyValueFactory<>("litros"));
        colLitros.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasoil, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasoil, String> param) {
                DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
                return new SimpleStringProperty(decimalFormat.format(param.getValue().getLitros()));
            }
        });

        colStock.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasoil, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasoil, String> param) {
                DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
                return new SimpleStringProperty(decimalFormat.format(param.getValue().getStock()));
            }
        });

        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
    }

    private void loadData(List<Gasoil> lst) {
        try {
            clearAll();
            if (null != lst) {
                lst.forEach((item) -> {
                    data.add(item);
                });
                table.setItems(data);
            }
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }

    private void loadData(LocalDate local_desde, LocalDate local_hasta) {
        try {
            clearAll();
            loadData(listGasoil(local_desde, local_hasta));
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }

    private List<Gasoil> listGasoil() {
        EntityManager em = jpa.getFactory().createEntityManager();
        TypedQuery<Gasoil> query
                = em.createQuery("SELECT c FROM Gasoil c ORDER BY c.fecha, c.idGasto ASC", Gasoil.class);
        List<Gasoil> results = query.getResultList();
        if (!results.isEmpty()) {
            return results;
        } else {
            return null;
        }
    }
    private List<Gasoil> listGasoil(LocalDate local_desde, LocalDate local_hasta) {
        DateUtils du = new DateUtils();
        Date desde = (Date) du.convertToDateViaSqlDate(local_desde);
        Date hasta = (Date) du.convertToDateViaSqlDate(local_hasta);
        EntityManager em = jpa.getFactory().createEntityManager();
        TypedQuery<Gasoil> query
                = em.createQuery("SELECT c FROM Gasoil c"
                        + "  WHERE c.fecha BETWEEN :start AND :end"
                        + " ORDER BY c.fecha, c.idGasto ASC", Gasoil.class)
                        .setParameter("start", desde)
                        .setParameter("end", hasta);
        List<Gasoil> results = query.getResultList();
        if (!results.isEmpty()) {
            return results;
        } else {
            return null;
        }
    }

    private Gasoil ultimoMovimiento() {
        return listGasoil().get(listGasoil().size() - 1);
    }

    private void updateStock() {
        try {
            List<Gasoil> lst = listGasoil();
            boolean flag = false;
            for (Gasoil item : lst) {
                if (flag) {
                    switch (item.getMovimineto()) {
                        case 0://Carga
                            item.setStock(gasoilSelect.getStock() - item.getLitros());
                            break;
                        case 1://Descarga
                            item.setStock(gasoilSelect.getStock() + item.getLitros());
                            break;
                    }
                    item.setPrecio(gasoilSelect.getPrecio());
                    jpa.getGasoil().edit(item);
                    gasoilSelect = item;
                } else {
                    gasoilSelect = item;
                    flag = true;
                }
            };

            this.loadData(listGasoil());
            updateTanque();
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }

    private void initTanque() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis(0, 100, 10);                

                XYChart.Series dataSerie = new XYChart.Series();
                double porcentaje = ((ultimoMovimiento().getStock() * 100) / 50000);
                dataSerie.getData().add(new XYChart.Data("Tanques", porcentaje));

                bcGasoil = new BarChart(xAxis, yAxis);
                bcGasoil.getData().add(dataSerie);                
                bcGasoil.setLegendVisible(false);
                vhTanque.getChildren().add(bcGasoil);
            }
        });
    }

    private void updateTanque() {
        bcGasoil.getData().clear();
        XYChart.Series dataSerie = new XYChart.Series();
        double porcentaje = ((ultimoMovimiento().getStock() * 100) / 36000);
        dataSerie.getData().add(new XYChart.Data("Tanques", porcentaje));
        bcGasoil.getData().add(dataSerie);
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Gasoil>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getInfo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (item.getInfo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Gasoil> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        gasoilSelect = (Gasoil) table.getSelectionModel().getSelectedItem();
    }

    private void add() {
        gasoilSelect = null;
        edit();
    }

    private void edit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gasoil/GasoilDialog.fxml"));
            GasoilDialogController controller = new GasoilDialogController(gasoilSelect);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());
            Stage dialog = new Stage();
            dialog.setTitle("Movimiento de Gas-Oil");
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();
            this.loadData(listGasoil(dpDesde.getValue(), dpHasta.getValue()));
            updateTanque();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    private void clearAll() {
        data.clear();
        searchBox.clear();
        gasoilSelect = null;
    }

}