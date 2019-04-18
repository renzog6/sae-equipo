package ar.nex.pedido;

import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Repuesto;
import ar.nex.equipo.EquipoService;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.RepuestoJpaController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoDialogController implements Initializable {

    public RepuestoDialogController() {
    }

    public RepuestoDialogController(Repuesto r) {
        this.repuesto = r;
    }

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField boxCodigo;
    @FXML
    private TextField boxDescripcion;
    @FXML
    private TextField boxMarca;
    @FXML
    private TextField boxProvedor;
    @FXML
    private TextField boxInfo;
    @FXML
    private TextField boxModelo;

    private Repuesto repuesto;

    private RepuestoJpaController jpaRepuesto;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initControls();
        loadDataModelo();
    }

    private void initControls() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxCodigo.requestFocus();
            }
        });

        btnCancelar.setOnAction(e -> cancelar(e));
    }
    
    private void cancelar(ActionEvent e){
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void guardar(ActionEvent event) {
        System.out.println("ar.nex.pedido.RepuestoDialogController.guardar()");
        try {
            repuesto.setCodigo(boxCodigo.getText());
            repuesto.setDescripcion(boxDescripcion.getText());
            repuesto.setMarca("marca");
            //repuesto.setEmpresaList(null);
            repuesto.setInfo(boxInfo.getText());
            repuesto.getEquipoModeloList().add(modeloSelect);

            jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaRepuesto.create(repuesto);

            cancelar(event);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private EquipoModelo modeloSelect;

    private final ObservableList<EquipoModelo> dataModelo = FXCollections.observableArrayList();

    private void loadDataModelo() {
        try {
            this.dataModelo.clear();
            EquipoModeloJpaController jpaModelo = new EquipoService().getModelo();
            List<EquipoModelo> lst = jpaModelo.findEquipoModeloEntities();
            lst.forEach((item) -> {
                this.dataModelo.add(item);
            });

            AutoCompletionBinding<EquipoModelo> autoModelo = TextFields.bindAutoCompletion(boxModelo, dataModelo);

            autoModelo.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<EquipoModelo> event) -> {
                        modeloSelect = event.getCompletion();
                    }
            );

        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
