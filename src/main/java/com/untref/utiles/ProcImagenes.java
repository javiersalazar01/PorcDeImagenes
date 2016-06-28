package com.untref.utiles;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Javier Salazar, Karla Gutierrez
 */
public class ProcImagenes {

    //Imagen actual que se ha cargado
    private BufferedImage imageActual;
    private BufferedImage screen = imageActual;
    private int nivelIntensidad;
    private Operaciones Op;
    private Filtros f = new Filtros();

    public ProcImagenes() {
        this.Op = new Operaciones();
    }

    //Método que devuelve una imagen abierta desde archivo
    //Retorna un objeto BufferedImagen
    public BufferedImage abrirImagen(Component frame) {
        //Creamos la variable que será devuelta (la creamos como null)
        BufferedImage bmp = null;

        //Creamos un nuevo cuadro de diálogo para seleccionar imagen
        JFileChooser selector = new JFileChooser();
        //Le damos un título
        selector.setDialogTitle("Seleccione una imagen");
        //Filtramos los tipos de archivos
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG, GIF, BMP, JPG, PGM, BPM, JPEG, RAW", "jpeg", "jpg", "gif", "bmp", "pgm", "ppm", "raw");
        selector.setFileFilter(filtroImagen);
        //Abrimos el cuadro de diálog
        int flag = selector.showOpenDialog(null);
        //Comprobamos que pulse en aceptar
        if (flag == JFileChooser.APPROVE_OPTION) {
            try {
                //Devuelve el fichero seleccionado
                File fileImagenSeleccionada = selector.getSelectedFile();

                if (fileImagenSeleccionada.getName().split("\\.")[1].equalsIgnoreCase("RAW")) {
                    String alto = JOptionPane.showInputDialog(frame, "Alto:");
                    String ancho = JOptionPane.showInputDialog(frame, "Ancho:");

                    bmp = leerUnaImagenRAW(fileImagenSeleccionada, Integer.parseInt(ancho), Integer.parseInt(alto));
                } else {
                    bmp = ImageIO.read(fileImagenSeleccionada);
                }

                imageActual = bmp;

                //Asignamos a la variable bmp la imagen leida
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //Asignamos la imagen cargada a la propiedad imageActual
        //Retornamos el valorDeImagen.BMP
        return imageActual;
    }

    private BufferedImage leerUnaImagenRAW(File archivoActual, int width, int height) {

        BufferedImage imagen = null;
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(archivoActual.toPath());

            imagen = new BufferedImage(width, height,
                    BufferedImage.TYPE_3BYTE_BGR);
            int contador = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    int argb = 0;
                    argb += -16777216; // 255 alpha
                    int blue = ((int) bytes[contador] & 0xff);
                    int green = ((int) bytes[contador] & 0xff) << 8;
                    int red = ((int) bytes[contador] & 0xff) << 16;
                    int color = argb + red + green + blue;
                    imagen.setRGB(j, i, color);
                    contador++;
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return imagen;
    }

    public BufferedImage escalaGrises() {
        //Variables que almacenarán los píxeles
        int mediaPixel, colorSRGB;
        Color colorAux;

        //Recorremos la imagen píxel a píxel
        for (int i = 0; i < imageActual.getWidth(); i++) {
            for (int j = 0; j < imageActual.getHeight(); j++) {
                //Almacenamos el color del píxel
                colorAux = new Color(this.imageActual.getRGB(i, j));
                //Calculamos la media de los tres canales (rojo, verde, azul)
                mediaPixel = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
                //Cambiamos a formato sRGB
                colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
                //Asignamos el nuevo valor al BufferedImage
                imageActual.setRGB(i, j, colorSRGB);
            }
        }
        //Retornamos la imagen
        return imageActual;
    }

    public BufferedImage escalaGrises(BufferedImage image) {
        //Variables que almacenarán los píxeles
        int mediaPixel, colorSRGB;
        Color colorAux;

        //Recorremos la imagen píxel a píxel
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                //Almacenamos el color del píxel
                colorAux = new Color(image.getRGB(i, j));
                //Calculamos la media de los tres canales (rojo, verde, azul)
                mediaPixel = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
                //Cambiamos a formato sRGB
                colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
                //Asignamos el nuevo valor al BufferedImage
                image.setRGB(i, j, colorSRGB);
            }
        }
        //Retornamos la imagen
        return image;
    }

    public BufferedImage umbralizarGrises(BufferedImage screen, int umbral) {
        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        Color c;
        for (int i = 0; i < screen.getWidth(); i++) {
            for (int j = 0; j < screen.getHeight(); j++) {
                c = valorPixel(screen, i, j);
                if (c.getRed() <= umbral) {
                    copia.setRGB(i, j, new Color(0, 0, 0).getRGB());
                } else {
                    copia.setRGB(i, j, new Color(255, 255, 255).getRGB());
                }
            }
        }
        return copia;
    }

