package co.gov.igac.snc.structureXtf.util;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;
import reactor.core.publisher.Mono;

public class Utilidades {

	public static ResponseEntity<String> consumirApiValidacionXTF(Map<String, String> peticion, String urlApi) throws ExcepcionesDeNegocio {
		
		ResponseEntity<String> response;

		WebClient webClient = WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

		response = webClient.method(HttpMethod.POST).uri(urlApi).body(Mono.just(peticion), Map.class).retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class)
						.map(body -> new ExcepcionesDeNegocio(body, "Error al consumir servicio " + urlApi,
								HttpStatus.CONFLICT)))
				.onStatus(HttpStatus::is5xxServerError,
						clientResponse -> clientResponse.bodyToMono(String.class)
								.map(body -> new ExcepcionesDeNegocio(body, "Error al consumir servicio " + urlApi,
										HttpStatus.CONFLICT)))
				.toEntity(String.class).block();
		return response;
		
	}
}
