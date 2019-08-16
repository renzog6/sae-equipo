package ar.nex.equipo.transporte;

import ar.nex.entity.equipo.Transporte;
import ar.nex.equipo.util.DialogController;
import ar.nex.service.JpaService;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class TransporteController implements Initializable {

    public TransporteController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/transporte/TransporteList.fxml"));
            root.setStyle("/fxml/equipo/Equipo.css");
        } catch (IOException ex) {
            Logger.getLogger(TransporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpTransporte;
    @FXML
    private TextField searchBox;
    @FXML
    private MenuButton mbMenu;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private final ObservableList<Transporte> data = FXCollections.observableArrayList();
    private final FilteredList<Transporte> filteredData = new FilteredList<>(data);
    private Transporte transporteSelect;
    @FXML
    private TableView<Transporte> table;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<Transporte, String> colChofer;
    @FXML
    private TableColumn<Transporte, String> colCamion;
    @FXML
    private TableColumn<Transporte, String> colAcoplado;
    @FXML
    private TableColumn<?, ?> colInfo;
    @FXML
    private TableColumn<?, ?> colOtro;

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
                iniTable();
                loadData();
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void iniTable() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colChofer.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transporte, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transporte, String> param) {
                try {
                     String result = param.getValue().getChofer().getNombreCompleto();
                    return new SimpleStringProperty(result);
                } catch (Exception e) {
                    return new SimpleStringProperty("NNN");
                }
            }
        });

        colCamion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transporte, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transporte, String> param) {
                try {
                    String result = String.join("\n", param.getValue().getCamion().getPatente(),
                            param.getValue().getCamion().getAnio());
                    return new SimpleStringProperty(result);
                } catch (Exception e) {
                    return new SimpleStringProperty("CCC");
                }
            }
        });

        colAcoplado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transporte, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transporte, String> param) {
                try {
                    String result = String.join("\n", param.getValue().getAcoplado().getPatente(),
                            param.getValue().getAcoplado().getAnio());
                    return new SimpleStringProperty(result);
                } catch (Exception e) {
                    return new SimpleStringProperty("XXX");
                }
            }
        });

        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
    }

    private void loadData() {
        try {
            clearAll();
            List<Transporte> lst = jpa.getTransporte().findTransporteEntities();
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

    @FXML
    private void showOnClick(MouseEvent event) {
    }

    private void add() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void edit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FXML
    private void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Transporte>) item -> {
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
        SortedList<Transporte> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void clearAll() {
        data.clear();
        searchBox.clear();
        transporteSelect = null;
    }

}
