package com.untref.modelo;

import java.util.ArrayList;
import java.util.List;

public enum Fotogramas {

    ABUELA,
    HOJA,
    CUADROS,
    OFICINA;

    public static List<String> obtenerTodosLosFotogramas(Fotogramas fotogramas) {

        List<String> lista = new ArrayList<>();

        switch (fotogramas) {
            case ABUELA:
                for (int i = 1; i < 96; i++) {

                    lista.add("Abuela/Frame" + i + ".jpeg");

                }
                break;
            case HOJA:
                for (int i = 1; i < 269; i++) {
                    String numero = "";
                    if (i < 10) {
                        numero = "00" + i;
                    } else if (i < 100) {
                        numero = "0" + i;
                    } else {
                        numero = "" + i;
                    }
                    lista.add("/resources/Hojita/PIC00" + numero + ".jpg");
                }
                break;
            case CUADROS:
                for (int i = 1; i < 198; i++) {

                    lista.add("/resources/Cuadros/Frame" + i + ".jpeg");
                }
                break;
            case OFICINA:
                for (int i = 1; i < 88; i++) {

                    lista.add("/resources/Oficina/Frame" + i + ".jpeg");
                }
                break;
        }

        /* if (ABUELA.equals(fotogramas)) {

         for (int i = 1; i < 96; i++) {

         lista.add("/resources/Abuela/Frame" + i + ".jpeg");
         }
         }*/
        return lista;
    }
}
