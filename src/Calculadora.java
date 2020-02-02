import javax.swing.*;
import java.awt.*;

public class Calculadora extends JFrame {
    public Calculadora() {
        setTitle("Calculadora");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 350);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Calculadora();
    }
}
