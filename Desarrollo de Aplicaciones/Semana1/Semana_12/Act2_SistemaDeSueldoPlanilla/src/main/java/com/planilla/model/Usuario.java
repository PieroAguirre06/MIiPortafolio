/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.model;

public class Usuario {
    private int codUsuario;
    private String username;
    private String clave;
    private String nombres;
    private String apellidos;
    private String correo;
    private boolean estado;
    private int codRol;
    private String nomRol;

    public int getCodUsuario() { return codUsuario; }
    public void setCodUsuario(int codUsuario) { this.codUsuario = codUsuario; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getCodRol() { return codRol; }
    public void setCodRol(int codRol) { this.codRol = codRol; }
    public String getNomRol() { return nomRol; }
    public void setNomRol(String nomRol) { this.nomRol = nomRol; }
}