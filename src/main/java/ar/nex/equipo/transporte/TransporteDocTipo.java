package ar.nex.equipo.transporte;

/**
 *
 * @author Renzo
 */
public enum TransporteDocTipo {

    PATENTE(0, "Patente"),
    TECNICA(1, "Tecnica"),
    RUTA(2, "RUTA"),
    OTRO(3, "Otro");

    private final int value;
    private final String estado;

    private TransporteDocTipo(int value, String estado) {
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
