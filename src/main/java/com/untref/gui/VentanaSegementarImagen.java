/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.gui;

import com.untref.dialogs.DialogsHelper;
import com.untref.enums.FormatoDeImagen;
import com.untref.enums.NivelMensaje;
import com.untref.listeners.MarcarFotogramaListener1Imagen;
import com.untref.modelo.Imagen;
import com.untref.procesadores.Segmentador;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author javi_
 */
public class VentanaSegementarImagen extends javax.swing.JInternalFrame {

    /**
     * Creates new form VentanaSegementarImagen
     */
    private JLabel labelPrincipal;
    private BufferedImage imagenActual;
    private Integer x1 = 0;
    private Integer x2 = 0;
    private Integer y1 = 0;
    private Integer y2 = 0;
    private BufferedImage original;

    public VentanaSegementarImagen(BufferedImage imagen) {
        initComponents();
        labelPrincipal = new JLabel("");
        labelPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        labelPrincipal.setBounds(0, 0, 400, 300);
        getContentPane().add(labelPrincipal);
        labelPrincipal.setIcon(new ImageIcon(imagen));
        jButton1.setEnabled(false);
        this.original = imagen;
        setImagenActual(copiarBufferedImage(imagen));
    }

    public BufferedImage getImagen() {
        return imagenActual;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        repeticiones = new javax.swing.JTextField();
        diferenciaDeColor = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        repeticiones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeticionesActionPerformed(evt);
            }
        });

        jLabel1.setText("Repeticiones: ");

        jLabel2.setText("Diferencia De Color. ");

        jButton1.setText("Calcular");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Seleccionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(410, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diferenciaDeColor))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(43, 43, 43)
                        .addComponent(repeticiones, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(repeticiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(diferenciaDeColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void repeticionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeticionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_repeticionesActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!repeticiones.getText().toString().isEmpty() && !diferenciaDeColor.getText().toString().isEmpty()) {

            try {
                String rep = repeticiones.getText().toString().trim();
                String diferenciaColor = diferenciaDeColor.getText().toString().trim();

                if (!rep.isEmpty() && !diferenciaColor.isEmpty()) {

                    Imagen i = new Imagen(this.original, FormatoDeImagen.JPG, "hola");
                    BufferedImage bufferSegmentado = Segmentador.segmentarImagenPrimeraVez(i,
                            new Point(getX1(), getY1()),
                            new Point(getX2(), getY2()), Integer.parseInt(rep), Integer.parseInt(diferenciaColor));
                    setImagenActual(bufferSegmentado);

                } else {

                    DialogsHelper.mostarMensaje(this, "Por favor complete los Paramentros necesarios", NivelMensaje.ERROR);
                }
            } catch (Exception ex) {

                DialogsHelper.mostarMensaje(this, "Por favor ingresa umbrales Validos", NivelMensaje.ERROR);
                ex.printStackTrace();
            }
        } else {

            DialogsHelper.mostarMensaje(this, "Por favor ingresa umbrales Validos", NivelMensaje.ERROR);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        setImagenActual(copiarBufferedImage(original));
        labelPrincipal.addMouseListener(new MarcarFotogramaListener1Imagen(this));
        jButton1.setEnabled(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private BufferedImage copiarBufferedImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public JLabel getPanelDeImagen() {

        return labelPrincipal;
    }

    public void setImagenActual(BufferedImage ima) {
        this.imagenActual = ima;
        labelPrincipal.setIcon(new ImageIcon(imagenActual));
    }

    public Integer getX1() {
        return x1;
    }

    public void setX1(Integer x1) {
        this.x1 = x1;
    }

    public Integer getX2() {
        return x2;
    }

    public void setX2(Integer x2) {
        this.x2 = x2;
    }

    public Integer getY1() {
        return y1;
    }

    public void setY1(Integer y1) {
        this.y1 = y1;
    }

    public Integer getY2() {
        return y2;
    }

    public void setY2(Integer y2) {
        this.y2 = y2;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField diferenciaDeColor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField repeticiones;
    // End of variables declaration//GEN-END:variables
}
