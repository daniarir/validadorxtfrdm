package co.gov.igac.snc.structureXtf.util;

import co.gov.igac.snc.structureXtf.exception.ExcepcionesDeNegocio;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.file.datalake.DataLakeDirectoryClient;
import com.azure.storage.file.datalake.DataLakeFileClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class AzureStorage {

    private final Log log = LogFactory.getLog(getClass());

    private Propiedades prop;

    private DataLakeFileSystemClient fileSystemClient;

    public AzureStorage(Propiedades prop) {
        this.prop = prop;
        this.getDataLakeServiceClient();
    }

    private void getDataLakeServiceClient() {

        String endpoint = "https://" + prop.getAccountName() + ".dfs.core.windows.net";

        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(prop.getClientId())
                .clientSecret(prop.getClientSecret())
                .tenantId(prop.getTenantId())
                .build();

        DataLakeServiceClientBuilder builder = new DataLakeServiceClientBuilder();

        this.fileSystemClient = builder.credential(clientSecretCredential)
                .endpoint(endpoint)
                .buildClient()
                .getFileSystemClient(prop.getContenedor());
    }

    public Boolean subirArchivoGrande(String rutaArchivo, String rutaStorage, String nombreArchivo) throws ExcepcionesDeNegocio {

        if(!new File(rutaArchivo).exists()) {
            throw new ExcepcionesDeNegocio("Error al subir archivo",
            		"El archivo " + rutaArchivo + "no existe", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        DataLakeFileClient fileClient = fileSystemClient.getDirectoryClient(rutaStorage).getFileClient(nombreArchivo);

        if(fileClient.exists()) {
        	System.out.println("error subiendo archivo al storage");
            throw new ExcepcionesDeNegocio("Azure storage",
            		 "Ya existe el archivo en azure storage", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        fileClient.uploadFromFile(rutaArchivo);
        return fileClient.exists();

    }

    public String descargarArchivo(String rutaStorage, String nombreArchivo, String rutaDescarga) throws ExcepcionesDeNegocio {

        DataLakeFileClient fileClient = fileSystemClient.getDirectoryClient(rutaStorage).getFileClient(nombreArchivo);

        if(!fileClient.exists()) {
            throw new ExcepcionesDeNegocio("El archivo no existe en azureStorage: " + fileClient.getFilePath(),"Archivo inexistente",HttpStatus.CONFLICT);
        }

        File file = new File(rutaDescarga + nombreArchivo);
        if(file.exists()) {
            return file.getPath();
        }
        OutputStream targetStream;
        try {
            targetStream = new FileOutputStream(file);
            fileClient.read(targetStream);
            targetStream.close();
        } catch (FileNotFoundException ex) {
            log.error(ex);
            throw new ExcepcionesDeNegocio("descarga de archivo",
            		"Error al descargar archivo " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            log.error(ex);
            throw new ExcepcionesDeNegocio("descarga de archivo",
            		"Error al descargar archivo " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return file.getPath();
    }

    public void descargarModelos(String rutaStorage, String rutaDescarga) throws ExcepcionesDeNegocio {
        try {
        	
        	DataLakeDirectoryClient directoryClient = fileSystemClient.getDirectoryClient(rutaStorage);
        	
        	directoryClient.listPaths().forEach(fileStorage->{
                String nameArchivo = new File(fileStorage.getName()).getName();
                System.out.println("> " + nameArchivo);
                DataLakeFileClient fileClient = directoryClient.getFileClient(nameArchivo);
                File file = new File(rutaDescarga + "/" + nameArchivo);
                System.out.println(file.getPath());
                if(!file.exists()) {
                    OutputStream targetStream;
                    try {
                        System.out.println("Descargando: " + fileClient.getFilePath());
                        targetStream = new FileOutputStream(file);
                        fileClient.read(targetStream);
                        targetStream.close();
                    } catch (FileNotFoundException ex) {
                    	log.error("Error no encontro el archivo: " + ex.getMessage());
                    } catch (IOException ex) {
                    	log.error("Error descargando los archivos .ili: " + ex.getMessage());
                    }
                }

            });
		} catch (Exception e) {
			log.error("Se presento un error descargando los archivos .ili en la carpeta modelos " + e.getMessage());
			throw new ExcepcionesDeNegocio("Se presento un error descargando los archivos .ili en la carpeta modelos: " + e.getMessage(),
            		"Error al descargar archivo ", HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
