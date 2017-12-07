package cristian.web.nucleo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import cristian.web.anotaciones.Peticion;

class ManejadorPeticiones {

	/**
	 * Mapa que almazena los ejecutadores, la clave es la ruta
	 */
	protected static HashMap<String, EjecutadorPeticion> ejecutadores;

	/**
	 * Lista que almazena los ejecutadores con ruta segmentada
	 */
	protected static ArrayList<EjecutadorPeticion> ejecutadoresSegmentados;

	/**
	 * Iniciar el Manejador de peticiones
	 */
	protected static void iniciar() throws Exception {

		if(Aplicacion.iniciado) {
			throw new IllegalStateException("El manejador ya ha sido iniciado.");
		}

		ejecutadores = new HashMap<>();
		ejecutadoresSegmentados = new ArrayList<>();
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
		String ruta = peticion.ruta().toLowerCase();

		// Comprobar si la ruta esta segmentada
		if(esRutaSegmentada(ruta)) {
			ejecutadoresSegmentados.add(ejecutador);
		} else {
			ejecutadores.put(ruta, ejecutador);
		}
	}

	/**
	 * @param ruta - La ruta 'lower case'
	 * @param metodo - El metodo
	 * @return - El ejecutador si existe o null si no
	 */
	protected static EjecutadorPeticion obtenerEjecutador(String ruta, String metodo) {

		/*
		 * Obtenemos el ejecutador del mapa con la ruta si el ejecutador no es null
		 * y el metodo de la peticion esta permitido devolvemos el ejecutador.
		 * 
		 * En caso contrario buscamos en los ejecutadores con ruta segmentada si
		 * encontramos un ejecutador con la ruta asignable y metodo permitido los
		 * comparamos entre si para encontrar el mas especifico.
		 */

		EjecutadorPeticion ejecutador = ejecutadores.get(ruta);

		if(ejecutador!=null && metodoPermitido(ejecutador, metodo)) {
			return ejecutador;
		} else {

			EjecutadorPeticion actual = null;

			for(EjecutadorPeticion ejec : ejecutadoresSegmentados) {
				if(esRutaAsignable(ruta, ejec.peticion.ruta()) && metodoPermitido(ejec, metodo))
					actual = masEspecifico(ruta, actual, ejec);
			}

			return actual;
		}
	}

	/**
	 * @param ejecutador - El ejecutador
	 * @param metodo - El metodo
	 * @return true si el metodo permitido esta establezido en la @Peticion del ejecutador
	 */
	private static boolean metodoPermitido(EjecutadorPeticion ejecutador, String metodo) {

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

	/**
	 * @param ruta - La ruta
	 * @return true si la ruta es segmentada
	 */
	private static boolean esRutaSegmentada(String ruta) {
		for(String segmento : ruta.split("/")) {
			if(esSegmento(segmento))
				return true;
		}

		return false;
	}

	/**
	 * @param parte - El segmento de la ruta
	 * @return true si es un segmento
	 */
	private static boolean esSegmento(String parte) {
		return parte.startsWith("{") && parte.endsWith("}");
	}

	/**
	 * @param rutaPeticion - La ruta de la peticion
	 * @param rutaEjecutador - La ruta del ejecutador
	 * @return true si la ruta de la peticion es asignable a la ruta segmentada del ejecutador
	 */
	private static boolean esRutaAsignable(String rutaPeticion, String rutaEjecutador) {

		String[] peticion = rutaPeticion.split("/");
		String[] ejecutador = rutaEjecutador.split("/");

		if(peticion.length != ejecutador.length)
			return false;

		for(int i=0; i<peticion.length; i++) {
			String segPet = peticion[i];
			String segEj = ejecutador[i];

			if(!esSegmento(segEj) && !segPet.equalsIgnoreCase(segEj))
				return false;
		}

		return true;
	}

	/**
	 * @param ruta - La ruta de la peticion
	 * @param actual - El ejecutador actual
	 * @param siguiente - El ejecutador siguiente
	 * @return El ejecutador mas especifico de la peticion o el 
	 * siguiente si tienen los mismos puntos de similitud
	 */
	private static EjecutadorPeticion masEspecifico(String ruta, 
			EjecutadorPeticion actual, EjecutadorPeticion siguiente) {

		if(actual == null)
			return siguiente;

		int simActual = similitud(ruta, actual);
		int simSiguiente = similitud(ruta, siguiente);

		if(simActual == simSiguiente)
			return siguiente;
		else
			return simActual>simSiguiente ? actual : siguiente;
	}
	
	/**
	 * @param ruta - La ruta de la peticion
	 * @param ejecutador - El ejecutador
	 * @return - Los puntos de similitud
	 */
	private static int similitud(String ruta, EjecutadorPeticion ejecutador) {
		int similitud = 0;

		String[] arrRuta = ruta.split("/");
		String[] arrEjec = ejecutador.peticion.ruta().split("/");

		for(int i=0; i<arrRuta.length; i++) {
			String parteRuta = arrRuta[i];
			String parteEjec = arrEjec[i];

			if(!esSegmento(parteEjec) && parteRuta.equalsIgnoreCase(parteEjec))
				similitud++;
		}

		return similitud;
	}

}
