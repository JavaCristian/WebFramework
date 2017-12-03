package cristian.web.nucleo;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

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
	protected void ejecutar(CazadorPeticiones cazador, String ruta, Request baseRequest, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object[] parametros = obtenerParametros(ruta, request, response);

		if(esVoid) {
			baseRequest.setHandled(true);
			metodo.invoke(padreMetodoInstancia, parametros);
		} else {
			Object devolucion = metodo.invoke(padreMetodoInstancia, parametros);
			
			/*
			 * Si la devolucion es un Recurso
			 */
			if(devolucion instanceof Recurso) {
				// Obtener recurso
				Recurso recurso = (Recurso) devolucion;
				
				// Ejecutar caza del recurso
				cazador.recurso(recurso, baseRequest, response);
				return;
			}
			
			baseRequest.setHandled(true);
			PrintWriter writer = response.getWriter();
			writer.print(peticion.rest() ? gson.toJson(devolucion) : devolucion);
			writer.close();
		}

	}
	
	/**
	 * @return Los parametros para ejecutar el metodo
	 */
	private Object[] obtenerParametros(String ruta, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
		
		else if(tipo.equals(Locale.class))
			return request.getLocale();

		return null;
	}

}
