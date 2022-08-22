package co.gov.igac.snc.structureXtf.dto;

public class DataDTO {
	
	private String rutaArchivo;
    private String nombreArchivo;
    private String origen;
    
	public String getRutaArchivo() {
		return rutaArchivo;
	}
	
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public void setOrigen(String origen) {
		this.origen = origen;
	}
}
