package ar.nex.util;

import ar.nex.entity.Marca;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

/**
 *
 * @author Renzo
 */
public class DialogController {

//    private static EntityManagerFactory factory;
//    private static EmpresaJpaController srvEmpresa;
//    private static Empresa empresaSelect;

    public static void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion Dialogo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean confirmDialog(String msg) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmacion Dialogo");
        alert.setHeaderText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public static String selectMarca() {
        System.out.println("ar.nex.util.DialogController.selectMarca()");

        MarcaController marcac = new MarcaController();
        ObservableList<Marca> data = FXCollections.observableArrayList();
        data = marcac.gatData();

        ChoiceDialog<Marca> dialog;
        dialog = new ChoiceDialog<>(data.get(0), data);

        dialog.setTitle("Marca Dialogo");
        dialog.setContentText("Marca:");

        Optional<Marca> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get().getNombre();
        }
        return "";
    }
//
//    public static Pedido addToPedido(Repuesto repuesto) {
//        System.out.println("ar.nex.util.DialogController.addToPedido()");
//        Pedido pedido = new Pedido();
//
//        Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Agregar a Pedido");
//        alert.setHeaderText("Repuesto: " + repuesto.toString());
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 20, 10, 10));
//
//        grid.add(new Label("Fecha: "), 0, 0);
//        TextField fecha = new TextField();
//        Date d = new Date();
//        DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
//        fecha.setText(fd.format(d));
//        fecha.setAlignment(Pos.CENTER);
//        grid.add(fecha, 1, 0);
//
//        grid.add(new Label("Cantidad: "), 0, 1);
//        TextField cantidad = new TextField();
//        cantidad.setAlignment(Pos.CENTER);
//        grid.add(cantidad, 1, 1);
//
//        grid.add(new Label("Proveedor: "), 0, 2);
//        TextField empresa = new TextField();
//        empresa.setAlignment(Pos.CENTER);
//        grid.add(empresa, 1, 2);
//
//        factory = Persistence.createEntityManagerFactory("SaeFxPU");
//        srvEmpresa = new EmpresaJpaController(factory);
//        List<Empresa> lst = srvEmpresa.findEmpresaEntities();
//
//        TextFields.bindAutoCompletion(empresa, lst).setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<Empresa>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<Empresa> event) {
//                empresaSelect = event.getCompletion();
//            }
//        });
//
//        grid.add(new Label("Observacion: "), 0, 3);
//        TextField observacion = new TextField();
//        empresa.setAlignment(Pos.CENTER);
//        grid.add(observacion, 1, 3);
//
//        alert.getDialogPane().setContent(grid);
//        Platform.runLater(() -> cantidad.requestFocus());
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            try {
//                pedido.setFechaInicio(fd.parse(fecha.getText()));
//                pedido.getRepuestoList().add(repuesto);
//                pedido.setCantidad(Integer.parseInt(cantidad.getText()));
//                pedido.setInfo(observacion.getText());
//                //pedido.setEstado(EstadoPedido.PENDIENTE);
//                pedido.setEstado(1);
//                pedido.setEmpresa(empresaSelect);
//            } catch (ParseException ex) {
//                Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return pedido;
//    }
}
