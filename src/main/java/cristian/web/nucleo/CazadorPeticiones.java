package cristian.web.nucleo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

class CazadorPeticiones extends AbstractHandler {
	
	@Override
	public void handle(String ruta, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// Obtener el ejecutador de peticiones
		EjecutadorPeticion ejecutador = ManejadorPeticiones.obtenerEjecutador(ruta, request.getMethod());
		
		try {
			
			// si el ejecutador no es null ejecutarlo y indicar capturada la peticion
			if(ejecutador != null) {
				baseRequest.setHandled(true);
				ejecutador.ejecutar(ruta, request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
