
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
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
            super("Whack-a-mole game");
            
            moleIcon = loadImage("mole.png");
            
            //start and score button
            JPanel controlPanel = new JPanel();
            controlPanel.setBackground(new Color(#8B4513)); //brown
            
            JButton startButton = new JButton("Start");
            controlPanel.add(startButton);
            
            JLabel scoreLabel = new JLabel("Score: 0");
            controlPanel.add(scoreLabel);
            
            //3x3 grid panel
            JPanel gridPanel = new JPanel(new GridLayout(3,3,5,5)); //gaps between button
            gridPanel.setBackground(new Color(#654321)); //dark brown

            for (int i = 0; i < 9; i++) {
                JButton button = new JButton();
                button.setEnabled(false);

                button.setBackground(Color.decode("#AB886D")); //light brown
                button.setOpaque(true);
                button.setBorder(BorderFactory.createLineBorder((Color.DARK_GRAY)));

                gridButtons[i] = button;
                gridPanel.add(button);
            }
            
            // add panel to frame
            setLayout(new BorderLayout());
            add(controlPanel, BorderLayout.NORTH);
            add(gridPanel, BorderLayout.CENTER);
            
            //start button listener
            startButton.addActionListener(e-> {
                score = 0;
                scoreLabel.setText("Score: 0");
                for (JButton btn : gridButtons) btn.setEnabled(true);
                
                //timer for mole appearance
                gameTimer = new Timer(1000, evt -> {
                    int randomIndex = random.nextInt(9);
                    for (JButton btn : gridButtons) {
                        btn.setIcon(null);
                    }
                    gridButtons[randomIndex].setIcon(moleIcon);
                });
                gameTimer.start();
                
                //game duration
                Timer gameDurationTimer = new Timer(20000, evt -> {
                    gameTimer.stop();
                    JOptionPane.showMessageDialog(null, "Game over! Final score: " + score);
                    for (JButton btn : gridButtons) {
                        btn.setEnabled(false);
                        btn.setIcon(null);
                    }
                });
                gameDurationTimer.setRepeats(false);
                gameDurationTimer.start();
            });
            
            for (JButton button : gridButtons) {
                button.addActionListener(e -> {
                    if (button.getIcon() == moleIcon) {
                        score++;
                        scoreLabel.setText("Score: " + score);
                        playSound("whack.wav");
                    }
                });
            }
            
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(400,400);
            setVisible(true);
        }

        private ImageIcon loadImage(String path) {
            Image image = new ImageIcon(this.getClass().getResource(path)).getImage();
            Image scaledImage = image.getScaledInstance(132,132, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        
        private void playSound(String soundFileName) {
            try {
                File soundFile = new File(soundFileName);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public static void main(String[] args){
            new WhackAMole();
    }
}
