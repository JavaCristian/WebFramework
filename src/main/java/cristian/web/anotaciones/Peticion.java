package cristian.web.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Aplicable a metodos, se ejecuta cuando se recibe una peticion y la ruta y el metodo coinciden.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Peticion {

	/**
	 * @return - La ruta para la peticion
	 */
	String ruta() default "/";

	/**
	 * @return - Los metodos permitidos para la peticion
	 */
	String[] metodos() default "GET";

	/**
	 * @return - true si la consulta es de tipo REST
	 */
	boolean rest() default false;
	
	/**
	 * @return - true si se permite el 'Access-Control-Allow-Origin'
	 */
	boolean origenCruzado() default false;

}
