package cristian.web.nucleo;

import java.io.IOException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * Esta clase es el cazador de peticiones para el servidor, solicitara un ejecutador al
 * ManejadorAplicaciones para ejecutar la @Peticion.
 */
class CazadorPeticiones extends ResourceHandler {
	
	protected MultipartConfigElement multipartConfigElement;
	
	protected CazadorPeticiones() {
		setResourceBase("recursos/");
		setDirectoriesListed(false);
		setDirAllowed(false);
		
		/*
		 * Instancia para las peticiones multipart, pasando la ruta para 
		 * archivos temporales del sistema operativo.
		 */
		multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
	}
	
	@Override
	public void handle(String ruta, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// Obtener el ejecutador de peticiones
		EjecutadorPeticion ejecutador = ManejadorPeticiones.obtenerEjecutador(ruta.toLowerCase(), request.getMethod());
		
		try {
			
			// si el ejecutador no es null ejecutarlo
			if(ejecutador != null) {
				ejecutador.ejecutar(this, ruta, baseRequest, request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Usar este metodo para devolver un archivo.
	 * 
	 * @param recurso - El recurso
	 * @param request - La peticion
	 * @param response - La respuesta
	 * @throws Exception - Si ejecutando la superclase hay algun error.
	 */
	protected void recurso(Recurso recurso, Request request, HttpServletResponse response) throws Exception{
		/*
		 * Modificar la ruta del request para que el cazador de recursos encuentre la peticion
		 */
		request.setPathInfo(recurso.ruta());
		
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
	private void validarMetodo(Request request) {
		MetaData.Request data = request.getMetaData();
		
		if(data!=null && !data.getMethod().equalsIgnoreCase("GET") || !data.getMethod().equalsIgnoreCase("HEAD")) {
			request.setMethod("GET");
		}
	}
	
}
