package model;

/**
 * Created by GENESIS on 08/03/2017.
 */

public class ItemCompra {
    public String identificador,cedula;
    public String idproducto;
    public double valor;

    public String getidentificador() {
        return identificador;
    }

    public void setidentificador(String surtifdor) {
        this.identificador = surtifdor;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public ItemCompra(String identificador, String cedula, String idproducto, double valor) {
        this.identificador = identificador;
        this.cedula = cedula;
        this.idproducto = idproducto;
        this.valor = valor;
    }
}
