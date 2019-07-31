package ar.nex.gasoil;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.gasto.Gasoil;
import ar.nex.service.JpaService;
import ar.nex.util.DateUtils;
import ar.nex.util.DialogController;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class GasoilDialogController implements Initializable {

    public GasoilDialogController(Gasoil gasoil) {
        this.gasoil = gasoil;
    }

    @FXML
    private DatePicker dpFecha;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField boxEquipo;
    @FXML
    private TextField boxLitros;
    @FXML
    private TextField boxPrecio;
    @FXML
    private TextField boxInfo;
    @FXML
    private ComboBox<GasoilMovimiento> cbMovimiento;
    @FXML
    private Label lblPrecio_kms;

    private Gasoil gasoil;
    private Equipo equipoSelect;
    private final ObservableList<Equipo> dataEquipo = FXCollections.observableArrayList();

    private JpaService jpa;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jpa = new JpaService();
        initControls();
    }

    private void initControls() {
        try {
            btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
            btnGuardar.setOnAction(e -> guardar(e));

            listaEquipo();
            AutoCompletionBinding<Equipo> autoProvedor = TextFields.bindAutoCompletion(boxEquipo, dataEquipo);
            autoProvedor.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Equipo> event) -> {
                equipoSelect = event.getCompletion();
                if (equipoSelect.getTipo().getNombre().equalsIgnoreCase("Camion")) {
                    boxPrecio.setDisable(false);
                    lblPrecio_kms.setText("Kilometros");
                } else {
                    boxPrecio.setDisable(true);
                    lblPrecio_kms.setText("?");
                }
            }
            );

            boxLitros.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*{0,7}([\\.]\\d*{0,4})?")) {
                        boxLitros.setText(oldValue);
                    }
                }
            });

            boxPrecio.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*{0,7}([\\.]\\d*{0,4})?")) {
                        boxLitros.setText(oldValue);
                    }
                }
            });
            boxPrecio.setDisable(true);

            cbMovimiento.getItems().addAll((ObservableList) FXCollections.observableArrayList(GasoilMovimiento.values()));
            cbMovimiento.getSelectionModel().select(GasoilMovimiento.CARGA.getValue());
            cbMovimiento.valueProperty().addListener(new ChangeListener<GasoilMovimiento>() {
                @Override
                public void changed(ObservableValue<? extends GasoilMovimiento> observable, GasoilMovimiento oldValue, GasoilMovimiento newValue) {
                    switch (newValue) {
                        case CARGA:
                            boxPrecio.setDisable(true);
                            boxEquipo.setDisable(false);
                            lblPrecio_kms.setText("?");
                            break;
                        case DESCARDA:
                            boxPrecio.setDisable(false);
                            lblPrecio_kms.setText("Precio x litro");
                            boxEquipo.setDisable(true);
                            break;
                        case OTRO:
                            boxPrecio.setDisable(true);
                            boxEquipo.setDisable(true);
                            break;
                    }
                }
            });

            DateUtils du = new DateUtils();
            if (gasoil != null) {
                dpFecha.setValue(du.convertToLocalDateViaInstant(gasoil.getFecha()));
                boxEquipo.setText(gasoil.getEquipo().toString());
                boxLitros.setText(gasoil.getLitros().toString());
                boxInfo.setText(gasoil.getInfo());
                boxPrecio.setText(gasoil.getPrecio().toString());
                cbMovimiento.getSelectionModel().select(gasoil.getMovimineto());
            } else {
                gasoil = new Gasoil();
                dpFecha.setValue(LocalDate.now());
            }
        } catch (Exception ex) {
            DialogController.showException(ex);
        }

    }

    private void guardar(ActionEvent e) {
        try {
            DateUtils du = new DateUtils();
            gasoil.setFecha(du.convertToDateViaSqlDate(dpFecha.getValue()));
            gasoil.setLitros(Double.parseDouble(boxLitros.getText()));

            gasoil.setMovimineto(cbMovimiento.getValue().getValue());
            Gasoil ultimo = ultimoMovimiento();
            switch (cbMovimiento.getValue()) {
                case CARGA:
                    gasoil.setStock(ultimo.getStock() - gasoil.getLitros());
                    gasoil.setPrecio(ultimo.getPrecio());
                    gasoil.setEquipo(equipoSelect);
                    if (equipoSelect.getTipo().getNombre().equalsIgnoreCase("Camion")) {
                        gasoil.setKms(Double.parseDouble(boxPrecio.getText()));
                    }                    
                    break;
                case DESCARDA:
                    gasoil.setStock(ultimo.getStock() + gasoil.getLitros());
                    gasoil.setPrecio(Double.parseDouble(boxPrecio.getText()));
                    gasoil.setEquipo(jpa.getEquipo().findEquipo(143L));
                    gasoil.setKms(0);
                    break;
            }

            gasoil.setInfo(boxInfo.getText());

            if (gasoil.getIdGasto() != null) {
                jpa.getGasoil().edit(gasoil);
            } else {
                jpa.getGasoil().create(gasoil);
            }
        } catch (Exception ex) {
            DialogController.showException(ex);
        } finally {
            ((Node) (e.getSource())).getScene().getWindow().hide();
            e.consume();
        }
    }

    private Gasoil ultimoMovimiento() {
        EntityManager em = jpa.getFactory().createEntityManager();
        TypedQuery<Gasoil> query
                = em.createQuery("SELECT c FROM Gasoil c ORDER BY c.fecha, c.idGasto ASC", Gasoil.class);
        List<Gasoil> results = query.getResultList();
        if (!results.isEmpty()) {
            return ((Gasoil) results.get(results.size() - 1));
        } else {
            return null;
        }
    }

    private void listaEquipo() {
        EntityManager em = jpa.getFactory().createEntityManager();
        TypedQuery<Equipo> query
                = em.createQuery("SELECT c FROM Equipo c WHERE c.gasoil=1", Equipo.class);
        List<Equipo> results = query.getResultList();
        if (!results.isEmpty()) {
            results.forEach((item) -> {
                dataEquipo.add(item);
            });
        }
    }

}
