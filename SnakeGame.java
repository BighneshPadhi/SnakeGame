import javax.swing.*;
import java.awt.*;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        add(new Board());
        setResizable(false);
        pack();

        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(620, 645); // Size adjustment to fit the board
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
