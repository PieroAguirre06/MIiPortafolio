/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Sustentacion;
import controlador.SustentacionController;
import controlador.ProyectoController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class FrmSustentacion extends JFrame {

    private JComboBox<String> cmbProyecto, cmbModalidad, cmbCondicionActa, cmbAprobacionTipo;
    private JTextField txtResolucion, txtLugarEnlace, txtNotaNumerica, txtNotaLetras;
    private JTextArea txtObservaciones;
    private JSpinner spnFechaHora;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private SustentacionController ctrl;
    private ProyectoController proyectoCtrl;
    private int idSeleccionado = -1;

    public FrmSustentacion() {
        ctrl = new SustentacionController();
        proyectoCtrl = new ProyectoController();
        initComponents();
        cargarTabla();
        cargarProyectos();
        setTitle("Sustentaciones de Tesis - UPLA IBERCAP");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 245, 255));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(26, 58, 107));
        header.setBounds(0, 0, 950, 35);
        add(header);
        JLabel lbl = new JLabel("GESTIÓN DE SUSTENTACIONES DE TESIS", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBounds(0, 5, 950, 25);
        header.add(lbl);

        int y = 50;
        agregarLabel("Proyecto:", 20, y);
        cmbProyecto = new JComboBox<>();
        cmbProyecto.setBounds(100, y, 550, 25);
        add(cmbProyecto);

        agregarLabel("Resolución Expedito:", 680, y);
        txtResolucion = new JTextField();
        txtResolucion.setBounds(800, y, 120, 25);
        txtResolucion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtResolucion);

        y = 90;
        agregarLabel("Fecha/Hora Programada:", 20, y);
        SpinnerDateModel model = new SpinnerDateModel();
        spnFechaHora = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFechaHora, "yyyy-MM-dd HH:mm");
        spnFechaHora.setEditor(editor);
        spnFechaHora.setBounds(180, y, 200, 25);
        add(spnFechaHora);

        agregarLabel("Modalidad:", 420, y);
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "No_Presencial"});
        cmbModalidad.setBounds(510, y, 130, 25);
        add(cmbModalidad);

        agregarLabel("Lugar/Enlace:", 670, y);
        txtLugarEnlace = new JTextField();
        txtLugarEnlace.setBounds(760, y, 160, 25);
        txtLugarEnlace.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtLugarEnlace);

        y = 130;
        agregarLabel("Nota Final (0-20):", 20, y);
        txtNotaNumerica = new JTextField();
        txtNotaNumerica.setBounds(150, y, 80, 25);
        txtNotaNumerica.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtNotaNumerica);

        agregarLabel("Nota en Letras:", 260, y);
        txtNotaLetras = new JTextField();
        txtNotaLetras.setBounds(360, y, 220, 25);
        txtNotaLetras.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtNotaLetras);

        agregarLabel("Condición Acta:", 620, y);
        cmbCondicionActa = new JComboBox<>(new String[]{
            "Excelente", "Muy_Bueno", "Bueno", "Regular", "Desaprobado", "Pendiente_De_Sustentar"
        });
        cmbCondicionActa.setBounds(720, y, 200, 25);
        add(cmbCondicionActa);

        y = 170;
        agregarLabel("Aprobación Tipo:", 20, y);
        cmbAprobacionTipo = new JComboBox<>(new String[]{"Unanimidad", "Mayoria", "No_Aplica"});
        cmbAprobacionTipo.setBounds(140, y, 130, 25);
        add(cmbAprobacionTipo);

        agregarLabel("Observaciones:", 300, y);
        txtObservaciones = new JTextArea(3, 30);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setBounds(400, y, 520, 60);
        add(scrollObs);

        y = 245;
        btnNuevo = crearBoton("Nuevo", new Color(26, 58, 107), 20, y);
        btnGuardar = crearBoton("Guardar", new Color(0, 128, 0), 110, y);
        btnEditar = crearBoton("Editar", new Color(180, 120, 0), 200, y);
        btnEliminar = crearBoton("Eliminar", new Color(180, 0, 0), 290, y);
        btnLimpiar = crearBoton("Limpiar", new Color(100, 100, 100), 380, y);

        String[] cols = {"ID", "Proyecto", "Resolución", "Fecha/Hora", "Modalidad", "Nota", "Condición"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(new Color(26, 58, 107));
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 290, 925, 280);
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
                Sustentacion s = ctrl.buscarPorId(idSeleccionado);
                if (s != null) cargarDatosEnFormulario(s);
            }
        });
    }

    private void cargarProyectos() {
        cmbProyecto.removeAllItems();
        List<String> lista = proyectoCtrl.listarProyectosCombo();
        for (String item : lista) {
            cmbProyecto.addItem(item);
        }
    }

    private void cargarDatosEnFormulario(Sustentacion s) {
        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            String item = cmbProyecto.getItemAt(i);
            if (item.startsWith(s.getIdProyecto() + " -")) {
                cmbProyecto.setSelectedIndex(i);
                break;
            }
        }
        txtResolucion.setText(s.getResolucionExpedito());
        if (s.getFechaHoraProgramada() != null) {
            spnFechaHora.setValue(new java.util.Date(s.getFechaHoraProgramada().getTime()));
        }
        cmbModalidad.setSelectedItem(s.getModalidadSustentacion());
        txtLugarEnlace.setText(s.getLugarEnlace());
        if (s.getNotaFinalNumerica() != null) {
            txtNotaNumerica.setText(s.getNotaFinalNumerica().toString());
        }
        txtNotaLetras.setText(s.getNotaFinalLetras());
        cmbCondicionActa.setSelectedItem(s.getCondicionActa());
        cmbAprobacionTipo.setSelectedItem(s.getAprobacionTipo());
        txtObservaciones.setText(s.getObservacionesActa());
    }

    private void guardar() {
        Sustentacion s = obtenerSustentacion();
        if (s == null) return;
        if (ctrl.insertar(s)) {
            JOptionPane.showMessageDialog(this, "Sustentación guardada.");
            cargarTabla();
            limpiar();
            idSeleccionado = -1;
        }
    }

    private void editar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una sustentación.");
            return;
        }
        Sustentacion s = obtenerSustentacion();
        if (s == null) return;
        s.setIdSustentacion(idSeleccionado);
        if (ctrl.actualizar(s)) {
            JOptionPane.showMessageDialog(this, "Sustentación actualizada.");
            cargarTabla();
            limpiar();
            idSeleccionado = -1;
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una sustentación.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar sustentación #" + idSeleccionado + "?");
        if (conf == JOptionPane.YES_OPTION) {
            ctrl.eliminar(idSeleccionado);
            cargarTabla();
            limpiar();
            idSeleccionado = -1;
        }
    }

    private Sustentacion obtenerSustentacion() {
        Sustentacion s = new Sustentacion();
        String selectedProyecto = (String) cmbProyecto.getSelectedItem();
        if (selectedProyecto == null || selectedProyecto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto.");
            return null;
        }
        s.setIdProyecto(Integer.parseInt(selectedProyecto.split(" - ")[0]));
        s.setResolucionExpedito(txtResolucion.getText().trim());
        java.util.Date date = (java.util.Date) spnFechaHora.getValue();
        s.setFechaHoraProgramada(new Timestamp(date.getTime()));
        s.setModalidadSustentacion((String) cmbModalidad.getSelectedItem());
        s.setLugarEnlace(txtLugarEnlace.getText().trim());
        try {
            String notaStr = txtNotaNumerica.getText().trim();
            if (!notaStr.isEmpty()) {
                s.setNotaFinalNumerica(new BigDecimal(notaStr));
            }
        } catch (NumberFormatException ex) {
            s.setNotaFinalNumerica(BigDecimal.ZERO);
        }
        s.setNotaFinalLetras(txtNotaLetras.getText().trim());
        s.setCondicionActa((String) cmbCondicionActa.getSelectedItem());
        s.setAprobacionTipo((String) cmbAprobacionTipo.getSelectedItem());
        s.setObservacionesActa(txtObservaciones.getText().trim());
        return s;
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Sustentacion> lista = ctrl.listar();
        for (Sustentacion s : lista) {
            modelo.addRow(new Object[]{
                s.getIdSustentacion(), s.getIdProyecto(), s.getResolucionExpedito(),
                s.getFechaHoraProgramada() != null ? s.getFechaHoraProgramada().toString() : "",
                s.getModalidadSustentacion(), s.getNotaFinalNumerica(), s.getCondicionActa()
            });
        }
    }

    private void limpiar() {
        if (cmbProyecto.getItemCount() > 0) cmbProyecto.setSelectedIndex(0);
        txtResolucion.setText("");
        spnFechaHora.setValue(new java.util.Date());
        cmbModalidad.setSelectedIndex(0);
        txtLugarEnlace.setText("");
        txtNotaNumerica.setText("");
        txtNotaLetras.setText("");
        cmbCondicionActa.setSelectedIndex(5);
        cmbAprobacionTipo.setSelectedIndex(2);
        txtObservaciones.setText("");
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