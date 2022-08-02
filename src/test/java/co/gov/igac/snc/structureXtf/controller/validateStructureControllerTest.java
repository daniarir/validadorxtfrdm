package co.gov.igac.snc.structureXtf.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import co.gov.igac.snc.structureXtf.dto.Data;
import co.gov.igac.snc.structureXtf.dto.RequestArchivoDTO;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDto;
import co.gov.igac.snc.structureXtf.dto.ResponseArchivoDtoKafka;
import co.gov.igac.snc.structureXtf.service.validateStructureXtfService;
import co.gov.igac.snc.structureXtf.service.impl.KafkaProducerService;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class validateStructureControllerTest {
	
	@MockBean
	private KafkaProducerService mensajeKafka;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	final void testValidateStructureXtf() {

		try {
			
			RequestArchivoDTO dto = new RequestArchivoDTO();
			dto.setNombreArchivo("procesoRDM/XTF/SNR");
			dto.setOrigen("SNR");
			dto.setRutaArchivo("SNR_05001_08072022.xtf");
			
			ResponseArchivoDto response = new ResponseArchivoDto();
			response.setCodigoStatus("1");
			response.setNombreArchivo("SNR_05001_08072022.xtf");
			response.setOrigen("SNR");
			response.setRutaArchivo("procesoRDM/XTF/SNR/Procesados");
					
			Data data = new Data();
			data.setNombreArchivo("prueba.xtf");
			data.setOrigen("SNR");
			data.setRutaArchivo("procesoRDM/XTF/SNR/Procesados");
			
			validateStructureXtfService service = Mockito.spy(validateStructureXtfService.class);
			when(service.validarXtf("procesoRDM/XTF/SNR","SNR_05001_08072022.xtf","SNR")).thenReturn(response);
			doNothing().when(mensajeKafka).send(new ResponseArchivoDtoKafka("OK", data));
			
			assertEquals(response, service.validarXtf("procesoRDM/XTF/SNR","SNR_05001_08072022.xtf","SNR"));	
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}