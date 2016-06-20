package com.untref.utiles;

import com.untref.enums.Canal;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class MatricesManager {

    static float[][] matrizRojos;
    static float[][] matrizVerdes;
    static float[][] matrizAzules;

    public static int[][] multiplicarMatrices(int[][] matriz1, int[][] matriz2) {

        int filasMatrizResultante = matriz1.length;
        int columnasMatrizResultante = matriz2[0].length;
        int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

        for (int i = 0; i < filasMatrizResultante; i++) {
            for (int j = 0; j < columnasMatrizResultante; j++) {
                for (int k = 0; k < filasMatrizResultante; k++) {
                    matrizResultante[i][j] += matriz1[i][k] * matriz2[k][j];
                }
            }
        }
        return matrizResultante;
    }

    public static int[][] multiplicarMatrizPorEscalar(int[][] matriz,
            int escalar) {

        int filasMatrizResultante = matriz.length;
        int columnasMatrizResultante = matriz[0].length;
        int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

        for (int i = 0; i < filasMatrizResultante; i++) {
            for (int j = 0; j < columnasMatrizResultante; j++) {
                matrizResultante[i][j] += matriz[i][j] * escalar;
            }
        }
        return matrizResultante;
    }

    public static int[][] sumarMatrizYEscalar(int[][] matriz,
            int escalar) {

        int filasMatrizResultante = matriz.length;
        int columnasMatrizResultante = matriz[0].length;
        int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

        for (int i = 0; i < filasMatrizResultante; i++) {
            for (int j = 0; j < columnasMatrizResultante; j++) {
                matrizResultante[i][j] += matriz[i][j] + escalar;
            }
        }
        return matrizResultante;
    }

    public static int[][] restarMatrizYEscalar(int[][] matriz,
            int escalar) {

        int filasMatrizResultante = matriz.length;
        int columnasMatrizResultante = matriz[0].length;
        int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

        for (int i = 0; i < filasMatrizResultante; i++) {
            for (int j = 0; j < columnasMatrizResultante; j++) {
                matrizResultante[i][j] += matriz[i][j] * escalar;
            }
        }
        return matrizResultante;
    }

    public static int[][] sumarMatrices(int[][] matriz1, int[][] matriz2) {
        int filasMatrizResultante = matriz1.length;
        int columnasMatrizResultante = matriz1[0].length;
        int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

        for (int i = 0; i < filasMatrizResultante; i++) {
            for (int j = 0; j < columnasMatrizResultante; j++) {
                matrizResultante[i][j] += matriz1[i][j] + matriz2[i][j];
            }
        }

        return matrizResultante;
    }

    public static int[][] restarMatrices(int[][] matriz1, int[][] matriz2) {

        int filasMatrizResultante = matriz1.length;
        int columnasMatrizResultante = matriz1[0].length;
        int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

        for (int i = 0; i < filasMatrizResultante; i++) {
            for (int j = 0; j < columnasMatrizResultante; j++) {

                matrizResultante[i][j] += matriz1[i][j] - matriz2[i][j];
            }
        }

        return matrizResultante;
    }

    public static void toString(int[][] m) {
        String barra = "|";
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                barra += m[i][j] + " ";
            }
            System.out.println(barra + "|");
            barra = "|";
        }
        System.out.println("\n");

    }

    public static BufferedImage obtenerImagenDeMatriz(int[][] matriz) {

        int width = matriz[0].length;
        int height = matriz.length;

        BufferedImage imagenResultante = null;
        imagenResultante = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Color color = new Color(matriz[i][j]);
                imagenResultante.setRGB(j, i, color.getRGB());
            }
        }

        return imagenResultante;
    }

    public static int[][] calcularMatrizDeLaImagen(BufferedImage image, Canal canal) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] matriz = new int[height][width];
        //int[][] matriz2 = new int[width][height];

        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {

                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red

                Color color = new Color(argb, true);
                switch (canal) {

                    case VERDE:
                        matriz[row][col] = color.getGreen();
                        break;
                    case AZUL:
                        matriz[row][col] = color.getBlue();
                        break;
                    default:
                        matriz[row][col] = color.getRed();
                        break;
                }

                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    Color color = new Color(image.getRGB(i, j));
                    switch (canal) {
                        case VERDE:
                            matriz[j][i] = color.getGreen();
                            break;
                        case AZUL:
                            matriz[j][i] = color.getBlue();
                            break;
                        default:
                            matriz[j][i] = color.getRed();
                            break;
                    }
                }
            }

            /*for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
             int argb = 0;
             argb += -16777216; // 255 alpha
             argb += ((int) pixels[pixel] & 0xff); // blue
             argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
             argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red

             Color color = new Color(argb);
             switch (canal) {

             case VERDE:
             matriz[row][col] = color.getGreen();
             break;
             case AZUL:
             matriz[row][col] = color.getBlue();
             break;
             default:
             matriz[row][col] = color.getRed();
             break;
             }

             col++;
             if (col == width) {
             col = 0;
             row++;
             }
             }*/
        }

        return matriz;
    }

    public static BufferedImage generarImagenRGBconContraste(int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules) {

        int ancho = matrizRojos[0].length;
        int alto = matrizRojos.length;

        BufferedImage imagenResultante = new BufferedImage(alto, ancho, BufferedImage.TYPE_3BYTE_BGR);

        if (ancho == alto) {
            for (int i = 0; i < ancho; i++) {
                for (int j = 0; j < alto; j++) {

                    Color color = new Color(matrizRojos[i][j], matrizVerdes[i][j], matrizAzules[i][j]);
                    imagenResultante.setRGB(i, j, color.getRGB());
                }
            }
            //fix para el caso que la imagen se espeja y da vuelta si no es cuadrada
        } else {

            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {

                    Color color = new Color(matrizRojos[i][j], matrizVerdes[i][j], matrizAzules[i][j]);
                    imagenResultante.setRGB(i, j, color.getRGB());
                }
            }
        }

        return imagenResultante;
    }

    public static BufferedImage generarImagenRGB(int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules) {

        int ancho = matrizRojos[0].length;
        int alto = matrizRojos.length;

        BufferedImage imagenResultante = new BufferedImage(ancho, alto, BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {

                Color color = new Color(matrizRojos[i][j], matrizVerdes[i][j], matrizAzules[i][j]);
                imagenResultante.setRGB(j, i, color.getRGB());
            }
        }

        return imagenResultante;
    }

    public static int[][] aplicarTransformacionLogaritmica(int[][] matrizDesfasada) {

        float maximo;

        int[][] matrizTransformada;

        int filas = matrizDesfasada.length;
        int columnas = matrizDesfasada[0].length;

        matrizTransformada = new int[filas][columnas];

        maximo = 255;

        for (int f = 0; f < filas; f++) {
            for (int g = 0; g < columnas; g++) {

                int valorActual = matrizDesfasada[f][g];

                if (maximo < valorActual) {
                    maximo = valorActual;
                }
            }
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                int valorActual = matrizDesfasada[i][j];

                int valorTransformado = (int) ((255f / (Math.log(maximo))) * Math.log(1 + valorActual));;

                matrizTransformada[i][j] = valorTransformado;
            }
        }

        return matrizTransformada;
    }

    public static int[][] aplicarTransformacionLineal(int[][] matrizDesfasada) {

        float minimo;
        float maximo;

        int[][] matrizTransformada;

        int filas = matrizDesfasada.length;
        int columnas = matrizDesfasada[0].length;

        matrizTransformada = new int[filas][columnas];

        minimo = 0;
        maximo = 255;

        for (int f = 0; f < filas; f++) {
            for (int g = 0; g < columnas; g++) {

                int valorActual = matrizDesfasada[f][g];

                if (minimo > valorActual) {
                    minimo = valorActual;
                }

                if (maximo < valorActual) {
                    maximo = valorActual;
                }

            }

        }

        float[] maximosYMinimos = new float[2];
        maximosYMinimos[0] = minimo;
        maximosYMinimos[1] = maximo;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                int valorActual = matrizDesfasada[i][j];
                int valorTransformado = (int) ((((255f) / (maximo - minimo)) * valorActual) - ((minimo * 255f) / (maximo - minimo)));

                matrizTransformada[i][j] = valorTransformado;
            }
        }

        return matrizTransformada;
    }

    public static BufferedImage obtenerImagenDeMatrices(int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules) {

        int filas = matrizRojos.length;
        int columnas = matrizRojos[0].length;

        BufferedImage imagenFinal = new BufferedImage(filas, columnas, BufferedImage.TYPE_3BYTE_BGR);

        for (int f = 0; f < filas; f++) {
            for (int g = 0; g < columnas; g++) {

                Color color = new Color(matrizRojos[f][g], matrizVerdes[f][g], matrizAzules[f][g]);
                imagenFinal.setRGB(f, g, color.getRGB());
            }
        }

        return imagenFinal;
    }

    public static int[][] elevarAlCuadrado(int[][] matriz) {

        int filas = matriz.length;
        int columnas = matriz[0].length;

        for (int f = 0; f < filas; f++) {
            for (int g = 0; g < columnas; g++) {

                matriz[f][g] = (int) Math.pow(matriz[f][g], 2);
            }
        }

        return matriz;
    }

    public static int[][] multiplicarValores(int[][] matrizRojoEnX,
            int[][] matrizRojoEnY) {

        int filas = matrizRojoEnX.length;
        int columnas = matrizRojoEnX[0].length;
        int[][] matrizResultado = new int[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int g = 0; g < columnas; g++) {

                matrizResultado[f][g] = matrizRojoEnX[f][g] * matrizRojoEnY[f][g];
            }
        }

        return matrizResultado;
    }
}
