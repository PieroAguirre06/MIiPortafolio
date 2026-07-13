/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.io.Serializable;

public class RolBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int codRol;
    private String nomRol;
    private String descripcion;
    
    public RolBean() {}
    
    public int getCodRol() {
        return codRol;
    }
    
    public void setCodRol(int codRol) {
        this.codRol = codRol;
    }
    
    public String getNomRol() {
        return nomRol;
    }
    
    public void setNomRol(String nomRol) {
        this.nomRol = nomRol;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
