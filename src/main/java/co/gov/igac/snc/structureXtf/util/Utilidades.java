package co.gov.igac.snc.structureXtf.util;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import co.gov.igac.snc.structureXtf.exception.AplicacionEstandarDeExcepciones;
import reactor.core.publisher.Mono;

public class Utilidades {

	public static ResponseEntity<String> consumirApiValidacionXTF(Map<String, String> peticion, String urlApi) throws AplicacionEstandarDeExcepciones {
		
		ResponseEntity<String> response;
		
		try {
			
			WebClient webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

			response = webClient.method(HttpMethod.POST).uri(urlApi)
												.body(Mono.just(peticion), Map.class)
												.retrieve()
												.onStatus(HttpStatus::is4xxClientError, 
										        		clientResponse ->  clientResponse.bodyToMono(String.class)
										        		.map(body -> new AplicacionEstandarDeExcepciones(
										        				"/error/xtfValidatorRdm",
										        				"Error al consumir el servicio: " + urlApi,
										        				"E409",
										        				 "409 - Conflicto",
										        				 body,
										        				 "Util - Consumir Api"
										        				)))
												.toEntity(String.class)
												.block();
			return response;
		} catch (Exception e) {
			throw new AplicacionEstandarDeExcepciones("/error/xtfValidatorRdm",
					  								  "Servicio interno del servidor",
					  								  "E500",
					  								  "500 - Error interno del servicio", 
					  								  "La ruta o directorio especificado es incorrecto o el archivo no existe", 
					  								  "Util - Consumir Api");
		}
		
	}
}
