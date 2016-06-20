package com.untref.procesadores;

import com.untref.enums.Canal;
import com.untref.enums.FormatoDeImagen;
import com.untref.modelo.Archivo;
import com.untref.modelo.Imagen;
import com.untref.utiles.MatricesManager;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;




public class ProcesadorDeImagenes {

    private static ProcesadorDeImagenes instancia;
    private Archivo archivoActual;
    private static Imagen imagenActual;
    private static Imagen imagenOriginal;
    private Integer x1;
    private Integer x2;
    private Integer y1;
    private Integer y2;

    private ProcesadorDeImagenes() {
    }

    public static ProcesadorDeImagenes obtenerInstancia() {

        if (instancia == null) {

            instancia = new ProcesadorDeImagenes();
        }

        return instancia;
    }

    /**
     * Abre una imagen de archivo y la convierte en buffered image.
     *
     * @return Imagen
     */
    public Imagen cargarUnaImagenDesdeArchivo() {

        Imagen imagenADevolver = null;

        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione una imagen");

        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
                "JPG & GIF & BMP & PNG", "jpg", "gif", "bmp", "png");
        selector.setFileFilter(filtroImagen);

        int flag = selector.showOpenDialog(null);

        if (flag == JFileChooser.APPROVE_OPTION) {
            try {

                archivoActual = new Archivo(selector.getSelectedFile());
                FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
                        .getFormato(archivoActual.getExtension());

                BufferedImage bufferedImage = leerUnaImagen();

                if (formatoDeLaImagen != FormatoDeImagen.DESCONOCIDO) {

                    Imagen imagen = new Imagen(bufferedImage,
                            formatoDeLaImagen, archivoActual.getNombre());

                    imagen.setMatriz(MatricesManager.calcularMatrizDeLaImagen(
                            bufferedImage, Canal.ROJO), Canal.ROJO);
                    imagen.setMatriz(MatricesManager.calcularMatrizDeLaImagen(
                            bufferedImage, Canal.VERDE), Canal.VERDE);
                    imagen.setMatriz(MatricesManager.calcularMatrizDeLaImagen(
                            bufferedImage, Canal.AZUL), Canal.AZUL);

                    imagenActual = new Imagen(imagen.getBufferedImage(),
                            imagen.getFormato(), imagen.getNombre(),
                            imagen.getMatriz(Canal.ROJO),
                            imagen.getMatriz(Canal.VERDE),
                            imagen.getMatriz(Canal.AZUL));
                    imagenOriginal = new Imagen(imagen.getBufferedImage(),
                            imagen.getFormato(), imagen.getNombre(),
                            imagen.getMatriz(Canal.ROJO),
                            imagen.getMatriz(Canal.VERDE),
                            imagen.getMatriz(Canal.AZUL));
                    imagenADevolver = new Imagen(imagen.getBufferedImage(),
                            imagen.getFormato(), imagen.getNombre(),
                            imagen.getMatriz(Canal.ROJO),
                            imagen.getMatriz(Canal.VERDE),
                            imagen.getMatriz(Canal.AZUL));
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return imagenADevolver;
    }

    /**
     * Abre una imagen en formato RAW de archivo, con las medidas definidas y la
     * convierte en buffered image.
     *
     * @return Imagen
     */
    public Imagen cargarUnaImagenRawDesdeArchivo(Integer alto, Integer ancho) {

        Imagen imagenADevolver = null;

        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione una imagen RAW");

        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
                "RAW", "raw");
        selector.setFileFilter(filtroImagen);

        int flag = selector.showOpenDialog(null);

        if (flag == JFileChooser.APPROVE_OPTION) {
            try {

                archivoActual = new Archivo(selector.getSelectedFile());
                FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
                        .getFormato(archivoActual.getExtension());

                BufferedImage bufferedImage;
                bufferedImage = leerUnaImagenRAW(archivoActual, alto, ancho);

                Imagen imagen = new Imagen(bufferedImage, formatoDeLaImagen,
                        archivoActual.getNombre());

                int[][] matrizCanal = MatricesManager.calcularMatrizDeLaImagen(
                        bufferedImage, Canal.ROJO);

                imagen.setMatriz(matrizCanal, Canal.ROJO);
                imagen.setMatriz(matrizCanal, Canal.VERDE);
                imagen.setMatriz(matrizCanal, Canal.AZUL);

                imagenActual = new Imagen(imagen.getBufferedImage(),
                        imagen.getFormato(), imagen.getNombre(),
                        imagen.getMatriz(Canal.ROJO),
                        imagen.getMatriz(Canal.VERDE),
                        imagen.getMatriz(Canal.AZUL));
                imagenOriginal = new Imagen(imagen.getBufferedImage(),
                        imagen.getFormato(), imagen.getNombre(),
                        imagen.getMatriz(Canal.ROJO),
                        imagen.getMatriz(Canal.VERDE),
                        imagen.getMatriz(Canal.AZUL));
                imagenADevolver = new Imagen(imagen.getBufferedImage(),
                        imagen.getFormato(), imagen.getNombre(),
                        imagen.getMatriz(Canal.ROJO),
                        imagen.getMatriz(Canal.VERDE),
                        imagen.getMatriz(Canal.AZUL));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return imagenADevolver;
    }

    private BufferedImage leerUnaImagenRAW(Archivo archivoActual, int width, int height) {

        BufferedImage imagen = null;
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(archivoActual.getFile().toPath());
            imagen = new BufferedImage(width, height,
                    BufferedImage.TYPE_3BYTE_BGR);
            double[][] matrizDeImagen = new double[width][height];
            int contador = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    matrizDeImagen[j][i] = bytes[contador];

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

    private BufferedImage leerUnaImagen() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(archivoActual.getFile());
        return bufferedImage;
    }

    public Imagen getImagenActual() {
        return imagenActual;
    }

    public BufferedImage getBufferedImageDeMatriz(int[][] matriz, int ancho,
            int alto) {

        BufferedImage bufferedImage = new BufferedImage(ancho, alto,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                int pixel = matriz[i][j];
                bufferedImage.setRGB(i, j, pixel);
            }
        }

        return bufferedImage;
    }

   

    public int[] calcularValoresPromedio(BufferedImage bufferedImage,
            int ancho, int alto) {

        int acumuladorRojo = 0;
        int acumuladorVerde = 0;
        int acumuladorAzul = 0;
        int promedioRojo = 0;
        int promedioVerde = 0;
        int promedioAzul = 0;
        int cantidadPixeles = ancho * alto;
        int[] valoresPromedio = new int[3];
        Color color;

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                color = new Color(bufferedImage.getRGB(i, j));
                acumuladorRojo += color.getRed();
                acumuladorVerde += color.getGreen();
                acumuladorAzul += color.getBlue();
            }
        }

        promedioRojo = (acumuladorRojo / cantidadPixeles);
        promedioVerde = (acumuladorVerde / cantidadPixeles);
        promedioAzul = (acumuladorAzul / cantidadPixeles);
        valoresPromedio[0] = promedioRojo;
        valoresPromedio[1] = promedioVerde;
        valoresPromedio[2] = promedioAzul;

        return valoresPromedio;
    }

