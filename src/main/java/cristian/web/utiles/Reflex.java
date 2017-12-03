package cristian.web.utiles;

import java.lang.reflect.Field;

public class Reflex {
	
	public static void setValue(Object object, String field, Object value) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			if(!f.isAccessible()) 
				f.setAccessible(true);
			f.set(object, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Object getValue(Object object, String field) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			if(!f.isAccessible()) 
				f.setAccessible(true);
			return f.get(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object object, String field, Class<T> clazz) {
		return (T) getValue(object, field);
	}
	
}
