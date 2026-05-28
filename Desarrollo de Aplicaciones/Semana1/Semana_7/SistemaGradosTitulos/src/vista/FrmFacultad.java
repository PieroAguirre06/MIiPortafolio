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

public class FrmFacultad extends JFrame {
    private JTextField txtNombre;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private int idSeleccionado = -1;

    public FrmFacultad() {
        con = new Conexion().establecerConexion();
        initComponents();
        cargarTabla();
        setTitle("Facultades - UPLA");
        setSize(600, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(30, 60, 114));
        header.setBounds(0, 0, 600, 35);
        add(header);
        JLabel lbl = new JLabel("FACULTADES", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 600, 25);
        header.add(lbl);

        agregarLabel("Nombre Facultad:", 20, 60);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, 60, 400, 30);
        txtNombre.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtNombre);

        btnNuevo = crearBoton("Nuevo", new Color(30, 60, 114), 20, 110);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, 110);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, 110);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, 110);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, 110);

        String[] cols = {"ID", "Nombre de Facultad"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(30, 60, 114));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 155, 575, 280);
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
            }
        });
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre de facultad");
            return;
        }
        try {
            String sql = "INSERT INTO facultad (nombre) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtNombre.getText().trim());
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Facultad guardada");
                cargarTabla();
                limpiar();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una facultad");
            return;
        }
        try {
            String sql = "UPDATE facultad SET nombre=? WHERE id_facultad=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtNombre.getText().trim());
            ps.setInt(2, idSeleccionado);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Facultad actualizada");
                cargarTabla();
                limpiar();
                idSeleccionado = -1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una facultad");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar facultad?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM facultad WHERE id_facultad=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Facultad eliminada");
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
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM facultad ORDER BY id_facultad");
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt("id_facultad"), rs.getString("nombre")});
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        idSeleccionado = -1;
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 120, 30);
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