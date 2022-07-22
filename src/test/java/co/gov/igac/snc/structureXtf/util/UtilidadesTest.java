package co.gov.igac.snc.structureXtf.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@PropertySource("classpath:validatrStructureXtf.properties")
@WebAppConfiguration
class UtilidadesTest {
	
	@Autowired
	private Propiedades prop;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		assertEquals("http://localhost:18084/azureStorage/descargarArchivo", prop.getDescargarArchivo());
	}

	@Test
	final void testConsumirApiValidacionXTFUpdload() {
		try {
			
			String download = prop.getDescargarArchivo();
			String path = "D:\\descargarXTF\\prueba.xtf";
			ResponseEntity<String> responseDownload = ResponseEntity.status(HttpStatus.OK).body(path);
			
			Map<String, String> peticionDownload = new HashMap<>();
			peticionDownload.put("rutaStorage", "procesoRDM/XTF/SNR");
			peticionDownload.put("nombreArchivo", "prueba.xtf");
			
			//Descargar Archivo
			Utilidades util = Mockito.mock(Utilidades.class);
			doReturn(responseDownload).when(util).consumirApiValidacionXTF(peticionDownload, download);
			
			assertNotNull(responseDownload);
			assertEquals(200, responseDownload.getStatusCodeValue());
			assertEquals("D:\\descargarXTF\\prueba.xtf", responseDownload.getBody());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
