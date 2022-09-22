package co.gov.igac.snc.structureXtf.exception;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ExcepcionExecutionException extends ExecutionException {
private static final long serialVersionUID = 1L;
	
	@Getter
	private HttpStatus estado;
	@Getter
	private String titulo;

	public ExcepcionExecutionException(String mensaje, String titulo, HttpStatus estado) {
		super(mensaje);
		this.titulo = titulo;
		this.estado = estado;
	}

}
