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
        setTitle("Menú Principal - Sistema de Grados y Títulos UPLA");
        setSize(650, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(30, 60, 114));

        JLabel lblTitulo = new JLabel("Sistema de Grados y Títulos - UPLA", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBounds(0, 8, 650, 26);
        add(lblTitulo);

        JLabel lblRol = new JLabel("Usuario: " + codigoUsuario + "  |  Rol: " + rolUsuario, SwingConstants.CENTER);
        lblRol.setForeground(new Color(180, 210, 255));
        lblRol.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRol.setBounds(0, 34, 650, 18);
        add(lblRol);

        JPanel panel = new JPanel(new GridLayout(4, 4, 8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBounds(15, 60, 620, 380);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(panel);

        // Todos los módulos del sistema
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

        // ==============================================================
        // BOTÓN ESPECIAL PARA ESTUDIANTES - "🎉 MIS LOGROS"
        // ==============================================================
        if (rolUsuario.equals("Estudiante")) {
            JButton btnLogros = new JButton("🎉 MIS LOGROS");
            btnLogros.setBackground(new Color(0, 128, 0));
            btnLogros.setForeground(Color.WHITE);
            btnLogros.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            btnLogros.setFocusPainted(false);
            btnLogros.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnLogros.setBounds(200, 460, 250, 40);
            
            btnLogros.addActionListener(e -> verificarTesisAprobada());
            add(btnLogros);
        }

        // Botón de cerrar sesión
        JButton btnSalir = new JButton("🚪 Cerrar sesión");
        btnSalir.setBackground(new Color(180, 40, 40));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 12));
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(470, 460, 150, 40);
        btnSalir.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });
        add(btnSalir);
    }

    private JButton crearBoton(String etiqueta, String modulo) {
        JButton btn = new JButton("<html><center>" + etiqueta + "</center></html>");
        btn.setBackground(new Color(30, 60, 114));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            switch (modulo) {
                case "Usuarios":
                    new FrmUsuario().setVisible(true);
                    break;
                case "Facultades":
                    new FrmFacultad().setVisible(true);
                    break;
                case "Programas":
                    new FrmProgramaEstudios().setVisible(true);
                    break;
                case "Historial":
                    new FrmHistorialAcademico().setVisible(true);
                    break;
                case "Tramites":
                    new FrmTramite().setVisible(true);
                    break;
                case "Documentos":
                    new FrmDocumento().setVisible(true);
                    break;
                case "Proyectos":
                    new FrmProyecto().setVisible(true);
                    break;
                case "Asesores":
                    new FrmAsesor().setVisible(true);
                    break;
                case "Jurados":
                    new FrmJurado().setVisible(true);
                    break;
                case "Rubricas":
                    new FrmRubrica().setVisible(true);
                    break;
                case "Evaluaciones":
                    new FrmEvaluacion().setVisible(true);
                    break;
                case "Sustentaciones":
                    new FrmSustentacion().setVisible(true);
                    break;
                case "Reportes":
                    JOptionPane.showMessageDialog(this, "Módulo de Reportes en construcción", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "Configuracion":
                    JOptionPane.showMessageDialog(this, "Configuración del sistema\n- Cambiar contraseña\n- Respaldos", "Configuración", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "Ayuda":
                    JOptionPane.showMessageDialog(this, "Sistema de Grados y Títulos UPLA\nVersión 1.0\n© 2024", "Ayuda", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "Salir":
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        new Login().setVisible(true);
                        this.dispose();
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Módulo \"" + modulo + "\" en construcción.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return btn;
    }

    // ==============================================================
    // MÉTODO CORREGIDO PARA VERIFICAR SI LA TESIS FUE APROBADA
    // ==============================================================
    private void verificarTesisAprobada() {
        try {
            System.out.println("=== VERIFICANDO TESIS PARA: " + codigoUsuario + " ===");
            
            // Consulta CORREGIDA - Verifica proyecto SUSTENTADO O con nota registrada
            String sql = "SELECT " +
                         "p.id_proyecto, " +
                         "p.titulo, " +
                         "p.estado, " +
                         "s.nota_final_numerica, " +
                         "s.condicion_acta, " +
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
                
                System.out.println("Estado Proyecto: " + estadoProyecto);
                System.out.println("Nota: " + nota);
                System.out.println("Condición: " + condicion);
                System.out.println("Estado Trámite: " + estadoTramite);
                
                // CONDICIÓN CORREGIDA - Tesis aprobada si:
                // 1. Proyecto tiene estado 'Sustentado' O
                // 2. Sustentación tiene nota mayor a 10 O
                // 3. Trámite está 'Culminado'
                boolean tesisAprobada = false;
                
                if (estadoProyecto != null && estadoProyecto.equals("Sustentado")) {
                    tesisAprobada = true;
                }
                
                if (nota > 0 && nota >= 11) {
                    tesisAprobada = true;
                }
                
                if (condicion != null && (condicion.equals("Excelente") || condicion.equals("Muy_Bueno") || condicion.equals("Bueno"))) {
                    tesisAprobada = true;
                }
                
                if (estadoTramite != null && estadoTramite.equals("Culminado")) {
                    tesisAprobada = true;
                }
                
                if (tesisAprobada && estadoProyecto != null) {
                    // MOSTRAR MENSAJE DE FELICITACIÓN
                    String mensaje = "🎉 ¡FELICITACIONES! 🎉\n\n" +
                                     "Estimado(a) estudiante,\n\n" +
                                     "✅ Su tesis ha sido APROBADA exitosamente.\n\n" +
                                     "📖 Título: " + (titulo != null ? titulo : "No especificado") + "\n" +
                                     "📊 Estado del proyecto: " + (estadoProyecto != null ? estadoProyecto : "N/A") + "\n" +
                                     "⭐ Nota final: " + (nota > 0 ? nota : "No registrada") + "\n" +
                                     "🏆 Condición: " + (condicion != null ? condicion : "No registrada") + "\n" +
                                     "📋 Estado trámite: " + (estadoTramite != null ? estadoTramite : "N/A") + "\n\n" +
                                     "🌟 ¡Usted ya es un profesional!\n" +
                                     "🌟 Pronto recibirá su título.\n\n" +
                                     "¡Enhorabuena por este gran logro!";
                    
                    JOptionPane.showMessageDialog(this, mensaje, "🎓 TESIS APROBADA - FELICITACIONES", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Mostrar progreso del estudiante
                    String mensaje = "📌 Estado actual de tu proceso:\n\n";
                    
                    if (estadoTramite == null) {
                        mensaje += "❌ No has iniciado ningún trámite\n";
                    } else {
                        mensaje += "✅ Trámite: " + estadoTramite + "\n";
                    }
                    
                    if (estadoProyecto == null) {
                        mensaje += "❌ No has registrado ningún proyecto\n";
                    } else {
                        mensaje += "✅ Proyecto: " + estadoProyecto + "\n";
                    }
                    
                    if (nota <= 0) {
                        mensaje += "❌ Aún no has sustentado tu tesis\n";
                    } else {
                        mensaje += "✅ Sustentación completada con nota: " + nota + "\n";
                    }
                    
                    mensaje += "\n📋 Pasos que te faltan:\n";
                    
                    if (estadoTramite == null || estadoTramite.equals("Iniciado")) {
                        mensaje += "   1️⃣ Completar tu trámite\n";
                    }
                    if (estadoProyecto == null || estadoProyecto.equals("Registrado")) {
                        mensaje += "   2️⃣ Registrar y aprobar tu proyecto\n";
                    }
                    if (estadoProyecto != null && !estadoProyecto.equals("Sustentado") && nota <= 0) {
                        mensaje += "   3️⃣ Sustentar tu tesis ante el jurado\n";
                    }
                    if (nota > 0 && nota < 11) {
                        mensaje += "   ⚠️ Tu nota fue menor a 11, debes mejorar tu tesis\n";
                    }
                    
                    mensaje += "\n💪 ¡Sigue adelante, pronto lo lograrás!";
                    
                    JOptionPane.showMessageDialog(this, mensaje, "🎓 Estado de tu Tesis", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // No hay ningún dato del estudiante
                String mensaje = "📌 Aún no tienes una tesis aprobada.\n\n" +
                                 "Para obtener tu título debes:\n" +
                                 "1️⃣ Completar tu trámite\n" +
                                 "2️⃣ Registrar tu proyecto\n" +
                                 "3️⃣ Tener tu proyecto evaluado\n" +
                                 "4️⃣ Sustentar tu tesis\n\n" +
                                 "💪 ¡Sigue adelante, pronto lo lograrás!";
                
                JOptionPane.showMessageDialog(this, mensaje, "🎓 Estado de tu Tesis", JOptionPane.INFORMATION_MESSAGE);
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