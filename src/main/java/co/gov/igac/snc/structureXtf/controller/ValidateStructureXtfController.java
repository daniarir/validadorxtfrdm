package co.gov.igac.snc.structureXtf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.gov.igac.snc.structureXtf.dto.RequestArchivoDTO;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.dto.ResponseErrorDTO;
import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;
import co.gov.igac.snc.structureXtf.service.validateStructureXtfService;


@RestController
@RequestMapping("/xtfValidatorRdm")
public class ValidateStructureXtfController {
	
	@Autowired
	validateStructureXtfService service;
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> validateStructureXtf(@RequestBody RequestArchivoDTO data) {
		
		try {
			ResponseArchivoDto dto = service.validarXtf(data.getRutaArchivo(), data.getNombreArchivo(), data.getOrigen());
			return new ResponseEntity<>(dto,HttpStatus.OK);
		} catch (AplicacionEstandarDeExcepciones ex) {
			return new ResponseEntity<>(new ResponseErrorDTO(ex),HttpStatus.OK);
		}
	}
}
