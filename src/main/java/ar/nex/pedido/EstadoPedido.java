package ar.nex.pedido;

/**
 *
 * @author Renzo
 */
public enum EstadoPedido {

    PENDIENTE(1, "Pendiente"), 
    INCOMPLETO(3, "Incompleto"),
    COMPLETO(5, "Completo"),
    CANCELADO(7, "Cancelado");

    private int value;
    private String estado;

    private EstadoPedido(int value, String estado) {
        this.value = value;
        this.estado = estado;
    }

    public int getValue() {
        return value;
    }

    public String getNombre() {
        return estado;
    }

    @Override
    public String toString() {
        return estado;
    }

    
}
