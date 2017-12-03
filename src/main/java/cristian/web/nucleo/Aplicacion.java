package cristian.web.nucleo;

import org.eclipse.jetty.server.Server;

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
			
			// Asignarle el cazador e iniciar
			server.setHandler(new CazadorPeticiones());
			server.start();
			
			// Devolver el server
			return server;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
