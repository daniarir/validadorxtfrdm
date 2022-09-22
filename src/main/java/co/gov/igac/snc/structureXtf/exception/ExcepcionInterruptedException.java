package co.gov.igac.snc.structureXtf.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ExcepcionInterruptedException extends InterruptedException {
	
private static final long serialVersionUID = 1L;
	
	@Getter
	private HttpStatus estado;
	@Getter
	private String titulo;

	public ExcepcionInterruptedException(String mensaje, String titulo, HttpStatus estado) {
		super(mensaje);
		this.titulo = titulo;
		this.estado = estado;
	}

}
