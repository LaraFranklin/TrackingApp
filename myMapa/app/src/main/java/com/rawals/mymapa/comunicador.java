package com.rawals.mymapa;

/**
 * Created by Ramon on 11/4/16.
 */
class Comunicador {
    private static Object objeto = null;
        //Metodo para recoger el objeto carrera
    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }
    //Metodo para devolver el objeto carrera
    public static Object getObjeto() {
        return objeto;
    }
}