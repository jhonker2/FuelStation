package model;

/**
 * Created by GENESIS on 04/03/2017.
 */

public class Empresa {
    public  long codigo;
    public String  empresa, ciudad, periodo, direccion, telefono, ruc, pais, representante1;

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    private boolean seleccionado=false;

    public long getCodigo() {
        return codigo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRuc() {
        return ruc;
    }

    public String getPais() {
        return pais;
    }

    public String getRepresentante1() {
        return representante1;
    }

    public void setCodigo(String codigo) {
        this.codigo = Long.parseLong(codigo);
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setRepresentante1(String representante1) {
        this.representante1 = representante1;
    }


    public Empresa(String codigo, String Empresa, String Periodo){
        this.codigo=Long.parseLong(codigo);
        this.empresa=Empresa;
        this.periodo=Periodo;
    }

}
