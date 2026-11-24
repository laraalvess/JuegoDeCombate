package juego;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Ventana con la consola del juego

public class ConsolaVentana extends JFrame {

    private static final Color COLOR_DEFECTO = new Color(0, 200, 0);  // verde
    private static final Color COLOR_ROJO    = new Color(220, 60, 60);
    private static final Color COLOR_BLANCO  = Color.WHITE;

    // Detecta: ESC[Nm  (código de color)  o  cualquier secuencia de caracteres que no sean ESC
    private static final Pattern PATRON_ANSI =
            Pattern.compile("(\\[(\\d+)m)|([^]+)");

    private final JTextPane areaTexto;
    private final JTextField campoEntrada;
    private final LinkedBlockingQueue<String> colaEntrada = new LinkedBlockingQueue<>();

    public ConsolaVentana(String titulo) {
        super(titulo);

        Color fondoColor = Color.BLACK;
        Color bordeColor = new Color(0, 100, 0);
        Font  fuenteMono = new Font(Font.MONOSPACED, Font.PLAIN, 14);

        // JTextPane en lugar de JTextArea para soportar texto coloreado por tramos
        areaTexto = new JTextPane();
        areaTexto.setEditable(false);
        areaTexto.setBackground(fondoColor);
        areaTexto.setForeground(COLOR_DEFECTO);
        areaTexto.setFont(fuenteMono);
        areaTexto.setCaretColor(COLOR_DEFECTO);
        areaTexto.setMargin(new Insets(6, 8, 6, 8));

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createLineBorder(bordeColor));
        scroll.getViewport().setBackground(fondoColor);

        campoEntrada = new JTextField();
        campoEntrada.setBackground(fondoColor);
        campoEntrada.setForeground(COLOR_DEFECTO);
        campoEntrada.setFont(fuenteMono);
        campoEntrada.setCaretColor(COLOR_DEFECTO);
        campoEntrada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, bordeColor),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        // al pulsar Enter, añade la linea y limpia el campo de input
        campoEntrada.addActionListener(e -> {
            String texto = campoEntrada.getText();
            campoEntrada.setText("");
            colaEntrada.offer(texto);
        });

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(campoEntrada, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        setVisible(true);
        campoEntrada.requestFocusInWindow();
    }

    // añade una linea al area de texto (soporta códigos ANSI de color)
    public void println(String texto) {
        print(texto + "\n");
    }

    // añade texto sin salto de línea (soporta códigos ANSI de color)
    public void print(String texto) {
        SwingUtilities.invokeLater(() -> appendTexto(texto));
    }

    // borra todo el contenido del área de texto
    public void limpiar() {
        SwingUtilities.invokeLater(() -> areaTexto.setText(""));
    }

    // bloquea el hilo llamante hasta que el usuario pulse Enter
    public String readLine() {
        try {
            return colaEntrada.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    // Parsea códigos ANSI e inserta cada tramo con su color en el StyledDocument
    private void appendTexto(String texto) {
        StyledDocument doc = areaTexto.getStyledDocument();
        Color colorActual = COLOR_DEFECTO;
        Matcher m = PATRON_ANSI.matcher(texto);

        while (m.find()) {
            if (m.group(1) != null) {
                // Es un código ANSI: cambiar el color activo
                switch (Integer.parseInt(m.group(2))) {
                    case 31: colorActual = COLOR_ROJO;    break;
                    case 37: colorActual = COLOR_BLANCO;  break;
                    default: colorActual = COLOR_DEFECTO; break; // 0 = reset
                }
            } else {
                // Es texto normal: insertar con el color actual
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, colorActual);
                StyleConstants.setBackground(attrs, Color.BLACK);
                try {
                    doc.insertString(doc.getLength(), m.group(3), attrs);
                } catch (BadLocationException ex) {
                    // ignorado
                }
            }
        }
        areaTexto.setCaretPosition(doc.getLength());
    }
}
