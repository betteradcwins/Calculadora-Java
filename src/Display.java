import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Display extends JPanel implements Consumer<String> {
    private final JLabel label = new JLabel("0");

    public Display() {
        Memoria.getInstancia().adicionarObservador(this);
        setBackground(new Color(46,49,50));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("courier", Font.PLAIN, 30));
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25));
        add(label);
    }

    @Override
    public void accept(String novoValor) {
        label.setText(novoValor);
    }
}
