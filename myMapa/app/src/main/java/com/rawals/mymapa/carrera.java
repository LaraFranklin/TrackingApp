package com.rawals.mymapa;

/**
 * Created by Ramon on 31/3/16.
 */
public class carrera {
    private CharSequence fecha;
    private CharSequence distancia;
    private CharSequence duracion;
    private String polilinea;
    private String tipo;

    public carrera() {


    }

    public carrera(CharSequence fecha, CharSequence distancia, CharSequence duracion,String polilinea, String tipo) {
        this.fecha = fecha;
        this.distancia = distancia;
        this.duracion = duracion;
        this.polilinea = polilinea;
        this.tipo = tipo;

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPolilinea() {
        return polilinea;
    }

    public void setPolilinea(String polilinea) {
        this.polilinea = polilinea;
    }

    public CharSequence getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public CharSequence getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public CharSequence getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }


    @Override
    public String toString() {
        return

                "Fecha=" + fecha + "Distancia=" + distancia + "Duracion" + duracion;
    }
}
