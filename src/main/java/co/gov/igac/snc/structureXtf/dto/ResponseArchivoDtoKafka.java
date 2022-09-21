package co.gov.igac.snc.structureXtf.dto;

public class ResponseArchivoDtoKafka {

    private String key;
    private Data json;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Data getJson() {
        return json;
    }

    public void setJson(Data json) {
        this.json = json;
    }

    public ResponseArchivoDtoKafka() {
    }

    public ResponseArchivoDtoKafka(String key, Data json) {
        this.key = key;
        this.json = json;
    }

    @Override
    public String toString() {
        return "Jsontransmisiondearchivo [key=" + key + ", json={ 'rutaArchivo' : " + json.getRutaArchivo() + "," +
                "'nombreArchivo' :" + json.getNombreArchivo() + ", 'Origen' :" + json.getOrigen();
    }
}
