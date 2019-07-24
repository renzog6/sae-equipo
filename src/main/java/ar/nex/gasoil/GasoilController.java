package ar.nex.gasoil;

import ar.nex.entity.equipo.Gasoil;
import ar.nex.service.JpaService;
import ar.nex.util.DialogController;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Label;
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
            root = FXMLLoader.load(getClass().getResource("/fxml/gasoil/Gasoil.fxml"));
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
    private TableColumn<?, ?> colEquipo;
    @FXML
    private TableColumn<Gasoil, String> colMovimiento;
    @FXML
    private TableColumn<Gasoil, String> colLitros;
    @FXML
    private TableColumn<Gasoil, String> colStock;
    @FXML
    private TableColumn<?, ?> colInfo;
    
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
                iniTable();
                loadData(listGasoil());
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
    
    private void iniTable() {
        //colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasoil, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasoil, String> p) {
                return new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy").format(p.getValue().getFecha()));
            }
        });
        
        colEquipo.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        //colMovimiento.setCellValueFactory(new PropertyValueFactory<>("movimineto"));
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
            //List<Gasoil> lst = jpa.getGasoil().findGasoilEntities();
            lst.forEach((item) -> {
                data.add(item);
            });
            table.setItems(data);
        } catch (Exception e) {
            DialogController.showException(e);
        }
    }
    
    private List<Gasoil> listGasoil() {
        EntityManager em = jpa.getFactory().createEntityManager();
        TypedQuery<Gasoil> query
                = em.createQuery("SELECT c FROM Gasoil c ORDER BY c.fecha ASC", Gasoil.class);
        List<Gasoil> results = query.getResultList();
        if (!results.isEmpty()) {
            return results;
        } else {
            return null;
        }
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
                bcGasoil = new BarChart(xAxis, yAxis);
                
                XYChart.Series dataSerie = new XYChart.Series();
                double porcentaje = ((ultimoStock() * 100) / 36000);
                dataSerie.getData().add(new XYChart.Data("Tanques", porcentaje));
                
                bcGasoil.getData().add(dataSerie);
                
                bcGasoil.setLegendVisible(false);
                vhTanque.getChildren().add(bcGasoil);
            }
        });
    }
    
    private void updateTanque() {
        bcGasoil.getData().clear();
        XYChart.Series dataSerie = new XYChart.Series();
        double porcentaje = ((ultimoStock() * 100) / 36000);
        dataSerie.getData().add(new XYChart.Data("Tanques", porcentaje));
        bcGasoil.getData().add(dataSerie);
    }
    
    private Double ultimoStock() {
        List<Gasoil> results = listGasoil();
        if (!results.isEmpty()) {
            return ((Gasoil) results.get(results.size() - 1)).getStock();
        } else {
            return 0.0;
        }
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
            this.loadData(listGasoil());
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
