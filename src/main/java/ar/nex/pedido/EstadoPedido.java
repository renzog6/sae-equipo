package ar.nex.pedido;

/**
 *
 * @author Renzo
 */
public enum EstadoPedido {

    PENDIENTE(0, "Pendiente"),
    INCOMPLETO(1, "Incompleto"),
    COMPLETO(2, "Completo"),
    CANCELADO(3, "Cancelado");

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

    public EstadoPedido getEstado(int i) {
        switch (i) {
            case 1: return PENDIENTE;            
            case 3: return INCOMPLETO;
            case 5: return COMPLETO;
            case 7: return CANCELADO;
            default:return PENDIENTE;
        }
    }

    @Override
    public String toString() {
        return estado;
    }

}
