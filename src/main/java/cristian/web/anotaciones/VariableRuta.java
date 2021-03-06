package cristian.web.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Aplicable a parametros y para la clase String
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface VariableRuta {
	
	/**
	 * El nombre de la variable
	 */
	String nombre() default "";
	
}
