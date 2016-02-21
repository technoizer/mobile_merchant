package id.ac.its.alpro.merchant.component;

import java.io.Serializable;

/**
 * Created by ALPRO on 18/02/2016.
 */
public class NewRequest implements Serializable {

    private String tanggalrequest, namacustomer, nohp, lokasi, namajasa, catatancustomer, latLng;
    private Integer id;

    public NewRequest(String tanggalrequest, String namacustomer, String nohp, String lokasi, String namajasa, String catatancustomer, String latLng, Integer id) {
        this.tanggalrequest = tanggalrequest;
        this.namacustomer = namacustomer;
        this.nohp = nohp;
        this.lokasi = lokasi;
        this.namajasa = namajasa;
        this.catatancustomer = catatancustomer;
        this.latLng = latLng;
        this.id = id;
    }


    public String getTanggalrequest() {
        return tanggalrequest;
    }

    public void setTanggalrequest(String tanggalrequest) {
        this.tanggalrequest = tanggalrequest;
    }

    public String getNamacustomer() {
        return namacustomer;
    }

    public void setNamacustomer(String namacustomer) {
        this.namacustomer = namacustomer;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNamajasa() {
        return namajasa;
    }

    public void setNamajasa(String namajasa) {
        this.namajasa = namajasa;
    }

    public String getCatatancustomer() {
        return catatancustomer;
    }

    public void setCatatancustomer(String catatancustomer) {
        this.catatancustomer = catatancustomer;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toString (){
        return getId() + " " + getCatatancustomer();
    }
}
