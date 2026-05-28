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
import java.util.Vector;

public class FrmProgramaEstudios extends JFrame {
    private JComboBox<String> cmbFacultad;
    private JTextField txtNombre, txtGrado, txtTitulo;
    private JComboBox<String> cmbModalidad;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private int idSeleccionado = -1;

    public FrmProgramaEstudios() {
        con = new Conexion().establecerConexion();
        initComponents();
        cargarTabla();
        cargarFacultades();
        setTitle("Programas de Estudio - UPLA");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(30, 60, 114));
        header.setBounds(0, 0, 800, 35);
        add(header);
        JLabel lbl = new JLabel("PROGRAMAS DE ESTUDIO", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 800, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Facultad:", 20, y);
        cmbFacultad = new JComboBox<>();
        cmbFacultad.setBounds(100, y, 350, 25);
        add(cmbFacultad);

        agregarLabel("Modalidad:", 500, y);
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Semipresencial", "A_Distancia"});
        cmbModalidad.setBounds(580, y, 180, 25);
        add(cmbModalidad);

        y = 90;
        agregarLabel("Nombre Programa:", 20, y);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, y, 600, 25);
        txtNombre.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtNombre);

        y = 130;
        agregarLabel("Grado Académico:", 20, y);
        txtGrado = new JTextField();
        txtGrado.setBounds(150, y, 300, 25);
        txtGrado.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtGrado);

        agregarLabel("Título Profesional:", 480, y);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(600, y, 150, 25);
        txtTitulo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtTitulo);

        y = 175;
        btnNuevo = crearBoton("Nuevo", new Color(30, 60, 114), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Facultad", "Programa", "Grado", "Título", "Modalidad"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(30, 60, 114));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 215, 775, 290);
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
                txtNombre.setText((String) modelo.getValueAt(fila, 2));
                txtGrado.setText((String) modelo.getValueAt(fila, 3));
                txtTitulo.setText((String) modelo.getValueAt(fila, 4));
                cmbModalidad.setSelectedItem(modelo.getValueAt(fila, 5));
                
                // Seleccionar facultad
                String facultadNombre = (String) modelo.getValueAt(fila, 1);
                for (int i = 0; i < cmbFacultad.getItemCount(); i++) {
                    if (cmbFacultad.getItemAt(i).equals(facultadNombre)) {
                        cmbFacultad.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
    }

    private void cargarFacultades() {
        cmbFacultad.removeAllItems();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT id_facultad, nombre FROM facultad ORDER BY nombre");
            while (rs.next()) {
                cmbFacultad.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private int obtenerIdFacultad() {
        String nombreFacultad = (String) cmbFacultad.getSelectedItem();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id_facultad FROM facultad WHERE nombre=?");
            ps.setString(1, nombreFacultad);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_facultad");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return -1;
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre del programa");
            return;
        }
        try {
            String sql = "INSERT INTO programa_estudios (id_facultad, nombre, grado_academico, titulo_profesional, modalidad_estudio) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, obtenerIdFacultad());
            ps.setString(2, txtNombre.getText().trim());
            ps.setString(3, txtGrado.getText().trim());
            ps.setString(4, txtTitulo.getText().trim());
            ps.setString(5, (String) cmbModalidad.getSelectedItem());
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Programa guardado");
                cargarTabla();
                limpiar();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un programa");
            return;
        }
        try {
            String sql = "UPDATE programa_estudios SET id_facultad=?, nombre=?, grado_academico=?, titulo_profesional=?, modalidad_estudio=? WHERE id_programa=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, obtenerIdFacultad());
            ps.setString(2, txtNombre.getText().trim());
            ps.setString(3, txtGrado.getText().trim());
            ps.setString(4, txtTitulo.getText().trim());
            ps.setString(5, (String) cmbModalidad.getSelectedItem());
            ps.setInt(6, idSeleccionado);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Programa actualizado");
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
            JOptionPane.showMessageDialog(this, "Seleccione un programa");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar programa?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM programa_estudios WHERE id_programa=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Programa eliminado");
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
            String sql = "SELECT p.id_programa, f.nombre as facultad, p.nombre, p.grado_academico, p.titulo_profesional, p.modalidad_estudio " +
                         "FROM programa_estudios p JOIN facultad f ON p.id_facultad = f.id_facultad ORDER BY p.id_programa";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_programa"), rs.getString("facultad"),
                    rs.getString("nombre"), rs.getString("grado_academico"),
                    rs.getString("titulo_profesional"), rs.getString("modalidad_estudio")
                });
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtGrado.setText("");
        txtTitulo.setText("");
        if (cmbFacultad.getItemCount() > 0) cmbFacultad.setSelectedIndex(0);
        cmbModalidad.setSelectedIndex(0);
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