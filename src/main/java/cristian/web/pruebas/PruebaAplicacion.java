package cristian.web.pruebas;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import cristian.web.anotaciones.Controlador;
import cristian.web.anotaciones.Peticion;
import cristian.web.nucleo.Aplicacion;
import cristian.web.nucleo.Recurso;

@Controlador
public class PruebaAplicacion {

	public static void main(String[] args) {
		Aplicacion.iniciar();
	}
	
	@Peticion(ruta="/")
	private Recurso index() {
		return Recurso.of("index.html");
	}
	
	@Peticion(ruta="/goku")
	private Recurso goku(HttpServletRequest request) {
		return Recurso.of("goku.png");
	}
	
	@Peticion(ruta="/cr", rest=true)
	private Persona cristiano() {
		return new Persona("Cristiano", "Ronaldo", 32);
	}
	
	@Peticion(ruta="/locale")
	private Locale locale(Locale locale) {
		return locale;
	}
	
	@Peticion(ruta="/uuid", metodos={"GET", "POST"})
	private void uuid(PrintWriter writer) {
		writer.print(UUID.randomUUID().toString());
		writer.close();
	}
	
}