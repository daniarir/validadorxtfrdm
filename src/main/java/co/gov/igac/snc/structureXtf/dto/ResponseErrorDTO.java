package co.gov.igac.snc.structureXtf.dto;

import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;

public class ResponseErrorDTO {
	
	private String type;
	private String title;
	private String code;
	private String status;
	private String detail;
	private String instance;
	
	
	public ResponseErrorDTO() {
	}
	
	public ResponseErrorDTO(AplicacionEstandarDeExcepciones ex) {
		this.type = ex.getType();
		this.title = ex.getTitle();
		this.code = ex.getCode();
		this.status = ex.getStatus();
		this.detail = ex.getDetail();
		this.instance = ex.getInstance();
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
}
