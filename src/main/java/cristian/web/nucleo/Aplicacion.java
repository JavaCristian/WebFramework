package cristian.web.nucleo;

import org.eclipse.jetty.server.Server;

/**
 * Esta clase representa el centro del 'MiniFramework' simplemente usando el metodo
 * iniciar se iniciara la aplicacion, por defecto en el puerto 8080.
 */
public class Aplicacion {
	
	/**
	 * Representa si la aplicacion ha sido iniciada
	 */
	protected static boolean iniciado = false;
	
	/**
	 * Iniciar la aplicacion
	 * 
	 * @return El servidor jetty
	 */
	public static Server iniciar() {
		
		if(iniciado) {
			throw new IllegalStateException("La aplicacion ya ha sido iniciada.");
		}

		try {

			// Iniciar el Manjeador de Peticiones
			ManejadorPeticiones.iniciar();

			// Iniciar el Cargador de Controladores
			CargadorControladores.iniciar();
			
			// Indicar como iniciado para evitar que se ejecute de nuevo
			iniciado = true;

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
