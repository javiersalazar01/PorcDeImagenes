/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp0;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author javi_
 */
public class TP0 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {

        File file = new File("BARCO.RAW");
        BufferedImage image = ImageIO.read(file);
        ImageIO.write(image, "jpg", new File("barc.jpg"));

    }

}
