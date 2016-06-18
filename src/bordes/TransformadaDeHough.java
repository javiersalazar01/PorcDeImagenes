package bordes;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelo.Imagen;
import gui.Editar;
import java.awt.image.BufferedImage;

public class TransformadaDeHough {

    public static Imagen aplicarTransformadaDeHough(
            Imagen imagenOriginalUmbralizada, int titaMin, int titaMax,
            int discretizadoDeTitas, int roMin, int roMax, int discretizadoDeRos, int umbral, Editar ventana) {

        Point[][] matrizDeRectas = crearMatrizDeRectas(titaMin, titaMax, roMin,
                roMax, discretizadoDeTitas, discretizadoDeRos);

        int[][] matrizDeAcumulados = new int[matrizDeRectas.length][matrizDeRectas[0].length];

        for (int i = 0; i < imagenOriginalUmbralizada.getBufferedImage()
                .getWidth(); i++) {
            for (int j = 0; j < imagenOriginalUmbralizada.getBufferedImage()
                    .getHeight(); j++) {

                Color colorEnUmbralizada = new Color(imagenOriginalUmbralizada
                        .getBufferedImage().getRGB(i, j));
                if (colorEnUmbralizada.getRed() == 255
                        && colorEnUmbralizada.getGreen() == 255
                        && colorEnUmbralizada.getBlue() == 255) {

                    evaluarPuntoEnLaMatriz(matrizDeRectas, matrizDeAcumulados,
                            i, j);
                }
            }
        }

        List<Point> rectasMaximas = buscarRectasMaximas(matrizDeRectas,
                matrizDeAcumulados, umbral);

        dibujarLasRectas(imagenOriginalUmbralizada, rectasMaximas);

        // ventana.refrescarImagen();
        return null;
    }

    public static Imagen aplicarTransformadaDeHough(
            Imagen imagenOriginalUmbralizada, int titaMin, int titaMax,
            int discretizadoDeTitas, int roMin, int roMax, int discretizadoDeRos, int umbral) {

        Point[][] matrizDeRectas = crearMatrizDeRectas(titaMin, titaMax, roMin,
                roMax, discretizadoDeTitas, discretizadoDeRos);

        int[][] matrizDeAcumulados = new int[matrizDeRectas.length][matrizDeRectas[0].length];

        for (int i = 0; i < imagenOriginalUmbralizada.getBufferedImage()
                .getWidth(); i++) {
            for (int j = 0; j < imagenOriginalUmbralizada.getBufferedImage()
                    .getHeight(); j++) {

                Color colorEnUmbralizada = new Color(imagenOriginalUmbralizada
                        .getBufferedImage().getRGB(i, j));
                if (colorEnUmbralizada.getRed() == 255
                        && colorEnUmbralizada.getGreen() == 255
                        && colorEnUmbralizada.getBlue() == 255) {

                    evaluarPuntoEnLaMatriz(matrizDeRectas, matrizDeAcumulados,
                            i, j);
                }
            }
        }

        List<Point> rectasMaximas = buscarRectasMaximas(matrizDeRectas,
                matrizDeAcumulados, umbral);

        //Imagen imagenNueva = new Imagen(imagenOriginalUmbralizada.getBufferedImage(), imagenOriginalUmbralizada.getFormato(), imagenOriginalUmbralizada.getNombre() + "_Hough");
        dibujarLasRectas(imagenOriginalUmbralizada, rectasMaximas);

        System.out.println("Transformada de Hough OK");

        return null;
    }
    /*
     private static void dibujarLasRectas(Imagen imagen, List<Point> rectas) {

     for (Point recta: rectas){
			
     //angulo
     if ( recta.x == 0 || recta.x == 180){
				
     for (int i=0; i< imagen.getBufferedImage().getHeight(); i++){
     imagen.getBufferedImage().setRGB(recta.y, i, Color.RED.getRGB());
     }
     }
			
     if ( recta.x == 90 || recta.x == 270 ){
				
     for (int i=0; i< imagen.getBufferedImage().getWidth(); i++){
     imagen.getBufferedImage().setRGB(i, recta.y, Color.GREEN.getRGB());
     }
     }
			
     }
     }*/

    public static void dibujarLasRectas(Imagen imagen, List<Point> rectas) {

        for (Point recta : rectas) {

            //angulo
            if (recta.x == 0 || recta.x == 180) {

                for (int i = 0; i < imagen.getBufferedImage().getHeight(); i++) {
                    imagen.getBufferedImage().setRGB(recta.y, i, Color.RED.getRGB());
                }
            }

            if (recta.x == 90 || recta.x == 270) {

                for (int i = 0; i < imagen.getBufferedImage().getWidth(); i++) {
                    imagen.getBufferedImage().setRGB(i, recta.y, Color.GREEN.getRGB());
                }
            }

        }

    }

