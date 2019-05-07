package ar.nex.app;

import java.net.URL;
import java.util.ResourceBundle;
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
    private BorderPane bpHome;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void goPedido(MouseEvent event) {
        loadUI("pedido/Pedido");
    }

    @FXML
    private void goRepuesto(MouseEvent event) {
        loadUI("repuesto/Repuesto");
    }

    @FXML
    private void goEquipo(MouseEvent event) {
        loadUI("equipo/Equipo");
    }

    public void loadUI(String ui) {
        System.out.println("ar.nex.app.HomeController.loadUI() : " + ui);
        try {
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/" + ui + ".fxml"));
            bpHome.getStylesheets().add("/fxml/" + ui + ".css");
            bpHome.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

