package cristian.web.nucleo;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import cristian.web.anotaciones.Peticion;
import cristian.web.anotaciones.Controlador;

class CargadorControladores {
	
	/**
	 * Iniciar el cargador de controladores, buscando los @Controlador y 
	 * las @Peticion para registrarlas.
	 */
	protected static void iniciar() throws Exception {

		if(Aplicacion.iniciado) {
			throw new IllegalStateException("El cargador ya ha sido iniciado.");
		}

		/*
		 * Objeto Reflections:
		 *   - busca en todos los paquetes
		 *   - busca clases y metodos
		 */
		Reflections reflex = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(""))
				.addScanners(new TypeElementsScanner(), new MethodAnnotationsScanner())
				);

		/*
		 * Obtener las clases anotadas con @Controlador
		 */
		Set<Class<?>> clasesControlador = reflex.getTypesAnnotatedWith(Controlador.class);

		/*
		 * Recorrer todas las clases con anotacion @Controlador
		 * para encontrar los metodos con @Peticion
		 */
		for(Class<?> clazz : clasesControlador) {

			/*
			 * Obtener todos los metodos declarados en clazz
			 */
			Method[] todosMetodos = clazz.getDeclaredMethods();

			/*
			 * Recorrer los metodos declarados buscando @Peticion
			 */
			for(Method metodo : todosMetodos) {

				/*
				 * Obtener la anotacion @Peticion de metodo
				 */
				Peticion peticion = metodo.getDeclaredAnnotation(Peticion.class);

				/*
				 * Si el valor de peticion no es null, registrar la peticion
				 */
				if(peticion != null) {
					ManejadorPeticiones.registar(peticion, metodo);
				}
				
			}

		}
		
	}

}
