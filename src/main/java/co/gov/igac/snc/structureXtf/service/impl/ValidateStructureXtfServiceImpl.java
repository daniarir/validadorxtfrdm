package co.gov.igac.snc.structureXtf.service.impl;

import co.gov.igac.snc.structureXtf.util.AzureStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.gov.igac.snc.structureXtf.service.ValidateStructureXtfService;
import co.gov.igac.snc.structureXtf.util.Propiedades;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.interlis2.validator.Validator;

import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.config.ConfigValidatorXtf;
import co.gov.igac.snc.structureXtf.config.IliValidator;
import co.gov.igac.snc.structureXtf.dto.Data;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDtoKafka;
import co.gov.igac.snc.structureXtf.exception.ExcepcionExecutionException;
import co.gov.igac.snc.structureXtf.exception.ExcepcionInterruptedException;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

@Service
public class ValidateStructureXtfServiceImpl implements ValidateStructureXtfService {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private Propiedades propiedades;

	@Autowired
	private AzureStorage azureStorage;

	@Autowired
	private IliValidator ilivalidator;

	@Autowired
	KafkaProducerService kafkaProducer;

	@Async("methodAsync")
	public CompletableFuture<ResponseArchivoDto> validarXtf(String rutaAzureDownload, String nombreArchivo,
			String origen) throws ExcepcionesDeNegocio, ExcepcionLecturaDeArchivo, ExcepcionInterruptedException,
			ExcepcionExecutionException, ExcepcionPropertiesNoExiste {

		log.info("Inicio Proceso validarXtf");
		System.out.println("******************* Inicio Proceso validarXtf ******************");
		ResponseArchivoDto response = new ResponseArchivoDto();
		String status = null;
		Map<String, String> peticionSubirArchivo = new HashMap<>();
		String typeProcess = "";
		Boolean valor;

//		PROPERTIES
		log.info("Variables archivo .properties");
		System.out.println("******************* Variables archivo .properties ******************");
		String pathDefault = propiedades.getPathDefaultAzure();
		String urlUpload = propiedades.getSubirArchivo();
		String iliDirs = propiedades.getIliDirs();
		File pathLog = propiedades.getPathLogJSON();
		Boolean usaKafka = propiedades.getUsaKafka();

		log.info("Descargando archivo XTF");
		System.out.println("******************* Descargando archivo XTF ******************");
		String pathConvert = azureStorage.descargarArchivo(rutaAzureDownload, nombreArchivo, propiedades.getRutaDescargaXtf());

		log.info("Obteniendo el tipo de modelo RIC o SNR");
		System.out.println("******************* Obteniendo el tipo de modelo RIC o SNR ******************");
		String modelNames = ilivalidator.nombreTipoModelo(pathConvert);

		if (!new File(iliDirs + "\\\\" + modelNames).exists()) {
			log.info("Descargando Modelos");
			System.out.println("******************* Descargando Modelos ******************");
			azureStorage.descargarModelos(propiedades.getModelosStorage(), iliDirs);
		}

		log.info("Configurando settings para iliValidator");
		System.out.println("******************* Configurando settings para iliValidator ******************");
		CompletableFuture<Settings> settingIli = ilivalidator.configIliValidator(iliDirs, modelNames, pathLog);

		log.info("Ajustes bugs archivo XTF");
		System.out.println("******************* Ajustes bugs archivo XTF ******************");
		ConfigValidatorXtf.ajustesBugsValidatorXTF(pathConvert);

		try {

			String[] splitRoute = new File(pathConvert).getPath().split("\\\\");
			String convertRoute = "";

			for (int i = 0; i < splitRoute.length; i++) {
				convertRoute += splitRoute[i] + "/";
			}

			convertRoute = convertRoute.substring(0, convertRoute.length() - 1);
			String extensionFile = nombreArchivo.toLowerCase().substring(nombreArchivo.length() - 4, nombreArchivo.length());

			if (!extensionFile.equals(".xtf")) {
				throw new ExcepcionesDeNegocio("/error/xtfValidatorRdm",
						"Validacion del archivo XTF no permitido: " + extensionFile, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {

				log.info("Ejecutando libreria iliValidator");
				System.out.println("******************* Ejecutando libreria iliValidator ******************");
				valor = Validator.runValidation(convertRoute, settingIli.get());

				if (valor.equals(false)) {
					status = "0";
					typeProcess = "/NoProcesados";
					if (origen.equals("IGAC")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + origen + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					} else if (origen.equals("SNR")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + origen + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					}
				} else {
					status = "1";
					typeProcess = "/Procesados";
					if (origen.equals("IGAC")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + origen + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					} else if (origen.equals("SNR")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + origen + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					}
				}
			}
		} catch (Exception e) {
			throw new ExcepcionesDeNegocio("/error/xtfValidatorRdm",
					"Error en la implantacion del servicio:: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.info("Subiendo Archivo XTF en el azure Storage");
		System.out.println("******************* Subiendo Archivo XTF en el azure Storage ******************");
		azureStorage.subirArchivoGrande(peticionSubirArchivo.get("rutaArchivo"), peticionSubirArchivo.get("rutaStorage"), peticionSubirArchivo.get("nombreArchivo"));

		log.info("Configurando Log.json errores libreria iliValidator");
		System.out.println("******************* Configurando Log.json errores libreria iliValidator ******************");
		ilivalidator.configLogIlivalidator(valor, pathLog, nombreArchivo, origen, urlUpload);

		response.setRutaArchivo(pathDefault + origen + typeProcess);
		response.setNombreArchivo(nombreArchivo);
		response.setCodigoStatus(status);
		response.setOrigen(origen);

		log.info("Eliminando archivo");
		System.out.println("******************* Eliminando archivo ******************");
		eliminarArchivo(new File(pathConvert));

		if (usaKafka) {
			log.info("Enviando mensaje al topic Kafka");
			System.out.println("******************* Enviando mensaje al topic Kafka ******************");
			ResponseArchivoDtoKafka dtoKafka = new ResponseArchivoDtoKafka();
			Data dataKafka = new Data();
			dataKafka.setRutaArchivo(peticionSubirArchivo.get("rutaStorage"));
			dataKafka.setNombreArchivo(peticionSubirArchivo.get("nombreArchivo"));
			dataKafka.setOrigen(origen);
			if(typeProcess.equals("/Procesados")){
				dtoKafka.setKey("OK");
			}else{
				dtoKafka.setKey("NOK");
			}
			dtoKafka.setJson(dataKafka);
			kafkaProducer.send(dtoKafka);
		}

		log.info("Termino Proceso validarXtf");
		System.out.println("******************* Termino Proceso validarXtf ******************");
		return CompletableFuture.completedFuture(response);
	}

	private void eliminarArchivo(File archivo) {
		
	   if (archivo.exists()) {
	      System.gc();
	   } 
	}
}
