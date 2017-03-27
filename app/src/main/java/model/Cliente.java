package model;

/**
 * Created by GENESIS on 08/03/2017.
 */

public class Cliente {
    public long codigo;
    public String nombre,direcion,telefono,cedula_ruc;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCedula_ruc() {
        return cedula_ruc;
    }

    public void setCedula_ruc(String cedula_ruc) {
        this.cedula_ruc = cedula_ruc;
    }

    public Cliente(long codigo, String nombre, String direcion, String telefono, String cedula_ruc) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direcion = direcion;
        this.telefono = telefono;
        this.cedula_ruc = cedula_ruc;
    }

    public Cliente(String nombre, String direcion, String cedula_ruc) {
        this.nombre = nombre;
        this.direcion = direcion;
        this.cedula_ruc = cedula_ruc;
    }
}
