package cristian.web.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Aplicable a clases, cuando se inicia la aplicacion se buscan todas las clases
 * con esta anotacion para registrar los metodos con @Peticion que tengan.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controlador {
	
}
