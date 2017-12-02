package cristian.web.nucleo;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cristian.web.anotaciones.Peticion;

class EjecutadorPeticion {

	protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	protected final Peticion peticion;
	protected final Method metodo;
	protected final Object padreMetodoInstancia;
	protected final boolean esVoid;

	protected EjecutadorPeticion(Peticion peticion, Method metodo) throws Exception {
		this.peticion = peticion;
		this.metodo = metodo;

		// Obtener un objeto padre del metodo para poder ejecutar el metodo
		padreMetodoInstancia = metodo.getDeclaringClass().newInstance();

		// Comprobar si el metodo es void
		this.esVoid = metodo.getReturnType().equals(Void.class);

		// Comprobar si el metodo es accesible
		if(!metodo.isAccessible()) {
			metodo.setAccessible(true);
		}
	}
	
	/**
	 * Ejecuta la peticion
	 */
	protected void ejecutar(String ruta, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object[] parametros = obtenerParametros(metodo, ruta, request, response);

		if(esVoid) {
			metodo.invoke(padreMetodoInstancia, parametros);
		} else {
			Object devolucion = metodo.invoke(padreMetodoInstancia, parametros);
			PrintWriter writer = response.getWriter();
			writer.print(peticion.rest() ? gson.toJson(devolucion) : devolucion);
			writer.close();
		}

	}
	
	/**
	 * @return Los parametros para ejecutar el metodo
	 */
	private Object[] obtenerParametros(Method metodo, String ruta, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Class<?>[] tipoDeParametros = metodo.getParameterTypes();
		int cantidad = tipoDeParametros.length; 

		Object[] parametros = new Object[cantidad];

		for(int i=0; i<cantidad; i++) {
			parametros[i] = obtenerParametro(tipoDeParametros[i], ruta, request, response);
		}

		return parametros;
	}
	
	/**
	 * @return - El parametro (obtenido de los objetos de la peticion) o null si es de otro tipo
	 */
	private Object obtenerParametro(Class<?> tipo, String ruta, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(tipo.equals(String.class))
			return ruta; 

		else if(tipo.equals(HttpServletRequest.class))
			return request;

		else if(tipo.equals(HttpServletResponse.class))
			return response;

		else if(tipo.equals(PrintWriter.class))
			return response.getWriter();

		return null;
	}

}