    public BufferedImage contraste(BufferedImage imageActual, int rango) {
        int nrows, ncols;
        BufferedImage copia;
        nrows = imageActual.getWidth();
        ncols = imageActual.getHeight();
        copia = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
        Color c;

        int r1 = (255 / 2) - (rango);
        int r2 = r1 + (2 * rango);
        int[][] matrizGris = new int[nrows][ncols];
        int max = 0, min = 9999;

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                c = valorPixel(imageActual, i, j);
                int g = c.getRed();

                if (g <= r1) {
                    //matrizGris[i][j] = (int)(g / 2);

                    copia.setRGB(i, j, new Color(g / 2, g / 2, g / 2).getRGB());
                } else if (g >= r2) {
                    g = (int) (g * 1.5);
                    //matrizGris[i][j] = (int) (g * 1.5);
                } else {
                    //matrizGris[i][j] = g;
                    copia.setRGB(i, j, new Color(g, g, g).getRGB());
                }

                if (g > max) {
                    max = g;
                } else if (g < min) {
                    min = g;
                }
            }
        }
        //copia = Op.normalizaRango(matrizGris, max, min);
        return copia;
    }

    public BufferedImage ecualizarGris(BufferedImage screen) {

        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());

        Histograma2 histograma = new Histograma2(screen);
        //histograma de la imagen
        int histogramaGris[] = histograma.getHistogramaGris();
        int alto = screen.getWidth();
        int ancho = screen.getHeight();
        int L = 255;
        //histograma normalizado
        int histogramaNormal[] = new int[L + 1];
        //calcula el nuevo histograma
        for (int i = 1; i < L + 1; i++) {
            histogramaNormal[i] = histogramaNormal[i - 1] + histogramaGris[i];
        }
        //crea vector LUT
        int LUT[] = new int[L + 1];
        for (int i = 0; i < L; i++) {
            LUT[i] = (int) Math.floor(((L - 1) * histogramaNormal[i]) / (ancho * alto));
        }

        Color c;
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                c = valorPixel(screen, i, j);
                int val = LUT[c.getRed()];
                copia.setRGB(i, j, new Color(val, val, val).getRGB());
            }
        }

        return copia;
    }

    public BufferedImage generarImagenSinteticaMultiplicativa(int ancho, int largo, int valor) {
        BufferedImage res = new BufferedImage(ancho, largo, BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < res.getHeight(); i++) {
            for (int j = 0; j < res.getWidth(); j++) {
                res.setRGB(i, j, new Color(valor, valor, valor).getRGB());
            }
        }
        return res;
    }

    public Color valorPixel(BufferedImage imagen, int x, int y) {
        Color res;
        res = new Color(imagen.getRGB(x, y));
        return res;
    }

    public BufferedImage cambiarPixel(int x, int y, int r, int g, int b) {
        Color c = new Color(r, g, b);
        imageActual.setRGB(x, y, c.getRGB());
        //Retornamos la imagen
        return imageActual;
    }

    public BufferedImage getImageActual() {
        return imageActual;
    }

    public BufferedImage sumaConstante(int constante) {

        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());

        screenCopy = escalaGrises();
        // screenCopy = Op.suma(screenCopy, constante);
        //normalizarImagenGris(imageActual);
        System.out.println("suma constante");
        return screenCopy;
    }

    public BufferedImage sumaImagen(BufferedImage imageSegunda) {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.suma(screenCopy, imageSegunda);
        // normalizarImagenGris(screenCopy);
        return screenCopy;
    }

    public BufferedImage restaConstante(int constante) {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.resta(screenCopy, constante);
        // normalizarImagenGris(screenCopy);
        return screenCopy;

    }

    public BufferedImage restaImagen(BufferedImage imageSegunda) {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.resta(screenCopy, imageSegunda);
        // normalizarImagenGris(screenCopy);
        return screenCopy;

    }

    public BufferedImage productoImagen(BufferedImage imageSegunda) {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.producto(screenCopy, imageSegunda);
        // normalizarImagenGris(screenCopy);
        return screenCopy;
    }

    public BufferedImage productoEscalar(int escalar) {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.producto(screenCopy, escalar);
        //normalizarImagenGris(screenCopy);
        return screenCopy;

    }

    public BufferedImage rangoDin() {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        Op.rangoDinamico(screenCopy);
        return screenCopy;
    }

    public BufferedImage potenciaGamma(int gamma) {
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        Op.powerLaw(screenCopy, gamma);
        normalizarImagenGris(screenCopy);

        return screenCopy;

    }

    public void normalizarImagenGris(BufferedImage imageSegunda) {
        // nivel intensidad
        if (nivelIntensidad > 255) {
            nivelIntensidad = 255;
        }
        if (nivelIntensidad < 0) {
            nivelIntensidad = 0;
        }
        // matriz
        for (int fila = 0; fila < imageSegunda.getHeight(); fila++) {
            for (int columna = 0; columna < imageSegunda.getWidth(); columna++) {

                int pixel = imageSegunda.getRGB(fila, columna);
                // pixeles
                if (pixel > nivelIntensidad) {
                    pixel = nivelIntensidad;
                }
                if (pixel < 0) {
                    pixel = 0;
                }
                imageSegunda.setRGB(fila, columna, pixel);
            }
        }
    }

    public BufferedImage filtroMedia(int size) {
        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        copia = f.media(copia, size);

        return copia;

    }

    public BufferedImage filtroMediana(int size) {
        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        copia = f.mediana(copia, size);

        return copia;

    }

    public BufferedImage filtroGaussiano(int size) {
        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        copia = f.gauss(copia, size);

        return copia;

    }

    public BufferedImage filtroBordes() {
        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        copia = f.bordes(copia);

        return copia;

    }

    public File abrirFile() {

        File fileADevolver = null;

        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione una imagen");

        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
                "JPG & GIF & BMP & PNG", "jpg", "gif", "bmp", "png");
        selector.setFileFilter(filtroImagen);

        int flag = selector.showOpenDialog(null);

        if (flag == JFileChooser.APPROVE_OPTION) {
            try {

                fileADevolver = selector.getSelectedFile();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return fileADevolver;
    }

    public void setImageActual(BufferedImage imageActual) {
        this.imageActual = imageActual;
    }

}
