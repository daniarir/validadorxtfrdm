package co.gov.igac.snc.structureXtf.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.exception.ExcepcionInterruptedException;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

public interface ValidateStructureXtfService {
	public CompletableFuture<ResponseArchivoDto> validarXtf(String rutaAzureDownload, String nombreArchivo,
			String origen) throws ExcepcionesDeNegocio, ExcepcionLecturaDeArchivo, InterruptedException,
			ExecutionException, ExcepcionPropertiesNoExiste, ExcepcionInterruptedException, ExcepcionPropertiesNoExiste;
}
