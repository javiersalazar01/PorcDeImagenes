/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.bordes;

import com.untref.enums.Canal;
import com.untref.modelo.Imagen;
import ij.ImagePlus;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author karlagutz
 */
public class TransfHough {

    public static ImagePlus houghRectas(ImagePlus imagen, int umbral) {

        ImagePlus resultado = new ImagePlus();
        resultado.setImage(new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
        resultado.setTitle(imagen.getTitle() + " - Hough");

        double epsilon = 0.1;
        double[] valoresDeRo = new double[20];
        double[] valoresDeTheta = new double[20];

        for (int i = 0; i < 20; i++) {
            valoresDeTheta[i] = -90 + 9 * i;
        }

        int D = 0;
        if (imagen.getWidth() > imagen.getHeight()) {
            D = imagen.getWidth();
        } else {
            D = imagen.getHeight();
        }

        double incrementoDeRo = (2 * Math.sqrt(2) * D) / 20;
        for (int i = 0; i < 20; i++) {
            valoresDeRo[i] = -Math.sqrt(2) * D + incrementoDeRo * i;
        }

        int[][] acumulacion = new int[20][20];

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {

                if (imagen.getPixel(i, j)[0] == 255) {

                    for (int p = 0; p < valoresDeRo.length; p++) {
                        for (int t = 0; t < valoresDeTheta.length; t++) {

                            if (Math.abs(valoresDeRo[p] - i * Math.cos(Math.toRadians(valoresDeTheta[t]))
                                    - j * Math.sin(Math.toRadians(valoresDeTheta[t]))) < epsilon) {
                                acumulacion[p][t]++;
                            }
                        }
                    }
                }
            }
        }

        for (int p = 0; p < valoresDeRo.length; p++) {
            for (int t = 0; t < valoresDeTheta.length; t++) {

                if (acumulacion[p][t] >= 3) {

                    resultado.getProcessor().putPixel((int) valoresDeRo[p],
                            (int) (Math.sin(Math.toRadians(valoresDeTheta[t])) * valoresDeRo[p]), 255);
                }
            }
        }

        return resultado;
    }

    public static Imagen houghRectas(Imagen imagen, int umbral) {

        Imagen resultado = new Imagen(imagen.getBufferedImage(), imagen.getFormato(), imagen.getNombre(), imagen.getMatriz(Canal.ROJO), imagen.getMatriz(Canal.VERDE), imagen.getMatriz(Canal.AZUL));

        double epsilon = 0.1;
        double[] valoresDeRo = new double[20];
        double[] valoresDeTheta = new double[20];

        for (int i = 0; i < 20; i++) {
            valoresDeTheta[i] = -90 + 9 * i;
        }

        int D = 0;
        if (imagen.getBufferedImage().getWidth() > imagen.getBufferedImage().getHeight()) {
            D = imagen.getBufferedImage().getWidth();
        } else {
            D = imagen.getBufferedImage().getHeight();
        }

        double incrementoDeRo = (2 * Math.sqrt(2) * D) / 20;
        for (int i = 0; i < 20; i++) {
            valoresDeRo[i] = -Math.sqrt(2) * D + incrementoDeRo * i;
        }

        int[][] acumulacion = new int[20][20];

        for (int i = 0; i < imagen.getBufferedImage().getWidth(); i++) {
            for (int j = 0; j < imagen.getBufferedImage().getHeight(); j++) {

                if (imagen.getBufferedImage().getRGB(i, j) == 255) {

                    for (int p = 0; p < valoresDeRo.length; p++) {
                        for (int t = 0; t < valoresDeTheta.length; t++) {

                            if (Math.abs(valoresDeRo[p] - i * Math.cos(Math.toRadians(valoresDeTheta[t]))
                                    - j * Math.sin(Math.toRadians(valoresDeTheta[t]))) < epsilon) {
                                acumulacion[p][t]++;
                            }
                        }
                    }
                }
            }
        }

        for (int p = 0; p < valoresDeRo.length; p++) {
            for (int t = 0; t < valoresDeTheta.length; t++) {

                if (acumulacion[p][t] >= 3) {

                    resultado.getBufferedImage().setRGB((int) valoresDeRo[p], (int) (Math.sin(Math.toRadians(valoresDeTheta[t])) * valoresDeRo[p]), new Color(255).getRGB());
                                               // .getProcessor().putPixel((int) valoresDeRo[p],
                    //	(int) (Math.sin(Math.toRadians(valoresDeTheta[t])) * valoresDeRo[p]), 255);
                }
            }
        }

        return resultado;
    }

    public static ImagePlus houghCirculos(ImagePlus imagen, int umbral) {

        ImagePlus resultado = new ImagePlus();
        resultado.setImage(new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
        resultado.setTitle(imagen.getTitle() + " - Hough");

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {

            }
        }

        return resultado;
    }
}
