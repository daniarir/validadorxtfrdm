package co.gov.igac.snc.structureXtf.commons;

import org.interlis2.validator.Validator;

import ch.ehi.basics.settings.Settings;
import co.gov.igac.snc.structureXtf.util.PropertiesConfig;

public class iliValidator {
	
	private final PropertiesConfig prop = new PropertiesConfig();
	
	public Settings configIliValidator() {
		
//		Config IliValidator
		Settings settingIli = new Settings();
		settingIli.setValue(Validator.SETTING_ILIDIRS, prop.Properties().getProperty("co.gov.igac.ilivalidator.iliDirs"));
		settingIli.setValue(Validator.SETTING_MODELNAMES, prop.Properties().getProperty("co.gov.igac.ilivalidator.modelNames"));
		settingIli.setValue(Validator.SETTING_ALL_OBJECTS_ACCESSIBLE, Validator.TRUE);
		
		return settingIli;
	}
}
