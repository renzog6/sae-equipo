package ar.nex.app;

import ar.nex.entity.equipo.gasto.Gasoil;
import ar.nex.equipo.util.DateUtils;
import ar.nex.service.JpaService;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDate;

/**
 *
 * @author Renzo
 */
public class TestQuery {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        JpaService jpa = new JpaService();
//
//        EntityManager em = jpa.getFactory().createEntityManager();
//        TypedQuery<Gasoil> query
//                = em.createQuery("SELECT c FROM Gasoil c ORDER BY c.fecha, c.idGasto ASC", Gasoil.class);
//        List<Gasoil> results = query.getResultList();
//
//        System.out.println("ar.nex.app.TestQuery.main():" + results.toString());

        LocalDate ld = LocalDate.now();
        ld=null;
//        System.out.println("ar.nex.app.TestQuery.main() ld:" + ld.toString());
        System.out.println("ar.nex.app.TestQuery.main()  DateUtils.validate(ld):" + DateUtils.validate(ld));
    }

}
