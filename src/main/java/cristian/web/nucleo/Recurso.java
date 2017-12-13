package cristian.web.nucleo;

/**
 * Usar esta clase para metodos @Peticion en los que queremos devolver un recurso.
 */
public class Recurso {
	
	/**
	 * @param nombre - El nombre del recurso
	 * @return El recurso
	 */
	public static Recurso of(String nombre) {
		return new Recurso(nombre);
	}
	
	private final String nombre;
	
	private Recurso(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return - La ruta para ser cazada por cazador de recursos
	 */
	protected String ruta() {
		return "/".concat(nombre);
	}

}
