package co.gov.igac.snc.structureXtf.controller;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.gov.igac.snc.structureXtf.dto.RequestArchivoDTO;
import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;
import co.gov.igac.snc.structureXtf.exception.ExcepcionPropertiesNoExiste;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;
import co.gov.igac.snc.structureXtf.service.ValidateStructureXtfService;

@RestController
@RequestMapping("/xtfValidatorRdm")
public class ValidateStructureXtfController {

	@Autowired
	ValidateStructureXtfService service;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> validateStructureXtf(@RequestBody RequestArchivoDTO data) throws ExcepcionesDeNegocio, ExcepcionLecturaDeArchivo, InterruptedException, ExecutionException, ExcepcionPropertiesNoExiste {
		service.validarXtf(data.getRutaArchivo(), data.getNombreArchivo(), data.getOrigen());
		return ResponseEntity.accepted().body(Map.of("Accepted","202"));
	}
}
