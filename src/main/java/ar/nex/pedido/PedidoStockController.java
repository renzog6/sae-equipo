package ar.nex.pedido;

import ar.nex.entity.Pedido;
import ar.nex.entity.RepuestoStockDetalle;
import ar.nex.equipo.EquipoUtils;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.service.JpaService;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class PedidoStockController {

    public PedidoStockController(Pedido pedido) {
        this.pedido = pedido;
    }

    private final Pedido pedido;

    private RepuestoStockDetalle stock;

    private JpaService jpa;

    public void inStock() {
        System.out.println("ar.nex.pedido.PedidoStockController.inStock()");
        try {
            pedido.getRepuesto().setStock(pedido.getCantidad() + pedido.getRepuesto().getStock());
            RepuestoJpaController jpaRepuesto = new RepuestoJpaController(Persistence.createEntityManagerFactory("SaeFxPU"));
            jpaRepuesto.edit(pedido.getRepuesto());

        } catch (Exception e) {
            EquipoUtils.showException(e);
        }
    }

    public void outStock() {

    }
}
