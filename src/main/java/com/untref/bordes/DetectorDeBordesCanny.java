package com.untref.bordes;

import java.awt.image.BufferedImage;
import java.util.Arrays;


public class DetectorDeBordesCanny {


    private final static float GAUSSIAN_CUT_OFF = 0.005f;
    private final static float MAGNITUD_ESCALA = 100F;
    private final static float MAGNITUD_LIMITE = 1000F;
    private final static int MAGNITUD_MAX = (int) (MAGNITUD_ESCALA * MAGNITUD_LIMITE);

	
    private int height;
    private int width;
    private int tamImagen;
    private int[] data;
    private int[] magnitud;
    private BufferedImage imagenOriginal;
    private BufferedImage imagenFinal;

    private float gaussRadio;
    private float umbralBajo;
    private float umbralAlto;
    private int gaussWidth;
    private boolean contrasteNormalizado;

    private float[] xConv;
    private float[] yConv;
    private float[] xGradiente;
    private float[] yGradiente;

	// constructor
 
    public DetectorDeBordesCanny(float umbral1, float umbral2) {
	//umbralBajo = 2.5f;
        //umbralAlto = 7.5f;
        umbralBajo = umbral1;
        umbralAlto = umbral2;
        gaussRadio = 1.5f;
        gaussWidth = 8;
        contrasteNormalizado = false;
    }


    public BufferedImage getImagenOriginal() {
        return imagenOriginal;
    }


    public void setImagenOriginal(BufferedImage image) {
        imagenOriginal = image;
    }


    public BufferedImage getImagenFinal() {
        return imagenFinal;
    }

    public void setImagenFinal(BufferedImage edgesImage) {
        this.imagenFinal = edgesImage;
    }

    public float getUmbralBajo() {
        return umbralBajo;
    }

    public void setUmbralBajo(float umbral) {
        if (umbral < 0) {
            throw new IllegalArgumentException();
        }
        umbralBajo = umbral;
    }


    public float getUmbralAlto() {
        return umbralAlto;
    }

    public void setUmbralAlto(float umbral) {
        if (umbral < 0) {
            throw new IllegalArgumentException();
        }
        umbralAlto = umbral;
    }
public int getGaussWidth() {
        return gaussWidth;
    }

    public void setGaussWidth(int gaussWidth) {
        if (gaussWidth < 2) {
            throw new IllegalArgumentException();
        }
        this.gaussWidth = gaussWidth;
    }


    public float getGaussRadio() {
        return gaussRadio;
    }


    public void setGaussRadio(float gaussRadio) {
        if (gaussRadio < 0.1f) {
            throw new IllegalArgumentException();
        }
        this.gaussRadio = gaussRadio;
    }

    public boolean esContrasteNormalizado() {
        return contrasteNormalizado;
    }

    public void setContrasteNormalizado(boolean contrasteNormalizado) {
        this.contrasteNormalizado = contrasteNormalizado;
    }

	// metodos
    public void procesar() {
        width = imagenOriginal.getWidth();
        height = imagenOriginal.getHeight();
        tamImagen = width * height;
        iniciarArreglos();
        leerLuminance();
        if (contrasteNormalizado) {
            normalizarContraste();
        }
        obtenerGradientes(gaussRadio, gaussWidth);
        int low = Math.round(umbralBajo * MAGNITUD_ESCALA);
        int high = Math.round(umbralAlto * MAGNITUD_ESCALA);
        umbralizacionHysteresis(low, high);
        bordesUmbral();
        writeEdges(data);
    }

