package co.gov.igac.snc.structureXtf.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author gilber.lemus
 * @version 1.0
 */
public class AplicacionEstandarDeExcepciones extends Exception{
	
	private static final long serialVersionUID = 1L;
	private String type;
	private String title;
	private String code;
	private String status;
	private String detail;
	private String instance;
	private String tituloClient;
	private HttpStatus statusmessageClient;

	public AplicacionEstandarDeExcepciones() {
	}

	public AplicacionEstandarDeExcepciones(String type, String title, String code, String status, String detail, String instance) {
		this.type = type;
		this.title = title;
		this.code = code;
		this.status = status;
		this.instance = instance;
		this.detail = detail;
		
	}
	
	
	public AplicacionEstandarDeExcepciones(String type, String title, String code, HttpStatus status, String detail, String instance) {
		this.type = type;
		this.title = title;
		this.code = code;
		this.statusmessageClient = status;
		this.instance = instance;
		this.detail = detail;
		
	}
	
	public AplicacionEstandarDeExcepciones(String mensaje, String titulo, HttpStatus estado) {
		super(mensaje);
		this.statusmessageClient = estado;
		this.tituloClient = titulo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public HttpStatus getStatusmessageClient() {
		return statusmessageClient;
	}

	public void setStatusmessageClient(HttpStatus statusmessageClient) {
		this.statusmessageClient = statusmessageClient;
	}

	public String getTituloClient() {
		return tituloClient;
	}

	public void setTituloClient(String tituloClient) {
		this.tituloClient = tituloClient;
	}
}
