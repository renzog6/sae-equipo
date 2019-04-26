package ar.nex.pedido;

import ar.nex.entity.Pedido;
import ar.nex.entity.StockDetalle;

/**
 *
 * @author Renzo
 */
public class PedidoStockController {

    public PedidoStockController(Pedido pedido) {
        this.pedido = pedido;
    }
    
    
    private final Pedido pedido;
    private StockDetalle stock;
    
    public void inStock(){
        System.out.println("ar.nex.pedido.PedidoStockController.inStock()");
        stock = new StockDetalle(pedido.getFechaInicio());
        
    }
    
    public void outStock(){
        
    }
}
