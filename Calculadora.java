package com.mycompany.calculadora;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class Calculadora extends JFrame implements ActionListener {

    // Parte 1
    private JTextField pantalla;
    private StringBuilder operacion;
    private DecimalFormat formato = new DecimalFormat("0.########");

    // Parte 2
    public Calculadora() {
        setTitle("Calculadora Java");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        operacion = new StringBuilder();

        pantalla = new JTextField("0");
        pantalla.setEditable(false);
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setFont(new Font("Arial", Font.BOLD, 24));
        add(pantalla, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(6, 4, 5, 5));

        String[] botones = {
            "C", "⌫", "π", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", "√",
            "sin", "cos", "tan", "^"
        };

        for (String texto : botones) {
            JButton boton = new JButton(texto);
            boton.setFont(new Font("Arial", Font.BOLD, 18));
            boton.addActionListener(this);
            panel.add(boton);
        }

        add(panel, BorderLayout.CENTER);
    }

    // Parte 3
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            switch (cmd) {
                case "C":
                    operacion.setLength(0);
                    pantalla.setText("0");
                    break;
                case "⌫":
                    if (operacion.length() > 0) {
                        operacion.deleteCharAt(operacion.length() - 1);
                        pantalla.setText(operacion.length() > 0 ? operacion.toString() : "0");
                    } else {
                        pantalla.setText("0");
                    }
                    break;
                case "=":
                    if (operacion.length() > 0) {
                        double resultado = evaluarTodo(operacion.toString());
                        pantalla.setText(formato.format(resultado));
                        operacion.setLength(0);
                        operacion.append(formato.format(resultado));
                    }
                    break;
                case "π":
                    operacion.append("3.1416");
                    pantalla.setText(operacion.toString());
                    break;
                case "sin":
                    if (operacion.length() > 0) {
                        double num = Double.parseDouble(operacion.toString());
                        double resultado = Math.sin(Math.toRadians(num));
                        pantalla.setText(formato.format(resultado));
                        operacion.setLength(0);
                        operacion.append(formato.format(resultado));
                    }
                    break;
                case "cos":
                    if (operacion.length() > 0) {
                        double num = Double.parseDouble(operacion.toString());
                        double resultado = Math.cos(Math.toRadians(num));
                        pantalla.setText(formato.format(resultado));
                        operacion.setLength(0);
                        operacion.append(formato.format(resultado));
                    }
                    break;
                case "tan":
                    if (operacion.length() > 0) {
                        double num = Double.parseDouble(operacion.toString());
                        double resultado = Math.tan(Math.toRadians(num));
                        pantalla.setText(formato.format(resultado));
                        operacion.setLength(0);
                        operacion.append(formato.format(resultado));
                    }
                    break;
                case "√":
                    if (operacion.length() > 0) {
                        double num = Double.parseDouble(operacion.toString());
                        double resultado = Math.sqrt(num);
                        pantalla.setText(formato.format(resultado));
                        operacion.setLength(0);
                        operacion.append(formato.format(resultado));
                    }
                    break;
                case "^":
                    operacion.append("^");
                    pantalla.setText(operacion.toString());
                    break;
                default:
                    if (operacion.toString().equals("0") && !"+-*/.".contains(cmd)) {
                        operacion.setLength(0);
                    }
                    operacion.append(cmd);
                    pantalla.setText(operacion.toString());
            }
        } catch (Exception ex) {
            pantalla.setText("Error");
            operacion.setLength(0);
        }
    }

    // Parte 4
    private double evaluarTodo(String expr) {
        expr = expr.replace("π", "3.1416");
        
        while (expr.contains("^")) {
            int pos = expr.indexOf("^");
            String izquierda = expr.substring(0, pos);
            String derecha = expr.substring(pos + 1);
            
            double base = evaluarSimple(izquierda);
            double exponente = evaluarSimple(derecha);
            double resultado = Math.pow(base, exponente);
            
            expr = String.valueOf(resultado);
        }
        
        return evaluarSimple(expr);
    }
    
    private double evaluarSimple(String expr) {
        int multiplicacion = expr.indexOf("*");
        int division = expr.indexOf("/");
        int suma = expr.indexOf("+");
        int resta = expr.indexOf("-");
        
        if (multiplicacion != -1 || division != -1) {
            int pos = -1;
            char operador = ' ';
            
            if (multiplicacion != -1 && division != -1) {
                pos = Math.min(multiplicacion, division);
                operador = expr.charAt(pos);
            } else if (multiplicacion != -1) {
                pos = multiplicacion;
                operador = '*';
            } else {
                pos = division;
                operador = '/';
            }
            
            String num1Str = expr.substring(0, pos);
            String num2Str = expr.substring(pos + 1);
            
            double num1 = Double.parseDouble(num1Str);
            double num2 = Double.parseDouble(num2Str);
            
            if (operador == '*') {
                return num1 * num2;
            } else {
                if (num2 == 0) throw new ArithmeticException("División por cero");
                return num1 / num2;
            }
        }
        else if (suma != -1 || (resta != -1 && resta > 0)) {
            int pos = -1;
            char operador = ' ';
            
            if (suma != -1 && resta != -1 && resta > 0) {
                pos = Math.min(suma, resta);
                operador = expr.charAt(pos);
            } else if (suma != -1) {
                pos = suma;
                operador = '+';
            } else if (resta != -1 && resta > 0) {
                pos = resta;
                operador = '-';
            }
            
            if (pos != -1) {
                String num1Str = expr.substring(0, pos);
                String num2Str = expr.substring(pos + 1);
                
                double num1 = Double.parseDouble(num1Str);
                double num2 = Double.parseDouble(num2Str);
                
                if (operador == '+') {
                    return num1 + num2;
                } else {
                    return num1 - num2;
                }
            }
        }
        
        return Double.parseDouble(expr);
    }

    // Parte 5
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculadora().setVisible(true));
    }
}