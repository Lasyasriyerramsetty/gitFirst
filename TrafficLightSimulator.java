package myProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrafficLightSimulator extends JFrame implements ActionListener {
    private JRadioButton redButton, yellowButton, greenButton;
    private JLabel messageLabel;
    private ButtonGroup group;

    public TrafficLightSimulator() {
        setTitle("Traffic Light Simulator");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(messageLabel);

        redButton = new JRadioButton("Red");
        yellowButton = new JRadioButton("Yellow");
        greenButton = new JRadioButton("Green");


        group = new ButtonGroup();
        group.add(redButton);
        group.add(yellowButton);
        group.add(greenButton);

        redButton.addActionListener(this);
        yellowButton.addActionListener(this);
        greenButton.addActionListener(this);


        add(redButton);
        add(yellowButton);
        add(greenButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (redButton.isSelected()) {
            messageLabel.setText("Stop");
            messageLabel.setForeground(Color.RED);
        } else if (yellowButton.isSelected()) {
            messageLabel.setText("Ready");
            messageLabel.setForeground(Color.ORANGE);
        } else if (greenButton.isSelected()) {
            messageLabel.setText("Go");
            messageLabel.setForeground(Color.GREEN.darker());
        }
    }

    public static void main(String[] args) {
        new TrafficLightSimulator();
    }
}
