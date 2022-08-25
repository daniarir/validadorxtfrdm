package co.gov.igac.snc.structureXtf.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.http.HttpStatus;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import co.gov.igac.snc.structureXtf.exception.ExcepcionLecturaDeArchivo;

public class ConfigValidatorXtf {

	public static void ajustesBugsValidatorXTF(String filePath)
			throws TransformerException, ParserConfigurationException, SAXException, IOException, InterruptedException {

		String fuenteCabidaLinderosString = "";
		String fuenteDerechoString = "";
		String snrTitularString = "";
		String snrPredioRegistroString = "";
		String titularDerechoString = "";
		String derechoString = "";
		String restriccion = "";
		String datosSNR = "";
		Integer version = 0;

		// 1. Cree un objeto Archivo y asigne el archivo XML
		File file = new File(filePath);

		try {
			Thread.sleep(10000);

			// 2. Cree un objeto DocumentBuilderFactory para crear un objeto DocumentBuilder
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

			// 3. Cree un objeto DocumentBuilder para convertir archivos XML en objetos
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			// 4. Cree un objeto de documento y analice el archivo XML
			Document document = documentBuilder.parse(file);
			document.getDocumentElement().normalize();

			Node nDataSection = document.getElementsByTagName("DATASECTION").item(0);
			NodeList childDatosSNR = nDataSection.getChildNodes();

			if (childDatosSNR.item(1).getNodeName().equals("Submodelo_Insumos_SNR_V2_0.Datos_SNR") || 
				childDatosSNR.item(1).getNodeName().equals("Submodelo_Insumos_SNR_V1_0.Datos_SNR")) {

				if (childDatosSNR.item(1).getNodeName().equals("Submodelo_Insumos_SNR_V2_0.Datos_SNR")) {
					datosSNR = "Submodelo_Insumos_SNR_V2_0.Datos_SNR";
				} else {
					datosSNR = "Submodelo_Insumos_SNR_V1_0.Datos_SNR";
				}

				Node nDatosSNR = document.getElementsByTagName(datosSNR).item(0);
				NodeList childNodes = nDatosSNR.getChildNodes();

				if (nDatosSNR.getNodeName().equals("Submodelo_Insumos_SNR_V2_0.Datos_SNR")) {
					version = 2;
					fuenteCabidaLinderosString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_FuenteCabidaLinderos";
					fuenteDerechoString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_FuenteDerecho";
					snrTitularString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_Titular";
					snrPredioRegistroString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_PredioRegistro";
					titularDerechoString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.snr_titular_derecho";
					derechoString = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_Derecho";
					restriccion = "Submodelo_Insumos_SNR_V2_0.Datos_SNR.SNR_Restriccion";
				} else {
					version = 1;
					fuenteCabidaLinderosString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_FuenteCabidaLinderos";
					fuenteDerechoString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_FuenteDerecho";
					snrTitularString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_Titular";
					snrPredioRegistroString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_PredioRegistro";
					titularDerechoString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.snr_titular_derecho";
					derechoString = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_Derecho";
					restriccion = "Submodelo_Insumos_SNR_V1_0.Datos_SNR.SNR_Restriccion";
				}

				// Remover nodo
				for (int i = 0; i < childNodes.getLength(); i++) {
					if (childNodes.item(i).getNodeName().equals(snrPredioRegistroString)) {
						NodeList hijos = childNodes.item(i).getChildNodes();
						for (int j = 0; j < hijos.getLength(); j++) {
							if (hijos.item(j).getNodeName().equals("Matricula_Inmobiliaria_Matriz")) {
								Element pt = (Element) hijos.item(j);
								pt.getParentNode().removeChild(pt);
							}
						}
					}
				}

				// NODE SNR_FUENTE_CABIDA_LINDEROS
				Node newNodeFuenteCabidaLinderos = nDatosSNR.getOwnerDocument()
						.createElement(fuenteCabidaLinderosString);
				// Atributos del nodo SNR_FuenteCabidaLinderos
				Element newnodeElementFuenteCabidaLinderos = (Element) newNodeFuenteCabidaLinderos;
				Attr attributeFuenteCabidaLinderos = document.createAttribute("TID");
				attributeFuenteCabidaLinderos.setValue("Dummy1");
				newnodeElementFuenteCabidaLinderos.setAttributeNode(attributeFuenteCabidaLinderos);

				Element tipoDocumentoFuentecabidaLinderos = document.createElement("Tipo_Documento");
				Element numeroDocumentoFuentecabidaLinderos = document.createElement("Numero_Documento");
				Element fechaDocumentoFuentecabidaLinderos = document.createElement("Fecha_Documento");
				Element enteEmisorFuentecabidaLinderos = document.createElement("Ente_Emisor");
				Element ciudadEmisoraFuentecabidaLinderos = document.createElement("Ciudad_Emisora");
				// Element archivoFuentecabidaLinderos = document.createElement("Archivo"); --
				// Validar cuando venga un XTF
				// Validar cuando venga un XTF con este proceso
				// Etiquetas dentro del nodo SNR_FuenteCabidaLinderos
				tipoDocumentoFuentecabidaLinderos.setTextContent("Sentencia_Judicial");
				numeroDocumentoFuentecabidaLinderos.setTextContent("0000");
				fechaDocumentoFuentecabidaLinderos.setTextContent("1987-02-13");
				enteEmisorFuentecabidaLinderos.setTextContent("NOTARIA CUARTA");
				ciudadEmisoraFuentecabidaLinderos.setTextContent("Dummy");
				// archivoFuentecabidaLinderos.setTextContent("Dummy");
				newNodeFuenteCabidaLinderos.appendChild(tipoDocumentoFuentecabidaLinderos);
				newNodeFuenteCabidaLinderos.appendChild(numeroDocumentoFuentecabidaLinderos);
				newNodeFuenteCabidaLinderos.appendChild(fechaDocumentoFuentecabidaLinderos);
				newNodeFuenteCabidaLinderos.appendChild(enteEmisorFuentecabidaLinderos);
				newNodeFuenteCabidaLinderos.appendChild(ciudadEmisoraFuentecabidaLinderos);
				// newNodeFuenteCabidaLinderos.appendChild(archivoFuentecabidaLinderos);

				// NODE FUENTE_DERECHO
				Node newNodeFuenteDerecho = nDatosSNR.getOwnerDocument().createElement(fuenteDerechoString);
				// Atributos del nodo SNR_FuenteDerecho
				Element newnodeElementFuenteDerecho = (Element) newNodeFuenteDerecho;
				Attr attributeFuenteDerecho = document.createAttribute("TID");
				attributeFuenteDerecho.setValue("Dummy2");
				newnodeElementFuenteDerecho.setAttributeNode(attributeFuenteDerecho);

				Element tipoDocumentoFuenteDerecho = document.createElement("Tipo_Documento");
				Element numeroDocumentoFuenteDerecho = document.createElement("Numero_Documento");
				Element fechaDocumentoFuenteDerecho = document.createElement("Fecha_Documento");
				Element enteEmisorFuenteDerecho = document.createElement("Ente_Emisor");
				Element ciudadEmisoraFuenteDerecho = document.createElement("Ciudad_Emisora");
				// Etiquetas dentro del nodo SNR_FuenteDerecho
				tipoDocumentoFuenteDerecho.setTextContent("Escritura_Publica");
				numeroDocumentoFuenteDerecho.setTextContent("0000");
				fechaDocumentoFuenteDerecho.setTextContent("2014-04-04");
				enteEmisorFuenteDerecho.setTextContent("NOTARIA SESENTA");
				ciudadEmisoraFuenteDerecho.setTextContent("Dummy");
				newNodeFuenteDerecho.appendChild(tipoDocumentoFuenteDerecho);
				newNodeFuenteDerecho.appendChild(numeroDocumentoFuenteDerecho);
				newNodeFuenteDerecho.appendChild(fechaDocumentoFuenteDerecho);
				newNodeFuenteDerecho.appendChild(enteEmisorFuenteDerecho);
				newNodeFuenteDerecho.appendChild(ciudadEmisoraFuenteDerecho);

				// NODE SNR_TITULAR
				Node newNodeSNR_Titular = nDatosSNR.getOwnerDocument().createElement(snrTitularString);
				// Atributos del nodo SNR_Titular
				Element newnodeElementSNR_Titular = (Element) newNodeSNR_Titular;
				Attr attributeSNR_Titular = document.createAttribute("TID");
				attributeSNR_Titular.setValue("Dummy3");
				newnodeElementSNR_Titular.setAttributeNode(attributeSNR_Titular);

				Element tipoPersonaTitular = document.createElement("Tipo_Persona");
				Element tipoDocumentoTitular = document.createElement("Tipo_Documento");
				Element numeroDocumentoTitular = document.createElement("Numero_Documento");
				Element nombreTitular = document.createElement("Nombres");
				Element primerApellidoTitular = document.createElement("Primer_Apellido");
				Element segundoApellidoTitular = document.createElement("Segundo_Apellido");
				Element razonSocialTitular = document.createElement("Razon_Social");
				// Etiquetas dentro del nodo SNR_Titular
				tipoPersonaTitular.setTextContent("Persona_Natural");
				tipoDocumentoTitular.setTextContent("Cedula_Ciudadania");
				numeroDocumentoTitular.setTextContent("070-200532101");
				nombreTitular.setTextContent("Dummy");
				primerApellidoTitular.setTextContent("Dummy");
				segundoApellidoTitular.setTextContent("Dummy");
				razonSocialTitular.setTextContent("Dummy");
				newNodeSNR_Titular.appendChild(tipoPersonaTitular);
				newNodeSNR_Titular.appendChild(tipoDocumentoTitular);
				newNodeSNR_Titular.appendChild(numeroDocumentoTitular);
				newNodeSNR_Titular.appendChild(nombreTitular);
				newNodeSNR_Titular.appendChild(primerApellidoTitular);
				newNodeSNR_Titular.appendChild(segundoApellidoTitular);
				newNodeSNR_Titular.appendChild(razonSocialTitular);

				// NODE SNR_PREDIO_REGISTRO
				Node newNodeSNR_PredioRegistro = nDatosSNR.getOwnerDocument().createElement(snrPredioRegistroString);
				// Atributos del nodo SNR_PredioRegistro
				Element newnodeElementSNR_PredioRegistro = (Element) newNodeSNR_PredioRegistro;
				Attr attributeSNR_PredioRegistro = document.createAttribute("TID");
				attributeSNR_PredioRegistro.setValue("Dummy4");
				newnodeElementSNR_PredioRegistro.setAttributeNode(attributeSNR_PredioRegistro);

				Element codigoOripPredioRegistro = document.createElement("Codigo_ORIP");
				Element matricculaInmobiPredioRegistro = document.createElement("Matricula_Inmobiliaria");
				Element numeroPredialNPredioRegistro = document.createElement("Numero_Predial_Nuevo_en_FMI");
				Element numeroPredialAPredioRegistro = document.createElement("Numero_Predial_Anterior_en_FMI");
				Element nomenclaturaPredioRegistro = document.createElement("Nomenclatura_Registro");
				Element cabidaLinderosPredioRegistro = document.createElement("Cabida_Linderos");
				Element claseSueloPredioRegistro = document.createElement("Clase_Suelo_Registro");
				Element fechaPredioRegistro = document.createElement("Fecha_Datos");

				Element estadoPredioRegistro = null;
				Element matriculaOPredioRegistro = null;
				Element matriculaDlPredioRegistro = null;

				if (version.equals(2)) {
					estadoPredioRegistro = document.createElement("Estado");
					matriculaOPredioRegistro = document.createElement("Matricula_Origen");
					matriculaDlPredioRegistro = document.createElement("Matricula_Destino");
				}

				Element FuenteCabidaPredioRegistro = document.createElement("snr_fuente_cabidalinderos");
				Attr attributeFuenteCabidaPredioRegistro = document.createAttribute("REF");
				attributeFuenteCabidaPredioRegistro.setValue("3285");
				FuenteCabidaPredioRegistro.setAttributeNode(attributeFuenteCabidaPredioRegistro);

				// Etiquetas dentro del nodo SNR_PredioRegistro
				codigoOripPredioRegistro.setTextContent("000");
				matricculaInmobiPredioRegistro.setTextContent("0000");
				numeroPredialNPredioRegistro.setTextContent("15001010201640074903");
				numeroPredialAPredioRegistro.setTextContent("15001010201640074903");
				nomenclaturaPredioRegistro.setTextContent("Dummy");
				cabidaLinderosPredioRegistro.setTextContent("Dummy");
				claseSueloPredioRegistro.setTextContent("Urbano");
				fechaPredioRegistro.setTextContent("2022-08-02");
				if (version.equals(2)) {
					estadoPredioRegistro.setTextContent("Activo");
					matriculaOPredioRegistro.setTextContent("SIN INFORMACION");
					matriculaDlPredioRegistro.setTextContent("SIN INFORMACION");
				}
				newNodeSNR_PredioRegistro.appendChild(codigoOripPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(matricculaInmobiPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(numeroPredialNPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(numeroPredialAPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(nomenclaturaPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(cabidaLinderosPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(claseSueloPredioRegistro);
				newNodeSNR_PredioRegistro.appendChild(fechaPredioRegistro);
				if (version.equals(2)) {
					newNodeSNR_PredioRegistro.appendChild(estadoPredioRegistro);
					newNodeSNR_PredioRegistro.appendChild(matriculaOPredioRegistro);
					newNodeSNR_PredioRegistro.appendChild(matriculaDlPredioRegistro);
				}
				newNodeSNR_PredioRegistro.appendChild(FuenteCabidaPredioRegistro);

				// NODE SNR_TITULAR_DERECHO
				Node newNodeSNRTitularDerecho = nDatosSNR.getOwnerDocument().createElement(titularDerechoString);
				// Etiquetas dentro del nodo snr_titular_derecho
				Element snrTitularDerecho = document.createElement("snr_titular");
				Attr attributesnrTitularDerecho = document.createAttribute("REF");
				attributesnrTitularDerecho.setValue("Dummy3");
				snrTitularDerecho.setAttributeNode(attributesnrTitularDerecho);
				Element snrDerechorTitularDerecho = document.createElement("snr_derecho");
				Attr attributesnrDerechorTitularDerecho = document.createAttribute("REF");
				attributesnrDerechorTitularDerecho.setValue("Dummy5");
				snrDerechorTitularDerecho.setAttributeNode(attributesnrDerechorTitularDerecho);
				Element porceParticipaTitularDerecho = document.createElement("Porcentaje_Participacion");
				porceParticipaTitularDerecho.setTextContent("109%");
				newNodeSNRTitularDerecho.appendChild(snrTitularDerecho);
				newNodeSNRTitularDerecho.appendChild(snrDerechorTitularDerecho);
				newNodeSNRTitularDerecho.appendChild(porceParticipaTitularDerecho);

				// NODE SNR_DERECHO
				Node newNodeSNRDerecho = nDatosSNR.getOwnerDocument().createElement(derechoString);
				// Atributos del nodo SNR_DERECHO
				Element newnodeElementSNRDerecho = (Element) newNodeSNRDerecho;
				Attr attributeSNRDerecho = document.createAttribute("TID");
				attributeSNRDerecho.setValue("Dummy5");
				newnodeElementSNRDerecho.setAttributeNode(attributeSNRDerecho);

				Element calidadDerechoSnrDerecho = document.createElement("Calidad_Derecho_Registro");
				Element codigoNaturalezaJuridica = document.createElement("Codigo_Naturaleza_Juridica");
				Element fuenteDerecho = document.createElement("snr_fuente_derecho");
				Attr attributefuenteDerecho = document.createAttribute("REF");
				attributefuenteDerecho.setValue("Dummy2");
				fuenteDerecho.setAttributeNode(attributefuenteDerecho);
				Element predioRegistroDerecho = document.createElement("snr_predio_registro");
				Attr attributepredioRegistroDerecho = document.createAttribute("REF");
				attributepredioRegistroDerecho.setValue("Dummy4");
				predioRegistroDerecho.setAttributeNode(attributepredioRegistroDerecho);
				// Etiquetas dentro del nodo SNR_DERECHO
				calidadDerechoSnrDerecho.setTextContent("Falsa_Tradicion");
				codigoNaturalezaJuridica.setTextContent("610");
				newNodeSNRDerecho.appendChild(calidadDerechoSnrDerecho);
				newNodeSNRDerecho.appendChild(codigoNaturalezaJuridica);
				newNodeSNRDerecho.appendChild(fuenteDerecho);
				newNodeSNRDerecho.appendChild(predioRegistroDerecho);

				// NODE SNR_RESTRICCION
				Node newNodeSNRRestriccion = nDatosSNR.getOwnerDocument().createElement(restriccion);
				// Atributos del nodo SNR_RESTRICCION
				Element newnodeElementSNRRestriccion = (Element) newNodeSNRRestriccion;
				Attr attributeSNRRestriccion = document.createAttribute("TID");
				attributeSNRRestriccion.setValue("Dummy6");
				newnodeElementSNRRestriccion.setAttributeNode(attributeSNRRestriccion);

				// Element tipoRestriccionSnrRestriccion =
				// document.createElement("Tipo_Restriccion"); -- Validar cuando venga un XTF
				// con esta etiqueta
				Element codigoNaturalezaJuridicaRestriccion = document.createElement("Codigo_Naturaleza_Juridica");
				// Element snrPredioRegistroRestriccion =
				// document.createElement("snr_predio_registro"); -- Validar cuando venga un XTF
				// con esta etiqueta
				Attr attributePredioRegistroRestriccion = document.createAttribute("REF");
				attributePredioRegistroRestriccion.setValue("81166");
				// snrPredioRegistroRestriccion.setAttributeNode(attributePredioRegistroRestriccion);
				// Etiquetas dentro del nodo SNR_DERECHO
				// tipoRestriccionSnrRestriccion.setTextContent("Dummy_Dummy");
				codigoNaturalezaJuridicaRestriccion.setTextContent("0474");
				// newNodeSNRRestriccion.appendChild(tipoRestriccionSnrRestriccion);
				newNodeSNRRestriccion.appendChild(codigoNaturalezaJuridicaRestriccion);
				// newNodeSNRRestriccion.appendChild(snrPredioRegistroRestriccion);

				// FINISH XML
				nDatosSNR.insertBefore(newNodeSNRRestriccion, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));
				nDatosSNR.insertBefore(newNodeSNRDerecho, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));
				nDatosSNR.insertBefore(newNodeSNRTitularDerecho, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));
				nDatosSNR.insertBefore(newNodeSNR_PredioRegistro, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));
				nDatosSNR.insertBefore(newNodeSNR_Titular, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));
				nDatosSNR.insertBefore(newNodeFuenteDerecho, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));
				nDatosSNR.insertBefore(newNodeFuenteCabidaLinderos, childNodes.item(0));
				nDatosSNR.insertBefore(document.createTextNode("\n"), childNodes.item(0));

			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			// 10. Crea un objeto Transformer
			Transformer transformer = transformerFactory.newTransformer();

			// 11. Cree un objeto DOMSource
			DOMSource domSource = new DOMSource(document);

			// 12. Crear objeto StreamResult
			StreamResult reStreamResult = new StreamResult(filePath);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(domSource, reStreamResult);

		} catch (IOException | TransformerException | ParserConfigurationException | SAXException e) {
			throw new ExcepcionLecturaDeArchivo(
					"Error mapeando los bugs para el archivo XTF: " + file.getName() + " Error: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
