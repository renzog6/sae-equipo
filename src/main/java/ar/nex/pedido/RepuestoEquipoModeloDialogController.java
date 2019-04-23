package ar.nex.pedido;

import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Repuesto;
import ar.nex.equipo.EquipoService;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.RepuestoJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
public class RepuestoEquipoModeloDialogController implements Initializable {

    public RepuestoEquipoModeloDialogController(Repuesto r) {
        this.repuesto = r;
    }

    @FXML
    private TextField boxModelo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private final Repuesto repuesto;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancelar.setOnAction(e -> cancelar(e));
        loadDataModelo();
    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
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

    @FXML
    private void guardar(ActionEvent event) {
        try {
            repuesto.getEquipoModeloList().add(modeloSelect);

            RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaRepuesto.edit(repuesto);

            cancelar(event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
