package utn.ac.cr.fitwoman_management.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Entrenamiento implements Serializable {
    private String id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private int duraciónMinutos;
    private Date fecha;
    private String grupoMuscular;
    private String notas;

    public Entrenamiento(){
        this.id = UUID.randomUUID().toString();
        this.fecha = new Date();
    }
    public Entrenamiento(String nombre,String categoria,String descripcion,
                         int duraciónMinutos,String grupoMuscular,String notas) {

        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.duraciónMinutos = duraciónMinutos;
        this.grupoMuscular = grupoMuscular;
        this.notas = notas;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionMinutos() {
        return duraciónMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duraciónMinutos = duracionMinutos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "Entrenamiento{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", duracionMinutos=" + duraciónMinutos +
                ", fecha=" + fecha +
                ", grupoMuscular='" + grupoMuscular + '\'' +
                '}';
    }

    }














