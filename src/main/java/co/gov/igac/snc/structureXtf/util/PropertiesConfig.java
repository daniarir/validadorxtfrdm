package co.gov.igac.snc.structureXtf.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig {

	public Properties Properties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader("C:\\properties\\properties.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
