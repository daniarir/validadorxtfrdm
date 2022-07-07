package co.gov.igac.snc.structureXtf.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.interlis2.validator.Validator;

import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;
import co.gov.igac.snc.structureXtf.util.Utilidades;

import org.json.*;

public class iliValidator {

	public Settings configIliValidator(String iliDirs, String modelNames, File pathLog) throws AplicacionEstandarDeExcepciones {

		try {

			String path = pathLog.getPath().replace(".json", ".log");

//			Config IliValidator
			Settings settingIli = new Settings();
			settingIli.setValue(Validator.SETTING_ILIDIRS, iliDirs);
			settingIli.setValue(Validator.SETTING_MODELNAMES, modelNames);
			settingIli.setValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE, Validator.TRUE);

			settingIli.setValue(Validator.SETTING_LOGFILE, path);

			return settingIli;

		} catch (Exception e) {
			throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "Ilivalidator", "E500",
					"500 - Error interno del servicio",
					"Error al configurar los settings de la libreria iliValidator" + e.getMessage(), "iliValidator");
		}
	}

	public void configLogIlivalidator(Boolean validate, File pathLogJson, String nombreArchivo, String origen, String urlUpload) throws AplicacionEstandarDeExcepciones {

		// El log que deja la libreria IliValidator, el archivo .LOG no se puede
		// manipular mientras el .JAR de iliValidator se esta ejecutando,
		// Por lo tanto es imposible Combinar o eliminar el archivo .LOG

		String pathLog = pathLogJson.getPath().replace(".json", ".log");
		Map<String, String> peticionSubirArchivo = new HashMap<>();
		String[] pathSplitNombreArchivo = nombreArchivo.split("_");
		String[] pathSplit = pathLogJson.getPath().split("\\\\");
		String pathFinish = pathSplit[0] + "\\" + pathSplit[1] + "\\" + pathSplitNombreArchivo[1] + "_"
				+ pathSplitNombreArchivo[2].replace(".xtf", "_ValidacionXTF.json");
		String nombreArchivoStorage = pathSplitNombreArchivo[1] + "_"
				+ pathSplitNombreArchivo[2].replace(".xtf", "_ValidacionXTF.json");
		String rutaStorage = "procesoRDM/JSON/";

		File doc = new File(pathLog);
		try (BufferedReader obj = new BufferedReader(new FileReader(doc))) {
			String strng;

			Pattern paDatafile = Pattern.compile("dataFile");
			Pattern paObjectInClass = Pattern.compile("objects in CLASS");
			Pattern paError = Pattern.compile("Error:");

			JSONObject objectLog = new JSONObject();
			JSONObject data = new JSONObject();
			JSONArray arrayLog = new JSONArray();
			JSONArray arrayErrorLog = new JSONArray();
			JSONArray arrayErrorLogGeneral = new JSONArray();
			Map<String, String> objectClass = new HashMap<>();
			Map<String, String> Error = new HashMap<>();
			Map<String, String> ErrorGeneral = new HashMap<>();

			while ((strng = obj.readLine()) != null) {

				Matcher maDataFile = paDatafile.matcher(strng);
				Matcher maObjectInClass = paObjectInClass.matcher(strng);
				Matcher maError = paError.matcher(strng);

				if (maDataFile.find()) {
					String[] datafile = strng.split(" ");
					objectLog.put("DataFile", datafile[2].replace("<", "").replace(">", ""));
				}

				if (maObjectInClass.find()) {

					String[] data1 = strng.split("CLASS");
					String objectValidate = data1[0].substring(5);
					String[] cantidad = objectValidate.split("objects");
					cantidad[0] = cantidad[0].replace(" ", "").trim().substring(1);
					String objectName = data1[1].replace(" ", "");

					objectClass.put("nombreClase", objectName);
					objectClass.put("cantidadObjectosValidados", cantidad[0]);
					arrayLog.put(objectClass);
					objectLog.put("ObjectosClase", arrayLog);
				}

				if (!validate) {
					if (maError.find()) {

						String[] dataError = strng.split(":");
						String[] lineaError = dataError[1].split(" ");
						String[] tidError = dataError[3].split(" ");

						if (!lineaError[1].equals("line")) {

							ErrorGeneral.put("Mensaje", strng);
							arrayErrorLogGeneral.put(ErrorGeneral);
							objectLog.put("ErrorLogGeneral", arrayErrorLogGeneral);

						} else {

							Error.put("Linea", lineaError[2]);
							Error.put("Clase", dataError[2].trim());
							Error.put("TID", tidError[2].trim());
							Error.put("MensajeError", strng);

							arrayErrorLog.put(Error);
							objectLog.put("ErrorLog", arrayErrorLog);
						}
						data.put("Validation", objectLog);
					}
				}

				if (validate) {
					if (!maError.find()) {
						data.put("Validation", objectLog);
					}
				}
			}

			FileWriter file = new FileWriter(pathFinish);
			file.write(data.toString());
			file.flush();
			file.close();

			if (origen.equals("SNR")) {
				peticionSubirArchivo.put("rutaArchivo", pathFinish);
				peticionSubirArchivo.put("rutaStorage", rutaStorage + origen + "/" + pathSplitNombreArchivo[1]);
				peticionSubirArchivo.put("nombreArchivo", nombreArchivoStorage);
			} else {
				peticionSubirArchivo.put("rutaArchivo", pathFinish);
				peticionSubirArchivo.put("rutaStorage", rutaStorage + origen + "/" + pathSplitNombreArchivo[1]);
				peticionSubirArchivo.put("nombreArchivo", nombreArchivoStorage);
			}

			try {
				Utilidades.consumirApiValidacionXTF(peticionSubirArchivo, urlUpload);
			} catch (Exception e) {
				throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "Ilivalidator", "E500",
						"500 - Error interno del servicio",
						"Error al configurar el log en formato JSON " + e.getMessage(), "iliValidator");
			}
		} catch (Exception e) {
			throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm", "Ilivalidator", "E500",
					"500 - Error interno del servicio", "Error al configurar el log en formato JSON " + e.getMessage(),
					"iliValidator");
		}
	}
}
