package co.gov.igac.snc.structureXtf.dto;

public class ResponseArchivoDto {
	private String rutaArchivo;
	private String nombreArchivoValidado;
	private String codigoStatus;
	
	public String getRutaArchivo() {
		return rutaArchivo;
	}
	
	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
	
	public String getNombreArchivoValidado() {
		return nombreArchivoValidado;
	}
	
	public void setNombreArchivoValidado(String nombreArchivoValidado) {
		this.nombreArchivoValidado = nombreArchivoValidado;
	}
	
	public String getCodigoStatus() {
		return codigoStatus;
	}
	
	public void setCodigoStatus(String codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	public ResponseArchivoDto() {
	}
}
