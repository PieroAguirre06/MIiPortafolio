/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class FrmRubrica extends JFrame {
    private JTextField txtNombre, txtPuntajeMaximo;
    private JComboBox<String> cmbTipo;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private int idSeleccionado = -1;

    public FrmRubrica() {
        con = new Conexion().establecerConexion();
        initComponents();
        cargarTabla();
        setTitle("Rúbricas de Evaluación - UPLA IBERCAP");
        setSize(800, 550);
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
        JLabel lbl = new JLabel("GESTIÓN DE RÚBRICAS DE EVALUACIÓN", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 800, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Nombre Rúbrica:", 20, y);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, y, 350, 25);
        txtNombre.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtNombre);

        agregarLabel("Tipo:", 520, y);
        cmbTipo = new JComboBox<>(new String[]{
            "Plan_Cuantitativo", "Plan_Cualitativo", "Tesis_Cuantitativa", 
            "Tesis_Cualitativa", "TSP", "Sustentacion"
        });
        cmbTipo.setBounds(580, y, 180, 25);
        add(cmbTipo);

        y = 90;
        agregarLabel("Puntaje Máximo Total:", 20, y);
        txtPuntajeMaximo = new JTextField();
        txtPuntajeMaximo.setBounds(180, y, 120, 25);
        txtPuntajeMaximo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtPuntajeMaximo);

        y = 135;
        btnNuevo = crearBoton("Nuevo", new Color(26, 58, 107), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Nombre", "Tipo", "Puntaje Máximo"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(26, 58, 107));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 180, 775, 320);
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
                txtNombre.setText((String) modelo.getValueAt(fila, 1));
                cmbTipo.setSelectedItem(modelo.getValueAt(fila, 2));
                txtPuntajeMaximo.setText(modelo.getValueAt(fila, 3).toString());
            }
        });
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre de la rúbrica");
            return;
        }
        try {
            String sql = "INSERT INTO rubrica_catalogo (nombre, tipo, puntaje_maximo_total) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, (String) cmbTipo.getSelectedItem());
            ps.setBigDecimal(3, new BigDecimal(txtPuntajeMaximo.getText().trim()));
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Rúbrica guardada");
                cargarTabla();
                limpiar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una rúbrica");
            return;
        }
        try {
            String sql = "UPDATE rubrica_catalogo SET nombre=?, tipo=?, puntaje_maximo_total=? WHERE id_rubrica=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, (String) cmbTipo.getSelectedItem());
            ps.setBigDecimal(3, new BigDecimal(txtPuntajeMaximo.getText().trim()));
            ps.setInt(4, idSeleccionado);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Rúbrica actualizada");
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
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar rúbrica?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM rubrica_catalogo WHERE id_rubrica=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Rúbrica eliminada");
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
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM rubrica_catalogo ORDER BY id_rubrica");
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_rubrica"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getBigDecimal("puntaje_maximo_total")
                });
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        cmbTipo.setSelectedIndex(0);
        txtPuntajeMaximo.setText("");
        idSeleccionado = -1;
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 130, 25);
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
