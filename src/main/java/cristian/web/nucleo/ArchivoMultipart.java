package cristian.web.nucleo;

import javax.servlet.http.Part;

import org.eclipse.jetty.util.IO;

public class ArchivoMultipart {
	
	protected final Part part;
	
	protected ArchivoMultipart(Part part) {
		this.part = part;
	}
	
	/**
	 * @return El objeto original obtenido de la peticion
	 */
	public Part getPart() {
		return part;
	}
	
	/**
	 * @return El nombre del parametro del Part
	 */
	public String nombreParametro() {
		return part.getName();
	}
	
	/**
	 * @return El nombre del archivo subido
	 */
	public String nombreArchivo() {
		return part.getSubmittedFileName();
	}
	
	/**
	 * @return El tamaño del archivo en bytes
	 */
	public long tamano() {
		return part.getSize();
	}
	
	/**
	 * @return Los bytes del archivo subido obtenidos del input del Part
	 */
	public byte[] obtenerBytes() {
		try {
			return IO.readBytes(part.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
