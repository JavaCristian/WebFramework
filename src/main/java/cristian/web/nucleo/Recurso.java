package cristian.web.nucleo;

public class Recurso {
	
	public static Recurso of(String nombre) {
		return new Recurso(nombre);
	}
	
	private final String nombre;
	
	private Recurso(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return - La ruta para set cazada por cazador de recursos
	 */
	protected String ruta() {
		return "/".concat(nombre);
	}

}
