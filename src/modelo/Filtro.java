package modelo;

import enums.Canal;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImagingOpException;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;
import utiles.MatricesManager;



public class Filtro implements BufferedImageOp, RasterOp {

	public static final int PIXELES_BORDE_EN_CERO = 0;

	protected static Kernel kernel;
	private int condicionBorde;
	private RenderingHints hints;

	public Filtro(Kernel kernelP) {
		kernel = kernelP;
		condicionBorde = PIXELES_BORDE_EN_CERO;
	}

	public final BufferedImage filter(Imagen imagenOriginal, Imagen imagenDestino) {
		
		BufferedImage bufferOriginal = imagenOriginal.getBufferedImage();
		BufferedImage bufferDestino = imagenDestino.getBufferedImage();
		
		if (bufferOriginal == bufferDestino)
			throw new IllegalArgumentException("La imagen original y la de destino no pueden ser las mismas");

		if (bufferDestino == null)
			bufferDestino = createCompatibleDestImage(bufferOriginal, bufferOriginal.getColorModel());

		BufferedImage src1 = bufferOriginal;
		BufferedImage dst1 = bufferDestino;
		
		if (src1.getColorModel().getColorSpace().getType() != bufferDestino
				.getColorModel().getColorSpace().getType())
			dst1 = createCompatibleDestImage(bufferOriginal, bufferOriginal.getColorModel());

		filter(src1.getRaster(), dst1.getRaster());

		imagenDestino.setMatriz(MatricesManager.calcularMatrizDeLaImagen(bufferDestino, Canal.ROJO), Canal.ROJO);
		imagenDestino.setMatriz(MatricesManager.calcularMatrizDeLaImagen(bufferDestino, Canal.VERDE), Canal.VERDE);
		imagenDestino.setMatriz(MatricesManager.calcularMatrizDeLaImagen(bufferDestino, Canal.AZUL), Canal.AZUL);
		
		return bufferDestino;
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src,
			ColorModel modeloDeColoor) {
		if (modeloDeColoor != null)
			return new BufferedImage(modeloDeColoor, src.getRaster()
					.createCompatibleWritableRaster(),
					src.isAlphaPremultiplied(), null);

		return new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
	}

	public final RenderingHints getRenderingHints() {
		return hints;
	}

	public int getCondicionBorde() {
		return condicionBorde;
	}

	public final Kernel getMascara() {
		return (Kernel) kernel.clone();
	}

	public WritableRaster filter(Raster imagenInicial, WritableRaster imagenDestino) {
		if (imagenInicial == imagenDestino)
			throw new IllegalArgumentException("imagen origen y destino deben ser distintas");
		if (kernel.getWidth() > imagenInicial.getWidth()
				|| kernel.getHeight() > imagenInicial.getHeight())
			throw new ImagingOpException("La m�scara es muy grande");
		if (imagenDestino == null)
			imagenDestino = createCompatibleDestRaster(imagenInicial);
		else if (imagenInicial.getNumBands() != imagenDestino.getNumBands())
			throw new ImagingOpException(
					"imagen origen y destino tienen distinto numero de bandas");

		int anchoMascara = kernel.getWidth();
		int alturaMascara = kernel.getHeight();
		int izquierda = kernel.getXOrigin();
		int derecha = Math.max(anchoMascara - izquierda - 1, 0);
		int arriba = kernel.getYOrigin();
		int abajo = Math.max(alturaMascara - arriba - 1, 0);

		//Magia de buffered image
		int[] valorMaximo = imagenInicial.getSampleModel().getSampleSize();
		for (int i = 0; i < valorMaximo.length; i++)
			valorMaximo[i] = (int) Math.pow(2, valorMaximo[i]) - 1;

		int anchoDeLaRegionAlcanzable = imagenInicial.getWidth() - izquierda - derecha;
		int altoDeLaRegionAlcanzable = imagenInicial.getHeight() - arriba - abajo;
		float[] valoresDeLaMascara = kernel.getKernelData(null);
		float[] matrizTemporal = new float[anchoMascara * alturaMascara];

		float[]maximosYMinimos = calcularMaximosYMinimos(imagenInicial, imagenDestino);
		float minimo = maximosYMinimos[0];
		float maximo = maximosYMinimos[1];
		
		for (int x = 0; x < anchoDeLaRegionAlcanzable; x++) {
			for (int y = 0; y < altoDeLaRegionAlcanzable; y++) {

				for (int banda = 0; banda < imagenInicial.getNumBands(); banda++) {
					float v = 0;
					imagenInicial.getSamples(x, y, anchoMascara, alturaMascara, banda, matrizTemporal);
					for (int i = 0; i < matrizTemporal.length; i++)
						v += matrizTemporal[matrizTemporal.length - i - 1] * valoresDeLaMascara[i];

					float vTransformado = ((((float)valorMaximo[banda]) / (maximo - minimo)) * v) - ((minimo * (float)valorMaximo[banda]) / (maximo - minimo));

					imagenDestino.setSample(x +kernel.getXOrigin(), y + kernel.getYOrigin(), banda, vTransformado);
					
				}
			}
		}

		return imagenDestino;
	}
	
	public WritableRaster createCompatibleDestRaster(Raster src) {
		return src.createCompatibleWritableRaster();
	}

	public final Rectangle2D getBounds2D(BufferedImage src) {
		return src.getRaster().getBounds();
	}

	public final Rectangle2D getBounds2D(Raster src) {
		return src.getBounds();
	}

	public final Point2D getPoint2D(Point2D src, Point2D dst) {
		if (dst == null)
			return (Point2D) src.clone();
		dst.setLocation(src);
		return dst;
	}
	
	public static float[] calcularMaximosYMinimos(Raster imagenInicial, WritableRaster imagenDestino){
		
		int anchoMascara = kernel.getWidth();
		int alturaMascara = kernel.getHeight();
		int izquierda = kernel.getXOrigin();
		int derecha = Math.max(anchoMascara - izquierda - 1, 0);
		int arriba = kernel.getYOrigin();
		int abajo = Math.max(alturaMascara - arriba - 1, 0);
		
		float maximo = 0;
		float minimo = 0;

		int anchoDeLaRegionAlcanzable = imagenInicial.getWidth() - izquierda - derecha;
		int altoDeLaRegionAlcanzable = imagenInicial.getHeight() - arriba - abajo;
		float[] valoresDeLaMascara = kernel.getKernelData(null);
		float[] matrizTemporal = new float[anchoMascara * alturaMascara];

		for (int x = 0; x < anchoDeLaRegionAlcanzable-5; x++) {
			for (int y = 0; y < altoDeLaRegionAlcanzable-5; y++) {

				for (int banda = 0; banda < imagenInicial.getNumBands(); banda++) {
					float v = 0;
					imagenInicial.getSamples(x, y, anchoMascara, alturaMascara, banda, matrizTemporal);
					for (int i = 0; i < matrizTemporal.length; i++)
						v += matrizTemporal[matrizTemporal.length - i - 1] * valoresDeLaMascara[i];
					
					if (maximo < v){
						maximo = v;
					}
					
					if(minimo > v){
						minimo = v;
					}
				}
			}
		}
		float[] maximosYMinimos = new float[2];
		maximosYMinimos[0] = minimo;
		maximosYMinimos[1] = maximo;
		return maximosYMinimos;
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		// TODO Auto-generated method stub
		return null;
	}

}
