package co.gov.igac.snc.structureXtf.dto;

public class ResponseArchivoDTO {
	
	private String rutaArchivo;
	private String nombreArchivo;
	private String codigoStatus;
	private String origen;
	
	public ResponseArchivoDTO() {
		
	}	
	
	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

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

	public String getCodigoStatus() {
		return codigoStatus;
	}
	
	public void setCodigoStatus(String codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	@Override
	public String toString() {
		return "ResponseArchivoDto [rutaArchivo=" + rutaArchivo + ", nombreArchivo=" + nombreArchivo + ", codigoStatus="
				+ codigoStatus + ", origen=" + origen + "]";
	}
}
