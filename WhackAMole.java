import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.io.File;
import javax.sound.sampled.*;

public class WhackAMole extends JFrame {

    private JButton[] gridButtons = new JButton[9];
    private int score = 0;
    private Timer gameTimer;
    private Random random = new Random();
    private ImageIcon moleIcon;

    public WhackAMole() {
        super("Whack-a-Mole");

        moleIcon = loadImage("mole.png");

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.decode("#8B4513"));

        JButton startButton = new JButton("Start");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(startButton);

        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        controlPanel.add(scoreLabel);

        // Grid Panel
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gridPanel.setBackground(Color.decode("#654321"));

        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setEnabled(false);
            button.setBackground(Color.decode("#AB886D"));
            button.setOpaque(true);
            button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);

            gridButtons[i] = button;
            gridPanel.add(button);
        }

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // Start Button
        startButton.addActionListener(e -> {

            // Stop previous game if still running
            if (gameTimer != null && gameTimer.isRunning()) {
                gameTimer.stop();
            }

            score = 0;
            scoreLabel.setText("Score: 0");

            for (JButton btn : gridButtons) {
                btn.setEnabled(true);
                btn.setIcon(null);
            }

            // Mole appearance timer
            gameTimer = new Timer(1000, evt -> {

                int randomIndex = random.nextInt(9);

                for (JButton btn : gridButtons) {
                    btn.setIcon(null);
                }

                gridButtons[randomIndex].setIcon(moleIcon);

            });

            gameTimer.start();

            // Game duration timer (20 seconds)
            Timer gameDurationTimer = new Timer(20000, evt -> {

                gameTimer.stop();

                for (JButton btn : gridButtons) {
                    btn.setEnabled(false);
                    btn.setIcon(null);
                }

                if (score < 20) {
                    playSound("laugh.wav");
                }

                String title;
                String message;

                if (score >= 50) {
                    title = "🏆 Excellent!";
                    message = "You smashed it!\n\nFinal Score: "
                            + score
                            + "\nYou're a true Mole Master!";
                } else if (score >= 30) {
                    title = "🎉 Great Job!";
                    message = "Nice reflexes!\n\nFinal Score: " + score;
                } else {
                    title = "😅 Game Over";
                    message = "Better luck next time!\n\nFinal Score: " + score;
                }

                JOptionPane.showMessageDialog(
                        this,
                        message,
                        title,
                        JOptionPane.INFORMATION_MESSAGE);

            });

            gameDurationTimer.setRepeats(false);
            gameDurationTimer.start();

        });

        // Grid button listeners
        for (JButton button : gridButtons) {
            button.addActionListener(e -> {

                if (button.getIcon() == moleIcon) {

                    score++;
                    scoreLabel.setText("Score: " + score);

                    playSound("whack.wav");

                    // Remove mole immediately after successful hit
                    button.setIcon(null);

                } else {
                    playSound("laugh.wav");
                }

            });
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ImageIcon loadImage(String path) {
        Image image = new ImageIcon(getClass().getResource(path)).getImage();
        Image scaledImage = image.getScaledInstance(132, 132, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WhackAMole::new);
    }
}