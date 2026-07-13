/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.io.Serializable;

public class UsuarioBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int codUsuario;
    private String username;
    private String clave;
    private String nombres;
    private String apellidos;
    private String correo;
    private int estado;
    private int codRol;
    private String nombreRol;
    
    public UsuarioBean() {}
    
    // Getters y Setters
    public int getCodUsuario() {
        return codUsuario;
    }
    
    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getClave() {
        return clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public int getEstado() {
        return estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public int getCodRol() {
        return codRol;
    }
    
    public void setCodRol(int codRol) {
        this.codRol = codRol;
    }
    
    public String getNombreRol() {
        return nombreRol;
    }
    
    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
    
    public String getNombreCompleto() {
        return nombres + " " + (apellidos != null ? apellidos : "");
    }
}
