package model;

/**
 * Created by GENESIS on 06/03/2017.
 */

public class Caja {
    public long Codigo;
    public String Caja,Empresa,estacion,punto;

    public Caja() {

    }
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    private boolean seleccionado=false;

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getPunto() {
        return punto;
    }

    public void setPunto(String punto) {
        this.punto = punto;
    }

    public long getCodigo() {
        return Codigo;
    }

    public void setCodigo(long codigo) {
        Codigo = codigo;
    }

    public String getCaja() {
        return Caja;
    }

    public void setCaja(String caja) {
        Caja = caja;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public Caja(long codigo, String caja, String punto, String estacion) {
        this.Codigo = codigo;
        this.Caja = caja;
        this.estacion = estacion;
        this.punto = punto;

    }
}
