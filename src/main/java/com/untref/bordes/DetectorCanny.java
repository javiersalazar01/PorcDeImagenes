/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.bordes;

import ij.ImagePlus;
import java.awt.image.BufferedImage;
import com.untref.utiles.Convolucion;

/**
 *
 * @author karlagutz
 */
public class DetectorCanny {
    
    public static ImagePlus cannny(ImagePlus imagen, int umbral_t1, int umbral_t2) {

		int umbral = 128;

		/*
		 * Sobel
		 */
		int[][] horizontal = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
		int[][] vertical = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

		ImagePlus resultadoHorizontal;
		ImagePlus resultadoVertical;
		ImagePlus resultado = new ImagePlus();
		resultado.setImage(new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
		resultado.setTitle(imagen.getTitle() + " - Canny");

		resultadoHorizontal = convolucion(imagen, horizontal);
		resultadoVertical = convolucion(imagen, vertical);

		ImagePlus magnitud = magnitudDelGradiente(resultadoHorizontal, resultadoVertical, umbral);

		/*
		 * Ángulo del gradiente
		 */
		boolean[][] esBorde = new boolean[imagen.getWidth()][imagen.getHeight()];

		for (int i = 0; i < imagen.getWidth(); i++) {
			for (int j = 0; j < imagen.getHeight(); j++) {

				double Gx = resultadoHorizontal.getPixel(i, j)[0];
				double Gy = resultadoVertical.getPixel(i, j)[0];

				double phi;
				if (Gx == 0) {
					phi = 0;
					// (Para evitar división por cero)
				} else {
					phi = Math.toDegrees(Math.atan(Gy / Gx));
				}

				if (phi > 22.5 && phi <= 157.5) {

					double pixel = magnitud.getPixel(i, j)[0];

					if (phi > 22.5 && phi <= 67.5) {

						double a = magnitud.getPixel(i + 1, j - 1)[0];
						double b = magnitud.getPixel(i - 1, j + 1)[0];

						if (a > pixel || b > pixel) {
							esBorde[i][j] = false;
						} else {
							esBorde[i][j] = true;
						}
					}
					if (phi > 67.5 && phi <= 112.5) {

						double a = magnitud.getPixel(i, j - 1)[0];
						double b = magnitud.getPixel(i, j + 1)[0];

						if (a > pixel || b > pixel) {
							esBorde[i][j] = false;
						} else {
							esBorde[i][j] = true;
						}
					}
					if (phi > 112.5 && phi <= 157.5) {

						double a = magnitud.getPixel(i - 1, j - 1)[0];
						double b = magnitud.getPixel(i + 1, j + 1)[0];

						if (a > pixel || b > pixel) {
							esBorde[i][j] = false;
						} else {
							esBorde[i][j] = true;
						}
					}
				}
			}
		}

		/*
		 * Analizo por borde y umbral.
		 */
		for (int i = 0; i < imagen.getWidth(); i++) {
			for (int j = 0; j < imagen.getHeight(); j++) {

				if (esBorde[i][j] && magnitud.getPixel(i, j)[0] > umbral_t2) {
					resultado.getProcessor().putPixel(i, j, 255);
				}

				// if (esBorde[i][j] && magnitud.getPixel(i, j)[0] > umbral_t1
				// && conexo(esBorde, i, j)) {
				if (magnitud.getPixel(i, j)[0] > umbral_t1 && conexo(esBorde, i, j)) {
					resultado.getProcessor().putPixel(i, j, 255);
				}
			}
		}

		return resultado;
	}

	private static boolean conexo(boolean[][] esBorde, int i, int j) {

		if (j > 0 && esBorde[i][j - 1]) {
			return true;
		}
		if (j < esBorde[i].length - 1 && esBorde[i][j + 1]) {
			return true;
		}
		if (i < esBorde.length - 1 && esBorde[i + 1][j]) {
			return true;
		}
		if (i > 0 && esBorde[i - 1][j]) {
			return true;
		}

		if (i > 0 && j > 0 && esBorde[i - 1][j - 1]) {
			return true;
		}
		if (i > 0 && j < esBorde[i].length - 1 && esBorde[i - 1][j + 1]) {
			return true;
		}
		if (j > 0 && i < esBorde.length - 1 && esBorde[i + 1][j - 1]) {
			return true;
		}
		if (i < esBorde.length - 1 && j < esBorde[i].length - 1 && esBorde[i + 1][j + 1]) {
			return true;
		}

		return false;
	}
        
        public static ImagePlus convolucion(ImagePlus imagen, int[][] mascara) {

		if (imagen.getType() == 0) {
			return convolucionGRAY(imagen, mascara);
		} else {
			return convolucionRGB(imagen, mascara);
		}
	}

	private static ImagePlus convolucionGRAY(ImagePlus imagen, int[][] mascara) {

		ImagePlus resultado = new ImagePlus();
		resultado.setImage(new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_GRAY));

		int mascaraSize = 1;

		for (int i = 0; i < imagen.getWidth(); i++) {
			for (int j = 0; j < imagen.getHeight(); j++) {

				int sumador = 0;
				int contador_i2 = 0;

				for (int i2 = i - mascaraSize; i2 <= i + mascaraSize; i2++) {

					int contador_j2 = 0;

					for (int j2 = j - mascaraSize; j2 <= j + mascaraSize; j2++) {

						if (i2 > 0 && j2 > 0 && i2 < imagen.getWidth() && j2 < imagen.getHeight()) {

							sumador = sumador + imagen.getPixel(i2, j2)[0] * mascara[contador_i2][contador_j2];
						}
						contador_j2++;
					}
					contador_i2++;
				}

				resultado.getProcessor().putPixel(i, j, sumador);
			}
		}

		return resultado;
	}

