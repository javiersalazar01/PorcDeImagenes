/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.bordes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 *
 * @author javi_
 */
public class TranformadaDeHoughRectas {
    
     public static BufferedImage aplicarTranformadaDeHough(BufferedImage image, 
            int thetaMin, int thetaMax, int thetaDis, int roMin, int roMax,int roDis,int umbral) {
        
        // create a hough transform object with the right dimensions 
        HoughTransform h = new HoughTransform(image.getWidth(), image.getHeight(),thetaMin,thetaMax,thetaDis);

        // add the points from the image (or call the addPoint method separately if your points are not in an image 
        h.addPoints(image);

        // get the lines out 
        Vector<HoughLine> lines = h.getLines(umbral);

        // draw the lines back onto the image 
        for (int j = 0; j < lines.size(); j++) {
            HoughLine line = lines.elementAt(j);
            line.draw(image, Color.RED.getRGB());
        }
       
       
        return null;
    }
}
