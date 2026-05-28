/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Tramite;
import controlador.TramiteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmTramite extends JFrame {

    private JComboBox<String> cmbEstudiante, cmbTipoTramite, cmbEstado;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private final TramiteController ctrl;
    private int idSeleccionado = -1;

    public FrmTramite() {
        ctrl = new TramiteController();
        initComponents();
        cargarTabla();
        setTitle("Trámites - UPLA");
        setSize(700, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(30, 60, 114));
        header.setBounds(0, 0, 700, 35);
        add(header);
        JLabel lbl = new JLabel("TRÁMITES", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 700, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Estudiante:", 20, y);
        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setBounds(110, y, 280, 25);
        cargarEstudiantes();
        add(cmbEstudiante);

        y = 90;
        agregarLabel("Tipo Trámite:", 20, y);
        cmbTipoTramite = new JComboBox<>(new String[]{
            "Obtencion_Bachiller", "Obtencion_Titulo_Tesis", "Obtencion_Titulo_TSP"
        });
        cmbTipoTramite.setBounds(110, y, 250, 25);
        add(cmbTipoTramite);

        y = 130;
        agregarLabel("Estado:", 20, y);
        cmbEstado = new JComboBox<>(new String[]{
            "Iniciado", "Revision_Requisitos", "Aprobacion_Plan", "Desarrollo_Investigacion",
            "Revision_Similitud", "Revision_Jurado", "Expedito", "Sustentacion_Programada", "Culminado", "Rechazado"
        });
        cmbEstado.setBounds(110, y, 220, 25);
        add(cmbEstado);

        y = 175;
        btnNuevo    = crearBoton("Nuevo",    new Color(30,60,114),  20,  y);
        btnGuardar  = crearBoton("Guardar",  new Color(0,128,0),   105, y);
        btnEditar   = crearBoton("Editar",   new Color(180,120,0), 190, y);
        btnEliminar = crearBoton("Eliminar", new Color(180,0,0),   275, y);
        btnLimpiar  = crearBoton("Limpiar",  new Color(100,100,100),360, y);

        String[] cols = {"ID", "Estudiante", "Tipo Trámite", "Estado", "Fecha Inicio"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(30, 60, 114));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 215, 678, 220);
        add(scroll);

        btnNuevo.addActionListener(e -> { limpiar(); idSeleccionado = -1; });

        btnGuardar.addActionListener(e -> {
            Tramite t = new Tramite();
            String selected = (String) cmbEstudiante.getSelectedItem();
            if (selected == null || selected.isEmpty()) return;
            t.setCodigoEstudiante(selected.split(" - ")[0]);
            t.setTipoTramite((String) cmbTipoTramite.getSelectedItem());
            t.setEstadoActual((String) cmbEstado.getSelectedItem());
            if (ctrl.insertar(t)) {
                JOptionPane.showMessageDialog(this, "Trámite guardado.");
                cargarTabla(); 
                limpiar();
                idSeleccionado = -1;
            }
        });

        btnEditar.addActionListener(e -> {
            if (idSeleccionado == -1) { 
                JOptionPane.showMessageDialog(this, "Seleccione un trámite."); 
                return; 
            }
            Tramite t = new Tramite();
            t.setIdTramite(idSeleccionado);
            String selected = (String) cmbEstudiante.getSelectedItem();
            if (selected == null || selected.isEmpty()) return;
            t.setCodigoEstudiante(selected.split(" - ")[0]);
            t.setTipoTramite((String) cmbTipoTramite.getSelectedItem());
            t.setEstadoActual((String) cmbEstado.getSelectedItem());
            if (ctrl.actualizar(t)) {
                JOptionPane.showMessageDialog(this, "Trámite actualizado.");
                cargarTabla(); 
                limpiar(); 
                idSeleccionado = -1;
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == -1) { 
                JOptionPane.showMessageDialog(this, "Seleccione un trámite."); 
                return; 
            }
            int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar trámite #" + idSeleccionado + "?");
            if (conf == JOptionPane.YES_OPTION) {
                ctrl.eliminar(idSeleccionado);
                cargarTabla(); 
                limpiar(); 
                idSeleccionado = -1;
            }
        });

        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                cmbTipoTramite.setSelectedItem(modelo.getValueAt(fila, 2));
                cmbEstado.setSelectedItem(modelo.getValueAt(fila, 3));
            }
        });
    }

    private void cargarEstudiantes() {
        cmbEstudiante.removeAllItems();
        List<String> lista = ctrl.listarCodigosConNombre();
        for (String s : lista) cmbEstudiante.addItem(s);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Tramite> lista = ctrl.listar();
        for (Tramite t : lista) {
            modelo.addRow(new Object[]{
                t.getIdTramite(), t.getCodigoEstudiante(),
                t.getTipoTramite(), t.getEstadoActual(),
                t.getFechaInicio()
            });
        }
    }

    private void limpiar() {
        if (cmbEstudiante.getItemCount() > 0) cmbEstudiante.setSelectedIndex(0);
        cmbTipoTramite.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
    }

    private void agregarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 100, 25);
        add(lbl);
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

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }
}