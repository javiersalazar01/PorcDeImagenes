package bordes;

public class DetectorDeBordesLeclerc implements InterfaceDetectorDeBordes {

	private double sigma;
	
	public DetectorDeBordesLeclerc(double sigma){
		this.sigma = sigma;
	}

	@Override
	public float gradiente(float derivada) {
		return (float) Math.exp( (float) -Math.pow(Math.abs(derivada), 2) / Math.pow(sigma, 2));
	}
	
}
