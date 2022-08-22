package co.gov.igac.snc.structureXtf.dto;

public class ResponseArchivoDTOKafka {

    private String key;
    private DataDTO json;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataDTO getJson() {
        return json;
    }

    public void setJson(DataDTO json) {
        this.json = json;
    }

    public ResponseArchivoDTOKafka() {
    }

    public ResponseArchivoDTOKafka(String key, DataDTO json) {
        this.key = key;
        this.json = json;
    }

    @Override
    public String toString() {
        return "Jsontransmisiondearchivo [key=" + key + ", json={ 'rutaArchivo' : " + json.getRutaArchivo() + "," +
                "'nombreArchivo' :" + json.getNombreArchivo() + ", 'Origen' :" + json.getOrigen();
    }
}