	private static ImagePlus convolucionRGB(ImagePlus imagen, int[][] mascara) {

		ImagePlus resultado = new ImagePlus();
		resultado.setImage(new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB));

		int mascaraSize = 1;

		for (int i = 0; i < imagen.getWidth(); i++) {
			for (int j = 0; j < imagen.getHeight(); j++) {

				int sumadorR = 0;
				int sumadorG = 0;
				int sumadorB = 0;
				int contador_i2 = 0;

				for (int i2 = i - mascaraSize; i2 <= i + mascaraSize; i2++) {

					int contador_j2 = 0;

					for (int j2 = j - mascaraSize; j2 <= j + mascaraSize; j2++) {

						if (i2 > 0 && j2 > 0 && i2 < imagen.getWidth() && j2 < imagen.getHeight()) {

							sumadorR = sumadorR + imagen.getPixel(i2, j2)[0] * mascara[contador_i2][contador_j2];
							sumadorG = sumadorG + imagen.getPixel(i2, j2)[1] * mascara[contador_i2][contador_j2];
							sumadorB = sumadorB + imagen.getPixel(i2, j2)[2] * mascara[contador_i2][contador_j2];
						}
						contador_j2++;
					}
					contador_i2++;
				}

				int[] nuevoPixel = { sumadorR, sumadorG, sumadorB };
				resultado.getProcessor().putPixel(i, j, nuevoPixel);
			}
		}

		return resultado;
	}
        public static ImagePlus magnitudDelGradiente(ImagePlus imagen, ImagePlus otraImagen, int umbral) {

		ImagePlus resultado = new ImagePlus();
		resultado.setImage(new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_GRAY));

		for (int i = 0; i < imagen.getWidth(); i++) {
			for (int j = 0; j < imagen.getHeight(); j++) {

				int pixelA = imagen.getPixel(i, j)[0];
				int pixelB = otraImagen.getPixel(i, j)[0];

				double valor = Math.sqrt(Math.pow(pixelA, 2) + Math.pow(pixelB, 2));

				if (valor > umbral) {
					resultado.getProcessor().putPixel(i, j, 255);
				} else {
					resultado.getProcessor().putPixel(i, j, 0);
				}
			}
		}

		return resultado;
	}
}
