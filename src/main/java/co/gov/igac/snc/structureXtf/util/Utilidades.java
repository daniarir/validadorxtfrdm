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

	public static ResponseEntity<?> consumirApi(Map<String, String> peticion, String urlApi) throws AplicacionEstandarDeExcepciones {
		try {
			
			WebClient webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

			System.out.println("webClient: " + webClient);

			ResponseEntity<?> response = webClient.method(HttpMethod.POST).uri(urlApi)
												.body(Mono.just(peticion), Map.class)
												.retrieve()
												.onStatus(HttpStatus::is4xxClientError, 
										        		clientResponse ->  clientResponse.bodyToMono(String.class)
										        		.map(body -> new AplicacionEstandarDeExcepciones(body,"Error al consumir servicio " + urlApi, HttpStatus.CONFLICT)))
												.toEntity(String.class)
												.block();
			
			System.out.println("respuesta: " + response);

			return response;

		} catch (Exception e) {
			throw new AplicacionEstandarDeExcepciones("ClientService",
					  								  "Error al consumir el servicio: " + urlApi,
					  								  "E409",
					  								  HttpStatus.CONFLICT.toString(), 
					  								  "La ruta o directorio especificado es incorrecto: " + e.getMessage(), 
					  								  "/clientConsumeService");
		}
	}

}
