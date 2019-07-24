package ar.nex.app;

import ar.nex.equipo.EquipoController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class HomeController implements Initializable {

    public HomeController() {
    }

    public Parent getRoot() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
            root.setStyle("/fxml/Home.css");
        } catch (IOException ex) {
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }

    @FXML
    private BorderPane bpHome;
    
    @FXML
    private MenuButton mbEquipo;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       initMenu();
    }

    private void initMenu(){
        mbEquipo.getItems().get(0).setOnAction(e->loadUI("equipo/Equipo"));
        mbEquipo.getItems().get(1).setOnAction(e->loadUI("repuesto/Repuesto"));
        mbEquipo.getItems().get(2).setOnAction(e->loadUI("pedido/Pedido"));
        mbEquipo.getItems().get(3).setOnAction(e->loadUI("repuesto/RepuestoUso"));
        mbEquipo.getItems().get(4).setOnAction(e->loadUI("gasoil/GasoilList"));
    }
    
    public void loadUI(String ui) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + ui + ".fxml"));
            bpHome.getStylesheets().add("/fxml/" + ui + ".css");
            bpHome.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
