package ar.nex.marca;

import ar.nex.entity.Marca;
import ar.nex.util.DialogController;
import ar.nex.jpa.MarcaJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MarcaController implements Initializable {

    private DialogController dialog;

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxDescripcion;
    @FXML
    private TextField searchBox;
    @FXML
    private Button signOut;
    @FXML
    private Button addnewBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;

    ObservableList<Marca> data = FXCollections.observableArrayList();
    FilteredList<Marca> filteredData = new FilteredList<>(data);
    Marca select;

    @FXML
    private TableView<Marca> table;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colDescripcion;

    private EntityManagerFactory factory;
    private MarcaJpaController service;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.util.MarcaController.initialize()");
        initTable();
        initService();
        loadData();
    }

    public void clearAll() {
        System.out.println("ar.nex.util.MarcaController.clearAll()");
        data.clear();
        boxNombre.clear();
        boxDescripcion.clear();
        searchBox.clear();
        select = null;
    }

    public void initTable() {
        System.out.println("ar.nex.util.MarcaController.initTable()");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    public void initService() {
        System.out.println("ar.nex.util.MarcaController.initService()");
        factory = Persistence.createEntityManagerFactory("SaeFxPU");
        service = new MarcaJpaController(factory);
    }

    public void loadData() {
        System.out.println("ar.nex.util.MarcaController.loadData()");
        clearAll();

        List<Marca> lst = service.findMarcaEntities();
        for (Marca item : lst) {
            data.add(item);
            table.setItems(data);
        }
    }

    public ObservableList<Marca> gatData() {
        System.out.println("ar.nex.util.MarcaController.gatData()");
        initService();
        List<Marca> lst = service.findMarcaEntities();
        for (Marca item : lst) {
            data.add(item);            
        }
        return data;
    }
    
    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void goSignOut(ActionEvent event) {
        Stage stage = (Stage) signOut.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Add(ActionEvent event) {
        System.out.println("ar.nex.util.MarcaController.Add()");
        try {
            String nombre = boxNombre.getText();
            String descrp = boxDescripcion.getText();

            Marca item = new Marca();
            item.setNombre(nombre);
            item.setDescripcion(descrp);

            service.create(item);

        } catch (Exception e) {
            System.out.println(e);
        }
        dialog.showSuccess("New Added Successfully!!!");
        loadData();
    }

    @FXML
    private void Update(ActionEvent event) {
        System.out.println("ar.nex.util.MarcaController.Update()");
        try {
            String msg = "Confirma Actualizar " + select.toString() + " a " + boxNombre.getText();
            if (dialog.confirmDialog(msg)) {
                select.setNombre(boxNombre.getText());
                select.setDescripcion(boxDescripcion.getText());
                service.edit(select);
                dialog.showSuccess("Se Actualizo Correctamente!!!");
            } else {
                boxNombre.clear();
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
                service.destroy(select.getIdMarca());
                dialog.showSuccess("Se Elimino Correctamente!!!");
            } else {
                boxNombre.clear();
                boxDescripcion.clear();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadData();
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        System.out.println("ar.nex.util.MarcaController.showOnClick()");
        try {
            Marca item = (Marca) table.getSelectionModel().getSelectedItem();
            select = service.findMarca(item.getIdMarca());

            boxNombre.setText(item.getNombre());
            boxDescripcion.setText(item.getDescripcion());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