    /* public static Imagen houghRectas(Imagen imagen, int umbral) {

		
                
     Imagen resultado = new Imagen(imagen.getBufferedImage(), imagen.getFormato(), imagen.getNombre()+"_hough");
     resultado.setNombre(imagen.getNombre()+ " - Hough");

     BufferedImage res = resultado.getBufferedImage();
                
     double epsilon = 0.1;
     double[] valoresDeRo = new double[20];
     double[] valoresDeTheta = new double[20];

     for (int i = 0; i < 20; i++) {
     valoresDeTheta[i] = -90 + 9 * i;
     }

     int D = 0;
     if (res.getWidth() > res.getHeight()) {
     D = res.getWidth();
     } else {
     D = res.getHeight();
     }

     double incrementoDeRo = (2 * Math.sqrt(2) * D) / 20;
     for (int i = 0; i < 20; i++) {
     valoresDeRo[i] = -Math.sqrt(2) * D + incrementoDeRo * i;
     }

     int[][] acumulacion = new int[20][20];

     for (int i = 0; i < res.getWidth(); i++) {
     for (int j = 0; j < res.getHeight(); j++) {
     Color c = new Color(res.getRGB(i, j));
     if (c.getRed() == 255) {

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
     Color c = new Color(255,255,255);
     res.setRGB((int) valoresDeRo[p],(int) (Math.sin(Math.toRadians(valoresDeTheta[t])) * valoresDeRo[p]), c.getRed());
     /*resultado.getProcessor().putPixel((int) valoresDeRo[p],
     (int) (Math.sin(Math.toRadians(valoresDeTheta[t])) * valoresDeRo[p]), 255);
				
     }
     }
     }
     resultado.setBufferedImage(res);
     return resultado;
     }
     */
    private static List<Point> buscarRectasMaximas(Point[][] matrizDeRectas,
            int[][] matrizDeAcumulados, int umbral) {

        int maximo = Integer.MIN_VALUE;
        List<Point> posiciones = new ArrayList<>();
        List<Point> rectas = new ArrayList<>();

        for (int i = 0; i < matrizDeAcumulados.length; i++) {
            for (int j = 0; j < matrizDeAcumulados[0].length; j++) {

                if (matrizDeAcumulados[i][j] > maximo) {

                    maximo = matrizDeAcumulados[i][j];
                    posiciones.clear();
                    posiciones.add(new Point(i, j));
                } else if (matrizDeAcumulados[i][j] == maximo) {

                    posiciones.add(new Point(i, j));
                }
            }
        }

        //Busco las proximas al maximo
        posiciones.clear();
        for (int i = 0; i < matrizDeAcumulados.length; i++) {
            for (int j = 0; j < matrizDeAcumulados[0].length; j++) {

                if (maximo - matrizDeAcumulados[i][j] < umbral) {

                    posiciones.add(new Point(i, j));
                }
            }
        }

        for (Point puntos : posiciones) {

            int ro = matrizDeRectas[puntos.x][puntos.y].x;
            int tita = matrizDeRectas[puntos.x][puntos.y].y;

            rectas.add(new Point(ro, tita));
        }

        return rectas;
    }

    private static void evaluarPuntoEnLaMatriz(Point[][] matrizDeRectas,
            int[][] matrizDeAcumulados, int i, int j) {

        for (int w = 0; w < matrizDeRectas[0].length; w++) {
            for (int h = 0; h < matrizDeRectas.length; h++) {

                int tita = matrizDeRectas[h][w].x;
                int ro = matrizDeRectas[h][w].y;

                float ecuacion = (float) Math.abs(ro - (i * Math.cos(Math.toRadians(tita)))
                        - (j * Math.sin(Math.toRadians(tita))));
                float epsilon = 0.1f;

                if (ecuacion < epsilon) {

                    matrizDeAcumulados[h][w] = matrizDeAcumulados[h][w] + 1;
                }
            }
        }
    }
    /*
     private static Point[][] crearMatrizDeRectas(int titaMin, int titaMax,
     int roMin, int roMax, int discretizadoTitas, int discretizadoRos) {

     int cantidadDeTitas = (int) ((float) ((titaMax - titaMin) / discretizadoTitas)) + 1;
     int cantidadDeRos = (int) ((float) ((roMax - roMin) / discretizadoRos)) + 1;
     Point[][] matrizDeRectas = new Point[cantidadDeTitas][cantidadDeRos];

     for (int i = 0; i < cantidadDeTitas; i++) {
     for (int j = 0; j < cantidadDeRos; j++) {

     matrizDeRectas[i][j] = new Point(titaMin
     + (discretizadoTitas * i), roMin
     + (discretizadoRos * j));
     }
     }

     return matrizDeRectas;
     }*/

    private static Point[][] crearMatrizDeRectas(int titaMin, int titaMax,
            int roMin, int roMax, int discretizadoTitas, int discretizadoRos) {

        float titaDividida = (float) ((titaMax - titaMin) / discretizadoTitas);
        float RoDividia = (float) ((roMax - roMin) / discretizadoRos);
        Point[][] matrizDeRectas = new Point[discretizadoTitas][discretizadoRos];

        for (int i = 0; i < discretizadoTitas; i++) {

            for (int j = 0; j < discretizadoRos; j++) {

                matrizDeRectas[i][j] = new Point(titaMin
                        + Math.round(titaDividida * i), roMin
                        + Math.round(RoDividia * j));
            }
        }

        return matrizDeRectas;
    }
}
