package model;

/**
 * Created by GENESIS on 08/03/2017.
 */

public class Productos {
    public long codigo;
    public String producto;
    public String nombre;
    public String imagen;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Productos(long codigo, String producto, String nombre, String imagen) {
        this.codigo = codigo;
        this.producto = producto;
        this.nombre = nombre;
        this.imagen = imagen;
    }
}
