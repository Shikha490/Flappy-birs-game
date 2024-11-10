import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GRAVITY = 1;
    private static final int FLAP_STRENGTH = -15;
    private static final int PIPE_WIDTH = 80;
    private static final int PIPE_GAP = 200;

    private Timer timer;
    private int birdY;
    private int birdVelocity;
    private ArrayList<Rectangle> pipes;
    private boolean gameOver;
    private int score;

    public FlappyBird() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.cyan);
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        pipes = new ArrayList<>();
        score = 0;

        // Key listener to flap the bird
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    flap();
                }
            }
        });
        setFocusable(true);

        // Timer for the game loop
        timer = new Timer(20, this);
        timer.start();
    }

    public void flap() {
        birdVelocity = FLAP_STRENGTH;
    }

    private void moveBird() {
        birdY += birdVelocity;
        birdVelocity += GRAVITY;
    }

    private void generatePipes() {
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 300) {
            int gapY = new Random().nextInt(HEIGHT - PIPE_GAP - 100) + 50;
            pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, gapY)); // top pipe
            pipes.add(new Rectangle(WIDTH, gapY + PIPE_GAP, PIPE_WIDTH, HEIGHT - gapY - PIPE_GAP)); // bottom pipe
        }
    }

    private void movePipes() {
        ArrayList<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle pipe : pipes) {
            pipe.x -= 5;
            if (pipe.x + PIPE_WIDTH < 0) {
                toRemove.add(pipe);
            }
        }
        pipes.removeAll(toRemove);
    }

    private void checkCollisions() {
        // Check if bird hits the ground or the top of the screen
        if (birdY >= HEIGHT || birdY <= 0) {
            gameOver = true;
        }

        // Check for collisions with pipes
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(new Rectangle(WIDTH / 2 - 10, birdY, 20, 20))) {
                gameOver = true;
            }
        }
    }

    private void updateScore() {
        for (Rectangle pipe : pipes) {
            if (pipe.x + PIPE_WIDTH < WIDTH / 2 - 10 && !pipe.isVisible()) {
                score++;
                pipe.setVisible(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveBird();
            generatePipes();
            movePipes();
            checkCollisions();
            updateScore();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw bird
        g.setColor(Color.yellow);
        g.fillRect(WIDTH / 2 - 10, birdY, 20, 20);

        // Draw pipes
        g.setColor(Color.green);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Draw score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Score: " + score, 20, 50);

        // Draw game over message
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("Game Over!", WIDTH / 4, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
