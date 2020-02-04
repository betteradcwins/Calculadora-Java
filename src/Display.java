import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {
    private final JLabel label = new JLabel();

    public Display() {
        setBackground(new Color(46,49,50));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("courier", Font.PLAIN, 30));
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25));
    }
}
