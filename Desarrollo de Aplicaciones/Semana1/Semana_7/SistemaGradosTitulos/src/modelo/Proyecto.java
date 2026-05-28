/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Proyecto {

    private int idProyecto;
    private int idTramite;
    private String titulo;
    private String modalidad;  // 'Tesis', 'Trabajo_Suficiencia_Profesional'
    private String enfoque;    // 'Cuantitativa', 'Cualitativa', 'Mixta', 'No_Aplica'
    private BigDecimal porcentajeSimilitud;
    private String estado;     // 'Registrado', 'Plan_Aprobado', 'En_Ejecucion', 'Aprobado_Por_Asesor', 'Aprobado_Por_Jurado', 'Sustentado'
    private String urlRepositorio;
    private Timestamp fechaRegistro;

    public Proyecto() {}

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getEnfoque() { return enfoque; }
    public void setEnfoque(String enfoque) { this.enfoque = enfoque; }

    public BigDecimal getPorcentajeSimilitud() { return porcentajeSimilitud; }
    public void setPorcentajeSimilitud(BigDecimal porcentajeSimilitud) { this.porcentajeSimilitud = porcentajeSimilitud; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUrlRepositorio() { return urlRepositorio; }
    public void setUrlRepositorio(String urlRepositorio) { this.urlRepositorio = urlRepositorio; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}