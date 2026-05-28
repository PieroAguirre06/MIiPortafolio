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

public class FrmAsesor extends JFrame {
    private JComboBox<String> cmbProyecto, cmbDocente, cmbEstado;
    private JTextField txtResolucion;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private ProyectoController proyectoCtrl;
    private UsuarioController usuarioCtrl;
    private int idSeleccionado = -1;

    public FrmAsesor() {
        con = new Conexion().establecerConexion();
        proyectoCtrl = new ProyectoController();
        usuarioCtrl = new UsuarioController();
        initComponents();
        cargarTabla();
        cargarCombos();
        setTitle("Asesores de Proyectos - UPLA IBERCAP");
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
        JLabel lbl = new JLabel("GESTIÓN DE ASESORES DE PROYECTOS", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 800, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Proyecto:", 20, y);
        cmbProyecto = new JComboBox<>();
        cmbProyecto.setBounds(100, y, 500, 25);
        add(cmbProyecto);

        y = 90;
        agregarLabel("Docente Asesor:", 20, y);
        cmbDocente = new JComboBox<>();
        cmbDocente.setBounds(150, y, 400, 25);
        add(cmbDocente);

        agregarLabel("Estado:", 580, y);
        cmbEstado = new JComboBox<>(new String[]{"Activo", "Renuncia", "Cambiado"});
        cmbEstado.setBounds(640, y, 100, 25);
        add(cmbEstado);

        y = 130;
        agregarLabel("Resolución Asignación:", 20, y);
        txtResolucion = new JTextField();
        txtResolucion.setBounds(180, y, 350, 25);
        txtResolucion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtResolucion);

        y = 175;
        btnNuevo = crearBoton("Nuevo", new Color(26, 58, 107), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Proyecto", "Docente", "Resolución", "Estado"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(26, 58, 107));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 220, 775, 310);
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
        cmbProyecto.removeAllItems();
        java.util.List<String> proyectos = proyectoCtrl.listarProyectosCombo();
        if (proyectos.isEmpty()) {
            cmbProyecto.addItem("No hay proyectos registrados");
        } else {
            for (String item : proyectos) {
                cmbProyecto.addItem(item);
            }
        }

        cmbDocente.removeAllItems();
        java.util.List<modelo.Usuario> docentes = usuarioCtrl.listarPorRol("Docente");
        if (docentes.isEmpty()) {
            cmbDocente.addItem("No hay docentes registrados");
        } else {
            for (modelo.Usuario u : docentes) {
                cmbDocente.addItem(u.getCodigo() + " - " + u.getNombres() + " " + u.getApellidos());
            }
        }
    }

    private void guardar() {
        if (cmbProyecto.getSelectedItem() == null || cmbDocente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione proyecto y docente");
            return;
        }
        if (cmbProyecto.getSelectedItem().toString().equals("No hay proyectos registrados")) {
            JOptionPane.showMessageDialog(this, "No hay proyectos disponibles");
            return;
        }
        try {
            String sql = "INSERT INTO proyecto_asesor (id_proyecto, codigo_docente, resolucion_asignacion, estado) VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            String selectedProy = (String) cmbProyecto.getSelectedItem();
            ps.setInt(1, Integer.parseInt(selectedProy.split(" - ")[0]));
            String selectedDoc = (String) cmbDocente.getSelectedItem();
            ps.setString(2, selectedDoc.split(" - ")[0]);
            ps.setString(3, txtResolucion.getText().trim());
            ps.setString(4, (String) cmbEstado.getSelectedItem());
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Asesor asignado");
                cargarTabla();
                limpiar();
                cargarCombos();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editar() {
        if (idSeleccionado == -1) return;
        try {
            String sql = "UPDATE proyecto_asesor SET resolucion_asignacion=?, estado=? WHERE id_asignacion=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtResolucion.getText().trim());
            ps.setString(2, (String) cmbEstado.getSelectedItem());
            ps.setInt(3, idSeleccionado);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Asesor actualizado");
                cargarTabla();
                limpiar();
                idSeleccionado = -1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) return;
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar asignación de asesor?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM proyecto_asesor WHERE id_asignacion=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Asesor eliminado");
                    cargarTabla();
                    limpiar();
                    idSeleccionado = -1;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void cargarDatosDesdeBD() {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM proyecto_asesor WHERE id_asignacion=?");
            ps.setInt(1, idSeleccionado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idProyecto = rs.getInt("id_proyecto");
                String codigoDocente = rs.getString("codigo_docente");
                for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
                    if (cmbProyecto.getItemAt(i).startsWith(idProyecto + " -")) {
                        cmbProyecto.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < cmbDocente.getItemCount(); i++) {
                    if (cmbDocente.getItemAt(i).startsWith(codigoDocente + " -")) {
                        cmbDocente.setSelectedIndex(i);
                        break;
                    }
                }
                txtResolucion.setText(rs.getString("resolucion_asignacion"));
                cmbEstado.setSelectedItem(rs.getString("estado"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            String sql = "SELECT a.id_asignacion, p.id_proyecto, p.titulo, u.nombres, u.apellidos, a.resolucion_asignacion, a.estado " +
                         "FROM proyecto_asesor a JOIN proyecto p ON a.id_proyecto = p.id_proyecto " +
                         "JOIN usuario u ON a.codigo_docente = u.codigo ORDER BY a.id_asignacion DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                if (titulo.length() > 40) titulo = titulo.substring(0, 40) + "...";
                modelo.addRow(new Object[]{
                    rs.getInt("id_asignacion"),
                    rs.getInt("id_proyecto") + " - " + titulo,
                    rs.getString("nombres") + " " + rs.getString("apellidos"),
                    rs.getString("resolucion_asignacion"),
                    rs.getString("estado")
                });
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        if (cmbProyecto.getItemCount() > 0 && !cmbProyecto.getItemAt(0).equals("No hay proyectos registrados")) {
            cmbProyecto.setSelectedIndex(0);
        }
        if (cmbDocente.getItemCount() > 0 && !cmbDocente.getItemAt(0).equals("No hay docentes registrados")) {
            cmbDocente.setSelectedIndex(0);
        }
        txtResolucion.setText("");
        cmbEstado.setSelectedIndex(0);
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