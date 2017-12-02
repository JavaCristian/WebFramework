package cristian.web.pruebas;

import java.io.PrintWriter;
import java.util.UUID;

import cristian.web.anotaciones.Controlador;
import cristian.web.anotaciones.Peticion;
import cristian.web.nucleo.Aplicacion;

@Controlador
public class PruebaAplicacion {

	public static void main(String[] args) {
		Aplicacion.iniciar();
	}
	
	@Peticion(ruta="/", rest=true)
	private Persona persona() {
		return new Persona("Cristiano", "Ronaldo", 32);
	}
	
	@Peticion(ruta="/uuid", metodos={"GET", "POST"})
	private void uuid(PrintWriter writer) {
		writer.print(UUID.randomUUID().toString());
		writer.close();
	}
	
}