    public Imagen aplicarNegativo(Imagen imagen) {

        Imagen imagenEnNegativo = null;

        if (imagen != null) {

            imagenOriginal = imagen;
            BufferedImage resultado = imagen.getBufferedImage();

            for (int x = 0; x < resultado.getWidth(); x++) {
                for (int y = 0; y < resultado.getHeight(); y++) {

                    int rgba = resultado.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(255 - col.getRed(), 255 - col.getGreen(),
                            255 - col.getBlue());
                    resultado.setRGB(x, y, col.getRGB());
                }
            }

            imagenEnNegativo = new Imagen(resultado, imagen.getFormato(),
                    imagen.getNombre());
        }

        return imagenEnNegativo;
    }

    public void setImagenActual(Imagen imagen) {

        imagenActual = imagen;
    }

    public Archivo getArchivoActual() {

        return this.archivoActual;
    }

    public Imagen getImagenOriginal() {

        return imagenOriginal;
    }

    public void setImagenOriginal(Imagen imagen) {
        imagenOriginal = imagen;
    }



   

   
    public BufferedImage aplicarTransformacionLogaritmica(
            BufferedImage bufferedImage) {

        float rojoMax;
        float verdeMax;
        float azulMax;

        BufferedImage imagenTransformada;
        int nrows = bufferedImage.getWidth();
        int ncols = bufferedImage.getHeight();
        imagenTransformada = new BufferedImage(nrows, ncols,
                BufferedImage.TYPE_3BYTE_BGR);

        rojoMax = 255;
        verdeMax = 255;
        azulMax = 255;

        for (int f = 0; f < nrows; f++) {
            for (int g = 0; g < ncols; g++) {

                Color colorActual = new Color(bufferedImage.getRGB(f, g));
                int rojoActual = colorActual.getRed();
                int verdeActual = colorActual.getGreen();
                int azulActual = colorActual.getBlue();

                if (rojoMax < rojoActual) {
                    rojoMax = rojoActual;
                }

                if (verdeMax < verdeActual) {
                    verdeMax = verdeActual;
                }

                if (azulMax < azulActual) {
                    azulMax = azulActual;
                }

            }

        }

        float[] maximosYMinimos = new float[6];
        maximosYMinimos[0] = rojoMax;
        maximosYMinimos[1] = verdeMax;
        maximosYMinimos[2] = azulMax;

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {

                Color colorActual = new Color(bufferedImage.getRGB(i, j));
                int rojoActual = colorActual.getRed();
                int verdeActual = colorActual.getGreen();
                int azulActual = colorActual.getBlue();

                int rojoTransformado = (int) ((255f / (Math.log(rojoMax))) * Math
                        .log(1 + rojoActual));
                int verdeTransformado = (int) ((255f / (Math.log(verdeMax))) * Math
                        .log(1 + verdeActual));
                int azulTransformado = (int) ((255f / (Math.log(azulMax))) * Math
                        .log(1 + azulActual));

                Color colorModificado = new Color(rojoTransformado,
                        verdeTransformado, azulTransformado);
                imagenTransformada.setRGB(i, j, colorModificado.getRGB());
            }
        }

