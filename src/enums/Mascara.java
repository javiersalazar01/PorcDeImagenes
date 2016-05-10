package enums;

public enum Mascara {

	MEDIA("M�scara de la media"),
	MEDIANA("M�scara de la mediana"),
	PASA_BAJOS("M�scara pasa bajos"),
	PASA_ALTOS("M�scara pasa altos");
	
	private String descripcion;

	Mascara(String desc){
		
		this.descripcion = desc;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
