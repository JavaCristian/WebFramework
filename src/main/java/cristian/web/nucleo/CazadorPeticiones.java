package cristian.web.nucleo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;

import cristian.web.utiles.Reflex;

class CazadorPeticiones extends ResourceHandler {
	
	protected CazadorPeticiones() {
		setResourceBase("./recursos/");
		setDirectoriesListed(false);
		setDirAllowed(false);
	}
	
	@Override
	public void handle(String ruta, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// Obtener el ejecutador de peticiones
		EjecutadorPeticion ejecutador = ManejadorPeticiones.obtenerEjecutador(ruta, request.getMethod());
		
		try {
			
			// si el ejecutador no es null ejecutarlo
			if(ejecutador != null) {
				ejecutador.ejecutar(this, ruta, baseRequest, request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected void recurso(Recurso recurso, Request request, HttpServletResponse response) throws Exception{
		/*
		 * Modificar la ruta del request para que el cazador de recursos encuentre la peticion
		 */
		Reflex.setValue(request, "_pathInfo", recurso.ruta());
		
		// Validar metodo
		validarMetodo(request);
		
		/*
		 * Ejecutar caza desde la superclase
		 */
		super.handle(recurso.ruta(), request, request, response);
	}
	
	/**
	 * Validar metodo que sea 'GET' o 'HEAD' para que la superclase pueda cazarlo.
	 * 
	 * @param baseRequest - La peticion
	 */
	protected void validarMetodo(Request request) {
		MetaData.Request data = request.getMetaData();
		
		if(data!=null && !data.getMethod().equalsIgnoreCase("get") || !data.getMethod().equalsIgnoreCase("head")) {
			Reflex.setValue(data, "_method", "GET");
		}
	}
	
}
