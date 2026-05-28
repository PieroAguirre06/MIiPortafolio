/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.Conexion;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MenuPrincipal extends JFrame {

    private final String codigoUsuario;
    private final String rolUsuario;
    private Connection con;

    public MenuPrincipal(String codigo, String rol) {
        this.codigoUsuario = codigo;
        this.rolUsuario = rol;
        this.con = new Conexion().establecerConexion();
        initComponents();
        setTitle("Sistema de Grados y Títulos - UPLA - IBERCAP");
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(26, 58, 107));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(13, 43, 82));
        header.setBounds(0, 0, 750, 55);
        add(header);
        
        JLabel lblTitulo = new JLabel("Sistema de Grados y Títulos - UPLA", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(201, 160, 61));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(0, 8, 750, 26);
        header.add(lblTitulo);
        
        JLabel lblSubtitulo = new JLabel("Instituto IBERCAP - Automatización de Procesos Empresariales y Estadísticos", SwingConstants.CENTER);
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblSubtitulo.setBounds(0, 32, 750, 18);
        header.add(lblSubtitulo);

        JLabel lblRol = new JLabel("Usuario: " + codigoUsuario + "  |  Rol: " + rolUsuario, SwingConstants.CENTER);
        lblRol.setForeground(new Color(180, 210, 255));
        lblRol.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRol.setBounds(0, 58, 750, 18);
        add(lblRol);

        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBounds(15, 85, 720, 380);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(panel);

        String[][] botones = {
            {"👤 Usuarios", "Usuarios"},
            {"🏛 Facultades", "Facultades"},
            {"📋 Programas", "Programas"},
            {"📜 Historial", "Historial"},
            {"📑 Trámites", "Tramites"},
            {"📁 Documentos", "Documentos"},
            {"🎓 Proyectos", "Proyectos"},
            {"👨‍🏫 Asesores", "Asesores"},
            {"⚖ Jurados", "Jurados"},
            {"📊 Rúbricas", "Rubricas"},
            {"📝 Evaluaciones", "Evaluaciones"},
            {"🏆 Sustentaciones", "Sustentaciones"},
            {"📈 Reportes", "Reportes"},
            {"🔧 Configuración", "Configuracion"},
            {"❓ Ayuda", "Ayuda"},
            {"🚪 Salir", "Salir"}
        };

        for (String[] b : botones) {
            panel.add(crearBoton(b[0], b[1]));
        }

        if (rolUsuario.equals("Estudiante")) {
            JButton btnLogros = new JButton("🎉 MI LOGRO - TESIS APROBADA 🎉");
            btnLogros.setBackground(new Color(184, 134, 11));
            btnLogros.setForeground(Color.WHITE);
            btnLogros.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
            btnLogros.setFocusPainted(false);
            btnLogros.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnLogros.setBounds(200, 480, 350, 45);
            btnLogros.addActionListener(e -> verificarTesisAprobada());
            add(btnLogros);
        }

        JButton btnSalir = new JButton("🚪 Cerrar sesión");
        btnSalir.setBackground(new Color(180, 40, 40));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 12));
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(580, 480, 150, 45);
        btnSalir.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });
        add(btnSalir);
    }

    private JButton crearBoton(String etiqueta, String modulo) {
        JButton btn = new JButton("<html><center>" + etiqueta + "</center></html>");
        btn.setBackground(new Color(13, 43, 82));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            switch (modulo) {
                case "Usuarios": new FrmUsuario().setVisible(true); break;
                case "Facultades": new FrmFacultad().setVisible(true); break;
                case "Programas": new FrmProgramaEstudios().setVisible(true); break;
                case "Historial": new FrmHistorialAcademico().setVisible(true); break;
                case "Tramites": new FrmTramite().setVisible(true); break;
                case "Documentos": new FrmDocumento().setVisible(true); break;
                case "Proyectos": new FrmProyecto().setVisible(true); break;
                case "Asesores": new FrmAsesor().setVisible(true); break;
                case "Jurados": new FrmJurado().setVisible(true); break;
                case "Rubricas": new FrmRubrica().setVisible(true); break;
                case "Evaluaciones": new FrmEvaluacion().setVisible(true); break;
                case "Sustentaciones": new FrmSustentacion().setVisible(true); break;
                case "Reportes": 
                    JOptionPane.showMessageDialog(this, "Módulo de Reportes en construcción", "Aviso", JOptionPane.INFORMATION_MESSAGE); 
                    break;
                case "Configuracion": 
                    JOptionPane.showMessageDialog(this, "Configuración del sistema\n- Cambiar contraseña\n- Respaldos", "Configuración", JOptionPane.INFORMATION_MESSAGE); 
                    break;
                case "Ayuda": 
                    String mensajeAyuda = "Sistema de Grados y Títulos UPLA - IBERCAP\n\n" +
                                          "TESIS: Implementación de Sistema de Información\n" +
                                          "para la Automatización de Procesos Empresariales\n" +
                                          "y Estadísticos en el Instituto IBERCAP - Ayacucho 2019\n\n" +
                                          "AUTOR: David Israel Marmolejo Barbaran\n" +
                                          "ASESOR: Mg. Walter David Estares Ventocilla\n\n" +
                                          "© 2024 - Todos los derechos reservados";
                    JOptionPane.showMessageDialog(this, mensajeAyuda, "Ayuda - Información de la Tesis", JOptionPane.INFORMATION_MESSAGE); 
                    break;
                case "Salir": 
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        new Login().setVisible(true);
                        this.dispose();
                    }
                    break;
                default: 
                    JOptionPane.showMessageDialog(this, "Módulo en construcción", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return btn;
    }

    private void verificarTesisAprobada() {
        try {
            System.out.println("=== VERIFICANDO TESIS PARA: " + codigoUsuario + " ===");
            
            // Consulta para verificar el estado del proyecto y sustentación
            String sql = "SELECT " +
                         "p.id_proyecto, " +
                         "p.titulo, " +
                         "p.estado, " +
                         "p.porcentaje_similitud, " +
                         "s.nota_final_numerica, " +
                         "s.condicion_acta, " +
                         "s.aprobacion_tipo, " +
                         "t.estado_actual " +
                         "FROM usuario u " +
                         "LEFT JOIN tramite t ON u.codigo = t.codigo_estudiante " +
                         "LEFT JOIN proyecto p ON t.id_tramite = p.id_tramite " +
                         "LEFT JOIN sustentacion s ON p.id_proyecto = s.id_proyecto " +
                         "WHERE u.codigo = ? " +
                         "ORDER BY p.id_proyecto DESC LIMIT 1";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigoUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String estadoProyecto = rs.getString("estado");
                Double nota = rs.getDouble("nota_final_numerica");
                String condicion = rs.getString("condicion_acta");
                String titulo = rs.getString("titulo");
                String estadoTramite = rs.getString("estado_actual");
                Double similitud = rs.getDouble("porcentaje_similitud");
                String aprobacion = rs.getString("aprobacion_tipo");
                
                System.out.println("Estado Proyecto: " + estadoProyecto);
                System.out.println("Nota: " + nota);
                System.out.println("Condición: " + condicion);
                System.out.println("Estado Trámite: " + estadoTramite);
                
                // Condición para mostrar felicitaciones
                // El proyecto debe estar SUSTENTADO o tener nota registrada
                boolean tesisAprobada = false;
                
                if (estadoProyecto != null && estadoProyecto.equals("Sustentado")) {
                    tesisAprobada = true;
                }
                
                if (nota != null && nota >= 11) {
                    tesisAprobada = true;
                }
                
                if (condicion != null && (condicion.equals("Excelente") || condicion.equals("Muy_Bueno") || condicion.equals("Bueno"))) {
                    tesisAprobada = true;
                }
                
                if (estadoTramite != null && estadoTramite.equals("Culminado")) {
                    tesisAprobada = true;
                }
                
                if (tesisAprobada && estadoProyecto != null && !estadoProyecto.equals("Registrado")) {
                    // ==========================================================
                    // MENSAJE DE FELICITACIÓN - TESIS APROBADA
                    // ==========================================================
                    String mensaje = "╔══════════════════════════════════════════════════════════════════╗\n" +
                                     "║                                                                  ║\n" +
                                     "║                    🎓 TESIS APROBADA 🎓                          ║\n" +
                                     "║                    🎉 FELICITACIONES 🎉                          ║\n" +
                                     "║                                                                  ║\n" +
                                     "╠══════════════════════════════════════════════════════════════════╣\n" +
                                     "║                                                                  ║\n" +
                                     "║  Estimado(a) egresado,                                           ║\n" +
                                     "║                                                                  ║\n" +
                                     "║  ✅ Su tesis ha sido APROBADA exitosamente por unanimidad.       ║\n" +
                                     "║                                                                  ║\n" +
                                     "║  📖 TÍTULO:                                                      ║\n" +
                                     "║     " + titulo + "\n" +
                                     "║                                                                  ║\n" +
                                     "║  📊 DATOS DE LA SUSTENTACIÓN:                                    ║\n" +
                                     "║     • Porcentaje de similitud Turnitin: " + similitud + "%\n" +
                                     "║     • Nota final: " + nota + "\n" +
                                     "║     • Condición: " + condicion + "\n" +
                                     "║     • Aprobación: " + aprobacion + "\n" +
                                     "║                                                                  ║\n" +
                                     "║  🌟 ¡Usted ya es un profesional!                                 ║\n" +
                                     "║  🌟 Pronto recibirá su título de INGENIERO DE SISTEMAS Y         ║\n" +
                                     "║     COMPUTACIÓN.                                                 ║\n" +
                                     "║                                                                  ║\n" +
                                     "║  🏆 Instituto IBERCAP - ¡Gracias por confiar en nosotros!        ║\n" +
                                     "║                                                                  ║\n" +
                                     "╚══════════════════════════════════════════════════════════════════╝";
                    
                    JOptionPane.showMessageDialog(this, mensaje, "🎓 TESIS APROBADA - FELICITACIONES 🎉", JOptionPane.INFORMATION_MESSAGE);
                    
                } else if (estadoProyecto != null) {
                    // ==========================================================
                    // MENSAJE DE PROGRESO - TESIS EN PROCESO
                    // ==========================================================
                    String mensaje = "╔══════════════════════════════════════════════════════════════════╗\n" +
                                     "║                    📌 ESTADO DE TU TESIS                          ║\n" +
                                     "╠══════════════════════════════════════════════════════════════════╣\n" +
                                     "║                                                                  ║\n" +
                                     "║  📖 TÍTULO:                                                      ║\n" +
                                     "║     " + (titulo != null ? titulo.substring(0, Math.min(50, titulo.length())) + "..." : "No registrado") + "\n" +
                                     "║                                                                  ║\n" +
                                     "║  📊 ESTADO ACTUAL:                                               ║\n" +
                                     "║     • Estado del proyecto: " + estadoProyecto + "\n" +
                                     "║     • Estado del trámite: " + (estadoTramite != null ? estadoTramite : "No iniciado") + "\n" +
                                     "║     • % Similitud Turnitin: " + similitud + "%\n" +
                                     "║                                                                  ║\n" +
                                     "║  📋 PRÓXIMOS PASOS:                                              ║\n";
                    
                    if (!"Sustentado".equals(estadoProyecto)) {
                        mensaje += "║     ❌ Aún no has sustentado tu tesis                                ║\n";
                        mensaje += "║     📌 Debes esperar la programación de sustentación                 ║\n";
                        mensaje += "║     📌 Preparar la defensa de tesis                                 ║\n";
                        mensaje += "║     📌 Sustentar ante el jurado                                     ║\n";
                    } else {
                        mensaje += "║     ✅ Ya sustentaste tu tesis                                      ║\n";
                        mensaje += "║     📌 Esperar la publicación de resultados                         ║\n";
                    }
                    
                    mensaje += "║                                                                  ║\n" +
                               "║  💪 ¡Sigue adelante, pronto serás Ingeniero!                          ║\n" +
                               "║                                                                  ║\n" +
                               "╚══════════════════════════════════════════════════════════════════╝";
                    
                    JOptionPane.showMessageDialog(this, mensaje, "🎓 Estado de tu Tesis", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // ==========================================================
                    // MENSAJE - SIN TRÁMITE
                    // ==========================================================
                    String mensaje = "╔══════════════════════════════════════════════════════════════════╗\n" +
                                     "║                    📌 INICIAR PROCESO DE TITULACIÓN               ║\n" +
                                     "╠══════════════════════════════════════════════════════════════════╣\n" +
                                     "║                                                                  ║\n" +
                                     "║  Aún no has iniciado tu trámite de titulación.                   ║\n" +
                                     "║                                                                  ║\n" +
                                     "║  📋 PASOS PARA OBTENER TU TÍTULO:                                ║\n" +
                                     "║     1️⃣ Ir a la opción 'Trámites'                                 ║\n" +
                                     "║     2️⃣ Crear un nuevo trámite de titulación                      ║\n" +
                                     "║     3️⃣ Registrar tu proyecto de tesis                           ║\n" +
                                     "║     4️⃣ Subir tu documento de tesis (PDF)                        ║\n" +
                                     "║     5️⃣ Esperar asignación de asesor y jurados                    ║\n" +
                                     "║     6️⃣ Preparar y sustentar tu tesis                            ║\n" +
                                     "║                                                                  ║\n" +
                                     "║  💪 ¡Éxito en este importante paso!                              ║\n" +
                                     "║                                                                  ║\n" +
                                     "╚══════════════════════════════════════════════════════════════╝";
                    
                    JOptionPane.showMessageDialog(this, mensaje, "🎓 Iniciar proceso de titulación", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                String mensaje = "╔══════════════════════════════════════════════════════════════════╗\n" +
                                 "║                    📌 INICIAR PROCESO DE TITULACIÓN               ║\n" +
                                 "╠══════════════════════════════════════════════════════════════════╣\n" +
                                 "║                                                                  ║\n" +
                                 "║  Aún no has iniciado tu trámite de titulación.                   ║\n" +
                                 "║                                                                  ║\n" +
                                 "║  📋 PASOS PARA OBTENER TU TÍTULO:                                ║\n" +
                                 "║     1️⃣ Ir a la opción 'Trámites'                                 ║\n" +
                                 "║     2️⃣ Crear un nuevo trámite de titulación                      ║\n" +
                                 "║     3️⃣ Registrar tu proyecto de tesis                           ║\n" +
                                 "║     4️⃣ Subir tu documento de tesis (PDF)                        ║\n" +
                                 "║                                                                  ║\n" +
                                 "║  💪 ¡Sigue adelante, pronto lo lograrás!                         ║\n" +
                                 "║                                                                  ║\n" +
                                 "╚══════════════════════════════════════════════════════════════╝";
                
                JOptionPane.showMessageDialog(this, mensaje, "🎓 Iniciar proceso de titulación", JOptionPane.INFORMATION_MESSAGE);
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            System.out.println("Error al verificar tesis: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al consultar el estado de tu tesis: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}