package id.ac.its.alpro.merchant.component;

import java.io.Serializable;

/**
 * Created by ALPRO on 18/02/2016.
 */
public class NewRequest implements Serializable {

    private String tanggal, namaCustomer, noHpCustomer, lokasi, jenisServis, Catatan, latLng;
    private Integer idRequest;

    public NewRequest(String tanggal, String namaCustomer, String noHpCustomer, String lokasi, String jenisServis, String catatan, String latLng, Integer idRequest) {
        this.tanggal = tanggal;
        this.namaCustomer = namaCustomer;
        this.noHpCustomer = noHpCustomer;
        this.lokasi = lokasi;
        this.jenisServis = jenisServis;
        Catatan = catatan;
        this.latLng = latLng;
        this.idRequest = idRequest;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getNoHpCustomer() {
        return noHpCustomer;
    }

    public void setNoHpCustomer(String noHpCustomer) {
        this.noHpCustomer = noHpCustomer;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getJenisServis() {
        return jenisServis;
    }

    public void setJenisServis(String jenisServis) {
        this.jenisServis = jenisServis;
    }

    public String getCatatan() {
        return Catatan;
    }

    public void setCatatan(String catatan) {
        Catatan = catatan;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public Integer getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }
}
