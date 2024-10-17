import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private final int DELAY = 150; // Increased delay to slow down the game

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int snakeLength;
    private int foodX;
    private int foodY;
    private boolean running;
    private char direction;
    private Timer timer;

    public Board() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                }
            }
        });
        startGame(); // Start the game when the board is created
    }

    public void startGame() {
        running = true;
        snakeLength = 3;
        direction = 'R';
        generateFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, DOT_SIZE, DOT_SIZE);

            // Draw snake
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Head
                } else {
                    g.setColor(Color.YELLOW); // Body
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            // Display the length of the snake
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Length: " + snakeLength, 10, 20);
        } else {
            showGameOver(g);
        }
    }

    private void generateFood() {
        Random random = new Random();
        foodX = random.nextInt(WIDTH / DOT_SIZE) * DOT_SIZE;
        foodY = random.nextInt(HEIGHT / DOT_SIZE) * DOT_SIZE;

        // Ensure food does not spawn on the snake
        for (int i = 0; i < snakeLength; i++) {
            if (x[i] == foodX && y[i] == foodY) {
                generateFood(); // Regenerate food if it spawns on the snake
                return;
            }
        }
    }

    private void move() {
        // Move the snake's body
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Move the snake's head
        switch (direction) {
            case 'L': x[0] -= DOT_SIZE; break;
            case 'R': x[0] += DOT_SIZE; break;
            case 'U': y[0] -= DOT_SIZE; break;
            case 'D': y[0] += DOT_SIZE; break;
        }

        // Check for collisions and game over conditions
        checkCollisions();
    }

    private void checkCollisions() {
        // Check if the snake collides with itself or the walls
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false; // Game over
        }

        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false; // Game over
            }
        }

        // If the game is over, stop the timer
        if (!running) {
            timer.stop();
        }
    }

    private void checkFoodCollision() {
        // Check if the snake's head is on the food
        if (x[0] == foodX && y[0] == foodY) {
            snakeLength++; // Increase the length of the snake
            generateFood(); // Generate new food at a random location
        }
    }

    private void showGameOver(Graphics g) {
        // Display the game over screen
        String message = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);

        // Display the final length of the snake
        String scoreMessage = "Length: " + snakeLength;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        metrics = getFontMetrics(g.getFont());
        g.drawString(scoreMessage, (WIDTH - metrics.stringWidth(scoreMessage)) / 2, HEIGHT / 2 + 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFoodCollision();
        }
        repaint();
    }
}