        return imagenTransformada;
    }

    public BufferedImage aplicarTransformacionLineal(BufferedImage bufferedImage) {

        float rojoMin;
        float rojoMax;
        float verdeMin;
        float verdeMax;
        float azulMin;
        float azulMax;

        BufferedImage imagenTransformada;
        int nrows = bufferedImage.getWidth();
        int ncols = bufferedImage.getHeight();
        imagenTransformada = new BufferedImage(nrows, ncols,
                BufferedImage.TYPE_3BYTE_BGR);

        // Color color = new Color(bufferedImage.getRGB(0, 0));
        rojoMin = 0;
        rojoMax = 255;
        verdeMin = 0;
        verdeMax = 255;
        azulMin = 0;
        azulMax = 255;

        for (int f = 0; f < nrows; f++) {
            for (int g = 0; g < ncols; g++) {

                Color colorActual = new Color(bufferedImage.getRGB(f, g));
                int rojoActual = colorActual.getRed();
                int verdeActual = colorActual.getGreen();
                int azulActual = colorActual.getBlue();

                if (rojoMin > rojoActual) {
                    rojoMin = rojoActual;
                }

                if (rojoMax < rojoActual) {
                    rojoMax = rojoActual;
                }

                if (verdeMin > verdeActual) {
                    verdeMin = verdeActual;
                }

                if (verdeMax < verdeActual) {
                    verdeMax = verdeActual;
                }

                if (azulMin > azulActual) {
                    azulMin = azulActual;
                }

                if (azulMax < azulActual) {
                    azulMax = azulActual;
                }

            }

        }

        float[] maximosYMinimos = new float[6];
        maximosYMinimos[0] = rojoMin;
        maximosYMinimos[1] = rojoMax;
        maximosYMinimos[2] = verdeMin;
        maximosYMinimos[3] = verdeMax;
        maximosYMinimos[4] = azulMin;
        maximosYMinimos[5] = azulMax;

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {

                Color colorActual = new Color(bufferedImage.getRGB(i, j));
                int rojoActual = colorActual.getRed();
                int verdeActual = colorActual.getGreen();
                int azulActual = colorActual.getBlue();

                int rojoTransformado = (int) ((((255f) / (rojoMax - rojoMin)) * rojoActual) - ((rojoMin * 255f) / (rojoMax - rojoMin)));
                int verdeTransformado = (int) (((255f / (verdeMax - verdeMin)) * verdeActual) - ((verdeMin * 255f) / (verdeMax - verdeMin)));
                int azulTransformado = (int) (((255f / (azulMax - azulMin)) * azulActual) - ((azulMin * 255f) / (azulMax - azulMin)));

                Color colorModificado = new Color(rojoTransformado,
                        verdeTransformado, azulTransformado);
                imagenTransformada.setRGB(i, j, colorModificado.getRGB());
            }
        }

        return imagenTransformada;
    }

   

    public Integer[] getXEY() {
        Integer[] valoresXEY = new Integer[4];
        valoresXEY[0] = x1;
        valoresXEY[1] = x2;
        valoresXEY[2] = y1;
        valoresXEY[3] = y2;

        return valoresXEY;
    }

    public Integer getX1() {
        return x1;
    }

    public Integer getX2() {
        return x2;
    }

    public Integer getY1() {
        return y1;
    }

    public Integer getY2() {
        return y2;
    }

    public BufferedImage clonarBufferedImage(BufferedImage buffered) {

        ColorModel cm = buffered.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = buffered.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public File obtenerFileDesdeArchivo() {

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
}
