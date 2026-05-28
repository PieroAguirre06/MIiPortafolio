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
        setTitle("Sistema de Grados y Títulos - UPLA");
        setSize(320, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(30, 60, 114));

        JLabel lblIcon = new JLabel("🎓", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        lblIcon.setBounds(110, 15, 100, 65);
        add(lblIcon);

        JLabel lblSistema = new JLabel("SISTEMA DE GRADOS Y TÍTULOS", SwingConstants.CENTER);
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setFont(new Font("Arial", Font.BOLD, 10));
        lblSistema.setBounds(20, 80, 280, 18);
        add(lblSistema);

        JLabel lblUpla = new JLabel("UPLA", SwingConstants.CENTER);
        lblUpla.setForeground(Color.WHITE);
        lblUpla.setFont(new Font("Arial", Font.BOLD, 24));
        lblUpla.setBounds(20, 96, 280, 32);
        add(lblUpla);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(20, 140, 280, 200);
        add(panel);

        JLabel lblCod = new JLabel("Código:");
        lblCod.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCod.setBounds(15, 18, 80, 22);
        panel.add(lblCod);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(15, 40, 250, 28);
        txtCodigo.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
        panel.add(txtCodigo);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPass.setBounds(15, 78, 100, 22);
        panel.add(lblPass);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(15, 100, 250, 28);
        txtContrasena.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
        panel.add(txtContrasena);

        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBounds(15, 148, 250, 32);
        btnIngresar.setBackground(new Color(30, 60, 114));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 13));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnIngresar);

        // Agregar usuarios de prueba directos para debugging
        JLabel lblInfo = new JLabel("Prueba: ADMIN001/admin001 | EST001/est001", SwingConstants.CENTER);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 9));
        lblInfo.setBounds(20, 340, 280, 15);
        add(lblInfo);

        txtContrasena.addActionListener(e -> intentarLogin());
        btnIngresar.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String codigo = txtCodigo.getText().trim().toUpperCase();  // Convertir a mayúsculas
        String pass = new String(txtContrasena.getPassword()).trim().toLowerCase();  // Convertir a minúsculas

        if (codigo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete código y contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("Intentando login con: " + codigo + " / " + pass);

        UsuarioController ctrl = new UsuarioController();
        if (ctrl.login(codigo, pass)) {
            String rol = ctrl.obtenerRol(codigo);
            System.out.println("Login exitoso! Rol: " + rol);
            new MenuPrincipal(codigo, rol).setVisible(true);
            this.dispose();
        } else {
            System.out.println("Login fallido para: " + codigo);
            JOptionPane.showMessageDialog(this, "Código o contraseña incorrectos.\n\nUsuarios de prueba:\nADMIN001 / admin001\nEST001 / est001\nDOC001 / doc001\nDEC001 / dec001", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}