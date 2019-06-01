package ar.nex.pedido;


import ar.nex.repuesto.RepuestoStockController;
import ar.nex.entity.Pedido;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.util.DialogController;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PedidoReciboDialogController implements Initializable {

    public PedidoReciboDialogController(Pedido p) {
        this.pedido = p;
    }

    @FXML
    private Label lblCodigo;

    @FXML
    private TextField boxFecha;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<?> filtroEstado;

    private Pedido pedido;

    /**
     * Initializes the controller
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancelar.setOnAction(e -> cancelar(e));
        btnGuardar.setOnAction(e -> guardar(e));

        initControls();
    }

    private void initControls() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxCantidad.requestFocus();
                }
            });

            lblCodigo.setText("Codigo: " + pedido.getRepuesto().toString());

            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            boxFecha.setText(fd.format(new Date()));

            boxCantidad.setText(pedido.getCantidad().toString());

            boxInfo.setText(pedido.getInfo());

            ObservableList list = FXCollections.observableArrayList(EstadoPedido.values());
            filtroEstado.getItems().addAll(list);
            filtroEstado.getSelectionModel().select(EstadoPedido.COMPLETO.getValue());
            System.out.println("ar.nex.pedido.PedidoReciboDialogController.initControls() :" + pedido.getRepuesto().getStock());
            
        } catch (Exception e) {
           DialogController.showException(e); 
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        try {
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            pedido.setFechaFin(fd.parse(boxFecha.getText()));

            pedido.setCantidad(Double.valueOf(boxCantidad.getText()));
            pedido.setInfo(boxInfo.getText());

            pedido.setEstado(filtroEstado.getSelectionModel().getSelectedIndex());

            PedidoJpaController jpaPedido = new PedidoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            if (pedido.getIdPedido() != null) {                
                jpaPedido.edit(pedido);
                System.out.println("ar.nex.pedido.PedidoReciboDialogController.guardar() : " + pedido.getRepuesto().getStock());
                new RepuestoStockController().inStock(pedido);
            } else {
                //jpaPedido.create(pedido);
                System.out.println("ar.nex.pedido.PedidoReciboDialogController.guardar() : VERRRRRRRRRRRRRRRRRRRRRRR");
            }

            cancelar(event);
        } catch (Exception e) {
           DialogController.showException(e); 
        }

    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}
