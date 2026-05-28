/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.UsuarioController;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtCodigo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;

    public Login() {
        initComponents();
        setTitle("Sistema de Grados y Títulos - UPLA - IBERCAP");
        setSize(400, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(26, 58, 107));

        JLabel lblIcon = new JLabel("🎓", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 58));
        lblIcon.setBounds(150, 15, 100, 70);
        add(lblIcon);

        JLabel lblSistema = new JLabel("SISTEMA DE GRADOS Y TÍTULOS", SwingConstants.CENTER);
        lblSistema.setForeground(new Color(201, 160, 61));
        lblSistema.setFont(new Font("Arial", Font.BOLD, 11));
        lblSistema.setBounds(20, 85, 360, 18);
        add(lblSistema);

        JLabel lblUpla = new JLabel("UPLA - IBERCAP", SwingConstants.CENTER);
        lblUpla.setForeground(Color.WHITE);
        lblUpla.setFont(new Font("Arial", Font.BOLD, 22));
        lblUpla.setBounds(20, 103, 360, 32);
        add(lblUpla);

        JLabel lblTesis = new JLabel("Sistema de Información IBERCAP", SwingConstants.CENTER);
        lblTesis.setForeground(new Color(201, 160, 61));
        lblTesis.setFont(new Font("Arial", Font.ITALIC, 10));
        lblTesis.setBounds(20, 135, 360, 15);
        add(lblTesis);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(25, 165, 350, 230);
        add(panel);

        JLabel lblCod = new JLabel("Código:");
        lblCod.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCod.setBounds(25, 20, 80, 22);
        panel.add(lblCod);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(25, 42, 300, 30);
        txtCodigo.setBorder(BorderFactory.createLineBorder(new Color(26, 58, 107)));
        panel.add(txtCodigo);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPass.setBounds(25, 82, 100, 22);
        panel.add(lblPass);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(25, 104, 300, 30);
        txtContrasena.setBorder(BorderFactory.createLineBorder(new Color(26, 58, 107)));
        panel.add(txtContrasena);

        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBounds(25, 160, 300, 40);
        btnIngresar.setBackground(new Color(13, 43, 82));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnIngresar);

        JLabel lblInfo = new JLabel("Prueba: DAVID/david | ADMIN/admin | WALTER/walter", SwingConstants.CENTER);
        lblInfo.setForeground(new Color(201, 160, 61));
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        lblInfo.setBounds(25, 420, 350, 15);
        add(lblInfo);

        txtContrasena.addActionListener(e -> intentarLogin());
        btnIngresar.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String codigo = txtCodigo.getText().trim().toUpperCase();
        String pass = new String(txtContrasena.getPassword()).trim().toLowerCase();

        if (codigo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete código y contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioController ctrl = new UsuarioController();
        if (ctrl.login(codigo, pass)) {
            String rol = ctrl.obtenerRol(codigo);
            new MenuPrincipal(codigo, rol).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Código o contraseña incorrectos.\n\nUsuarios de prueba:\nDAVID / david (Estudiante)\nADMIN / admin (Administrativo)\nWALTER / walter (Docente/Asesor)", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
