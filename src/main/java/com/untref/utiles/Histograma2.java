/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.utiles;

import com.untref.modelo.Imagen1;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author javi_
 */
public class Histograma2 {
    
    private BufferedImage imageActual;
    int[] histogramaGris;
    private int nivelDominanteGris;
    private int maxNumPixelesGris;
    private boolean esEscalaDeGrises;
    private int[] histogramaR;
    private int[] histogramaG;
    private int[] histogramaB;
    private int nivelDominanteR;
    private int maxNumPixelesR;
    private int nivelDominanteG;
    private int maxNumPixelesG;
    private int nivelDominanteB;
    private int maxNumPixelesB;
    

    public Histograma2(BufferedImage imageActual) {
        this.imageActual = imageActual;
        Color c = new Color(imageActual.getRGB(0, 0));
        if (c.getBlue() == c.getGreen() && c.getGreen() == c.getRed()) {
            esEscalaDeGrises = true;
            calcularHistogramaGris();
        } else  {
            esEscalaDeGrises = false;
            calcularHistogramaRGB();
        }
        
    }
    
    
    
    private void calcularHistogramaGris() {
        histogramaGris = new int[256];
        Color c;
        int nivelGris;
        //realiza el conteo de numero de pixeles por nivel de intensidad
        for(int i = 0; i < imageActual.getWidth(); i++) {
            for(int j = 0; j < imageActual.getHeight(); j++) {
                c = new Color(imageActual.getRGB(i, j));
                nivelGris = c.getRed();
                if(nivelGris < 256)
                    histogramaGris[nivelGris]++;
            }
        }
        //busca el maximo numero de pixeles para el nivel de intensidad dominante
        nivelDominanteGris = 0;
        maxNumPixelesGris = 0;
        for(int i = 0; i < histogramaGris.length; i++) {
            if(histogramaGris[i] > maxNumPixelesGris) {
                maxNumPixelesGris = histogramaGris[i];
                nivelDominanteGris = i;
            }
        }
    }
    
     private Imagen1 getImagenHistogramaGris() {
        Imagen1 imagenHistograma = new Imagen1();
        imagenHistograma.setFormato("P2");
        imagenHistograma.setM((short)256);
        imagenHistograma.setN((short)270);
        imagenHistograma.setNivelIntensidad((short)255);
        //matriz para dibujar el histograma
        short matrizGris[][] = new short[imagenHistograma.getN()][imagenHistograma.getM()];
        //todos los elementos de la matriz estan en blanco
        for(int i = 0; i < imagenHistograma.getN(); i++) {
            for(int j = 0; j < imagenHistograma.getM(); j++) {
                matrizGris[i][j] = 255;
            }
        }
        //crea la matriz con los datos del histograma
        for(int j = 0; j < histogramaGris.length; j++) {
            int numPixeles = histogramaGris[j];
            numPixeles = (255 * numPixeles) / maxNumPixelesGris;
            for(int n = 0; n < numPixeles; n++) {
                int i = 255 - n;
                matrizGris[i][j] = 0;
            }
        }
        for(int i = 260; i < 270; i++) {
            for(int j = 0; j < imagenHistograma.getM(); j++) {
                matrizGris[i][j] = (short)j;
            }
        }
        //asigna la matriz del histograma a la imagen del histograma
        imagenHistograma.setMatrizGris(matrizGris);
        System.out.println("Histograma::Formato: "+imagenHistograma.getFormato()+" N: "+imagenHistograma.getN()+" M: "+imagenHistograma.getM()+" MaxNumPixeles: "+maxNumPixelesGris+" NivelDominante: "+nivelDominanteGris);
        return imagenHistograma;
    }
     
     private void calcularHistogramaRGB() {
        histogramaR = new int[256];
        histogramaG = new int[256];
        histogramaB = new int[256];
        //realiza el conteo de numero de pixeles por nivel de intensidad para cada canal RGB
        Color c;
        for(int i = 0; i < imageActual.getWidth(); i++) {
            for(int j = 0; j < imageActual.getHeight(); j++) {
                c = new Color(imageActual.getRGB(i, j));
                //canal R
                int nivelR = c.getRed();
                if(nivelR < 256)
                    histogramaR[nivelR]++;
                //canal G
                int nivelG = c.getGreen();
                if(nivelG < 256)
                    histogramaG[nivelG]++;
                //canal B
                int nivelB = c.getBlue();
                if(nivelB < 256)
                    histogramaB[nivelB]++;
            }
        }
        //busca el maximo numero de pixeles para el nivel de intensidad dominante por cada canal
        nivelDominanteR = 0;
        maxNumPixelesR = 0;
        nivelDominanteG = 0;
        maxNumPixelesG = 0;
        nivelDominanteB = 0;
        maxNumPixelesB = 0;
        for(int i = 0; i < histogramaR.length; i++) {//los histogramas RGB tienen la misma dimension
            //canal R
            if(histogramaR[i] > maxNumPixelesR) {
                maxNumPixelesR = histogramaR[i];
                nivelDominanteR = i;
            }
            //canal G
            if(histogramaG[i] > maxNumPixelesG) {
                maxNumPixelesG = histogramaG[i];
                nivelDominanteG = i;
            }
            //canal B
            if(histogramaB[i] > maxNumPixelesB) {
                maxNumPixelesB = histogramaB[i];
                nivelDominanteB = i;
            }
        }
    }
     
