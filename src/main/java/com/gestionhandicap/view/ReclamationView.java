package com.gestionhandicap.view;

import javax.swing.*;
import java.awt.*;

public class ReclamationView extends JPanel {
    public ReclamationView() {
        setBackground(Theme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));
        setLayout(new BorderLayout(0, 25));

        JLabel title = new JLabel("Gestion des réclamations");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.BLUE_DARK);
        add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setFont(Theme.NORMAL_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createTitledBorder("Saisir la réclamation"));

        JButton sendButton = new JButton("Envoyer la réclamation");
        sendButton.setBackground(Theme.BLUE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(Theme.LABEL_FONT);
        sendButton.setFocusPainted(false);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(sendButton, BorderLayout.SOUTH);
    }
}
