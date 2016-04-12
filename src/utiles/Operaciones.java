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

    public BufferedImage suma(BufferedImage imageActual, int valor) {
        for (int i = 0; i < imageActual.getHeight(); i++) {
            for (int j = 0; j < imageActual.getWidth(); j++) {
                Color color = new Color(imageActual.getRGB(i, j));
                int suma = color.getRed()+ valor;
                if (suma > 255) {

                    imageActual.setRGB(i, j, new Color (255,255,255).getRGB());
                } else {
                    imageActual.setRGB(i, j, new Color (suma, suma, suma).getRGB());
                }

            }
        }
        return imageActual;

    }

    public BufferedImage suma(BufferedImage imageActual, BufferedImage imageOperando) {
        BufferedImage imagenResultado;
        imagenResultado = imageActual;
        if (imageActual.getHeight() == imageOperando.getHeight() && imageActual.getWidth() == imageOperando.getWidth()) {

            for (int i = 0; i < imageActual.getHeight(); i++) {
                for (int j = 0; j < imageActual.getWidth(); j++) {
                    Color color1 = new Color(imageActual.getRGB(i, j));
                    Color color2 = new Color(imageOperando.getRGB(i, j));
                    int suma = color1.getRed() + color2.getRed();
                    if (suma > 255) {
                        imagenResultado.setRGB(i, j, new Color(255,255,255).getRGB());
                    } else {
                        imagenResultado.setRGB(i, j, new Color(suma,suma,suma).getRGB());
                    }
                }
            }
        }

        return imagenResultado;
    }

    public BufferedImage resta(BufferedImage imageActual, int valor) {
        for (int i = 0; i < imageActual.getWidth(); i++) {
            for (int j = 0; j < imageActual.getHeight(); j++) {
                Color color = new Color(imageActual.getRGB(i, j));
                int suma = color.getRed() - valor;
                if (suma > 255) {

                    imageActual.setRGB(i, j, new Color(255,255,255).getRGB());
                } else {
                    imageActual.setRGB(i, j, new Color(suma,suma,suma).getRGB());
                }

            }
        }
        return imageActual;
    }

    public BufferedImage resta(BufferedImage imageActual, BufferedImage imageOperando) {
        BufferedImage imagenResultado;
        imagenResultado = imageActual;
        if (imageActual.getHeight() == imageOperando.getHeight() && imageActual.getWidth() == imageOperando.getWidth()) {

            for (int i = 0; i < imageActual.getHeight(); i++) {
                for (int j = 0; j < imageActual.getWidth(); j++) {
                    Color color1 = new Color(imageActual.getRGB(i, j));
                    Color color2 = new Color(imageOperando.getRGB(i, j));
                    int suma = color1.getRed() - color2.getRed();
                    if (suma > 255) {
                        imagenResultado.setRGB(i, j, new Color(255,255,255).getRGB());
                    } else {
                        imagenResultado.setRGB(i, j, new Color(suma,suma,suma).getRGB());
                    }
                }
            }
        }

        return imagenResultado;
    }

    public BufferedImage producto(BufferedImage imageActual, BufferedImage imageOperando) {
        for (int i = 0; i < imageActual.getHeight(); i++) {
            for (int j = 0; j < imageActual.getWidth(); j++) {
                Color c1 = new Color(imageActual.getRGB(i, j));
                Color c2 = new Color(imageOperando.getRGB(i, j));

                int mult = c1.getRed() * c2.getRed();
                if (mult > 255) {
                    imageActual.setRGB(i, j, new Color(255,255,255).getRGB());
                } else {
                    imageActual.setRGB(i, j, new Color(mult,mult,mult).getRGB());
                }
            }
        }
        return imageActual;
    }

    public BufferedImage producto(BufferedImage imageActual, int escalar) {

        for (int i = 0; i < imageActual.getWidth(); i++) {
            for (int j = 0; j < imageActual.getHeight(); j++) {
                Color c1 = new Color(imageActual.getRGB(i, j));

                int mult = c1.getRed() * escalar;
                if (mult > 255 || mult < 0) {
                    imageActual.setRGB(i, j, new Color(255,255,255).getRGB());
                } else {
                    imageActual.setRGB(i, j, new Color(mult, mult, mult).getRGB());
                }
            }
        }
        return imageActual;
    }

    public BufferedImage rangoDinamico(BufferedImage image) {
        int R;
        int max = 0, posX = 0, posY = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color c = new Color(image.getRGB(i, j));

                if (c.getRed() > max) {
                    max = c.getRed();
                    posX = i;
                    posY = j;
                }
            }
        }
        R = max;

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int T, r;
                Color c = new Color(image.getRGB(i, j));
                r = c.getRed();
                T = (int) ((255 / Math.log(1 + R)) * Math.log(1 + r));
                image.setRGB(i, j, new Color(T,T,T).getRGB());
            }
        }
        return image;
    }

    public BufferedImage powerLaw(BufferedImage image, int gamma) {

        int R = 0;
        int max = 0, posX = 0, posY = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color c = new Color(image.getRGB(i, j));

                if (c.getRed() > max) {
                    max = c.getRed();
                    posX = i;
                    posY = j;
                }
            }
        }
        R = max;

        double c = 255 / Math.exp(gamma * R);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int T, r;
                Color color = new Color(image.getRGB(i, j));
                r = color.getRed();
                T = (int) (c * Math.pow(r, gamma));
                
                image.setRGB(i, j, new Color(T,T,T).getRGB());
            }
        }
        return image;

    }
}


