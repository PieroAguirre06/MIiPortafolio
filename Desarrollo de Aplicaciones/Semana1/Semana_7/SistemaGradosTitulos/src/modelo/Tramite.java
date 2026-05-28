/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Timestamp;

public class Tramite {

    private int idTramite;
    private String codigoEstudiante;
    private String tipoTramite;  // 'Obtencion_Bachiller', 'Obtencion_Titulo_Tesis', 'Obtencion_Titulo_TSP'
    private String estadoActual; // 'Iniciado', 'Revision_Requisitos', 'Aprobacion_Plan', 'Desarrollo_Investigacion', 'Revision_Similitud', 'Revision_Jurado', 'Expedito', 'Sustentacion_Programada', 'Culminado', 'Rechazado'
    private Timestamp fechaInicio;
    private Timestamp fechaActualizacion;

    public Tramite() {}

    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }

    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

    public String getTipoTramite() { return tipoTramite; }
    public void setTipoTramite(String tipoTramite) { this.tipoTramite = tipoTramite; }

    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }

    public Timestamp getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}