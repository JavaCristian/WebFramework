package cristian.web.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Aplicable a parametros y para dos tipos de clase String & ArchivoMultipart.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parametro {
	
	/**
	 * El nombre del parametro
	 */
	String nombre() default "";
	
}
