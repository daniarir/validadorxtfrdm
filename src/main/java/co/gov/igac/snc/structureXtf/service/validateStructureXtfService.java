package co.gov.igac.snc.structureXtf.service;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;

public interface validateStructureXtfService {
	public ResponseArchivoDto validarXtf(String rutaAzureDownload, String nombreArchivo, String origen)  throws AplicacionEstandarDeExcepciones;
}
