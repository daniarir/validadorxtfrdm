package co.gov.igac.snc.structureXtf.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDTO;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

public interface IvalidateStructureXtfService {
	public ResponseArchivoDTO validarXtf(String rutaAzureDownload, String nombreArchivo, String origen) throws ExcepcionPropertiesNoExiste, ExcepcionLecturaDeArchivo, ExcepcionesDeNegocio, TransformerException, ParserConfigurationException, SAXException, IOException, InterruptedException;
}
