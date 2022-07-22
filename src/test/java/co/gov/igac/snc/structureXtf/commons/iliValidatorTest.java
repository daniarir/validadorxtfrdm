package co.gov.igac.snc.structureXtf.commons;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.File;

import org.interlis2.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.util.Propiedades;

@SpringBootTest
@PropertySource("classpath:validatrStructureXtf.properties")
@WebAppConfiguration
class iliValidatorTest {

	@Autowired
	private Propiedades prop;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	final void testConfigIliValidator() {
		try {
			
			assertEquals("C:\\xlogsss\\logfile.json", prop.getPathLogJSON().toString());
			assertEquals("C:\\modelos", prop.getIliDirs());
			assertEquals("Modelo_Aplicacion_LADMCOL_Lev_Cat_V1_0", prop.getModelNames());
			
			String iliDirs = prop.getIliDirs();
			String modelNames = prop.getModelNames();
			File pathLog = prop.getPathLogJSON();

			Settings set = new Settings();
			set.setValue(Validator.SETTING_ILIDIRS, iliDirs);
			set.setValue(Validator.SETTING_MODELNAMES, modelNames);
			set.setValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE, Validator.TRUE);
			String path = pathLog.getPath().replace(".json", ".log");
			set.setValue(Validator.SETTING_LOGFILE, path);
			
			iliValidator service = Mockito.mock(iliValidator.class);
			when(service.configIliValidator(iliDirs, modelNames, pathLog)).thenReturn(set);

			assertNotNull(set);
			assertEquals("Settings{org.interlis2.validator.modelNames=Modelo_Aplicacion_LADMCOL_Lev_Cat_V1_0;"+
						 "org.interlis2.validator.ilidirs=C:\\modelos;"+
					     "org.interlis2.validator.log=C:\\xlogsss\\logfile.log;org.interlis2.validator.allobjectsaccessible=true}", 
						 set.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
