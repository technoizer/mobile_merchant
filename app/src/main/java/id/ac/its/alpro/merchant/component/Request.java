package id.ac.its.alpro.merchant.component;

import java.io.Serializable;

/**
 * Created by ALPRO on 18/02/2016.
 */
public class Request implements Serializable {

    private String tanggalrequest, namacustomer, nohp, lokasi, namajasa, catatancustomer, latLng, urlAmbil, jam, tanggalSelesai, catatanPenyediaJasa;
    private Integer id, hargaPerkiraan, hargaTotal;

    public Request(String tanggalrequest, String namacustomer, String nohp, String lokasi, String namajasa, String catatancustomer, String latLng, String urlAmbil, String jam, String tanggalSelesai, String catatanPenyediaJasa, Integer id, Integer hargaPerkiraan, Integer hargaTotal) {
        this.tanggalrequest = tanggalrequest;
        this.namacustomer = namacustomer;
        this.nohp = nohp;
        this.lokasi = lokasi;
        this.namajasa = namajasa;
        this.catatancustomer = catatancustomer;
        this.latLng = latLng;
        this.urlAmbil = urlAmbil;
        this.jam = jam;
        this.tanggalSelesai = tanggalSelesai;
        this.catatanPenyediaJasa = catatanPenyediaJasa;
        this.id = id;
        this.hargaPerkiraan = hargaPerkiraan;
        this.hargaTotal = hargaTotal;
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

    public String getUrlAmbil() {
        return urlAmbil;
    }

    public void setUrlAmbil(String urlAmbil) {
        this.urlAmbil = urlAmbil;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(String tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getCatatanPenyediaJasa() {
        return catatanPenyediaJasa;
    }

    public void setCatatanPenyediaJasa(String catatanPenyediaJasa) {
        this.catatanPenyediaJasa = catatanPenyediaJasa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHargaPerkiraan() {
        return hargaPerkiraan;
    }

    public void setHargaPerkiraan(Integer hargaPerkiraan) {
        this.hargaPerkiraan = hargaPerkiraan;
    }

    public Integer getHargaTotal() {
        return hargaTotal;
    }

    public void setHargaTotal(Integer hargaTotal) {
        this.hargaTotal = hargaTotal;
    }

    public String toString (){
        return getId() + " " + getCatatancustomer();
    }
}
