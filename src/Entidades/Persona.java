/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author nejo
 */
public class Persona {

    private int id;
    private String apellido;
    private String nombres;
    private String domicilio;
    private Date fecha_nac;
    private Integer nro_docu;
    private String cuil;
    private Integer celular1;
    private Integer celular2;
    private String email;
    private String tipo_personal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public Integer getNro_docu() {
        return nro_docu;
    }

    public void setNro_docu(Integer nro_docu) {
        this.nro_docu = nro_docu;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public Integer getCelular1() {
        return celular1;
    }

    public void setCelular1(Integer celular1) {
        this.celular1 = celular1;
    }

    public Integer getCelular2() {
        return celular2;
    }

    public void setCelular2(Integer celular2) {
        this.celular2 = celular2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo_personal() {
        return tipo_personal;
    }

    public void setTipo_personal(String tipo_personal) {
        this.tipo_personal = tipo_personal;
    }

    public Persona() {
        setId(0);
        setApellido("");
        setNombres("");
        setDomicilio("");
        setFecha_nac(null);
        setNro_docu(0);
        setCuil("");
        setCelular1(0);
        setCelular2(0);
        setEmail("");
        setTipo_personal("");
    }

    public Object[] getInfo() {

        Object[] oDato = {getId(),
            getApellido(),
            getNombres(),
            getNro_docu(),
            getTipo_personal()};
        return oDato;
    }

    public boolean isValidar() {
        boolean ok = false;
        ok = true;
        return ok;
    }
}
