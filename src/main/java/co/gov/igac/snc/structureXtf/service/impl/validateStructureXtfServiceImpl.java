package co.gov.igac.snc.structureXtf.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import co.gov.igac.snc.structureXtf.service.validateStructureXtfService;
import co.gov.igac.snc.structureXtf.util.PropertiesConfig;
import co.gov.igac.snc.structureXtf.util.Utilidades;
import lombok.extern.slf4j.*;

import org.interlis2.validator.Validator;
import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.commons.iliValidator;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;

@Service
@Slf4j
public class validateStructureXtfServiceImpl implements validateStructureXtfService {

	private final PropertiesConfig prop = new PropertiesConfig();
	private final iliValidator ilivalidator = new iliValidator();

//	Validar si esta BIEN la estructura o NO
	public ResponseArchivoDto validarXtf(String rutaAzureDownload, String nombreArchivo, String origen) throws AplicacionEstandarDeExcepciones {

		ResponseArchivoDto response = new ResponseArchivoDto();
		String status = null;
		Map<String, String> peticionSubirArchivo = new HashMap<>();
		Map<String, String> peticionDescargarArchivo = new HashMap<>();
		ResponseEntity<?> respuestApi = null;
		String typeProcess = "";

		try {

//			PROPERTIES
			String pathDefault = prop.Properties().getProperty("co.gov.igac.azure.pathDefaultAzure");
			String urlDownload = prop.Properties().getProperty("utilsStorage.descargarArchivo");
			String urlUpload   = prop.Properties().getProperty("utilsStorage.subirArchivo");

//			Inicializar configuraciones de la libreria IliValidator
			Settings settingIli = ilivalidator.configIliValidator();

			peticionDescargarArchivo.put("rutaStorage", rutaAzureDownload);
			peticionDescargarArchivo.put("nombreArchivo", nombreArchivo);
			respuestApi = Utilidades.consumirApi(peticionDescargarArchivo, urlDownload);
			
			String pathConvert = respuestApi.getBody().toString();
			String[] splitRoute = new File(pathConvert).getPath().split("\\\\");
			String convertRoute = "";
			
			for (int i = 0; i < splitRoute.length; i++) {
				convertRoute += splitRoute[i] + "/";
			}
			
			convertRoute = convertRoute.substring(1, convertRoute.length() - 2);
			
			String extensionFile = nombreArchivo.toLowerCase().substring(nombreArchivo.length() - 4, nombreArchivo.length());
			
			if (!extensionFile.equals(".xtf")) {
				throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "Method Not Allowed", "E405",
						HttpStatus.METHOD_NOT_ALLOWED.toString(),
						"Validacion del archivo XTF no permitido: " + nombreArchivo, "ilivalidator");
			} else {
			
				Boolean valor = Validator.runValidation(convertRoute, settingIli);

				if (valor.equals(false)) {
					status = "0";
					typeProcess = "/NoProcesados";
					if (origen.equals("IGAC")) {
						
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
						respuestApi = Utilidades.consumirApi(peticionSubirArchivo, urlUpload);

					} else if (origen.equals("SNR")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
						respuestApi = Utilidades.consumirApi(peticionSubirArchivo, urlUpload);
					}
				} else {
					status = "1";
					typeProcess = "/Procesados";
					if (origen.equals("IGAC")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
						respuestApi = Utilidades.consumirApi(peticionSubirArchivo, urlUpload);

					} else if (origen.equals("SNR")) {
						peticionSubirArchivo.put("rutaArchivo", convertRoute);
						peticionSubirArchivo.put("rutaStorage", pathDefault + typeProcess);
						peticionSubirArchivo.put("nombreArchivo", nombreArchivo);
						respuestApi = Utilidades.consumirApi(peticionSubirArchivo, urlUpload);
					}
				}

				response.setRutaArchivo(pathDefault + typeProcess);
				response.setNombreArchivoValidado(nombreArchivo);
				response.setCodigoStatus(status);
				return response;

			}

		} catch (Exception e) {
			throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "Error interno del servicio", "E500",
					HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), "xtfValidatorRdm");
		}
	}
}
