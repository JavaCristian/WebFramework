package cristian.web.nucleo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class Aplicacion {

	/**
	 * Representa si la aplicacion ha sido iniciada
	 */
	protected static boolean iniciado = false;

	public static Server iniciar() {

		if(iniciado) {
			throw new IllegalStateException("La aplicacion ya ha sido iniciada.");
		}

		iniciado = true;

		try {

			// Iniciar el Manjeador de Peticiones
			ManejadorPeticiones.iniciar();

			// Iniciar el Cargador de Controladores
			CargadorControladores.iniciar();

			//Crear el servidor con el puerto 8080
			Server server = new Server(8080);
			
			// Crear el cazador de recursos
			ResourceHandler recursos = new ResourceHandler();
			recursos.setResourceBase("./recursos/");
			recursos.setDirectoriesListed(false);
			recursos.setDirAllowed(false);
			
			// Crear la lisra de cazadores
			HandlerList cazadores = new HandlerList(new CazadorPeticiones(), recursos);
			
			// Asignarle los cazadores e iniciar
			server.setHandler(cazadores);
			server.start();
			
			// Devolver el server
			return server;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
