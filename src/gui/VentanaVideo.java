package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import dialogs.DialogsHelper;
import enums.FormatoDeImagen;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.swing.JInternalFrame;
import listeners.MarcarFotogramaListener;
import modelo.Fotogramas;
import modelo.Imagen;
import procesadores.ProcesadorDeVideo;
import procesadores.Segmentador;
import utiles.Umbralizador;

@SuppressWarnings("serial")
public class VentanaVideo extends JInternalFrame {

    private JPanel panelBotones;
    private JLabel labelPrincipal;
    private JButton botonSegmentar;
    private JButton botonSiguienteFotograma;
    private JButton botonFotogramaAnterior;
    private JButton botonPrincipio;
    private JButton botonPlay;
    private JLabel lblNewLabel;
    private JTextArea consola;

    public VentanaVideo() {
        this.setTitle("Segmentacion De Contornos Video");

        this.setResizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        setBounds(100, 100, 800, 600);
        getContentPane().setLayout(null);

        consola = new JTextArea();
        consola.setEditable(false);
        consola.setLineWrap(true);
        consola.setBounds(539, 35, 235, 439);
        getContentPane().add(consola);

        lblNewLabel = new JLabel("Informacion");
        lblNewLabel.setBackground(Color.GRAY);
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(539, 0, 235, 36);
        getContentPane().add(lblNewLabel);

        labelPrincipal = new JLabel("");
        labelPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        labelPrincipal.setBounds(0, 0, 540, 474);
        getContentPane().add(labelPrincipal);

        panelBotones = new JPanel();
        panelBotones.setBounds(0, 485, 774, 43);
        getContentPane().add(panelBotones);

        JPanel panel1 = new JPanel();
        panelBotones.add(panel1);

        botonSegmentar = new JButton("Segmentar");
        botonSegmentar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                BufferedImage bufferSegmentado = segmentarImagen();
                VentanaVideo.this.refrescarImagen(bufferSegmentado);
                habilitarBotonesNavegacion();
                botonSegmentar.setEnabled(false);
            }

        });
        botonSegmentar.setHorizontalAlignment(SwingConstants.LEFT);
        botonSegmentar.setEnabled(false);
        panel1.add(botonSegmentar);

        JButton botonSeleccionar = new JButton("Seleccionar");
        botonSeleccionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                DialogsHelper.mostarMensaje(getContentPane(), "Cliquea en la esquina superior izquierda y la inferior derecha que formar�n el cuadrado para marcar una regi�n en la imagen");
                labelPrincipal.addMouseListener(new MarcarFotogramaListener(VentanaVideo.this));
                botonSegmentar.setEnabled(true);
            }
        });
        panel1.add(botonSeleccionar);

        botonFotogramaAnterior = new JButton("Anterior");
        botonFotogramaAnterior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                botonSiguienteFotograma.setEnabled(true);
                botonPlay.setEnabled(true);

                if (ProcesadorDeVideo.obtenerInstancia().retrocederUnFotograma()) {

                    BufferedImage bufferSegmentado = volverASegmentarImagen();
                    refrescarImagen(bufferSegmentado);
                    botonPrincipio.setEnabled(true);
                    botonFotogramaAnterior.setEnabled(true);
                } else {

                    DialogsHelper.mostarMensaje(getContentPane(), "El video comienza aqu�");
                    botonPrincipio.setEnabled(false);
                    botonFotogramaAnterior.setEnabled(false);
                };

            }
        });

        botonPrincipio = new JButton("Principio");
        botonPrincipio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ProcesadorDeVideo.obtenerInstancia().reiniciar();
                consola.setText("");

                BufferedImage bufferSegmentado = segmentarImagen();
                refrescarImagen(bufferSegmentado);
                botonPrincipio.setEnabled(false);
                botonFotogramaAnterior.setEnabled(false);
                botonSiguienteFotograma.setEnabled(true);
                botonPlay.setEnabled(true);

            }
        });
        botonPrincipio.setEnabled(false);
        panel1.add(botonPrincipio);
        botonFotogramaAnterior.setEnabled(false);
        panel1.add(botonFotogramaAnterior);

        botonSiguienteFotograma = new JButton("Siguiente");
        botonSiguienteFotograma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                botonPrincipio.setEnabled(true);
                botonFotogramaAnterior.setEnabled(true);

                if (ProcesadorDeVideo.obtenerInstancia().avanzarUnFotograma()) {

                    BufferedImage bufferSegmentado = volverASegmentarImagen();
                    refrescarImagen(bufferSegmentado);
                    botonSiguienteFotograma.setEnabled(true);
                    botonPlay.setEnabled(true);
                } else {

                    DialogsHelper.mostarMensaje(getContentPane(), "Fin del video");
                    botonSiguienteFotograma.setEnabled(false);
                    botonPlay.setEnabled(false);
                };

            }
        });
        botonSiguienteFotograma.setEnabled(false);
        panel1.add(botonSiguienteFotograma);

        botonPlay = new JButton("Play");
        botonPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {

                        while (ProcesadorDeVideo.obtenerInstancia().avanzarUnFotograma()) {

                            BufferedImage bufferSegmentado = volverASegmentarImagen();
                            refrescarImagen(bufferSegmentado);
                        }
                        return null;
                    }
                };
                mySwingWorker.execute();

                botonPrincipio.setEnabled(true);
                botonFotogramaAnterior.setEnabled(true);
                botonSiguienteFotograma.setEnabled(false);
                botonPlay.setEnabled(false);
            }
        });
        botonPlay.setEnabled(false);
        panel1.add(botonPlay);

        JButton botonOtsuColor = new JButton("OtsuColor");
        botonOtsuColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {

                        while (ProcesadorDeVideo.obtenerInstancia().avanzarUnFotograma()) {

                            Imagen imagenUmbralizada = Umbralizador.generarUmbralizacionColor(ProcesadorDeVideo.obtenerInstancia().getImagenActual());
                            refrescarImagen(imagenUmbralizada.getBufferedImage());
                        }
                        return null;
                    }
                };
                mySwingWorker.execute();
            }
        });
        panel1.add(botonOtsuColor);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnNuevoVideo = new JMenu("Cargar Video");
        menuBar.add(mnNuevoVideo);

        JMenuItem mntmHoja = new JMenuItem("Abuela");
        mntmHoja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ProcesadorDeVideo.obtenerInstancia().cargarVideo(Fotogramas.ABUELA);
                refrescarImagen(ProcesadorDeVideo.obtenerInstancia().getImagenActual().getBufferedImage());
            }
        });
        mnNuevoVideo.add(mntmHoja);
    }

    private BufferedImage segmentarImagen() {

        ProcesadorDeVideo procesador = ProcesadorDeVideo.obtenerInstancia();
        //BufferedImage imagenNueva = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(procesador.getImagenActual().getBufferedImage());
        
        BufferedImage ima = procesador.getImagenActual().getBufferedImage();
        ColorModel cm = ima.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = ima.copyData(null);
        BufferedImage imagenNueva = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

        Imagen image = new Imagen(imagenNueva, FormatoDeImagen.JPEG, "segmentada");

        long tiempoDeInicio = new Date().getTime();

        BufferedImage bufferSegmentado = Segmentador.segmentarImagenPrimeraVez(image,
                new Point(procesador.getX1(), procesador.getY1()),
                new Point(procesador.getX2(), procesador.getY2()), 100, 50);

        long tiempoDeFin = new Date().getTime();
        String tiempo = "Tiempo: " + procesador.getPosicionActual() + ": " + (tiempoDeFin - tiempoDeInicio) + " milisegundos";
        System.out.println(tiempo);
        consola.setText(tiempo + "\n" + consola.getText().toString());

        return bufferSegmentado;
    }

    private BufferedImage volverASegmentarImagen() {

        ProcesadorDeVideo procesador = ProcesadorDeVideo.obtenerInstancia();
        
        //BufferedImage imagenNueva = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(procesador.getImagenActual().getBufferedImage());
        BufferedImage ima = procesador.getImagenActual().getBufferedImage();
        ColorModel cm = ima.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = ima.copyData(null);
        BufferedImage imagenNueva = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        
        Imagen image = new Imagen(imagenNueva, FormatoDeImagen.JPEG, "segmentada");

        long tiempoDeInicio = new Date().getTime();

        BufferedImage bufferSegmentado = Segmentador.volverASegmentar(image, 100, 50);

        long tiempoDeFin = new Date().getTime();
        String tiempo = "Tiempo de procesamiento del fotograma " + procesador.getPosicionActual() + ": " + (tiempoDeFin - tiempoDeInicio) + " milisegundos";
        System.out.println(tiempo);
        consola.setText(tiempo + "\n" + consola.getText().toString());

        return bufferSegmentado;
    }

    public void refrescarImagen(BufferedImage imagen) {

        labelPrincipal.setIcon(new ImageIcon(imagen));
    }

    public JLabel getPanelDeImagen() {

        return labelPrincipal;
    }

    public void habilitarBotonSegmentar() {

        botonSegmentar.setEnabled(true);
    }

    public void habilitarBotonesNavegacion() {

        botonSiguienteFotograma.setEnabled(true);
        botonPlay.setEnabled(true);
    }
}
