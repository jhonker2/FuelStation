package model;

/**
 * Created by GENESIS on 07/03/2017.
 */

public class Surtidor {
 public long codigo;
    public String empresa;
    public String producto;
    public String nombre;
    public String caja;
    public String identificador;
    public String suridor;
    public String imagen;
    public String getSuridor() {
        return suridor;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    public void setSuridor(String suridor) {    this.suridor = suridor;    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Surtidor(long codigo, String empresa, String producto, String nombre, String caja, String identificador) {
        this.codigo = codigo;
        this.empresa = empresa;
        this.producto = producto;
        this.nombre = nombre;
        this.caja = caja;
        this.identificador = identificador;
    }

    public Surtidor(long codigo, String producto, String caja, String identificador, String suridor) {
        this.codigo = codigo;
        this.producto = producto;
        this.caja = caja;
        this.identificador = identificador;
        this.suridor = suridor;
    }

    public Surtidor(Long codigo, String producto, String nombre, String imagen){
        this.codigo = codigo;
        this.producto = producto;
        this.imagen = imagen;
        this.nombre = nombre;

    }
}
