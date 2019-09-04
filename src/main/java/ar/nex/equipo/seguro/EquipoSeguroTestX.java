package ar.nex.equipo.seguro;

import ar.nex.entity.Seguro;
import ar.nex.entity.equipo.Equipo;
import ar.nex.equipo.util.DateUtils;
import ar.nex.service.JpaService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EquipoSeguroTestX extends Application {

    private final static Logger LOGGER = LogManager.getLogger(EquipoSeguroTestX.class.getName());

    @Override
    public void start(Stage stage) throws Exception {
        try {

//            JpaService jpa = new JpaService();
//            Seguro seg = new Seguro();
//            seg.setEmpresa(jpa.getEmpresa().findEmpresa(2L));
//            seg.setCompania(jpa.getEmpresa().findEmpresa(1083L));
//            seg.setDesde(new Date());
//            seg.setHasta(new Date());
//            seg.setPoliza("poliza-test");
//            seg.setReferencia("ref-test");
//            seg.setPrima(1234.0);
//            seg.setMonto(123456.0);
//            List<Equipo> lst = new ArrayList<>();
//            lst.add(jpa.getEquipo().findEquipo(59L));
//            lst.add(jpa.getEquipo().findEquipo(93L));
//            seg.setEquipoList(lst);
//            jpa.getSeguro().create(seg);

            stage.setTitle("SAE-App");
            Parent root = new EquipoSeguroController().getRoot();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "SAE-App launched on {}", DateUtils.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                LOGGER.log(Level.INFO, "SAE-App is closing on {}. Used for {} ms", DateUtils.formatDateTimeString(startTime), exitTime);
            }
        });
    }
}
