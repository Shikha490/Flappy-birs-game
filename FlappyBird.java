import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 800, HEIGHT = 600;
    private int birdY = HEIGHT / 2, birdX = WIDTH / 4;
    private int velocity = 0;
    private final int GRAVITY = 2, JUMP_STRENGTH = -15;
    private boolean gameOver = false;
    private ArrayList<Rectangle> pipes = new ArrayList<>();
    private int pipeSpeed = 5, score = 0;
    private Timer timer;

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");
        frame.add(this);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        timer = new Timer(20, this);
        timer.start();

        generatePipes();
    }

    private void generatePipes() {
        int gap = 150;
        int pipeWidth = 80;
        int lastPipeX = WIDTH;

        for (int i = 0; i < 5; i++) {
            int pipeHeight = 100 + (int)(Math.random() * 200);
            pipes.add(new Rectangle(lastPipeX, HEIGHT - pipeHeight, pipeWidth, pipeHeight));
            pipes.add(new Rectangle(lastPipeX, 0, pipeWidth, HEIGHT - pipeHeight - gap));
            lastPipeX += 300;
        }
    }

    private void movePipes() {
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= pipeSpeed;

            if (pipe.x + pipe.width < 0) {
                pipes.remove(pipe);
                int newPipeHeight = 100 + (int)(Math.random() * 200);
                int gap = 150;
                if (i % 2 == 0) { // Bottom pipe
                    pipes.add(new Rectangle(WIDTH, HEIGHT - newPipeHeight, pipe.width, newPipeHeight));
                } else { // Top pipe
                    pipes.add(new Rectangle(WIDTH, 0, pipe.width, HEIGHT - newPipeHeight - gap));
                }
                score++;
            }
        }
    }

    private void checkCollision() {
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(new Rectangle(birdX, birdY, 40, 40))) {
                gameOver = true;
                timer.stop();
            }
        }
        
        if (birdY > HEIGHT || birdY < 0) {
            gameOver = true;
            timer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT); // Background

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 100, WIDTH, 100); // Ground
        
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 110, WIDTH, 10); // Grass

        g.setColor(Color.red);
        g.fillRect(birdX, birdY, 40, 40); // Bird

        g.setColor(Color.green.darker());
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height); // Pipes
        }

        if (gameOver) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", WIDTH / 3, HEIGHT / 2);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Score: " + score, 10, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdY += velocity;
            velocity += GRAVITY;
            movePipes();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocity = JUMP_STRENGTH;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) {
        new FlappyBird();
    }
}
