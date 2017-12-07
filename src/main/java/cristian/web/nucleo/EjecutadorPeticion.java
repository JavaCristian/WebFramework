package cristian.web.nucleo;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cristian.web.anotaciones.Parametro;
import cristian.web.anotaciones.Peticion;
import cristian.web.anotaciones.VariableRuta;

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

		/*
		 * Comprobar si el tipo de peticion es 'multipart/form-data'
		 * para agregar el atributo MultipartConfigElement
		 */
		if (esMultipart(request)) {
			request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, cazador.multipartConfigElement);
		}

		// Agregar cabezeras
		if(peticion.rest())
			response.setHeader("Content-Type", "application/json");
		if(peticion.origenCruzado())
			response.setHeader("Access-Control-Allow-Origin", "*");

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
	 * @param request - La peticion
	 * @return true si la peticion es de tipo 'multipart/form-data'
	 */
	private boolean esMultipart(HttpServletRequest request) {
		String tipo = request.getContentType();
		return tipo!=null && tipo.startsWith("multipart/form-data");
	}

	/**
	 * @return Los parametros para ejecutar el metodo
	 */
	private Object[] obtenerParametros(String ruta, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Parameter[] parametros = metodo.getParameters();

		Object[] parametrosInvoke = new Object[parametros.length];

		for(int i=0; i<parametros.length; i++) {
			Parameter param = parametros[i];
			parametrosInvoke[i] = obtenerParametro(param, ruta, request, response);
		}

		return parametrosInvoke;
	}

	/**
	 * @return - El parametro (obtenido de los objetos de la peticion) o null si es de otro tipo
	 */
	private Object obtenerParametro(Parameter param, String ruta,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Class<?> tipo = param.getType();

		// Si el parametro tiene la anotacion @Parametro
		if(param.isAnnotationPresent(Parametro.class)) {
			Parametro parametro = param.getDeclaredAnnotation(Parametro.class);

			if(tipo.equals(String.class))
				return request.getParameter(parametro.nombre());

			else if(tipo.equals(ArchivoMultipart.class) && esMultipart(request)) {
				Part part = request.getPart(parametro.nombre());
				return part==null ? null : new ArchivoMultipart(part);
			}

			return null;
		}
		
		// Si el parametro tiene la anotacion @VariableRuta
		else if(param.isAnnotationPresent(VariableRuta.class)) {
			VariableRuta var = param.getDeclaredAnnotation(VariableRuta.class);
			
			if(var.nombre()!=null && !var.nombre().isEmpty())
				return obtenerRutaVariable(ruta, var.nombre());
			
			return null;
		}

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

	/**
	 * @param ruta - La ruta de la peticion
	 * @param nombre - El nombre de la variable de ruta
	 * @return - El valor de la variable si existe o null si no existe
	 */
	protected String obtenerRutaVariable(String ruta, String nombre) {
		
		nombre = "{".concat(nombre).concat("}");
		
		String[] ejecutador = peticion.ruta().split("/");

		for(int i=0; i<ejecutador.length; i++) {
			if(ejecutador[i].equalsIgnoreCase(nombre)) {
				return ruta.split("/")[i];
			}
		}

		return null;
	}

}