    private void iniciarArreglos() {
        if (data == null || tamImagen != data.length) {
            data = new int[tamImagen];
            magnitud = new int[tamImagen];

            xConv = new float[tamImagen];
            yConv = new float[tamImagen];
            xGradiente = new float[tamImagen];
            yGradiente = new float[tamImagen];
        }
    }

    
    private void obtenerGradientes(float kernelRadius, int kernelWidth) {

        //genera ascara gauss
        float kernel[] = new float[kernelWidth];
        float diffKernel[] = new float[kernelWidth];
        int kwidth;
        for (kwidth = 0; kwidth < kernelWidth; kwidth++) {
            float g1 = gaussian(kwidth, kernelRadius);
            if (g1 <= GAUSSIAN_CUT_OFF && kwidth >= 2) {
                break;
            }
            float g2 = gaussian(kwidth - 0.5f, kernelRadius);
            float g3 = gaussian(kwidth + 0.5f, kernelRadius);
            kernel[kwidth] = (g1 + g2 + g3) / 3f / (2f * (float) Math.PI * kernelRadius * kernelRadius);
            diffKernel[kwidth] = g3 - g2;
        }

        int initX = kwidth - 1;
        int maxX = width - (kwidth - 1);
        int initY = width * (kwidth - 1);
        int maxY = width * (height - (kwidth - 1));

        //realiza convolucion en direcciones x y y
        for (int x = initX; x < maxX; x++) {
            for (int y = initY; y < maxY; y += width) {
                int index = x + y;
                float sumX = data[index] * kernel[0];
                float sumY = sumX;
                int xOffset = 1;
                int yOffset = width;
                for (; xOffset < kwidth;) {
                    sumY += kernel[xOffset] * (data[index - yOffset] + data[index + yOffset]);
                    sumX += kernel[xOffset] * (data[index - xOffset] + data[index + xOffset]);
                    yOffset += width;
                    xOffset++;
                }

                yConv[index] = sumY;
                xConv[index] = sumX;
            }

        }

        for (int x = initX; x < maxX; x++) {
            for (int y = initY; y < maxY; y += width) {
                float sum = 0f;
                int index = x + y;
                for (int i = 1; i < kwidth; i++) {
                    sum += diffKernel[i] * (yConv[index - i] - yConv[index + i]);
                }

                xGradiente[index] = sum;
            }

        }

        for (int x = kwidth; x < width - kwidth; x++) {
            for (int y = initY; y < maxY; y += width) {
                float sum = 0.0f;
                int index = x + y;
                int yOffset = width;
                for (int i = 1; i < kwidth; i++) {
                    sum += diffKernel[i] * (xConv[index - yOffset] - xConv[index + yOffset]);
                    yOffset += width;
                }

                yGradiente[index] = sum;
            }

        }

        initX = kwidth;
        maxX = width - kwidth;
        initY = width * kwidth;
        maxY = width * (height - kwidth);
        for (int x = initX; x < maxX; x++) {
            for (int y = initY; y < maxY; y += width) {
                int index = x + y;
                int indexN = index - width;
                int indexS = index + width;
                int indexW = index - 1;
                int indexE = index + 1;
                int indexNW = indexN - 1;
                int indexNE = indexN + 1;
                int indexSW = indexS - 1;
                int indexSE = indexS + 1;

                float xGrad = xGradiente[index];
                float yGrad = yGradiente[index];
                float gradMag = hypot(xGrad, yGrad);

                //perform non-maximal supression
                float nMag = hypot(xGradiente[indexN], yGradiente[indexN]);
                float sMag = hypot(xGradiente[indexS], yGradiente[indexS]);
                float wMag = hypot(xGradiente[indexW], yGradiente[indexW]);
                float eMag = hypot(xGradiente[indexE], yGradiente[indexE]);
                float neMag = hypot(xGradiente[indexNE], yGradiente[indexNE]);
                float seMag = hypot(xGradiente[indexSE], yGradiente[indexSE]);
                float swMag = hypot(xGradiente[indexSW], yGradiente[indexSW]);
                float nwMag = hypot(xGradiente[indexNW], yGradiente[indexNW]);
                float tmp;
            
                
                if (xGrad * yGrad <= (float) 0 
                        ? Math.abs(xGrad) >= Math.abs(yGrad)
                                ? (tmp = Math.abs(xGrad * gradMag)) >= Math.abs(yGrad * neMag - (xGrad + yGrad) * eMag)
                                && tmp > Math.abs(yGrad * swMag - (xGrad + yGrad) * wMag) 
                                : (tmp = Math.abs(yGrad * gradMag)) >= Math.abs(xGrad * neMag - (yGrad + xGrad) * nMag)
                                && tmp > Math.abs(xGrad * swMag - (yGrad + xGrad) * sMag)
                        : Math.abs(xGrad) >= Math.abs(yGrad)
                                ? (tmp = Math.abs(xGrad * gradMag)) >= Math.abs(yGrad * seMag + (xGrad - yGrad) * eMag)
                                && tmp > Math.abs(yGrad * nwMag + (xGrad - yGrad) * wMag) 
                                : (tmp = Math.abs(yGrad * gradMag)) >= Math.abs(xGrad * seMag + (yGrad - xGrad) * sMag) 
                                && tmp > Math.abs(xGrad * nwMag + (yGrad - xGrad) * nMag) ) {
                    magnitud[index] = gradMag >= MAGNITUD_LIMITE ? MAGNITUD_MAX : (int) (MAGNITUD_ESCALA * gradMag);
		
                } else {
                    magnitud[index] = 0;
                }
            }
        }
    }

