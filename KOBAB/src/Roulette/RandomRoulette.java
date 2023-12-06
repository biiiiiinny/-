package Roulette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RandomRoulette extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private int angle = 0;
    private boolean spinning = false;
    private Timer timer;
    
    private String[] foods = {"피자", "떡볶이", "샐러드", "제육볶음", "치킨"};

    public RandomRoulette() {
        setTitle("Random Timed Roulette Animation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton showRouletteButton = new JButton("룰렛 보기");
        showRouletteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRouletteDialog();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showRouletteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showRouletteDialog() {
        JFrame dialogFrame = new JFrame("점심 메뉴 추천 룰렛");
        dialogFrame.setSize(400, 400);
        dialogFrame.setLocationRelativeTo(null);

        JButton spinButton = new JButton("돌리기!");
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!spinning) {
                    spinning = true;
                    spinRoulette(dialogFrame);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(spinButton);

        dialogFrame.add(buttonPanel, BorderLayout.SOUTH);

        RoulettePanel roulettePanel = new RoulettePanel();
        dialogFrame.add(roulettePanel, BorderLayout.CENTER);

        dialogFrame.setVisible(true);
    }

    private void spinRoulette(JFrame dialogFrame) {
        Random random = new Random();
        int targetAngle = random.nextInt(360) + 3600;

        int spinDuration = random.nextInt(2000) + 2000;

        timer = new Timer(10, new ActionListener() {
            int elapsedTime = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                angle = (angle + 5) % 360;
                dialogFrame.repaint();
                elapsedTime += 10;

                if (elapsedTime >= spinDuration) {
                    ((Timer) e.getSource()).stop();
                    spinning = false;

                    // Select a random food when spinning is complete
                    int selectedFoodIndex = (angle / 72) % foods.length;
                    String selectedFood = foods[selectedFoodIndex];

                    // Display the selected food (you can modify this part as needed)
                    JOptionPane.showMessageDialog(dialogFrame, " 오늘의 추천 메뉴는 " + selectedFood + "!!");
                }
            }
        });

        timer.start();
    }

    private class RoulettePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 4;

            int x = centerX - radius;
            int y = centerY - radius;

            // Draw the foods with animation to simulate roulette spinning
            for (int i = 0; i < foods.length; i++) {
                // Use an orange color for the roulette
                Color color = new Color(255 - 10 * i, 165 - 15 * i, 0);  // RGB values for orange color
                g.setColor(color);

                // Draw the food at its current position based on the spinning angle
                int foodAngle = (angle + i * 72) % 360;
                int arcAngle = 72;
                g.fillArc(x, y, 2 * radius, 2 * radius, foodAngle, arcAngle);

                // Calculate the position to draw the food name
                double radian = Math.toRadians(-(foodAngle + arcAngle / 2));
                int textX = (int) (centerX + radius * 0.7 * Math.cos(radian));
                int textY = (int) (centerY + radius * 0.7 * Math.sin(radian));

                g.setColor(Color.BLACK);
                g.drawString(foods[i], textX, textY);
            }

        }
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RandomRoulette().setVisible(true);
            }
        });
    }
}
