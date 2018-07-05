package ar.nex.saefx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class HomeController implements Initializable {

    @FXML
    private BorderPane homePane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void goPedido(MouseEvent event) {
        loadUI("Pedido");
    }

    @FXML
    private void goRepuesto(MouseEvent event) {
        loadUI("Repuesto");
    }

    @FXML
    private void goEquipo(MouseEvent event) {
        loadUI("Equipo");
    }

    public void loadUI(String ui) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/" + ui + ".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        homePane.getStylesheets().add("/styles/" + ui + ".css");
        homePane.setCenter(root);
    }
}
