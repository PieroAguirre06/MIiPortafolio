/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.Conexion;
import controlador.TramiteController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FrmDocumento extends JFrame {
    private JComboBox<String> cmbTramite, cmbTipoDocumento;
    private JTextField txtRutaArchivo;
    private JTextArea txtObservacion;
    private JComboBox<String> cmbEstadoValidacion;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar, btnSeleccionarArchivo;
    private JTable tabla;
    private DefaultTableModel modelo;
    private Connection con;
    private TramiteController tramCtrl;
    private int idSeleccionado = -1;

    public FrmDocumento() {
        con = new Conexion().establecerConexion();
        tramCtrl = new TramiteController();
        initComponents();
        cargarTabla();
        cargarCombos();
        setTitle("Documentos - UPLA");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(30, 60, 114));
        header.setBounds(0, 0, 900, 35);
        add(header);
        JLabel lbl = new JLabel("DOCUMENTOS DE TRÁMITE", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 900, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Trámite:", 20, y);
        cmbTramite = new JComboBox<>();
        cmbTramite.setBounds(100, y, 350, 25);
        add(cmbTramite);

        agregarLabel("Tipo Documento:", 480, y);
        cmbTipoDocumento = new JComboBox<>(new String[]{
            "Solicitud_FUT", "DNI_Copia", "Foto_Pasaporte", "Constancia_Matricula",
            "Certificado_Idiomas", "Recibo_Pago", "Certificado_Trabajo_TSP",
            "Plan_Tesis", "Borrador_Tesis", "Informe_Similitud"
        });
        cmbTipoDocumento.setBounds(600, y, 250, 25);
        add(cmbTipoDocumento);

        y = 90;
        agregarLabel("Archivo:", 20, y);
        txtRutaArchivo = new JTextField();
        txtRutaArchivo.setBounds(100, y, 550, 25);
        txtRutaArchivo.setEditable(false);
        txtRutaArchivo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtRutaArchivo);
        
        btnSeleccionarArchivo = new JButton("Seleccionar");
        btnSeleccionarArchivo.setBounds(660, y, 100, 25);
        btnSeleccionarArchivo.setBackground(new Color(30, 60, 114));
        btnSeleccionarArchivo.setForeground(Color.WHITE);
        add(btnSeleccionarArchivo);

        agregarLabel("Estado Validación:", 20, 130);
        cmbEstadoValidacion = new JComboBox<>(new String[]{"Pendiente", "Validado", "Observado"});
        cmbEstadoValidacion.setBounds(150, 130, 150, 25);
        add(cmbEstadoValidacion);

        agregarLabel("Observación:", 350, 130);
        txtObservacion = new JTextArea();
        txtObservacion.setLineWrap(true);
        txtObservacion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollObs = new JScrollPane(txtObservacion);
        scrollObs.setBounds(450, 130, 400, 60);
        add(scrollObs);

        y = 205;
        btnNuevo = crearBoton("Nuevo", new Color(30, 60, 114), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Trámite", "Tipo Documento", "Archivo", "Estado", "Fecha Subida"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(30, 60, 114));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 245, 875, 310);
        add(scroll);

        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
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
        cmbTramite.removeAllItems();
        try {
            String sql = "SELECT t.id_tramite, u.nombres, u.apellidos, t.tipo_tramite " +
                         "FROM tramite t JOIN usuario u ON t.codigo_estudiante = u.codigo " +
                         "ORDER BY t.id_tramite DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                cmbTramite.addItem(rs.getInt("id_tramite") + " - " + rs.getString("nombres") + " " + rs.getString("apellidos") + " (" + rs.getString("tipo_tramite") + ")");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Documentos PDF, DOC, DOCX", "pdf", "doc", "docx", "jpg", "png");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtRutaArchivo.setText(selectedFile.getAbsolutePath());
        }
    }

    private void guardar() {
        if (cmbTramite.getSelectedItem() == null || txtRutaArchivo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios");
            return;
        }
        try {
            String sql = "INSERT INTO documento_requisito (id_tramite, tipo_documento, ruta_archivo, estado_validacion, observacion) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            String selected = (String) cmbTramite.getSelectedItem();
            ps.setInt(1, Integer.parseInt(selected.split(" - ")[0]));
            ps.setString(2, (String) cmbTipoDocumento.getSelectedItem());
            ps.setString(3, txtRutaArchivo.getText());
            ps.setString(4, (String) cmbEstadoValidacion.getSelectedItem());
            ps.setString(5, txtObservacion.getText());
            
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Documento registrado");
                cargarTabla();
                limpiar();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un documento");
            return;
        }
        try {
            String sql = "UPDATE documento_requisito SET estado_validacion=?, observacion=? WHERE id_documento=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, (String) cmbEstadoValidacion.getSelectedItem());
            ps.setString(2, txtObservacion.getText());
            ps.setInt(3, idSeleccionado);
            
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Documento actualizado");
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
            JOptionPane.showMessageDialog(this, "Seleccione un documento");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar documento?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM documento_requisito WHERE id_documento=?");
                ps.setInt(1, idSeleccionado);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Documento eliminado");
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM documento_requisito WHERE id_documento=?");
            ps.setInt(1, idSeleccionado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Seleccionar trámite
                int idTramite = rs.getInt("id_tramite");
                for (int i = 0; i < cmbTramite.getItemCount(); i++) {
                    String item = cmbTramite.getItemAt(i);
                    if (item.startsWith(idTramite + " -")) {
                        cmbTramite.setSelectedIndex(i);
                        break;
                    }
                }
                cmbTipoDocumento.setSelectedItem(rs.getString("tipo_documento"));
                txtRutaArchivo.setText(rs.getString("ruta_archivo"));
                cmbEstadoValidacion.setSelectedItem(rs.getString("estado_validacion"));
                txtObservacion.setText(rs.getString("observacion"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            String sql = "SELECT d.id_documento, t.id_tramite, u.nombres, u.apellidos, d.tipo_documento, d.ruta_archivo, d.estado_validacion, d.fecha_subida " +
                         "FROM documento_requisito d JOIN tramite t ON d.id_tramite = t.id_tramite " +
                         "JOIN usuario u ON t.codigo_estudiante = u.codigo ORDER BY d.id_documento DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_documento"),
                    "T" + rs.getInt("id_tramite") + " - " + rs.getString("nombres") + " " + rs.getString("apellidos"),
                    rs.getString("tipo_documento"),
                    rs.getString("ruta_archivo"),
                    rs.getString("estado_validacion"),
                    rs.getTimestamp("fecha_subida")
                });
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        if (cmbTramite.getItemCount() > 0) cmbTramite.setSelectedIndex(0);
        cmbTipoDocumento.setSelectedIndex(0);
        txtRutaArchivo.setText("");
        cmbEstadoValidacion.setSelectedIndex(0);
        txtObservacion.setText("");
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