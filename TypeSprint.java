package myProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TypeSprint extends JFrame implements ActionListener, KeyListener {
    private final String[] sentences = {
        "Practice makes a man perfect.",
        "Java is a versatile programming language.",
        "Typing fast requires focus and accuracy.",
        "Never stop learning new skills.",
        "Consistency beats intensity every time.",
        "The quick brown fox jumps over the lazy dog.",
        "Programming is the art of telling another human what one wants the computer to do."
    };

    // GUI Components
    private JLabel displaySentence, timerLabel, speedLabel, accuracyLabel, messageLabel, highScoreLabel, averageWpmLabel;
    private JTextArea typingArea;
    private JButton startButton, resetButton;
    private JProgressBar progressBar;
    private Timer timer;
    
    // Game variables
    private int timeLeft = 60;
    private long startTime;
    private String currentSentence;
    private int highScore = 0;
    private int testsCompleted = 0;
    private int totalScore = 0;
    private boolean isTestRunning = false;

    public TypeSprint() {
        setTitle("💬 Typing Speed Coach - Beat Your Record!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default
        }

        // --- Title Panel ---
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(34, 89, 150));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("💬 Typing Speed Coach", JLabel.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);
        
        highScoreLabel = new JLabel("🏆 High Score: 0 WPM", JLabel.CENTER);
        highScoreLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        highScoreLabel.setForeground(Color.YELLOW);
        titlePanel.add(highScoreLabel, BorderLayout.SOUTH);
        
        add(titlePanel, BorderLayout.NORTH);

        // --- Main content panel ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Sentence display 
        displaySentence = new JLabel("Click 'Start Test' to begin your typing challenge!", JLabel.LEFT);
        displaySentence.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        displaySentence.setForeground(new Color(50, 50, 50));
        displaySentence.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        displaySentence.setOpaque(true);
        displaySentence.setBackground(new Color(250, 253, 255));
        
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(displaySentence, BorderLayout.NORTH);
        displayPanel.setBackground(new Color(240, 248, 255));
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        // Progress bar and Typing Area container
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressBar.setForeground(new Color(34, 150, 90));
        progressBar.setBackground(new Color(230, 230, 230));
        centerPanel.add(progressBar, BorderLayout.NORTH);

        // Typing area
        typingArea = new JTextArea(5, 40);
        typingArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        typingArea.setLineWrap(true);
        typingArea.setWrapStyleWord(true);
        typingArea.setEnabled(false);
        typingArea.addKeyListener(this);
        
        JScrollPane scrollPane = new JScrollPane(typingArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240)), 
            " Start typing here..."));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // --- Control & Stats Panel (SOUTH) ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        controlPanel.setBackground(new Color(240, 248, 255));
        
        startButton = new JButton("Start Test");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startButton.setBackground(new Color(34, 150, 90));
        startButton.addActionListener(this);
        
        resetButton = new JButton("Reset Test");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetButton.setBackground(new Color(150, 34, 90));
        resetButton.addActionListener(this);
        resetButton.setEnabled(false);
        
        controlPanel.add(startButton);
        controlPanel.add(resetButton);
        bottomPanel.add(controlPanel, BorderLayout.NORTH);

        // Stats Panel (1 row, 4 columns)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setBackground(new Color(240, 248, 255));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        timerLabel = createStatLabel(" Time: 60s");
        speedLabel = createStatLabel(" Speed: 0 WPM");
        accuracyLabel = createStatLabel(" Accuracy: 0%");
        averageWpmLabel = createStatLabel(" Avg WPM: 0");

        statsPanel.add(timerLabel);
        statsPanel.add(speedLabel);
        statsPanel.add(accuracyLabel);
        statsPanel.add(averageWpmLabel);
        
        bottomPanel.add(statsPanel, BorderLayout.CENTER);
        
        messageLabel = createStatLabel("Ready to type?");
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240)),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startTest();
        } else if (e.getSource() == resetButton) {
            resetTest();
        }
    }

    private void startTest() {
        if (isTestRunning) return;

        Random rand = new Random();
        currentSentence = sentences[rand.nextInt(sentences.length)];
        
        typingArea.setEnabled(true);
        typingArea.setText("");
        typingArea.setForeground(Color.BLACK);
        typingArea.requestFocus();
        
        timeLeft = 60;
        startTime = System.currentTimeMillis();
        startButton.setEnabled(false);
        resetButton.setEnabled(true);
        progressBar.setValue(0);
        timerLabel.setForeground(Color.BLACK);
        messageLabel.setText("Typing... Go! ");
        isTestRunning = true;

        updateSentenceDisplay(""); 

        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft + "s");
            
            // Update progress bar
            int progress = (int) ((60 - timeLeft) * 100.0 / 60);
            progressBar.setValue(progress);
            
            if (timeLeft <= 10) {
                timerLabel.setForeground(Color.RED);
            }
            if (timeLeft <= 0) {
                timer.stop();
                endTest();
            }
        });
        timer.start();
    }

    private void resetTest() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        typingArea.setEnabled(false);
        typingArea.setText("");
        displaySentence.setText("<html><div style='text-align: center; padding: 10px; font-size: 18pt; font-family: Segoe UI;'>Click 'Start Test' to begin your typing challenge!</div></html>");
        timerLabel.setText(" Time: 60s");
        messageLabel.setText("Test Reset. Start again?");
        progressBar.setValue(0);
        timerLabel.setForeground(Color.BLACK);
        startButton.setEnabled(true);
        resetButton.setEnabled(false);
        isTestRunning = false;
    }

    private void endTest() {
        if (!isTestRunning) return; 
        isTestRunning = false;
        
        typingArea.setEnabled(false);
        long endTime = System.currentTimeMillis();
        String typed = typingArea.getText();
        
        // Calculate the actual time used (in minutes). 
        double timeInSeconds = (endTime - startTime) / 1000.0;
        if (timeInSeconds == 0) timeInSeconds = 1; // Safety check
        double timeInMinutes = timeInSeconds / 60.0;

        int correctChars = 0;
        int maxLen = Math.min(typed.length(), currentSentence.length());

        for (int i = 0; i < maxLen; i++) {
            if (typed.charAt(i) == currentSentence.charAt(i)) {
                correctChars++;
            }
        }
        
        // Accuracy based on correct chars typed vs. *total* sentence length
        double accuracy = currentSentence.length() > 0 ? (correctChars * 100.0) / currentSentence.length() : 0;

        // Standardized WPM (Net WPM): (Correct Characters / 5) / Time in Minutes
        int wpm = timeInMinutes > 0 ? (int) ((correctChars / 5.0) / timeInMinutes) : 0;
        
        // Update high score
        if (wpm > highScore) {
            highScore = wpm;
            highScoreLabel.setText("🏆 High Score: " + highScore + " WPM");
            highScoreLabel.setForeground(new Color(255, 165, 0)); // Orange
        }

        // Update overall stats
        testsCompleted++;
        totalScore += wpm;
        int averageWPM = testsCompleted > 0 ? totalScore / testsCompleted : 0;

        speedLabel.setText(" Speed: " + wpm + " WPM");
        accuracyLabel.setText(" Accuracy: " + String.format("%.1f", accuracy) + "%");
        averageWpmLabel.setText(" Avg WPM: " + averageWPM);

        // Gamified feedback
        String feedback;
        Color feedbackColor;
        
        if (wpm >= 80) {
            feedback = " Legendary Typist! Amazing!";
            feedbackColor = new Color(0, 100, 0);
        } else if (wpm >= 60) {
            feedback = " Excellent! You're a pro!";
            feedbackColor = new Color(0, 150, 0);
        } else if (wpm >= 40) {
            feedback = " Great Job! Keep improving!";
            feedbackColor = new Color(0, 120, 0);
        } else if (wpm >= 20) {
            feedback = " Good start! Practice more!";
            feedbackColor = new Color(200, 150, 0);
        } else {
            feedback = " Keep practicing! You'll get better!";
            feedbackColor = Color.RED;
        }

        messageLabel.setText(feedback);
        messageLabel.setForeground(feedbackColor);
        
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
        timerLabel.setText(" Test Complete!");
        updateSentenceDisplay(typed); // Final update to show where typing stopped
    }

    // Dynamic Sentence Color Coding 
    private void updateSentenceDisplay(String typed) {
        if (currentSentence == null) return;
        
        StringBuilder html = new StringBuilder("<html><div style='text-align: left; padding: 10px; font-size: 18pt; font-family: Segoe UI;'>");
        boolean errorFound = false;

        for (int i = 0; i < currentSentence.length(); i++) {
            char targetChar = currentSentence.charAt(i);
            String targetCharStr = String.valueOf(targetChar);

            if (i < typed.length()) {
                char typedChar = typed.charAt(i);
                
                if (errorFound) {
                    // Gray out remaining sentence after first error
                    html.append("<span style='color: #BBBBBB;'>").append(targetCharStr).append("</span>");
                } else if (typedChar == targetChar) {
                    // Correct character
                    html.append("<span style='color: #008000;'>").append(targetCharStr).append("</span>"); // Green
                } else {
                    // Incorrect character
                    html.append("<span style='background-color: #FFDDDD; color: #990000; text-decoration: underline;'>").append(targetCharStr).append("</span>"); // Red background/text
                    errorFound = true;
                }
            } else {
                // Untyped characters
                html.append("<span style='color: #BBBBBB;'>").append(targetCharStr).append("</span>"); // Gray
            }
        }
        
        html.append("</div></html>");
        displaySentence.setText(html.toString());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (isTestRunning && currentSentence != null) {
            String typed = typingArea.getText();
            
            // Update dynamic sentence display
            updateSentenceDisplay(typed);
            
            // Real-time WPM/Accuracy and Color coding logic
            int correctChars = 0;
            int maxLen = Math.min(typed.length(), currentSentence.length());

            for (int i = 0; i < maxLen; i++) {
                if (typed.charAt(i) == currentSentence.charAt(i)) {
                    correctChars++;
                }
            }
            
            // Live Accuracy update
            double liveAccuracy = maxLen > 0 ? (correctChars * 100.0) / maxLen : 0; 
            accuracyLabel.setText(" Accuracy: " + String.format("%.1f", liveAccuracy) + "%");
            
            // Color coding for typing area
            if (typed.length() > 0 && typed.equals(currentSentence.substring(0, typed.length()))) {
                typingArea.setForeground(new Color(0, 150, 0)); // Green for correct
            } else if (typed.length() > 0) {
                typingArea.setForeground(Color.RED); // Red for errors
            } else {
                typingArea.setForeground(Color.BLACK); // Default color for empty
            }
            
            // Auto-complete detection
            if (typed.length() >= currentSentence.length() && typed.equals(currentSentence)) {
                timer.stop();
                endTest();
                messageLabel.setText(" Perfect! Completed early!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TypeSprint::new);
    }
}