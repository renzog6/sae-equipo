package ar.nex.equipo;

import ar.nex.entity.Empresa;
import ar.nex.entity.Equipo;
import ar.nex.entity.EquipoCategoria;
import ar.nex.entity.EquipoCompraVenta;
import ar.nex.entity.EquipoModelo;
import ar.nex.entity.EquipoTipo;
import ar.nex.entity.Marca;

/**
 *
 * @author Renzo
 */
public class AppTestX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EquipoService service = new EquipoService();
        System.out.println("ar.nex.equipo.AppTestX.main()" + service.toString());

        EquipoCategoria c = service.getCategoria().findEquipoCategoria(Long.valueOf(1));//new EquipoCategoria();
        System.out.println("ar.nex.equipo.AppTestX.main()" + c.toString());
        //c.setNombre("Tractor");
        //service.getCategoria().create(c);
        EquipoModelo m = service.getModelo().findEquipoModelo(Long.valueOf(1));
        System.out.println("ar.nex.equipo.AppTestX.main()" + m.toString());
        // m.setNombre("8420");
        //service.getModelo().create(m);
        EquipoTipo t = service.getTipo().findEquipoTipo(Long.valueOf(1));
        System.out.println("ar.nex.equipo.AppTestX.main()" + t.toString());

        EquipoCompraVenta cv = service.getCompraVenta().findEquipoCompraVenta(Long.valueOf(1));
        System.out.println("ar.nex.equipo.AppTestX.main()" + cv.toString());

        Marca ma = service.getMarca().findMarca(Long.valueOf(1));
        System.out.println("ar.nex.equipo.AppTestX.main()" + m.toString());

        Equipo e = new Equipo();
        e.setAnio("2018");
        e.setCategoria(c);
        e.setModelo(m);
        e.setTipo(t);
        e.setMarca(ma);
        e.setCompraVenta(cv);

        Empresa emp = new Empresa();
        
        try {
            service.getEquipo().create(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            
        }

    }

}
