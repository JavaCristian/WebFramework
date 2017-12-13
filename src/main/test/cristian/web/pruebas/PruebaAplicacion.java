package cristian.web.pruebas;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Locale;
import java.util.UUID;

import cristian.web.anotaciones.Controlador;
import cristian.web.anotaciones.Parametro;
import cristian.web.anotaciones.Peticion;
import cristian.web.anotaciones.VariableRuta;
import cristian.web.nucleo.Aplicacion;
import cristian.web.nucleo.ArchivoMultipart;
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
	
	@Peticion(ruta="/goku", metodos={"GET", "POST"})
	private Recurso goku() {
		return Recurso.of("goku.png");
	}
	
	@Peticion(ruta="/cr", rest=true, origenCruzado=true)
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
	
	@Peticion(ruta="/upload", metodos="POST")
	private void test(PrintWriter writer, @Parametro(nombre="archivo") ArchivoMultipart archivo) {
		try {
			String ruta = System.getProperty("user.home") + "\\Desktop\\" + archivo.nombreArchivo();
			Files.write(new File(ruta).toPath(), archivo.obtenerBytes());
			writer.print("exito: true");
		} catch (Exception e) {
			writer.print("exito: false");
		} finally {
			writer.close();
		}
		
	}
	
	@Peticion(ruta="/var/{nombre}")
	private String rutaVariable(@VariableRuta(nombre="nombre") String nombre) {
		return nombre;
	}
	
	@Peticion(ruta="/{nombre}/{edad}")
	private String rutaVariable2(@VariableRuta(nombre="nombre") String nombre,
			@VariableRuta(nombre="edad") String edad) {
		return nombre + " / " + edad;
	}
	
}