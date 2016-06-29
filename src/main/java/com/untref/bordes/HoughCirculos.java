/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.bordes;

import static com.untref.gui.Editar.matToBufferedImage;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author javi_
 */
public class HoughCirculos {

    public static BufferedImage implementarCiculos(BufferedImage screen,int acumulador,int radioMin,int radioMax) {
        Mat source = new Mat(screen.getHeight(), screen.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) screen.getRaster().getDataBuffer()).getData();
        source.put(0, 0, data);
        //ImageIO.write(screen, "jpg", "imagen");
        //Mat source = Highgui.imread("test.jpg", Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());

        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);

        Imgproc.GaussianBlur(destination, destination, new Size(3, 3), 0, 0);

        Mat circles = new Mat();
        Imgproc.HoughCircles(destination, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 30, 10, acumulador, radioMin, radioMax);

        int radius;
        org.opencv.core.Point pt;
        for (int x = 0; x < circles.cols(); x++) {
            double vCircle[] = circles.get(0, x);

            if (vCircle == null) {
                break;
            }

            pt = new org.opencv.core.Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
            radius = (int) Math.round(vCircle[2]);

            // draw the found circle
            Core.circle(source, pt, radius, new Scalar(150, 0, 0), 2);
            Core.circle(source, pt, 1, new Scalar(0, 0, 0), 2);
        }
        BufferedImage res = matToBufferedImage(source);

        return res;

    }

}
