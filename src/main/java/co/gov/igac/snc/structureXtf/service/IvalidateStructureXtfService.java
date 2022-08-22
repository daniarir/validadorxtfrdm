package co.gov.igac.snc.structureXtf.service;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDTO;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

public interface IvalidateStructureXtfService {
	public ResponseArchivoDTO validarXtf(String rutaAzureDownload, String nombreArchivo, String origen)  throws ExcepcionPropertiesNoExiste, ExcepcionLecturaDeArchivo, ExcepcionesDeNegocio;
}
