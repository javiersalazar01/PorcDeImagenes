package utiles;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Karla
 */
public class ProcImagenes {

    //Imagen actual que se ha cargado
    private BufferedImage imageActual;
    private int nivelIntensidad;

    //Método que devuelve una imagen abierta desde archivo
    //Retorna un objeto BufferedImagen
    public BufferedImage abrirImagen() {
        //Creamos la variable que será devuelta (la creamos como null)
        BufferedImage bmp = null;
        //Creamos un nuevo cuadro de diálogo para seleccionar imagen
        JFileChooser selector = new JFileChooser();
        //Le damos un título
        selector.setDialogTitle("Seleccione una imagen");
        //Filtramos los tipos de archivos
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG, GIF, BMP, JPG, PGM, BPM, jpeg", "jpeg","jpg", "gif", "bmp", "pgm", "ppm", "raw");
        selector.setFileFilter(filtroImagen);
        //Abrimos el cuadro de diálog
        int flag = selector.showOpenDialog(null);
        //Comprobamos que pulse en aceptar
        if (flag == JFileChooser.APPROVE_OPTION) {
            try {
                //Devuelve el fichero seleccionado
                File imagenSeleccionada = selector.getSelectedFile();
                //Asignamos a la variable bmp la imagen leida
                bmp = ImageIO.read(imagenSeleccionada);
            } catch (Exception e) {
            }

        }
        //Asignamos la imagen cargada a la propiedad imageActual
        imageActual = bmp;
        //Retornamos el valor
        return bmp;
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

    public BufferedImage escalaGrises(BufferedImage imgSegunda) {
        //Variables que almacenarán los píxeles
        int mediaPixel, colorSRGB;
        Color colorAux;

        //Recorremos la imagen píxel a píxel
        for (int i = 0; i < imgSegunda.getWidth(); i++) {
            for (int j = 0; j < imgSegunda.getHeight(); j++) {
                //Almacenamos el color del píxel
                colorAux = new Color(imgSegunda.getRGB(i, j));
                //Calculamos la media de los tres canales (rojo, verde, azul)
                mediaPixel = (int) ((colorAux.getRed() + colorAux.getGreen() + colorAux.getBlue()) / 3);
                //Cambiamos a formato sRGB
                colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
                //Asignamos el nuevo valor al BufferedImage
                imgSegunda.setRGB(i, j, colorSRGB);
            }
        }
        //Retornamos la imagen
        return imageActual;
    }
    public Color valorPixel(int x, int y) {
        Color res;
        res = new Color(this.imageActual.getRGB(x, y));
        return res;
    }

    public BufferedImage cambiarPixel(int x,int y,int r,int g, int b) {
        Color c = new Color(r, g, b);
        imageActual.setRGB(x, y, c.getRGB());
        //Retornamos la imagen
        return imageActual;
    }

    public BufferedImage getImageActual() {
        return imageActual;
    }
    
    public void normalizarImagenGris(BufferedImage imageSegunda) {
        // nivel intensidad
        if(nivelIntensidad > 255) {
            nivelIntensidad = 255;
        }
        if(nivelIntensidad < 0) {
            nivelIntensidad = 0;
        }
        // matriz
        for(int fila = 0; fila < imageSegunda.getHeight(); fila++) {
            for(int columna = 0; columna < imageSegunda.getWidth(); columna++) {
               
                int pixel = imageSegunda.getRGB(fila, columna);
                // pixeles
                if(pixel > nivelIntensidad) {
                    pixel = nivelIntensidad;
                }
                if(pixel < 0) {
                    pixel = 0;
                }
               imageSegunda.setRGB(fila, columna, pixel);
            }
        }
    }
    
    
    

}
