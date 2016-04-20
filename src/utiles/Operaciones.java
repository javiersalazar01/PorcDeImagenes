/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author karlagutz
 */
public class Operaciones {

    //private BufferedImage imageActual;
    //private int nivelIntensidad;
    //ProcImagenes p = new ProcImagenes();

    /*public BufferedImage suma(BufferedImage imageActual, int valor) {
     int nrows, ncols;
     BufferedImage imageFinal;
     nrows = imageActual.getWidth();
     ncols = imageActual.getHeight();
     imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

     int[][] matrizGris = new int[nrows][ncols];
     int max = 0, min = 9999;
        
     for (int i = 0; i < nrows; i++) {
     for (int j = 0; j < ncols; j++) {
     Color color = new Color(imageActual.getRGB(i, j));
     int x = color.getRed();
     int suma = x + valor;
     matrizGris[i][j] = suma;                
     if (x > max) {
     max = x;
     } else if(min < x){
     min = x;
     }
   
     }
     }
        
     imageFinal = normalizaRango(matrizGris, max, min);
     return imageFinal;

     }
     */
    public BufferedImage suma(BufferedImage imageActual, BufferedImage imageOperando) {
        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        if (imageActual.getHeight() == imageOperando.getHeight() && imageActual.getWidth() == imageOperando.getWidth()) {

            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    Color color1 = new Color(imageActual.getRGB(i, j));
                    Color color2 = new Color(imageOperando.getRGB(i, j));

                    int suma = color1.getRed() + color2.getRed();
                    matrizGris[i][j] = suma;
                    if (suma > max) {
                        max = suma;
                    } else if (suma < min) {
                        min = suma;
                    }
                }
            }

        }
        imageFinal = normalizaRango(matrizGris, max, min);
        return imageFinal;
    }

    public BufferedImage resta(BufferedImage imageActual, int valor) {
        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                Color color = new Color(imageActual.getRGB(i, j));
                int suma = color.getRed() - valor;
                matrizGris[i][j] = suma;
                if (suma > max) {
                    max = suma;
                } else if (suma < min) {
                    min = suma;
                }

            }
        }
        imageFinal = normalizaRango(matrizGris, max, min);
        return imageFinal;
    }

    public BufferedImage resta(BufferedImage imageActual, BufferedImage imageOperando) {
        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        if (imageActual.getHeight() == imageOperando.getHeight() && imageActual.getWidth() == imageOperando.getWidth()) {

            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    Color color1 = new Color(imageActual.getRGB(i, j));
                    Color color2 = new Color(imageOperando.getRGB(i, j));

                    int suma = color1.getRed() - color2.getRed();
                    matrizGris[i][j] = suma;
                    if (suma > max) {
                        max = suma;
                    } else if (suma < min) {
                        min = suma;
                    }

                }
            }
        }
        imageFinal = normalizaRango(matrizGris, max, min);
        return imageFinal;
    }

    public BufferedImage producto(BufferedImage imageActual, BufferedImage imageOperando) {
        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                Color c1 = new Color(imageActual.getRGB(i, j));
                Color c2 = new Color(imageOperando.getRGB(i, j));

                int mult = c1.getRed() * c2.getRed();

                matrizGris[i][j] = mult;
                if (mult > max) {
                    max = mult;
                } else if (mult < min) {
                    min = mult;
                }
            }
        }
        imageFinal = normalizaRango(matrizGris, max, min);

        return imageFinal;
    }

    public BufferedImage producto(BufferedImage imageActual, int escalar) {
        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                Color c1 = new Color(imageActual.getRGB(i, j));

                int mult = c1.getRed() * escalar;
                matrizGris[i][j] = mult;
                if (mult > max) {
                    max = mult;
                } else if (mult < min) {
                    min = mult;
                }
            }
        }
        imageFinal = normalizaRango(matrizGris, max, min);
        return imageFinal;
    }

    public BufferedImage rangoDinamico(BufferedImage image) {

        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = image.getWidth();
        ncols = image.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        int R;
        int vMax = 0, posX = 0, posY = 0;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j));

                if (c.getRed() > vMax) {
                    vMax = c.getRed();
                    posX = i;
                    posY = j;
                }
            }
        }
        R = vMax;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int T, r;
                Color c = new Color(image.getRGB(i, j));
                r = c.getRed();
                T = (int) ((255 / Math.log(1 + R)) * Math.log(1 + r));

                matrizGris[i][j] = T;
                if (T > max) {
                    max = T;
                } else if (T < min) {
                    min = T;
                }

            }
        }
        imageFinal = normalizaRango(matrizGris, max, min);
        return imageFinal;
    }

    public BufferedImage powerLaw(BufferedImage imageActual, double gamma) {

        int nrows, ncols;
        BufferedImage imageFinal;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        int R = 0;
        int rn, vMax = 0, posX = 0, posY = 0;
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                Color c = new Color(imageActual.getRGB(i, j));
                rn = c.getRed();
                if (rn > vMax) {
                    vMax = rn;
                    posX = i;
                    posY = j;
                }
            }
        }
        R = vMax;

        double c = 255 / Math.pow(R, gamma);
        int T=0, r=0;
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
               
                Color color = new Color(imageActual.getRGB(i, j));
                r = color.getRed();
                
                T = (int) (c * Math.pow(r, gamma));

                matrizGris[i][j] = T;
                if (T > max) {
                    max = T;
                } else if (T < min) {
                    min = T;
                }

            }
        }
        imageFinal = normalizaRango(matrizGris, max, min);

        return imageFinal;
    }

    public BufferedImage normalizaRango(int[][] matriz, int max, int min) {
        int nrows, ncols;
        BufferedImage copia;
        nrows = matriz.length;
        ncols = matriz[0].length;
        //nrows = imageActual.getWidth();
        //ncols = imageActual.getHeight();
        copia = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

        int x = 0, T = 0;

        for (int i = 0; i < copia.getWidth(); i++) {
            for (int j = 0; j < copia.getHeight(); j++) {
                //Color c = new Color(copia.getRGB(i, j));
                x = matriz[i][j];
               
                T =  Math.abs(((255*x) - (255*min)) / (max-min));
                //System.out.println(T);
                //T = ((x - min) * 255) / (max - min);
                copia.setRGB(i, j, new Color(T, T, T).getRGB());
            }
        }
        System.out.println("normaliza OK");
        return copia;

    }
}
