/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.Conexion;
import controlador.ProyectoController;
import controlador.UsuarioController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class FrmEvaluacion extends JFrame {
    private JComboBox<String> cmbProyecto, cmbEvaluador, cmbRubrica, cmbEtapa, cmbCondicion;
    private JTextArea txtComentarios;
    private JTextField txtPuntajeObtenido;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private ProyectoController proyectoCtrl;  // ← NOMBRE CORRECTO (sin 's')
    private UsuarioController usuarioCtrl;
    private int idSeleccionado = -1;

    public FrmEvaluacion() {
        con = new Conexion().establecerConexion();
        proyectoCtrl = new ProyectoController();  // ← NOMBRE CORRECTO
        usuarioCtrl = new UsuarioController();
        initComponents();
        cargarTabla();
        cargarCombos();
        setTitle("Evaluaciones de Proyectos - UPLA");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(30, 60, 114));
        header.setBounds(0, 0, 850, 35);
        add(header);
        JLabel lbl = new JLabel("EVALUACIONES DE PROYECTOS", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 850, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Proyecto:", 20, y);
        cmbProyecto = new JComboBox<>();
        cmbProyecto.setBounds(100, y, 450, 25);
        add(cmbProyecto);

        agregarLabel("Evaluador:", 580, y);
        cmbEvaluador = new JComboBox<>();
        cmbEvaluador.setBounds(660, y, 160, 25);
        add(cmbEvaluador);

        y = 90;
        agregarLabel("Rúbrica:", 20, y);
        cmbRubrica = new JComboBox<>();
        cmbRubrica.setBounds(100, y, 300, 25);
        add(cmbRubrica);

        agregarLabel("Etapa:", 430, y);
        cmbEtapa = new JComboBox<>(new String[]{"Revision_Plan", "Revision_Borrador_Final", "Sustentacion"});
        cmbEtapa.setBounds(490, y, 150, 25);
        add(cmbEtapa);

        agregarLabel("Condición Final:", 660, y);
        cmbCondicion = new JComboBox<>(new String[]{"Aprobado", "Aprobado_Observaciones_Menores", "Desaprobado_Observaciones_Mayores", "Desaprobado"});
        cmbCondicion.setBounds(660, y, 160, 25);
        add(cmbCondicion);

        y = 130;
        agregarLabel("Puntaje Obtenido:", 20, y);
        txtPuntajeObtenido = new JTextField();
        txtPuntajeObtenido.setBounds(150, y, 120, 25);
        txtPuntajeObtenido.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtPuntajeObtenido);

        agregarLabel("Comentarios Generales:", 300, y);
        txtComentarios = new JTextArea();
        txtComentarios.setLineWrap(true);
        txtComentarios.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollCom = new JScrollPane(txtComentarios);
        scrollCom.setBounds(450, y, 370, 60);
        add(scrollCom);

        y = 205;
        btnNuevo = crearBoton("Nuevo", new Color(30, 60, 114), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Proyecto", "Evaluador", "Rúbrica", "Etapa", "Puntaje", "Condición", "Fecha"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(30, 60, 114));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 250, 825, 310);
        add(scroll);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                cargarDatosDesdeBD();
            }
        });
    }

    private void cargarCombos() {
        // Cargar proyectos - USANDO proyectoCtrl (con nombre correcto)
        cmbProyecto.removeAllItems();
        try {
            java.util.List<String> proyectos = proyectoCtrl.listarProyectosCombo();
            if (proyectos.isEmpty()) {
                cmbProyecto.addItem("No hay proyectos registrados");
            } else {
                for (String item : proyectos) {
                    cmbProyecto.addItem(item);
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargar proyectos: " + e.getMessage());
            cmbProyecto.addItem("Error al cargar proyectos");
        }

        // Cargar evaluadores (solo Docentes)
        cmbEvaluador.removeAllItems();
        java.util.List<modelo.Usuario> docentes = usuarioCtrl.listarPorRol("Docente");
        if (docentes.isEmpty()) {
            cmbEvaluador.addItem("No hay docentes registrados");
        } else {
            for (modelo.Usuario u : docentes) {
                cmbEvaluador.addItem(u.getCodigo() + " - " + u.getNombres() + " " + u.getApellidos());
            }
        }

        // Cargar rúbricas
        cmbRubrica.removeAllItems();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT id_rubrica, nombre, puntaje_maximo_total FROM rubrica_catalogo");
            while (rs.next()) {
                cmbRubrica.addItem(rs.getInt("id_rubrica") + " - " + rs.getString("nombre") + " (Max: " + rs.getBigDecimal("puntaje_maximo_total") + ")");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error cargar rúbricas: " + ex.getMessage());
        }
    }

    private void guardar() {
        if (cmbProyecto.getSelectedItem() == null || cmbEvaluador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione proyecto y evaluador");
            return;
        }
        
        if (cmbProyecto.getSelectedItem().toString().equals("No hay proyectos registrados")) {
            JOptionPane.showMessageDialog(this, "No hay proyectos disponibles para evaluar");
            return;
        }
        
        try {
            String sql = "INSERT INTO evaluacion_cabecera (id_proyecto, codigo_evaluador, id_rubrica, etapa, comentarios_generales, puntaje_obtenido, condicion_final) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            String selectedProy = (String) cmbProyecto.getSelectedItem();
            ps.setInt(1, Integer.parseInt(selectedProy.split(" - ")[0]));
            
            String selectedEval = (String) cmbEvaluador.getSelectedItem();
            ps.setString(2, selectedEval.split(" - ")[0]);
            
            String selectedRub = (String) cmbRubrica.getSelectedItem();
            ps.setInt(3, Integer.parseInt(selectedRub.split(" - ")[0]));
            
            ps.setString(4, (String) cmbEtapa.getSelectedItem());
            ps.setString(5, txtComentarios.getText());
            
            double puntaje = 0;
            try {
                puntaje = Double.parseDouble(txtPuntajeObtenido.getText().trim());
            } catch (NumberFormatException e) {
                puntaje = 0;
            }
            ps.setBigDecimal(6, new BigDecimal(puntaje));
            ps.setString(7, (String) cmbCondicion.getSelectedItem());
            
            if (ps.executeUpdate() > 0) {
                // Actualizar estado del proyecto
                String selectedProyId = selectedProy.split(" - ")[0];
                PreparedStatement psUpdate = con.prepareStatement("UPDATE proyecto SET estado = 'Aprobado_Por_Asesor' WHERE id_proyecto = ?");
                psUpdate.setInt(1, Integer.parseInt(selectedProyId));
                psUpdate.executeUpdate();
                psUpdate.close();
                
                JOptionPane.showMessageDialog(this, "Evaluación guardada y proyecto actualizado");
                cargarTabla();
                limpiar();
                cargarCombos(); // Recargar combos
            }
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void editar() {
        if (idSeleccionado == -1) return;
        try {
            String sql = "UPDATE evaluacion_cabecera SET comentarios_generales=?, puntaje_obtenido=?, condicion_final=? WHERE id_evaluacion=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtComentarios.getText());
            
            double puntaje = 0;
            try {
                puntaje = Double.parseDouble(txtPuntajeObtenido.getText().trim());
            } catch (NumberFormatException e) {
                puntaje = 0;
            }
            ps.setBigDecimal(2, new BigDecimal(puntaje));
            ps.setString(3, (String) cmbCondicion.getSelectedItem());
            ps.setInt(4, idSeleccionado);
            
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Evaluación actualizada");
                cargarTabla();
                limpiar();
                idSeleccionado = -1;
            }
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) return;
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar evaluación?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM evaluacion_cabecera WHERE id_evaluacion=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Evaluación eliminada");
                    cargarTabla();
                    limpiar();
                    idSeleccionado = -1;
                }
                ps.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void cargarDatosDesdeBD() {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM evaluacion_cabecera WHERE id_evaluacion=?");
            ps.setInt(1, idSeleccionado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idProyecto = rs.getInt("id_proyecto");
                String codigoEvaluador = rs.getString("codigo_evaluador");
                int idRubrica = rs.getInt("id_rubrica");
                
                // Seleccionar proyecto
                for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
                    String item = cmbProyecto.getItemAt(i);
                    if (item.startsWith(idProyecto + " -")) {
                        cmbProyecto.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Seleccionar evaluador
                for (int i = 0; i < cmbEvaluador.getItemCount(); i++) {
                    String item = cmbEvaluador.getItemAt(i);
                    if (item.startsWith(codigoEvaluador + " -")) {
                        cmbEvaluador.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Seleccionar rúbrica
                for (int i = 0; i < cmbRubrica.getItemCount(); i++) {
                    String item = cmbRubrica.getItemAt(i);
                    if (item.startsWith(idRubrica + " -")) {
                        cmbRubrica.setSelectedIndex(i);
                        break;
                    }
                }
                
                cmbEtapa.setSelectedItem(rs.getString("etapa"));
                txtComentarios.setText(rs.getString("comentarios_generales"));
                txtPuntajeObtenido.setText(rs.getBigDecimal("puntaje_obtenido").toString());
                cmbCondicion.setSelectedItem(rs.getString("condicion_final"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error cargar datos: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            String sql = "SELECT e.id_evaluacion, p.id_proyecto, p.titulo, u.nombres, u.apellidos, r.nombre, e.etapa, e.puntaje_obtenido, e.condicion_final, e.fecha_evaluacion " +
                         "FROM evaluacion_cabecera e " +
                         "JOIN proyecto p ON e.id_proyecto = p.id_proyecto " +
                         "JOIN usuario u ON e.codigo_evaluador = u.codigo " +
                         "JOIN rubrica_catalogo r ON e.id_rubrica = r.id_rubrica " +
                         "ORDER BY e.id_evaluacion DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                if (titulo.length() > 30) titulo = titulo.substring(0, 30) + "...";
                modelo.addRow(new Object[]{
                    rs.getInt("id_evaluacion"),
                    rs.getInt("id_proyecto") + " - " + titulo,
                    rs.getString("nombres") + " " + rs.getString("apellidos"),
                    rs.getString("nombre"),
                    rs.getString("etapa"),
                    rs.getBigDecimal("puntaje_obtenido"),
                    rs.getString("condicion_final"),
                    rs.getTimestamp("fecha_evaluacion")
                });
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error cargar tabla: " + ex.getMessage());
        }
    }

    private void limpiar() {
        if (cmbProyecto.getItemCount() > 0 && !cmbProyecto.getItemAt(0).equals("No hay proyectos registrados")) {
            cmbProyecto.setSelectedIndex(0);
        }
        if (cmbEvaluador.getItemCount() > 0 && !cmbEvaluador.getItemAt(0).equals("No hay docentes registrados")) {
            cmbEvaluador.setSelectedIndex(0);
        }
        if (cmbRubrica.getItemCount() > 0) cmbRubrica.setSelectedIndex(0);
        cmbEtapa.setSelectedIndex(0);
        txtComentarios.setText("");
        txtPuntajeObtenido.setText("");
        cmbCondicion.setSelectedIndex(0);
        idSeleccionado = -1;
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 120, 25);
        add(lbl);
    }

    private JButton crearBoton(String texto, Color color, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 80, 30);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        add(btn);
        return btn;
    }
}