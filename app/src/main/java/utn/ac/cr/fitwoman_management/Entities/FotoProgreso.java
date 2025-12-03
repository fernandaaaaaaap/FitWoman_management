package utn.ac.cr.fitwoman_management.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class FotoProgreso implements Serializable {
    private String id;
    private String rutaImagen;
    private Date fechaTomada;
    private String mes;
    private String notas;
    private double peso;
    private String categoria;

    public FotoProgreso(){
        this.id = UUID.randomUUID().toString();
        this.fechaTomada = new Date();
    }
    public FotoProgreso(String rutaImagen, String mes, String notas, double peso,String categoria){
        this.id= UUID.randomUUID().toString();
        this.rutaImagen=rutaImagen;
        this.fechaTomada= new Date();
        this.mes=mes;
        this.peso=peso;
        this.categoria= categoria;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public Date getFechaTomada() {
        return fechaTomada;
    }

    public void setFechaTomada(Date fechaTomada) {
        this.fechaTomada = fechaTomada;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "FotoProgreso{" +
                "id='" + id + '\'' +
                ", mes='" + mes + '\'' +
                ", fechaTomada=" + fechaTomada +
                ", peso=" + peso +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
