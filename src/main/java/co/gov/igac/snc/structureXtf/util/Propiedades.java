package co.gov.igac.snc.structureXtf.util;

import java.io.File;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:validatrStructureXtf.properties")
@Getter
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

	@Value("${co.gov.igac.ilivalidator.modelNamesRIC}")
	private String modelNamesRIC;
	
	@Value("${co.gov.igac.ilivalidator.modelNamesSNR}")
	private String modelNamesSNR;
	
	@Value("${co.gov.igac.ilivalidator.pathLogJSON}")
	private File pathLogJSON;

	@Value("${accountName}")
	private String accountName;

	@Value("${accountKey}")
	private String accountKey;

	@Value("${clientId}")
	private String clientId;

	@Value("${tenantId}")
	private String tenantId;

	@Value("${clientSecret}")
	private String clientSecret;

	@Value("${contenedor}")
	private String contenedor;

	@Value("${azure.modelos}")
	private String modelosStorage;

	@Value("${rutaDescargaXtf}")
	private String rutaDescargaXtf;

	@Value("${rdm.storage.json}")
	private String storageJson;
	
	@Value("${usaKafka}")
	private Boolean usaKafka;
}
