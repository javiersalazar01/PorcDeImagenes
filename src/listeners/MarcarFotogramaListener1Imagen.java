package listeners;

import enums.FormatoDeImagen;
import gui.VentanaSegementarImagen;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import gui.VentanaVideoSegmentar;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.LinkedList;
import java.util.List;
import modelo.Imagen;
import procesadores.ProcesadorDeVideo;

public class MarcarFotogramaListener1Imagen implements MouseListener {

    private VentanaSegementarImagen ventanaVideo;
    private Integer x1;
    private Integer y1;
    private Integer x2;
    private Integer y2;
    private Dimension bordeSuperiorIzquierdo;
    private Dimension bordeInferiorDerecho;

    public MarcarFotogramaListener1Imagen(VentanaSegementarImagen ventana) {
        this.ventanaVideo = ventana;
        bordeInferiorDerecho = calcularBordeInferiorDerecho(ventanaVideo.getPanelDeImagen(), ventanaVideo.getImagen());
        bordeSuperiorIzquierdo = calcularBordeSuperiorIzquierdo(ventanaVideo.getPanelDeImagen(), ventanaVideo.getImagen());
    }

    @Override
    public void mouseClicked(MouseEvent click) {

        if (x1 == null) {

            x1 = click.getX();
            y1 = click.getY();
        } else {

            x2 = click.getX();
            y2 = click.getY();

            int x1Final = (int) (x1 - bordeSuperiorIzquierdo.getWidth());
            int y1Final = (int) (y1 - bordeSuperiorIzquierdo.getHeight());

            int x2Final = (int) (x2 - bordeSuperiorIzquierdo.getWidth());
            int y2Final = (int) (y2 - bordeSuperiorIzquierdo.getHeight());

            marcarImagenActual(x1Final, y1Final, x2Final, y2Final, ventanaVideo);

            //ProcesadorDeVideo.obtenerInstancia().marcarImagenActual(x1Final, y1Final, x2Final, y2Final, ventanaVideo);
            ((Component) click.getSource()).removeMouseListener(this);
        }
    }

    public void marcarImagenActual(Integer x1Param, Integer y1Param, Integer x2Param,
            Integer y2Param, VentanaSegementarImagen ventana) {

        ventana.setX1(x1Param);
        ventana.setX2(x2Param);
        ventana.setY1(y1Param);
        ventana.setY2(y2Param);

        //BufferedImage imagen = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(getImagenActual().getBufferedImage());
        BufferedImage imea = ventana.getImagen();
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

        }
        ventana.setImagenActual(image.getBufferedImage());
    }

    public boolean validarClick(int x1, int y1) {

        return x1 >= bordeSuperiorIzquierdo.getWidth() && y1 >= bordeSuperiorIzquierdo.getHeight() && x1 <= bordeInferiorDerecho.getWidth() && y1 <= bordeInferiorDerecho.getHeight();
    }

    public Dimension calcularBordeSuperiorIzquierdo(JLabel panelPrincipal, BufferedImage imagenActual) {

        int centroImagenX = (imagenActual.getWidth()) / 2;
        int centroPanelPrincipalX = panelPrincipal.getWidth() / 2;

        int centroImagenY = (imagenActual.getHeight()) / 2;
        int centroPanelPrincipalY = panelPrincipal.getHeight() / 2;

        int xInicial = centroPanelPrincipalX - centroImagenX;
        int YInicial = centroPanelPrincipalY - centroImagenY;

        return new Dimension(xInicial, YInicial);
    }

    public Dimension calcularBordeInferiorDerecho(JLabel panelPrincipal, BufferedImage imagenActual) {

        int centroImagenX = (imagenActual.getWidth()) / 2;
        int centroPanelPrincipalX = panelPrincipal.getWidth() / 2;

        int centroImagenY = (imagenActual.getHeight()) / 2;
        int centroPanelPrincipalY = panelPrincipal.getHeight() / 2;

        int xInicial = centroPanelPrincipalX + centroImagenX;
        int YInicial = centroPanelPrincipalY + centroImagenY;

        return new Dimension(xInicial, YInicial);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {


    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}