     private Imagen1 getImagenHistogramaRGB() {
        Imagen1 imagenHistograma = new Imagen1();
        imagenHistograma.setFormato("P3");
        imagenHistograma.setM((short)768);
        imagenHistograma.setN((short)270);
        imagenHistograma.setNivelIntensidad((short)255);
        //matriz para dibujar el histograma por cada canal RGB
        short matrizR[][] = new short[imagenHistograma.getN()][imagenHistograma.getM()];
        short matrizG[][] = new short[imagenHistograma.getN()][imagenHistograma.getM()];
        short matrizB[][] = new short[imagenHistograma.getN()][imagenHistograma.getM()];
        //todos los elementos de la matriz RGB estan en blanco
        for(int i = 0; i < imagenHistograma.getN(); i++) {
            for(int j = 0; j < imagenHistograma.getM(); j++) {
                matrizR[i][j] = 255;
                matrizG[i][j] = 255;
                matrizB[i][j] = 255;
            }
        }
        //crea la matriz RGB con los datos del histograma
        //canal R
        for(int j = 0; j < histogramaR.length; j++) {
            int numPixeles = histogramaR[j];
            numPixeles = (255 * numPixeles) / maxNumPixelesR;
            for(int n = 0; n < numPixeles; n++) {
                int i = 255 - n;
                matrizG[i][j] = 0;
                matrizB[i][j] = 0;
            }
        }
        for(int i = 260; i < 270; i++) {
            for(int j = 0; j < 255; j++) {
                matrizR[i][j] = (short)j;
                matrizG[i][j] = 0;
                matrizB[i][j] = 0;
            }
        }
        //cana G
        for(int n = 0; n < histogramaG.length; n++) {
            int numPixeles = histogramaG[n];
            numPixeles = (255 * numPixeles) / maxNumPixelesG;
            for(int i = 0; i < numPixeles; i++) {
                for(int j = 256; j < 510; j++) {
                    int k = 255 - i;
                    matrizR[k][n+256] = 0;
                    matrizB[k][n+256] = 0;
                }
            }
        }
        for(int i = 260; i < 270; i++) {
            int nivel = 0;
            for(int j = 256; j < 510; j++) {
                matrizG[i][j] = (short)nivel;
                matrizR[i][j] = 0;
                matrizB[i][j] = 0;
                nivel++;
            }
        }
        //canal B
        for(int n = 0; n < histogramaB.length; n++) {
            int numPixeles = histogramaB[n];
            numPixeles = (255 * numPixeles) / maxNumPixelesB;
            for(int i = 0; i < numPixeles; i++) {
                for(int j = 256; j < 510; j++) {
                    int k = 255 - i;
                    matrizR[k][n+511] = 0;
                    matrizG[k][n+511] = 0;
                }
            }
        }
        for(int i = 260; i < 270; i++) {
            int nivel = 0;
            for(int j = 511; j < 768; j++) {
                matrizB[i][j] = (short)nivel;
                matrizR[i][j] = 0;
                matrizG[i][j] = 0;
                nivel++;
            }
        }
        //asigna la matriz RGB del histograma a la imagen del histograma
        imagenHistograma.setMatrizR(matrizR);
        imagenHistograma.setMatrizG(matrizG);
        imagenHistograma.setMatrizB(matrizB);
        System.out.println("Histograma::Formato: "+imagenHistograma.getFormato()+" N: "+imagenHistograma.getN()+" M: "+imagenHistograma.getM()+" MaxNumPixelesR: "+maxNumPixelesR+" NivelDominanteR: "+nivelDominanteR
                +" MaxNumPixelesG: "+maxNumPixelesG+" NivelDominanteG: "+nivelDominanteG+" MaxNumPixelesB: "+maxNumPixelesB+" NivelDominanteB: "+nivelDominanteB);
        return imagenHistograma;
    }
     
     public Imagen1 getImageHistograma(){
         if (esEscalaDeGrises) {
             return getImagenHistogramaGris();
         } else {
             return getImagenHistogramaRGB();
         }
         
     }

    public int[] getHistogramaGris() {
        return histogramaGris;
    }
     
     
     
    
}
