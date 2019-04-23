package ar.nex.pedido;

import ar.nex.entity.Empresa;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.equipo.EquipoService;
import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.PedidoJpaController;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class RepuestoPedidoDialogController implements Initializable {
    
    public RepuestoPedidoDialogController(Repuesto r) {
        this.respuesto = r;
    }
    
    @FXML
    private Label lblCodigo;
    @FXML
    private Label lblProvedor;
    
    @FXML
    private TextField boxFecha;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxProvedor;
    @FXML
    private TextField boxInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    
    private final Repuesto respuesto;

    /**
     * Initializes the controll
     *
     * @param url class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initControls();
        loadDataProvedor();
    }
    
    private void initControls() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxCantidad.requestFocus();
                }
            });
            
            lblCodigo.setText("Codigo: " + respuesto.toString());
            
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            boxFecha.setText(fd.format(new Date()));

            //boxProvedor.setPromptText(respuesto.getLastPedido().getEmpresa().toString());
            lblProvedor.setText("Ultimo Provedor: " + respuesto.getLastPedido().getEmpresa().toString());
            provedorSelect = respuesto.getLastPedido().getEmpresa();
            
            btnGuardar.setOnAction(e -> guardar(e));
            btnCancelar.setOnAction(e -> cancelar(e));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Empresa provedorSelect;
    
    private final ObservableList<Empresa> dataProvedor = FXCollections.observableArrayList();
    
    private void loadDataProvedor() {
        try {
            this.dataProvedor.clear();
            EmpresaJpaController jpaProvedor = new EquipoService().getEmpresa();
            List<Empresa> lst = jpaProvedor.findEmpresaEntities();
            lst.forEach((item) -> {
                this.dataProvedor.add(item);
            });
            
            AutoCompletionBinding<Empresa> autoProvedor = TextFields.bindAutoCompletion(boxProvedor, dataProvedor);
            
            autoProvedor.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<Empresa> event) -> {
                        provedorSelect = event.getCompletion();
                    }
            );
            
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    @FXML
    private void guardar(ActionEvent event) {
        try {
            Pedido pedido = new Pedido(respuesto);
            
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            pedido.setFechaInicio(fd.parse(boxFecha.getText()));
            
            pedido.setCantidad(Integer.parseInt(boxCantidad.getText()));
            pedido.setEmpresa(provedorSelect);
            pedido.setInfo(boxInfo.getText());
            
            pedido.setEstado(EstadoPedido.PENDIENTE.getValue());
            
            PedidoJpaController jpaPedido = new PedidoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            if (pedido.getIdPedido() != null) {
                jpaPedido.edit(pedido);
            } else {
                jpaPedido.create(pedido);
            }
            
            cancelar(event);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }
}
