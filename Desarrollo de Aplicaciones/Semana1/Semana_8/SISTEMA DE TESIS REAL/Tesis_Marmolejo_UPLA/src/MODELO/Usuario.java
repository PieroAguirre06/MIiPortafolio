/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Usuario {

    private String codigo;
    private String dni;
    private String nombres;
    private String apellidos;
    private String emailInstitucional;
    private String passwordHash;
    private String rol;
    private String codigoOrcid;
    private String estado;

    public Usuario() {}

    public Usuario(String codigo, String dni, String nombres, String apellidos,
                   String emailInstitucional, String passwordHash, String rol, String estado) {
        this.codigo = codigo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.emailInstitucional = emailInstitucional;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmailInstitucional() { return emailInstitucional; }
    public void setEmailInstitucional(String emailInstitucional) { this.emailInstitucional = emailInstitucional; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getCodigoOrcid() { return codigoOrcid; }
    public void setCodigoOrcid(String codigoOrcid) { this.codigoOrcid = codigoOrcid; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return codigo + " - " + nombres + " " + apellidos;
    }
}