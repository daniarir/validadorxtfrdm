package co.gov.igac.snc.structureXtf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import co.gov.igac.snc.structureXtf.service.IvalidateStructureXtfService;
import co.gov.igac.snc.structureXtf.util.Utilidades;
import co.gov.igac.snc.structureXtf.util.Propiedades;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.interlis2.validator.Validator;

import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.config.ConfigValidatorXtf;
import co.gov.igac.snc.structureXtf.config.IliValidator;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDTO;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

@Service
public class ValidateStructureXtfServiceImpl implements IvalidateStructureXtfService {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private Propiedades propiedades;
	private final IliValidator ilivalidator = new IliValidator();

//	Validar si esta BIEN la estructura o NO
	public ResponseArchivoDTO validarXtf(String rutaAzureDownload, String nombreArchivo, String origen)
			throws ExcepcionPropertiesNoExiste, ExcepcionLecturaDeArchivo, ExcepcionesDeNegocio, TransformerException,
			ParserConfigurationException, SAXException, IOException, InterruptedException {

		ResponseArchivoDTO response = new ResponseArchivoDTO();
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

		if (pathDefault.isEmpty() || urlDownload.isEmpty() || urlUpload.isEmpty() || iliDirs.isEmpty()
				|| modelNames.isEmpty() || pathLog.toString().isEmpty()) {
			throw new ExcepcionPropertiesNoExiste("No se encontro datos en el properties: ", HttpStatus.NOT_FOUND);
		}

//		Inicializar configuraciones de la libreria IliValidator
		Settings settingIli = ilivalidator.configIliValidator(iliDirs, modelNames, pathLog);

		peticionDescargarArchivo.put("rutaStorage", rutaAzureDownload);
		peticionDescargarArchivo.put("nombreArchivo", nombreArchivo);
		respuestApi = Utilidades.consumirApiValidacionXTF(peticionDescargarArchivo, urlDownload);

		if (!respuestApi.getStatusCode().is2xxSuccessful()) {
			throw new ExcepcionesDeNegocio(respuestApi.getBody().toString(),
					"Error consumiendo " + peticionDescargarArchivo, HttpStatus.CONFLICT);
		}

		String pathConvert = respuestApi.getBody().toString();

//		Ajustes bugs para el archivo XTF
		ConfigValidatorXtf.ajustesBugsValidatorXTF(pathConvert);

		try {

			String[] splitRoute = new File(pathConvert).getPath().split("\\\\");
			String convertRoute = "";

			for (int i = 0; i < splitRoute.length; i++) {
				convertRoute += splitRoute[i] + "/";
			}

			convertRoute = convertRoute.substring(0, convertRoute.length() - 1);

			String extensionFile = nombreArchivo.toLowerCase().substring(nombreArchivo.length() - 4,
					nombreArchivo.length());

			if (!extensionFile.equals(".xtf")) {
				throw new ExcepcionesDeNegocio("/error/xtfValidatorRdm",
						"Validacion del archivo XTF no permitido: " + extensionFile, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {

				valor = Validator.runValidation(convertRoute, settingIli);

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

		respuestApi = Utilidades.consumirApiValidacionXTF(peticionSubirArchivo, urlUpload);

		if (!respuestApi.getStatusCode().is2xxSuccessful()) {
			throw new ExcepcionesDeNegocio(respuestApi.getBody().toString(),
					"Error consumiendo " + peticionSubirArchivo, HttpStatus.CONFLICT);
		}

		ilivalidator.configLogIlivalidator(valor, pathLog, nombreArchivo, origen, urlUpload);

		response.setRutaArchivo(pathDefault + origen + typeProcess);
		response.setNombreArchivo(nombreArchivo);
		response.setCodigoStatus(status);
		response.setOrigen(origen);
		eliminarArchivo(new File(pathConvert));
		return response;
	}

	private void eliminarArchivo(File archivo) {
		if (archivo.exists()) {
			System.gc();
			log.info("File Eliminar archivo " + archivo.getPath() + ": " + archivo.delete());
		}
	}
}
