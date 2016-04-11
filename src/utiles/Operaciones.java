/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.awt.image.BufferedImage;

/**
 *
 * @author karlagutz
 */
public class Operaciones {

   // private BufferedImage imageActual;
    private int nivelIntensidad;
    ProcImagenes p = new ProcImagenes();

    public BufferedImage suma(BufferedImage imageActual, int valor) {
        for (int i = 0; i < imageActual.getWidth(); i++) {
            for (int j = 0; j < imageActual.getHeight(); j++) {
                int suma = imageActual.getRGB(i, j) + valor;
                if (suma>255) {
                     imageActual.setRGB(i, j, 255);
                }else{
                 imageActual.setRGB(i, j, suma);
                }
               
            }
        } 
        return imageActual;
        
      //  p.normalizarImagenGris(imageActual);
       // repaint(screen, screenCopy);
       
    }
    
    public BufferedImage suma (BufferedImage imageActual, BufferedImage imageOperando){
               BufferedImage imagenResultado;
               imagenResultado = imageActual;
        if(imageActual.getHeight()==imageOperando.getHeight() && imageActual.getWidth()==imageOperando.getWidth()){

            for (int i = 0; i < imageActual.getHeight(); i++) {
               for (int j = 0; j < imageActual.getWidth(); j++) {
                   int suma = imageActual.getRGB(i, j)+imageOperando.getRGB(i, j);
                   if(suma > 255) {
                       imagenResultado.setRGB(i, j, 255);
                   } else {
                   imagenResultado.setRGB(i, j, suma);}
               }
           }
        }
        
        return imagenResultado;
    }

}