	//NOTE: It is quite feasible to replace the implementation of this method
    //with one which only loosely approximates the hypot function. I've tested
    //simple approximations such as Math.abs(x) + Math.abs(y) and they work fine.
    private float hypot(float x, float y) {
        return (float) Math.hypot(x, y);
    }

    private float gaussian(float x, float sigma) {
        return (float) Math.exp(-(x * x) / (2f * sigma * sigma));
    }

    private void umbralizacionHysteresis(int low, int high) {
		
        Arrays.fill(data, 0);

        int offset = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[offset] == 0 && magnitud[offset] >= high) {
                    follow(x, y, offset, low);
                }
                offset++;
            }
        }
    }

    private void follow(int x1, int y1, int i1, int umbral) {
        int x0 = x1 == 0 ? x1 : x1 - 1;
        int x2 = x1 == width - 1 ? x1 : x1 + 1;
        int y0 = y1 == 0 ? y1 : y1 - 1;
        int y2 = y1 == height - 1 ? y1 : y1 + 1;

        data[i1] = magnitud[i1];
        for (int x = x0; x <= x2; x++) {
            for (int y = y0; y <= y2; y++) {
                int i2 = x + y * width;
                if ((y != y1 || x != x1)
                        && data[i2] == 0
                        && magnitud[i2] >= umbral) {
                    follow(x, y, i2, umbral);
                    return;
                }
            }
        }
    }

    private void bordesUmbral() {
        for (int i = 0; i < tamImagen; i++) {
            data[i] = data[i] > 0 ? -1 : 0xff000000;
        }
    }

    private int luminance(float r, float g, float b) {
        return Math.round(0.299f * r + 0.587f * g + 0.114f * b);
    }

    private void leerLuminance() {
        int type = imagenOriginal.getType();
        if (type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_ARGB) {
            int[] pixels = (int[]) imagenOriginal.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < tamImagen; i++) {
                int p = pixels[i];
                int r = (p & 0xff0000) >> 16;
                int g = (p & 0xff00) >> 8;
                int b = p & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else if (type == BufferedImage.TYPE_BYTE_GRAY) {
            byte[] pixels = (byte[]) imagenOriginal.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < tamImagen; i++) {
                data[i] = (pixels[i] & 0xff);
            }
        } else if (type == BufferedImage.TYPE_USHORT_GRAY) {
            short[] pixels = (short[]) imagenOriginal.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < tamImagen; i++) {
                data[i] = (pixels[i] & 0xffff) / 256;
            }
        } else if (type == BufferedImage.TYPE_3BYTE_BGR) {
            byte[] pixels = (byte[]) imagenOriginal.getData().getDataElements(0, 0, width, height, null);
            int offset = 0;
            for (int i = 0; i < tamImagen; i++) {
                int b = pixels[offset++] & 0xff;
                int g = pixels[offset++] & 0xff;
                int r = pixels[offset++] & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else {
            throw new IllegalArgumentException("Unsupported image type: " + type);
        }
    }

    private void normalizarContraste() {
        int[] histogram = new int[256];
        for (int i = 0; i < data.length; i++) {
            histogram[data[i]]++;
        }
        int[] remap = new int[256];
        int sum = 0;
        int j = 0;
        for (int i = 0; i < histogram.length; i++) {
            sum += histogram[i];
            int target = sum * 255 / tamImagen;
            for (int k = j + 1; k <= target; k++) {
                remap[k] = i;
            }
            j = target;
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = remap[data[i]];
        }
    }

    private void writeEdges(int pixels[]) {
		//NOTE: There is currently no mechanism for obtaining the edge data
        //in any other format other than an INT_ARGB type BufferedImage.
        //This may be easily remedied by providing alternative accessors.
        if (imagenFinal == null) {
            imagenFinal = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        imagenFinal.getWritableTile(0, 0).setDataElements(0, 0, width, height, pixels);
    }

}
