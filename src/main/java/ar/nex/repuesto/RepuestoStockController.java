package ar.nex.repuesto;

import ar.nex.entity.Equipo;
import ar.nex.entity.Pedido;
import ar.nex.entity.Repuesto;
import ar.nex.entity.RepuestoStockDetalle;
import ar.nex.util.EquipoUtils;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.jpa.RepuestoStockDetalleJpaController;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class RepuestoStockController {

    public RepuestoStockController() {                
    }    

    public void inStock(Pedido pedido) {
        System.out.println("ar.nex.pedido.PedidoStockController.inStock()");
        try {
            pedido.getRepuesto().setStock(pedido.getCantidad() + pedido.getRepuesto().getStock());
            RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaRepuesto.edit(pedido.getRepuesto());

            RepuestoStockDetalle stock = new RepuestoStockDetalle();
            stock.setFecha(pedido.getFechaFin());
            stock.setDetalle("Compra: " + pedido.getEmpresa().getNombre());
            stock.setCantidad(pedido.getCantidad());
            stock.setRepuesto(pedido.getRepuesto());
            stock.setEstado(1);
            RepuestoStockDetalleJpaController jpaStock = new RepuestoStockDetalleJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaStock.create(stock);

        } catch (Exception ex) {
            EquipoUtils.showException(ex);
        }
    }

    public void outStock(RepuestoStockDetalle stockDetalle) {
        System.out.println("ar.nex.repuesto.RepuestoStockController.outStock() " + stockDetalle.toString());
        try {
            RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            stockDetalle.getRepuesto().setStock(stockDetalle.getRepuesto().getStock()-stockDetalle.getCantidad());
            jpaRepuesto.edit(stockDetalle.getRepuesto());
            
            RepuestoStockDetalleJpaController jpaStockDetalle = new RepuestoStockDetalleJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaStockDetalle.create(stockDetalle);
            
        } catch (Exception ex) {
            //EquipoUtils.showException(ex);
            ex.printStackTrace();
        }
    }
}
