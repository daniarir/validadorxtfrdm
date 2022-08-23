package co.gov.igac.snc.structureXtf.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import co.gov.igac.snc.structureXtf.dto.DataDTO;
import co.gov.igac.snc.structureXtf.dto.RequestArchivoDTO;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDTO;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDTOKafka;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;
import co.gov.igac.snc.structureXtf.service.IvalidateStructureXtfService;
import co.gov.igac.snc.structureXtf.service.impl.KafkaProducerServiceImpl;

@RestController
@RequestMapping("/xtfValidatorRdm")
public class ValidateStructureXtfController {

	@Autowired
	IvalidateStructureXtfService service;

	@Autowired
	KafkaProducerServiceImpl kafkaProducer;

	@Value("${usaKafka}")
	private Boolean usaKafka;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> validateStructureXtf(@RequestBody RequestArchivoDTO data) throws ExcepcionPropertiesNoExiste, ExcepcionLecturaDeArchivo, ExcepcionesDeNegocio, TransformerException, ParserConfigurationException, SAXException, IOException, InterruptedException {

		ResponseArchivoDTO dto = service.validarXtf(data.getRutaArchivo(), data.getNombreArchivo(), data.getOrigen());

		if (usaKafka) {
			if (dto.getCodigoStatus().equals("1") || dto.getCodigoStatus().equals("0")) {
				ResponseArchivoDTOKafka dtoKafka = new ResponseArchivoDTOKafka();
				DataDTO dataKafka = new DataDTO();
				dataKafka.setRutaArchivo(dto.getRutaArchivo());
				dataKafka.setNombreArchivo(dto.getNombreArchivo());
				dataKafka.setOrigen(dto.getOrigen());
				dtoKafka.setKey("OK");
				dtoKafka.setJson(dataKafka);
				kafkaProducer.send(dtoKafka);
			}
		}

		System.out.println("fin - validateStructureXtf - OK");
		return new ResponseEntity<>(dto, HttpStatus.OK);

	}
}
