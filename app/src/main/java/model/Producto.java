package model;

/**
 * Created by GENESIS on 09/03/2017.
 */

public class Producto {
    public long codigos;
    public String empresas,descripcion1,iva;
    public Double pvp1,ivaValor,costo;

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Double getIvaValor() {
        return ivaValor;
    }

    public void setIvaValor(Double ivaValor) {
        this.ivaValor = ivaValor;
    }

    public long getCodigos() {
        return codigos;
    }

    public void setCodigos(long codigos) {
        this.codigos = codigos;
    }

    public String getEmpresas() {
        return empresas;
    }

    public void setEmpresas(String empresas) {
        this.empresas = empresas;
    }

    public String getDescripcion1() {
        return descripcion1;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public Double getPvp1() {
        return pvp1;
    }

    public void setPvp1(Double pvp1) {
        this.pvp1 = pvp1;
    }

    public Producto(long codigos, String empresas, String descripcion1, String iva, Double pvp1,Double ivaValor,Double costo) {
        this.codigos = codigos;
        this.empresas = empresas;
        this.descripcion1 = descripcion1;
        this.iva = iva;
        this.pvp1 = pvp1;
        this.ivaValor = ivaValor;
        this.costo = costo;
    }
}
