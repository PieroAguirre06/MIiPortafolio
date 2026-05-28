/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Proyecto;
import controlador.ProyectoController;
import controlador.TramiteController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class FrmProyecto extends JFrame {

    private JComboBox<String> cmbTramite, cmbModalidad, cmbEnfoque, cmbEstado;
    private JTextField txtTitulo, txtSimilitud, txtUrl;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ProyectoController ctrl;
    private TramiteController tramCtrl;
    private int idSeleccionado = -1;

    public FrmProyecto() {
        ctrl = new ProyectoController();
        tramCtrl = new TramiteController();
        initComponents();
        cargarTabla();
        setTitle("Proyectos de Investigación - UPLA IBERCAP");
        setSize(850, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(26, 58, 107));
        header.setBounds(0, 0, 850, 35);
        add(header);
        JLabel lbl = new JLabel("GESTIÓN DE PROYECTOS DE INVESTIGACIÓN", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 850, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Trámite:", 20, y);
        cmbTramite = new JComboBox<>();
        cmbTramite.setBounds(100, y, 250, 25);
        cargarTramites();
        add(cmbTramite);

        agregarLabel("Modalidad:", 380, y);
        cmbModalidad = new JComboBox<>(new String[]{"Tesis", "Trabajo_Suficiencia_Profesional"});
        cmbModalidad.setBounds(460, y, 200, 25);
        add(cmbModalidad);

        agregarLabel("Estado:", 690, y);
        cmbEstado = new JComboBox<>(new String[]{
            "Registrado", "Plan_Aprobado", "En_Ejecucion", 
            "Aprobado_Por_Asesor", "Aprobado_Por_Jurado", "Sustentado"
        });
        cmbEstado.setBounds(740, y, 90, 25);
        add(cmbEstado);

        y = 90;
        agregarLabel("Título:", 20, y);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(80, y, 600, 25);
        txtTitulo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtTitulo);

        agregarLabel("Enfoque:", 710, y);
        cmbEnfoque = new JComboBox<>(new String[]{"Cuantitativa", "Cualitativa", "Mixta", "No_Aplica"});
        cmbEnfoque.setBounds(760, y, 70, 25);
        add(cmbEnfoque);

        y = 130;
        agregarLabel("% Similitud:", 20, y);
        txtSimilitud = new JTextField();
        txtSimilitud.setBounds(110, y, 80, 25);
        txtSimilitud.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtSimilitud);

        agregarLabel("URL Repositorio:", 210, y);
        txtUrl = new JTextField();
        txtUrl.setBounds(330, y, 500, 25);
        txtUrl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtUrl);

        y = 175;
        btnNuevo = crearBoton("Nuevo", new Color(26, 58, 107), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Trámite", "Título", "Modalidad", "Enfoque", "% Similitud", "Estado"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(26, 58, 107));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 220, 825, 290);
        add(scroll);

        btnNuevo.addActionListener(e -> { limpiar(); idSeleccionado = -1; });
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                Proyecto p = ctrl.buscarPorId(idSeleccionado);
                if (p != null) cargarDatosEnFormulario(p);
            }
        });
    }

    private void cargarTramites() {
        cmbTramite.removeAllItems();
        List<modelo.Tramite> lista = tramCtrl.listar();
        for (modelo.Tramite t : lista) {
            cmbTramite.addItem(t.getIdTramite() + " - " + t.getCodigoEstudiante());
        }
    }

    private void cargarDatosEnFormulario(Proyecto p) {
        txtTitulo.setText(p.getTitulo());
        cmbModalidad.setSelectedItem(p.getModalidad());
        cmbEnfoque.setSelectedItem(p.getEnfoque());
        txtSimilitud.setText(p.getPorcentajeSimilitud() != null ? p.getPorcentajeSimilitud().toString() : "");
        cmbEstado.setSelectedItem(p.getEstado());
        txtUrl.setText(p.getUrlRepositorio() != null ? p.getUrlRepositorio() : "");
        for (int i = 0; i < cmbTramite.getItemCount(); i++) {
            String item = cmbTramite.getItemAt(i);
            if (item.startsWith(p.getIdTramite() + " -")) {
                cmbTramite.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        Proyecto p = obtenerProyecto();
        if (p == null) return;
        if (ctrl.insertar(p)) {
            JOptionPane.showMessageDialog(this, "Proyecto guardado.");
            cargarTabla();
            limpiar();
            idSeleccionado = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto.");
            return;
        }
        Proyecto p = obtenerProyecto();
        if (p == null) return;
        p.setIdProyecto(idSeleccionado);
        if (ctrl.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "Proyecto actualizado.");
            cargarTabla();
            limpiar();
            idSeleccionado = -1;
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar proyecto #" + idSeleccionado + "?");
        if (conf == JOptionPane.YES_OPTION) {
            ctrl.eliminar(idSeleccionado);
            cargarTabla();
            limpiar();
            idSeleccionado = -1;
        }
    }

    private Proyecto obtenerProyecto() {
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el título.");
            return null;
        }
        Proyecto p = new Proyecto();
        String selectedTramite = (String) cmbTramite.getSelectedItem();
        if (selectedTramite == null || selectedTramite.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un trámite.");
            return null;
        }
        p.setIdTramite(Integer.parseInt(selectedTramite.split(" - ")[0]));
        p.setTitulo(titulo);
        p.setModalidad((String) cmbModalidad.getSelectedItem());
        p.setEnfoque((String) cmbEnfoque.getSelectedItem());
        try {
            p.setPorcentajeSimilitud(new BigDecimal(txtSimilitud.getText().trim()));
        } catch (NumberFormatException ex) {
            p.setPorcentajeSimilitud(BigDecimal.ZERO);
        }
        p.setEstado((String) cmbEstado.getSelectedItem());
        p.setUrlRepositorio(txtUrl.getText().trim());
        return p;
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Proyecto> lista = ctrl.listar();
        for (Proyecto p : lista) {
            modelo.addRow(new Object[]{
                p.getIdProyecto(), p.getIdTramite(), 
                p.getTitulo().length() > 50 ? p.getTitulo().substring(0, 50) + "..." : p.getTitulo(),
                p.getModalidad(), p.getEnfoque(), p.getPorcentajeSimilitud(), p.getEstado()
            });
        }
    }

    private void limpiar() {
        txtTitulo.setText("");
        txtSimilitud.setText("");
        txtUrl.setText("");
        cmbModalidad.setSelectedIndex(0);
        cmbEnfoque.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        if (cmbTramite.getItemCount() > 0) cmbTramite.setSelectedIndex(0);
        idSeleccionado = -1;
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 80, 25);
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