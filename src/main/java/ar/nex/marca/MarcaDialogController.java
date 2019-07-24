package ar.nex.marca;

import ar.nex.entity.Marca;
import ar.nex.jpa.MarcaJpaController;
import ar.nex.service.JpaService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class MarcaDialogController implements Initializable {

    public MarcaDialogController(Marca m) {
        this.marca = m;
    }

    @FXML
    private AnchorPane apModelo;
    @FXML
    private TextField boxModelo;
    @FXML
    private TextField boxDescripcion;
    @FXML
    private TextField boxAnio;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private final Marca marca;
    
    private MarcaJpaController jpaMarca;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        jpaMarca = new JpaService().getMarca();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxModelo.requestFocus();
            }
        });

        btnGuardar.setOnAction(e -> guardar(e));
        btnCancelar.setOnAction(e ->cancelar(e));
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            marca.setNombre(boxModelo.getText());
           
            jpaMarca.create(marca);

            cancelar(event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}
