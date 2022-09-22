package co.gov.igac.snc.structureXtf.dto;

public class RequestArchivoDTO {
	
	private String rutaArchivo;
	private String nombreArchivo;
	private String origen;
	
	public String getRutaArchivo() {
		return rutaArchivo;
	}
	
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public RequestArchivoDTO(String rutaArchivo, String nombreArchivo, String origen) {
		this.rutaArchivo = rutaArchivo;
		this.nombreArchivo = nombreArchivo;
		this.origen = origen;
	}
	
	public RequestArchivoDTO() {
		
	}
}
