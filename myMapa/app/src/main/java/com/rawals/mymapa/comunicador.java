package com.rawals.mymapa;

/**
 * Created by Ramon on 11/4/16.
 */
class Comunicador {
    private static Object objeto = null;

    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }

    public static Object getObjeto() {
        return objeto;
    }
}