package ar.nex.pedido;

import ar.nex.entity.Repuesto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    private TextField boxNombre;
    @FXML
    private TextField boxCompra;
    @FXML
    private TextField boxVenta;
    @FXML
    private TextField boxObservacion;
    
    private Repuesto repuesto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxCodigo.requestFocus();
            }
        });

        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
    }    

    private void initControls(){
        
    }
    
    @FXML
    private void guardar(ActionEvent event) {
    }
    
}
