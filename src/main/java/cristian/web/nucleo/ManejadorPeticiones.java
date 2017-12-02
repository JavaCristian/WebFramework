package cristian.web.nucleo;

import java.lang.reflect.Method;
import java.util.HashMap;

import cristian.web.anotaciones.Peticion;

class ManejadorPeticiones {

	/**
	 * Representa si el manejador ha sido iniciado.
	 */
	protected static boolean iniciado = false;
	
	/**
	 * Mapa que almazena los ejecutadores, la clave es la ruta
	 */
	protected static HashMap<String, EjecutadorPeticion> ejecutadores;

	protected static void iniciar() throws Exception {

		if(iniciado) {
			throw new IllegalStateException("El manejador ya ha sido iniciado.");
		}

		iniciado = true;
		ejecutadores = new HashMap<>();
	}
	
	/**
	 * Registra la consulta en el mapa.
	 * 
	 * @param peticion - La peticion
	 * @param metodo - El metodo
	 * @throws Exception
	 */
	protected static void registar(Peticion peticion, Method metodo) throws Exception {
		EjecutadorPeticion ejecutador = new EjecutadorPeticion(peticion, metodo);
		ejecutadores.put(peticion.ruta().toLowerCase(), ejecutador);
	}
	
	/**
	 * @param ruta - La ruta
	 * @param metodo - El metodo
	 * @return - El ejecutador si existe o null si no
	 */
	protected static EjecutadorPeticion obtenerEjecutador(String ruta, String metodo) {
		
		EjecutadorPeticion ejecutador = ejecutadores.get(ruta.toLowerCase());
		
		if(ejecutador != null && metodoPermitido(ejecutador, metodo)) {
			return ejecutador;
		}
		
		return null;
	}
	
	/**
	 * @param ejecutador - El ejecutador
	 * @param metodo - El metodo
	 * @return true si el metodo permitido esta establezido en la @Peticion del ejecutador
	 */
	protected static boolean metodoPermitido(EjecutadorPeticion ejecutador, String metodo) {
		
		String[] metodos = ejecutador.peticion.metodos();
		
		if(metodos==null || metodos.length==0)
			return false;
		
		for(String mem : metodos) {
			if(mem.equalsIgnoreCase(metodo)) {
				return true;
			}
		}
		
		return false;
	}

}
