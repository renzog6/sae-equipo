package ar.nex.equipo;

import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.EquipoModelo;
import ar.nex.jpa.EquipoCategoriaJpaController;
import ar.nex.jpa.EquipoModeloJpaController;
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
public class EquipoCategoriaDialogController implements Initializable {

    public EquipoCategoriaDialogController(EquipoCategoria c) {
        this.categoria = c;
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

    private final EquipoCategoria categoria;
    private EquipoCategoriaJpaController jpaCategoria;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        jpaCategoria = new EquipoService().getCategoria();

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
            categoria.setNombre(boxModelo.getText());
           
            jpaCategoria.create(categoria);

            cancelar(event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}
