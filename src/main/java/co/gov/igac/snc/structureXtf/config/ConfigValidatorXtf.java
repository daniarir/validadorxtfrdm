package co.gov.igac.snc.structureXtf.config;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.http.HttpStatus;

import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

public class ConfigValidatorXtf {

	public static void ajustesBugsValidatorXTF(String filePath) throws ExcepcionesDeNegocio {

		String fuenteCabidaLinderosString = "";
		String fuenteDerechoString = "";
		String snrTitularString = "";
		String snrPredioRegistroString = "";
		String titularDerechoString = "";
		String derechoString = "";
		String Restriccion = "";
		String datosSNR = "";
		Boolean valida = false;
		Boolean validaFuenteCabidaLinderosString = false;
		Boolean validaFuenteDerechoString = false;
		Boolean validaSnrTitularString = false;
		Boolean validaSnrPredioRegistroString = false;
		Boolean validaTitularDerechoString = false;
		Boolean validaDerechoString = false;
		Boolean validaRestriccion = false;

		try {

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			FileInputStream fileStream = new FileInputStream(filePath);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(fileStream);
			
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = new Document();
			Element rootElement = null;
			List<Element> datosSNRvalida = null;

			while (eventReader.hasNext()) {
				XMLEvent nextEvent;
				nextEvent = eventReader.nextEvent();

				if (nextEvent.isStartElement()) {
					StartElement startElement = nextEvent.asStartElement();

					if (startElement.getName().getLocalPart().equals("Submodelo_Insumos_SNR_V2_0.Datos_SNR")) {
						datosSNR = "Submodelo_Insumos_SNR_V2_0.Datos_SNR";
						fuenteCabidaLinderosString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_FuenteCabidaLinderos";
						fuenteDerechoString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_FuenteDerecho";
						snrTitularString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_Titular";
						snrPredioRegistroString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_PredioRegistro";
						titularDerechoString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.snr_titular_derecho";
						derechoString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_Derecho";
						Restriccion = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_Restriccion";

					} else {
						datosSNR = "Submodelo_Insumos_SNR_V1_0.Datos_SNR";
						fuenteCabidaLinderosString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_FuenteCabidaLinderos";
						fuenteDerechoString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_FuenteDerecho";
						snrTitularString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_Titular";
						snrPredioRegistroString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_PredioRegistro";
						titularDerechoString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.snr_titular_derecho";
						derechoString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_Derecho";
						Restriccion = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_Restriccion";
					}

					if (startElement.getName().getLocalPart().equals("Submodelo_Insumos_SNR_V2_0.Datos_SNR") || startElement.getName().getLocalPart().equals("Submodelo_Insumos_SNR_V1_0.Datos_SNR")) {
	
						valida = true;
						document = saxBuilder.build(new FileInputStream(filePath));
						rootElement = document.getRootElement();
						datosSNRvalida = rootElement.getChildren();
						
						// Eliminar tags de Matricula_Inmobiliaria_Matriz
						for (Element staff : datosSNRvalida) {

							List<Element> prueba = staff.getChildren();
							for (Element staff2 : prueba) {

								List<Element> prueba2 = staff2.getChildren();
								for (int i = 0; i < prueba2.size(); i++) {

									Element studentElement = prueba2.get(i);
									if (studentElement.getName().equals(snrPredioRegistroString)) {

										List<Element> hijos = studentElement.getChildren();
										for (int j = 0; j < hijos.size(); j++) {
											Element hijos1 = hijos.get(j);

											if (hijos1.getName().equals("Matricula_Inmobiliaria_Matriz")) {
												hijos1.getParentElement().removeContent(j);
											}
										}
									}
								}
							}
						}
						
						for (Element elementData : datosSNRvalida) {
							List<Element> agregarData = elementData.getChildren();
							for (Element datosSNRr : agregarData) {
								
								List<Element> clases = datosSNRr.getChildren();
								for (int i = 0; i < clases.size(); i++) {
									Element e = clases.get(i);
									if (e.getName().equals(fuenteCabidaLinderosString)) {
										validaFuenteCabidaLinderosString = true;
									}
									
									if (e.getName().equals(fuenteDerechoString)) {
										validaFuenteDerechoString = true;
									}
									
									if (e.getName().equals(snrTitularString)) {
										validaSnrTitularString = true;
									}

									if (e.getName().equals(snrPredioRegistroString)) {
										validaSnrPredioRegistroString = true;
									}

									if (e.getName().equals(titularDerechoString)) {
										validaTitularDerechoString = true;
									}

									if (e.getName().equals(derechoString)) {
										validaDerechoString = true;
									}

									if (e.getName().equals(Restriccion)) {
										validaRestriccion = true;
									}
								}
							}
						}

						for (Element studentElement : datosSNRvalida) {
							List<Element> agregarElementos = studentElement.getChildren();
							for (Element datosSNRr : agregarElementos) {

								if (datosSNRr.getName().equals(datosSNR)) {

									if (validaFuenteCabidaLinderosString) {

										// NODE SNR_FUENTE_CABIDA_LINDEROS
										Element newnodeElementFuenteCabidaLinderos = new Element(
												fuenteCabidaLinderosString);
										// Atributos del nodo SNR_FuenteCabidaLinderos
										Attribute attributeFuenteCabidaLinderos = new Attribute("TID", "Dummy1");
										newnodeElementFuenteCabidaLinderos.setAttribute(attributeFuenteCabidaLinderos);

										Element tipoDocumentoFuentecabidaLinderos = new Element("Tipo_Documento");
										tipoDocumentoFuentecabidaLinderos.setText("Sentencia_Judicial");

										Element numeroDocumentoFuentecabidaLinderos = new Element("Numero_Documento");
										numeroDocumentoFuentecabidaLinderos.setText("0000");

										Element fechaDocumentoFuentecabidaLinderos = new Element("Fecha_Documento");
										fechaDocumentoFuentecabidaLinderos.setText("1987-02-13");

										Element enteEmisorFuentecabidaLinderos = new Element("Ente_Emisor");
										enteEmisorFuentecabidaLinderos.setText("NOTARIA CUARTA");

										Element ciudadEmisoraFuentecabidaLinderos = new Element("Ciudad_Emisora");
										ciudadEmisoraFuentecabidaLinderos.setText("Dummy");

										// Se agregar elementos al tag de fuente cabida linderos
										newnodeElementFuenteCabidaLinderos.addContent(tipoDocumentoFuentecabidaLinderos);
										newnodeElementFuenteCabidaLinderos.addContent(numeroDocumentoFuentecabidaLinderos);
										newnodeElementFuenteCabidaLinderos.addContent(enteEmisorFuentecabidaLinderos);
										newnodeElementFuenteCabidaLinderos.addContent(ciudadEmisoraFuentecabidaLinderos);

										// Se agrega todos los tags formado a la rama principal
										datosSNRr.addContent(0, newnodeElementFuenteCabidaLinderos);

									}

									if (validaFuenteDerechoString) {

										// NODE FUENTE_DERECHO
										Element newNodeFuenteDerecho = new Element(fuenteDerechoString);
										// Atributos del nodo FUENTE_DERECHO
										Attribute attributeFuenteDerecho = new Attribute("TID", "Dummy2");
										newNodeFuenteDerecho.setAttribute(attributeFuenteDerecho);

										Element tipoDocumentoFuenteDerecho = new Element("Tipo_Documento");
										tipoDocumentoFuenteDerecho.setText("Escritura_Publica");

										Element numeroDocumentoFuenteDerecho = new Element("Numero_Documento");
										numeroDocumentoFuenteDerecho.setText("0000");

										Element fechaDocumentoFuenteDerecho = new Element("Fecha_Documento");
										fechaDocumentoFuenteDerecho.setText("2014-04-04");

										Element enteEmisorFuenteDerecho = new Element("Ente_Emisor");
										enteEmisorFuenteDerecho.setText("NOTARIA SESENTA");

										Element ciudadEmisoraFuenteDerecho = new Element("Ciudad_Emisora");
										ciudadEmisoraFuenteDerecho.setText("Dummy");

										newNodeFuenteDerecho.addContent(tipoDocumentoFuenteDerecho);
										newNodeFuenteDerecho.addContent(numeroDocumentoFuenteDerecho);
										newNodeFuenteDerecho.addContent(fechaDocumentoFuenteDerecho);
										newNodeFuenteDerecho.addContent(enteEmisorFuenteDerecho);
										newNodeFuenteDerecho.addContent(ciudadEmisoraFuenteDerecho);
										datosSNRr.addContent(0, newNodeFuenteDerecho);

									}

									if (validaSnrTitularString) {

										// NODE SNR_TITULAR
										Element newNodeSNR_Titular = new Element(snrTitularString);
										// Atributos del nodo SNR_Titular
										Attribute attributeSNR_Titular = new Attribute("TID", "Dummy3");
										newNodeSNR_Titular.setAttribute(attributeSNR_Titular);

										Element tipoPersonaTitular = new Element("Tipo_Documento");
										tipoPersonaTitular.setText("Persona_Natural");

										Element tipoDocumentoTitular = new Element("Tipo_Documento");
										tipoDocumentoTitular.setText("Cedula_Ciudadania");

										Element numeroDocumentoTitular = new Element("Numero_Documento");
										numeroDocumentoTitular.setText("070-200532101");

										Element nombreTitular = new Element("Nombres");
										nombreTitular.setText("Dummy");

										Element primerApellidoTitular = new Element("Primer_Apellido");
										primerApellidoTitular.setText("Dummy");

										Element segundoApellidoTitular = new Element("Segundo_Apellido");
										segundoApellidoTitular.setText("Dummy");

										Element razonSocialTitular = new Element("Razon_Social");
										razonSocialTitular.setText("Dummy");

										newNodeSNR_Titular.addContent(tipoPersonaTitular);
										newNodeSNR_Titular.addContent(tipoDocumentoTitular);
										newNodeSNR_Titular.addContent(numeroDocumentoTitular);
										newNodeSNR_Titular.addContent(nombreTitular);
										newNodeSNR_Titular.addContent(primerApellidoTitular);
										newNodeSNR_Titular.addContent(segundoApellidoTitular);
										newNodeSNR_Titular.addContent(razonSocialTitular);
										datosSNRr.addContent(0, newNodeSNR_Titular);

									}

									if (validaSnrPredioRegistroString) {

										// NODE SNR_PREDIO_REGISTRO
										Element newNodeSNR_PredioRegistro = new Element(snrPredioRegistroString);
										// Atributos del nodo SNR_PredioRegistro
										Attribute attributeSNR_PredioRegistro = new Attribute("TID", "Dummy4");
										newNodeSNR_PredioRegistro.setAttribute(attributeSNR_PredioRegistro);

										Element codigoOripPredioRegistro = new Element("Codigo_ORIP");
										codigoOripPredioRegistro.setText("000");

										Element matricculaInmobiPredioRegistro = new Element("Matricula_Inmobiliaria");
										matricculaInmobiPredioRegistro.setText("0000");

										Element numeroPredialNPredioRegistro = new Element("Numero_Predial_Nuevo_en_FMI");
										numeroPredialNPredioRegistro.setText("15001010201640074903");

										Element numeroPredialAPredioRegistro = new Element(
												"Numero_Predial_Anterior_en_FMI");
										numeroPredialAPredioRegistro.setText("15001010201640074903");

										Element nomenclaturaPredioRegistro = new Element("Nomenclatura_Registro");
										nomenclaturaPredioRegistro.setText("Dummy");

										Element cabidaLinderosPredioRegistro = new Element("Cabida_Linderos");
										cabidaLinderosPredioRegistro.setText("Dummy");

										Element claseSueloPredioRegistro = new Element("Clase_Suelo_Registro");
										claseSueloPredioRegistro.setText("Urbano");

										Element fechaPredioRegistro = new Element("Fecha_Datos");
										fechaPredioRegistro.setText("2022-08-02");

										Element estadoPredioRegistro = new Element("Estado");
										estadoPredioRegistro.setText("Activo");

										Element matriculaOPredioRegistro = new Element("Matricula_Origen");
										matriculaOPredioRegistro.setText("SIN INFORMACION");

										Element matriculaDlPredioRegistro = new Element("Matricula_Destino");
										matriculaDlPredioRegistro.setText("SIN INFORMACION");

										Element FuenteCabidaPredioRegistro = new Element("snr_fuente_cabidalinderos");
										Attribute attributeFuenteCabidaPredioRegistro = new Attribute("REF", "3285");
										FuenteCabidaPredioRegistro.setAttribute(attributeFuenteCabidaPredioRegistro);

										newNodeSNR_PredioRegistro.addContent(codigoOripPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(matricculaInmobiPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(numeroPredialNPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(numeroPredialAPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(nomenclaturaPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(cabidaLinderosPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(claseSueloPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(fechaPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(estadoPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(matriculaOPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(matriculaDlPredioRegistro);
										newNodeSNR_PredioRegistro.addContent(FuenteCabidaPredioRegistro);
										datosSNRr.addContent(0, newNodeSNR_PredioRegistro);

									}

									if (validaTitularDerechoString) {

										// NODE SNR_TITULAR_DERECHO
										Element newNodeSNRTitularDerecho = new Element(titularDerechoString);
										// Etiquetas dentro del nodo snr_titular_derecho

										Element snrTitularDerecho = new Element("snr_titular");
										Attribute attributesnrTitularDerecho = new Attribute("REF", "Dummy3");
										snrTitularDerecho.setAttribute(attributesnrTitularDerecho);

										Element snrDerechorTitularDerecho = new Element("snr_derecho");
										Attribute attributesnrDerechorTitularDerecho = new Attribute("REF", "Dummy5");
										snrDerechorTitularDerecho.setAttribute(attributesnrDerechorTitularDerecho);

										Element porceParticipaTitularDerecho = new Element("Porcentaje_Participacion");
										porceParticipaTitularDerecho.setText("109%");

										newNodeSNRTitularDerecho.addContent(snrTitularDerecho);
										newNodeSNRTitularDerecho.addContent(snrDerechorTitularDerecho);
										newNodeSNRTitularDerecho.addContent(porceParticipaTitularDerecho);
										datosSNRr.addContent(0, newNodeSNRTitularDerecho);

									}

									if (validaDerechoString) {

										// NODE SNR_DERECHO
										Element newNodeSNRDerecho = new Element(derechoString);
										// Atributos del nodo SNR_DERECHO
										Attribute attributeSNRDerecho = new Attribute("TID", "Dummy5");
										newNodeSNRDerecho.setAttribute(attributeSNRDerecho);

										Element calidadDerechoSnrDerecho = new Element("Calidad_Derecho_Registro");
										calidadDerechoSnrDerecho.setText("Falsa_Tradicion");

										Element codigoNaturalezaJuridica = new Element("Codigo_Naturaleza_Juridica");
										codigoNaturalezaJuridica.setText("610");

										Element fuenteDerecho = new Element("snr_fuente_derecho");
										Attribute attributefuenteDerecho = new Attribute("REF", "Dummy2");
										fuenteDerecho.setAttribute(attributefuenteDerecho);

										Element predioRegistroDerecho = new Element("snr_predio_registro");
										Attribute attributepredioRegistroDerecho = new Attribute("REF", "Dummy4");
										predioRegistroDerecho.setAttribute(attributepredioRegistroDerecho);

										newNodeSNRDerecho.addContent(calidadDerechoSnrDerecho);
										newNodeSNRDerecho.addContent(codigoNaturalezaJuridica);
										newNodeSNRDerecho.addContent(fuenteDerecho);
										newNodeSNRDerecho.addContent(predioRegistroDerecho);
										datosSNRr.addContent(0, newNodeSNRDerecho);

									}

									if (validaRestriccion) {

										// NODE SNR_RESTRICCION
										Element newNodeSNRRestriccion = new Element(Restriccion);
										// Atributos del nodo SNR_RESTRICCION
										Attribute attributeSNRRestriccion = new Attribute("TID", "Dummy6");
										newNodeSNRRestriccion.setAttribute(attributeSNRRestriccion);

										Element codigoNaturalezaJuridicaRestriccion = new Element(
												"Codigo_Naturaleza_Juridica");
										codigoNaturalezaJuridicaRestriccion.setText("0474");

										newNodeSNRRestriccion.addContent(codigoNaturalezaJuridicaRestriccion);
										datosSNRr.addContent(0, newNodeSNRRestriccion);

									}
								}
							}
						}
					}
				}
			}

			if (valida) {
				FileWriter file = new FileWriter(filePath);
				Format format = Format.getCompactFormat();
				format.setEncoding("UTF-8");
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.output(document, file);
				file.close();
				eventReader.close();
			}else {
				fileStream.close();
				eventReader.close();
			}

		} catch (XMLStreamException | JDOMException | IOException e) {
			System.out.println(e.getMessage());
			throw new ExcepcionesDeNegocio("/ConfigValidatorXtf",
					"Se presento un error al momento de realizar ajustes BUGS para archivo XTF: " + filePath
							+ e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}