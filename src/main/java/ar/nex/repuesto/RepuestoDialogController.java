package ar.nex.repuesto;

import ar.nex.entity.Empresa;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.Marca;
import ar.nex.entity.Repuesto;
import ar.nex.equipo.EquipoService;
import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.MarcaJpaController;
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
    @FXML
    private TextField boxParte;

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
        loadDataProvedor();
        loadDataMarca();
        loadDataModelo();
        initBoxs();
    }

    private void initControls() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxCodigo.requestFocus();
            }
        });

        btnCancelar.setOnAction(e -> cancelar(e));

        if (repuesto.getIdRepuesto() != null) {
            boxMarca.setDisable(true);
            boxModelo.setDisable(true);
            boxProvedor.setDisable(true);
        }
    }

    private void initBoxs() {
        boxCodigo.setText(repuesto.getCodigo());
        boxDescripcion.setText(repuesto.getDescripcion());
        boxMarca.setText(repuesto.getMarca());
        boxInfo.setText(repuesto.getInfo());
        boxParte.setText(repuesto.getParte());
    }

    private void cancelar(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void guardar(ActionEvent event) {
        System.out.println("ar.nex.pedido.RepuestoDialogController.guardar()");
        try {
            repuesto.setCodigo(boxCodigo.getText());
            repuesto.setDescripcion(boxDescripcion.getText());
            repuesto.setInfo(boxInfo.getText());
            repuesto.setParte(boxParte.getText());
            repuesto.setStock(0.0);

            jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            if (repuesto.getIdRepuesto() != null) {
                jpaRepuesto.edit(repuesto);
            } else {
                repuesto.setMarca(marcaSelect.toString());

                List<Empresa> lstProveedor = new ArrayList<>();
                lstProveedor.add(provedorSelect);
                repuesto.setEmpresaList(lstProveedor);

                List<EquipoModelo> lstModelo = new ArrayList<>();
                lstModelo.add(modeloSelect);
                repuesto.setModeloList(lstModelo);

                jpaRepuesto.create(repuesto);
            }

            cancelar(event);
        } catch (Exception e) {
            System.out.println(e);
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

        private Marca marcaSelect;

    private final ObservableList<Marca> dataMarca = FXCollections.observableArrayList();

    private void loadDataMarca() {
        try {
            this.dataMarca.clear();
            MarcaJpaController jpaMarca = new EquipoService().getMarca();
            List<Marca> lst = jpaMarca.findMarcaEntities();
            lst.forEach((item) -> {
                this.dataMarca.add(item);
            });

            AutoCompletionBinding<Marca> autoMarca = TextFields.bindAutoCompletion(boxMarca, dataMarca);

            autoMarca.setOnAutoCompleted(
                    (AutoCompletionBinding.AutoCompletionEvent<Marca> event) -> {
                        marcaSelect = event.getCompletion();
                    }
            );

        } catch (Exception e) {
            System.err.println(e);
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
