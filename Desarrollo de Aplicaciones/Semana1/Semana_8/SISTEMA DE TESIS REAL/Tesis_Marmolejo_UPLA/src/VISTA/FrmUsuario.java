/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Usuario;
import controlador.UsuarioController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmUsuario extends JFrame {

    private JTextField txtCodigo, txtDni, txtNombres, txtApellidos, txtEmail, txtOrcid;
    private JComboBox<String> cmbRol, cmbEstado;
    private JPasswordField txtContrasena;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private UsuarioController ctrl;
    private String codigoSeleccionado = null;

    public FrmUsuario() {
        ctrl = new UsuarioController();
        initComponents();
        cargarTabla();
        setTitle("Usuarios - UPLA IBERCAP");
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
        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBounds(0, 5, 800, 25);
        header.add(lblTitulo);

        int y = 50;
        agregarLabel("Código:", 20, y); 
        txtCodigo = agregarTextField(100, y, 120);
        
        agregarLabel("Rol:", 240, y);
        cmbRol = new JComboBox<>(new String[]{"Estudiante", "Docente", "Administrativo", "Decano"});
        cmbRol.setBounds(290, y, 140, 25); 
        add(cmbRol);

        agregarLabel("ORCID:", 450, y); 
        txtOrcid = agregarTextField(510, y, 250);

        y = 85;
        agregarLabel("DNI:", 20, y); 
        txtDni = agregarTextField(100, y, 120);
        agregarLabel("Nombres:", 240, y); 
        txtNombres = agregarTextField(320, y, 200);

        y = 120;
        agregarLabel("Apellidos:", 20, y); 
        txtApellidos = agregarTextField(100, y, 200);
        agregarLabel("Estado:", 320, y);
        cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        cmbEstado.setBounds(380, y, 120, 25); 
        add(cmbEstado);

        y = 155;
        agregarLabel("Email:", 20, y); 
        txtEmail = agregarTextField(100, y, 300);
        agregarLabel("Contraseña:", 420, y); 
        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(510, y, 250, 25);
        txtContrasena.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtContrasena);

        y = 195;
        btnNuevo = crearBoton("Nuevo", new Color(26, 58, 107), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] columnas = {"Código", "DNI", "Nombres", "Apellidos", "Rol", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(26, 58, 107));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 240, 775, 260);
        add(scroll);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String codigo = (String) modelo.getValueAt(fila, 0);
                Usuario u = ctrl.buscarPorCodigo(codigo);
                if (u != null) cargarDatos(u);
            }
        });
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Usuario u : ctrl.listar()) {
            modelo.addRow(new Object[]{u.getCodigo(), u.getDni(), u.getNombres(), u.getApellidos(), u.getRol(), u.getEstado()});
        }
    }

    private void cargarDatos(Usuario u) {
        txtCodigo.setText(u.getCodigo());
        txtDni.setText(u.getDni());
        txtNombres.setText(u.getNombres());
        txtApellidos.setText(u.getApellidos());
        txtEmail.setText(u.getEmailInstitucional());
        txtOrcid.setText(u.getCodigoOrcid() != null ? u.getCodigoOrcid() : "");
        cmbRol.setSelectedItem(u.getRol());
        cmbEstado.setSelectedItem(u.getEstado());
        txtContrasena.setText("");
        codigoSeleccionado = u.getCodigo();
    }

    private void guardar() {
        Usuario u = obtenerDatos();
        if (u == null) return;
        if (ctrl.insertar(u)) {
            JOptionPane.showMessageDialog(this, "Usuario guardado.");
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar. El código o DNI ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }
        Usuario u = obtenerDatos();
        if (u == null) return;
        u.setCodigo(codigoSeleccionado);
        if (ctrl.actualizar(u)) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado.");
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar usuario " + codigoSeleccionado + "?");
        if (conf == JOptionPane.YES_OPTION) {
            ctrl.eliminar(codigoSeleccionado);
            cargarTabla();
            limpiar();
        }
    }

    private Usuario obtenerDatos() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese código."); return null; }
        Usuario u = new Usuario();
        u.setCodigo(codigo);
        u.setDni(txtDni.getText().trim());
        u.setNombres(txtNombres.getText().trim());
        u.setApellidos(txtApellidos.getText().trim());
        u.setEmailInstitucional(txtEmail.getText().trim());
        String pass = new String(txtContrasena.getPassword());
        if (!pass.isEmpty()) u.setPasswordHash(pass);
        else if (codigoSeleccionado != null) {
            Usuario existing = ctrl.buscarPorCodigo(codigoSeleccionado);
            if (existing != null) u.setPasswordHash(existing.getPasswordHash());
        }
        u.setRol((String) cmbRol.getSelectedItem());
        u.setEstado((String) cmbEstado.getSelectedItem());
        u.setCodigoOrcid(txtOrcid.getText().trim());
        return u;
    }

    private void limpiar() {
        txtCodigo.setText(""); txtDni.setText(""); txtNombres.setText("");
        txtApellidos.setText(""); txtEmail.setText(""); txtOrcid.setText("");
        txtContrasena.setText("");
        cmbRol.setSelectedIndex(0); cmbEstado.setSelectedIndex(0);
        codigoSeleccionado = null;
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 90, 25);
        add(lbl);
    }

    private JTextField agregarTextField(int x, int y, int w) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, 25);
        tf.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(tf);
        return tf;
    }

    private JButton crearBoton(String texto, Color color, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 80, 28);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        add(btn);
        return btn;
    }
}