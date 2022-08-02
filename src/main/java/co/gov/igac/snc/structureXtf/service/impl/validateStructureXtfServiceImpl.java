package co.gov.igac.snc.structureXtf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import co.gov.igac.snc.structureXtf.service.validateStructureXtfService;
import co.gov.igac.snc.structureXtf.util.Utilidades;
import co.gov.igac.snc.structureXtf.util.Propiedades;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.interlis2.validator.Validator;

import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.commons.iliValidator;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;

@Service
public class validateStructureXtfServiceImpl implements validateStructureXtfService {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private Propiedades propiedades;
	private final iliValidator ilivalidator = new iliValidator();

//	   Validar si esta BIEN la estructura o NO
	   public ResponseArchivoDto validarXtf(String rutaAzureDownload, String nombreArchivo, String origen) throws AplicacionEstandarDeExcepciones {
		   
		ResponseArchivoDto response = new ResponseArchivoDto();
		String status = null;
		Map<String, String> peticionSubirArchivo = new HashMap<>();
		Map<String, String> peticionDescargarArchivo = new HashMap<>();
		ResponseEntity<?> respuestApi = null;
		String typeProcess = "";
		Boolean valor;

//		PROPERTIES
		String pathDefault = propiedades.getPathDefaultAzure();
		String urlDownload = propiedades.getDescargarArchivo();
		String urlUpload = propiedades.getSubirArchivo();
		String iliDirs = propiedades.getIliDirs();
		String modelNames = propiedades.getModelNames();
		File pathLog = propiedades.getPathLogJSON();

//		Inicializar configuraciones de la libreria IliValidator
		Settings settingIli = ilivalidator.configIliValidator(iliDirs, modelNames, pathLog);

		peticionDescargarArchivo.put("rutaStorage", rutaAzureDownload);
		peticionDescargarArchivo.put("nombreArchivo", nombreArchivo);
		respuestApi = Utilidades.consumirApiValidacionXTF(peticionDescargarArchivo, urlDownload);

		String pathConvert = respuestApi.getBody().toString();

		try {

			String[] splitRoute = new File(pathConvert).getPath().split("\\\\");

			String convertRoute = "";

			for (int i = 0; i < splitRoute.length; i++) {
				convertRoute += splitRoute[i] + "/";
			}

			convertRoute = convertRoute.substring(0, convertRoute.length() - 1);

			String extensionFile = nombreArchivo.toLowerCase().substring(nombreArchivo.length() - 4, nombreArchivo.length());

			if (!extensionFile.equals(".xtf")) {
				throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "Metodo no permitido", "E405",
						"405 - Metodo No Permitido", "Validacion del archivo XTF no permitido: " + nombreArchivo,
						"ilivalidator");
			} else {
				
				valor = Validator.runValidation(convertRoute, settingIli);

				if (valor.equals(false)) {
					status = "0";
					typeProcess = "/NoProcesados";
					if (origen.equals("IGAC")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					} else if (origen.equals("SNR")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					}
				} else {
					status = "1";
					typeProcess = "/Procesados";
					if (origen.equals("IGAC")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					} else if (origen.equals("SNR")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
					}
				}
			}
		} catch (Exception e) {
			throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "xtfValidatorRdm", "E500",
					"500 - Error interno del servicio", "Error en la implantacion del servicio: ",
					"validateStructureImpl");
		}
		
		respuestApi = Utilidades.consumirApiValidacionXTF(peticionSubirArchivo, urlUpload);
		ilivalidator.configLogIlivalidator(valor, pathLog, nombreArchivo, origen, urlUpload);
		
		response.setRutaArchivo(pathDefault + typeProcess);
		response.setNombreArchivo(nombreArchivo);
		response.setCodigoStatus(status);
		response.setOrigen(origen);
		eliminarArchivo(new File(pathConvert));
		return response;
	}
		
	private void eliminarArchivo(File archivo) {
		if(archivo.exists()) {
			System.gc();
			log.info("File Eliminar archivo " + archivo.getPath() + ": " + archivo.delete());
		}
	}
}
