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
<<<<<<< HEAD
    private BufferedImage screen = imageActual;
    private int nivelIntensidad;
    private Operaciones Op;
    
    public ProcImagenes(){
        this.Op = new Operaciones();
    }

=======
>>>>>>> origin/master
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
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG, GIF, BMP, JPG, PGM, BPM, jpeg", "jpeg", "jpg", "gif", "bmp", "pgm", "ppm", "raw");
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

    public BufferedImage umbralizarGrises(BufferedImage screen,int umbral) {
        BufferedImage copia = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        Color c;
        for (int i = 0; i < screen.getWidth(); i++) {
            for (int j = 0; j < screen.getHeight(); j++) {
                c = valorPixel(i, j);
                if (c.getRed() <= umbral) {
                    copia.setRGB(i, j, new Color(0, 0, 0).getRGB());
                } else {
                    copia.setRGB(i, j, new Color(255, 255, 255).getRGB());
                }
            }
        }
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
        int ancho = screen.getWidth();
        int alto = screen.getHeight();
        int L = 255;
        //histograma normalizado
        int histogramaNormal[] = new int[L+1];
        //calcula el nuevo histograma
        for(int i = 1; i < L+1; i++) {
            histogramaNormal[i] = histogramaNormal[i-1] + histogramaGris[i];
        }
        //crea vector LUT
        int LUT[] = new int[L+1];
        for(int i = 0; i < L; i++) {
            LUT[i] = (int)Math.floor(((L-1)*histogramaNormal[i])/(ancho*alto));
        }
       
        Color c;
        for(int i = 0; i < alto; i++) {
            for(int j = 0; j < ancho; j++) {
                c = valorPixel(i, j);
                int val = LUT[c.getRed()];
                copia.setRGB(i, j, new Color(val,val,val).getRGB());
            }
        }
        
        return copia;
    }
    
    public BufferedImage generarImagenSinteticaMultiplicativa(int ancho,int largo,int valor){
        BufferedImage res = new BufferedImage(ancho, largo, BufferedImage.TYPE_3BYTE_BGR);
        
        for (int i = 0; i < res.getHeight(); i++) {
            for (int j = 0; j < res.getWidth(); j++) {
                res.setRGB(i, j, new Color(valor,valor,valor).getRGB());
            }
        }
        
        return res;
    }

    public Color valorPixel(int x, int y) {
        Color res;
        res = new Color(this.imageActual.getRGB(x, y));
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
    
<<<<<<< HEAD
    public BufferedImage sumaConstante(int constante){
       
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        
        screenCopy = escalaGrises();
        screenCopy = Op.suma(screenCopy, constante);
        //normalizarImagenGris(imageActual);
        System.out.println("suma constante");
        return screenCopy;        
    }
    
    public BufferedImage sumaImagen (BufferedImage imageSegunda){
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();      
        screenCopy = Op.suma(screenCopy, imageSegunda);
       // normalizarImagenGris(screenCopy);
        return screenCopy;  
    }
    
     public BufferedImage restaConstante(int constante){
         BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.resta(screenCopy, constante);
       // normalizarImagenGris(screenCopy);
        return screenCopy;
        
    }
    
    public BufferedImage restaImagen (BufferedImage imageSegunda){
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();      
        screenCopy = Op.resta(screenCopy, imageSegunda);
       // normalizarImagenGris(screenCopy);
        return screenCopy;
        
    }
    
    public BufferedImage productoImagen (BufferedImage imageSegunda){
        BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.producto(screenCopy, imageSegunda);
       // normalizarImagenGris(screenCopy);
        return screenCopy;
    }
    
      public BufferedImage productoEscalar (int escalar){
          BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        escalaGrises();
        screenCopy = Op.producto(screenCopy, escalar);
        //normalizarImagenGris(screenCopy);
        return screenCopy;
    
      }
      
     public BufferedImage rangoDin(){
         BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
         escalaGrises();
         Op.rangoDinamico(screenCopy);
         return screenCopy;
     }
     
     
     public BufferedImage potenciaGamma(int gamma){
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
    

=======

    
    
>>>>>>> origin/master

}
