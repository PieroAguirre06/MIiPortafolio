/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Sustentacion {

    private int idSustentacion;
    private int idProyecto;
    private String resolucionExpedito;
    private Timestamp fechaHoraProgramada;
    private String modalidadSustentacion;  // 'Presencial', 'No_Presencial'
    private String lugarEnlace;
    private BigDecimal notaFinalNumerica;
    private String notaFinalLetras;
    private String condicionActa;  // 'Excelente', 'Muy_Bueno', 'Bueno', 'Regular', 'Desaprobado', 'Pendiente_De_Sustentar'
    private String aprobacionTipo;  // 'Unanimidad', 'Mayoria', 'No_Aplica'
    private String observacionesActa;

    public Sustentacion() {}

    public int getIdSustentacion() { return idSustentacion; }
    public void setIdSustentacion(int idSustentacion) { this.idSustentacion = idSustentacion; }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public String getResolucionExpedito() { return resolucionExpedito; }
    public void setResolucionExpedito(String resolucionExpedito) { this.resolucionExpedito = resolucionExpedito; }

    public Timestamp getFechaHoraProgramada() { return fechaHoraProgramada; }
    public void setFechaHoraProgramada(Timestamp fechaHoraProgramada) { this.fechaHoraProgramada = fechaHoraProgramada; }

    public String getModalidadSustentacion() { return modalidadSustentacion; }
    public void setModalidadSustentacion(String modalidadSustentacion) { this.modalidadSustentacion = modalidadSustentacion; }

    public String getLugarEnlace() { return lugarEnlace; }
    public void setLugarEnlace(String lugarEnlace) { this.lugarEnlace = lugarEnlace; }

    public BigDecimal getNotaFinalNumerica() { return notaFinalNumerica; }
    public void setNotaFinalNumerica(BigDecimal notaFinalNumerica) { this.notaFinalNumerica = notaFinalNumerica; }

    public String getNotaFinalLetras() { return notaFinalLetras; }
    public void setNotaFinalLetras(String notaFinalLetras) { this.notaFinalLetras = notaFinalLetras; }

    public String getCondicionActa() { return condicionActa; }
    public void setCondicionActa(String condicionActa) { this.condicionActa = condicionActa; }

    public String getAprobacionTipo() { return aprobacionTipo; }
    public void setAprobacionTipo(String aprobacionTipo) { this.aprobacionTipo = aprobacionTipo; }

    public String getObservacionesActa() { return observacionesActa; }
    public void setObservacionesActa(String observacionesActa) { this.observacionesActa = observacionesActa; }
}