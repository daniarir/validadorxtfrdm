package co.gov.igac.snc.structureXtf.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Configuration
@PropertySource("classpath:validatrStructureXtf.properties")
@Component
public class Propiedades {

	@Value("${utilsStorage.subirArchivo}")
	private String subirArchivo;

	@Value("${utilsStorage.descargarArchivo}")
	private String descargarArchivo;

	@Value("${co.gov.igac.azure.pathDefaultAzure}")
	private String pathDefaultAzure;

	@Value("${co.gov.igac.azure.fileSystemClient}")
	private String fileSystemClient;

	@Value("${co.gov.igac.ilivalidator.iliDirs}")
	private String iliDirs;

	@Value("${co.gov.igac.ilivalidator.modelNames}")
	private String modelNames;
	
	@Value("${co.gov.igac.ilivalidator.pathLogJSON}")
	private File pathLogJSON;
	
	public Propiedades() {
		
	}

	public File getPathLogJSON() {
		return pathLogJSON;
	}

	public void setPathLogJSON(File pathLogJSON) {
		this.pathLogJSON = pathLogJSON;
	}
	
	public String getSubirArchivo() {
		return subirArchivo;
	}

	public void setSubirArchivo(String subirArchivo) {
		this.subirArchivo = subirArchivo;
	}

	public String getDescargarArchivo() {
		return descargarArchivo;
	}

	public void setDescargarArchivo(String descargarArchivo) {
		this.descargarArchivo = descargarArchivo;
	}

	public String getPathDefaultAzure() {
		return pathDefaultAzure;
	}

	public void setPathDefaultAzure(String pathDefaultAzure) {
		this.pathDefaultAzure = pathDefaultAzure;
	}
	
	public String getFileSystemClient() {
		return fileSystemClient;
	}
	
	public void setFileSystemClient(String fileSystemClient) {
		this.fileSystemClient = fileSystemClient;
	}

	public String getIliDirs() {
		return iliDirs;
	}

	public void setIliDirs(String iliDirs) {
		this.iliDirs = iliDirs;
	}

	public String getModelNames() {
		return modelNames;
	}

	public void setModelNames(String modelNames) {
		this.modelNames = modelNames;
	}

	@Override
	public String toString() {
		return "Propiedades [subirArchivo=" + subirArchivo + ", descargarArchivo=" + descargarArchivo
				+ ", pathDefaultAzure=" + pathDefaultAzure + ", fileSystemClient=" + fileSystemClient + ", iliDirs="
				+ iliDirs + ", modelNames=" + modelNames + "]";
	}
	
	
}
