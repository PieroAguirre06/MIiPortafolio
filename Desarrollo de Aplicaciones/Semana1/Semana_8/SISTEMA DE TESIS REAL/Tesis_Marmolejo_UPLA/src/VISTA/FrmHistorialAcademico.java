/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.Conexion;
import controlador.UsuarioController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrmHistorialAcademico extends JFrame {
    private JComboBox<String> cmbEstudiante, cmbPrograma, cmbCondicion;
    private JTextField txtFechaEgreso, txtFechaBachiller;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private UsuarioController usuarioCtrl;
    private int idSeleccionado = -1;

    public FrmHistorialAcademico() {
        con = new Conexion().establecerConexion();
        usuarioCtrl = new UsuarioController();
        initComponents();
        cargarTabla();
        cargarCombos();
        setTitle("Historial Académico - UPLA IBERCAP");
        setSize(800, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(26, 58, 107));
        header.setBounds(0, 0, 800, 35);
        add(header);
        JLabel lbl = new JLabel("GESTIÓN DE HISTORIAL ACADÉMICO", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 800, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Estudiante:", 20, y);
        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setBounds(110, y, 350, 25);
        add(cmbEstudiante);

        agregarLabel("Programa:", 480, y);
        cmbPrograma = new JComboBox<>();
        cmbPrograma.setBounds(560, y, 200, 25);
        add(cmbPrograma);

        y = 90;
        agregarLabel("Condición Actual:", 20, y);
        cmbCondicion = new JComboBox<>(new String[]{"Egresado", "Bachiller", "Titulado"});
        cmbCondicion.setBounds(150, y, 150, 25);
        add(cmbCondicion);

        agregarLabel("Fecha Egreso (YYYY-MM-DD):", 330, y);
        txtFechaEgreso = new JTextField();
        txtFechaEgreso.setBounds(530, y, 120, 25);
        txtFechaEgreso.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtFechaEgreso);

        agregarLabel("Fecha Bachiller:", 680, y);
        txtFechaBachiller = new JTextField();
        txtFechaBachiller.setBounds(680, y, 90, 25);
        txtFechaBachiller.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtFechaBachiller);

        y = 135;
        btnNuevo = crearBoton("Nuevo", new Color(26, 58, 107), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Estudiante", "Programa", "Condición", "Egreso", "Bachiller"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(26, 58, 107));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 180, 775, 350);
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
                txtFechaEgreso.setText(modelo.getValueAt(fila, 4) != null ? modelo.getValueAt(fila, 4).toString() : "");
                txtFechaBachiller.setText(modelo.getValueAt(fila, 5) != null ? modelo.getValueAt(fila, 5).toString() : "");
                cmbCondicion.setSelectedItem(modelo.getValueAt(fila, 3));
            }
        });
    }

    private void cargarCombos() {
        cmbEstudiante.removeAllItems();
        for (String item : usuarioCtrl.listarEstudiantesConNombre()) {
            cmbEstudiante.addItem(item);
        }

        cmbPrograma.removeAllItems();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT id_programa, nombre FROM programa_estudios ORDER BY nombre");
            while (rs.next()) {
                cmbPrograma.addItem(rs.getInt("id_programa") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void guardar() {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO historial_academico (codigo_estudiante, id_programa, condicion_actual, fecha_egreso, fecha_bachiller) VALUES (?,?,?,?,?)");
            String selectedEst = (String) cmbEstudiante.getSelectedItem();
            ps.setString(1, selectedEst.split(" - ")[0]);
            String selectedProg = (String) cmbPrograma.getSelectedItem();
            ps.setInt(2, Integer.parseInt(selectedProg.split(" - ")[0]));
            ps.setString(3, (String) cmbCondicion.getSelectedItem());
            ps.setDate(4, txtFechaEgreso.getText().trim().isEmpty() ? null : java.sql.Date.valueOf(txtFechaEgreso.getText().trim()));
            ps.setDate(5, txtFechaBachiller.getText().trim().isEmpty() ? null : java.sql.Date.valueOf(txtFechaBachiller.getText().trim()));
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Historial guardado");
                cargarTabla();
                limpiar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editar() {
        if (idSeleccionado == -1) return;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE historial_academico SET condicion_actual=?, fecha_egreso=?, fecha_bachiller=? WHERE id_historial=?");
            ps.setString(1, (String) cmbCondicion.getSelectedItem());
            ps.setDate(2, txtFechaEgreso.getText().trim().isEmpty() ? null : java.sql.Date.valueOf(txtFechaEgreso.getText().trim()));
            ps.setDate(3, txtFechaBachiller.getText().trim().isEmpty() ? null : java.sql.Date.valueOf(txtFechaBachiller.getText().trim()));
            ps.setInt(4, idSeleccionado);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Historial actualizado");
                cargarTabla();
                limpiar();
                idSeleccionado = -1;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) return;
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar historial?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM historial_academico WHERE id_historial=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Historial eliminado");
                    cargarTabla();
                    limpiar();
                    idSeleccionado = -1;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            String sql = "SELECT h.id_historial, u.nombres, u.apellidos, p.nombre, h.condicion_actual, h.fecha_egreso, h.fecha_bachiller " +
                         "FROM historial_academico h JOIN usuario u ON h.codigo_estudiante = u.codigo " +
                         "JOIN programa_estudios p ON h.id_programa = p.id_programa ORDER BY h.id_historial DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_historial"),
                    rs.getString("nombres") + " " + rs.getString("apellidos"),
                    rs.getString("nombre"),
                    rs.getString("condicion_actual"),
                    rs.getDate("fecha_egreso"),
                    rs.getDate("fecha_bachiller")
                });
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtFechaEgreso.setText("");
        txtFechaBachiller.setText("");
        cmbCondicion.setSelectedIndex(0);
        idSeleccionado = -1;
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 140, 25);
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