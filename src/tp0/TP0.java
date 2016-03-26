/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp0;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author javi_
 */
public class TP0 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        BufferedImage image = ImageIO.read(
         new URL("http://www.cosy.sbg.ac.at/~pmeerw/Watermarking/lena_color.gif"));

    int w = image.getWidth();
    int h = image.getHeight();

    int[] dataBuffInt = image.getRGB(0, 0, w, h, null, 0, w); 

    Color c = new Color(dataBuffInt[100]);

    System.out.println(c.getRed());   // = (dataBuffInt[100] >> 16) & 0xFF
    System.out.println(c.getGreen()); // = (dataBuffInt[100] >> 8)  & 0xFF
    System.out.println(c.getBlue());  // = (dataBuffInt[100] >> 0)  & 0xFF
    System.out.println(c.getAlpha()); // = (dataBuffInt[100] >> 24) & 0xFF
    }
    
}
