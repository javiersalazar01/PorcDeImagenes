package procesadores;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import enums.FormatoDeImagen;
import gui.VentanaSegementarImagen;
import gui.VentanaVideo;
import gui.VentanaVideoSegmentar;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import modelo.Fotogramas;
import modelo.Imagen;
import modelo.Video;



public class ProcesadorDeVideo {

    private static ProcesadorDeVideo instancia;
    private Video videoActual;
    private int posicionActual;
    private Imagen imagenActual;
    private Integer x1;
    private Integer x2;
    private Integer y1;
    private Integer y2;

    private ProcesadorDeVideo() {
        posicionActual = 0;
    }

    public static ProcesadorDeVideo obtenerInstancia() {

        if (instancia == null) {

            instancia = new ProcesadorDeVideo();
        }

        return instancia;
    }

    public void cargarVideo(Fotogramas video) {

        videoActual = new Video(video.toString(), Fotogramas.obtenerTodosLosFotogramas(video));
        posicionActual = 0;
    }

    public Video getVideoActual() {
        return videoActual;
    }

    public boolean avanzarUnFotograma() {

        boolean sePudoAvanzar = false;

        if (posicionActual != videoActual.getFotogramas().size() - 1) {

            posicionActual++;
            sePudoAvanzar = true;
        }

        return sePudoAvanzar;
    }

    public boolean retrocederUnFotograma() {

        boolean sePudoRetroceder = false;

        if (posicionActual != 0) {

            posicionActual--;
            sePudoRetroceder = true;
        }

        return sePudoRetroceder;
    }

    public void reiniciar() {

        this.posicionActual = 0;
    }

    public Imagen getImagenActual() {

        String fotogramaActual = VentanaVideo.class.getResource(getVideoActual().getFotogramaPorPosicion(posicionActual)).getPath();

        try {

            File img = new File(fotogramaActual);
            BufferedImage image = ImageIO.read(img);
            imagenActual = new Imagen(image, FormatoDeImagen.JPEG, "fotograma_" + posicionActual);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return imagenActual;
    }

    public int getPosicionActual() {

        return this.posicionActual;
    }

    public void marcarImagenActual(Integer x1Param, Integer y1Param, Integer x2Param,
            Integer y2Param, VentanaVideoSegmentar ventana) {

        this.x1 = x1Param;
        this.x2 = x2Param;
        this.y1 = y1Param;
        this.y2 = y2Param;

        //BufferedImage imagen = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(getImagenActual().getBufferedImage());
        BufferedImage imea = imagenActual.getBufferedImage();
        ColorModel cm = imea.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = imea.copyData(null);
        BufferedImage imagen = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        
        Imagen image = new Imagen(imagen, FormatoDeImagen.JPEG, "original");

        if (image != null) {

            List<Point> pixeles = new LinkedList<Point>();

            for (int i = x1Param; i <= x2Param; i++) {

                pixeles.add(new Point(i, y1Param));
                pixeles.add(new Point(i, y2Param));
            }

            for (int j = y1Param; j <= y2Param; j++) {

                pixeles.add(new Point(x1Param, j));
                pixeles.add(new Point(x2Param, j));
            }

            for (Point pixel : pixeles) {

                image.getBufferedImage().setRGB(pixel.x, pixel.y, Color.RED.getRGB());
            }

            ventana.refrescarImagen(image.getBufferedImage());
        }
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
}
