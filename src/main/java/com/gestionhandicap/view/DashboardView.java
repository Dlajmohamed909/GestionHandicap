package com.gestionhandicap.view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {
    public DashboardView() {
        setBackground(Theme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        setLayout(new BorderLayout(0, 30));

        JLabel title = new JLabel("Tableau de bord");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.BLUE_DARK);
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        cardsPanel.setBackground(Theme.WHITE);

        cardsPanel.add(createCard("Demandes en cours", "12"));
        cardsPanel.add(createCard("Demandes acceptées", "8"));
        cardsPanel.add(createCard("Demandes refusées", "3"));
        cardsPanel.add(createCard("Réclamations", "5"));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String label, String number) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.LIGHT_GRAY);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        numberLabel.setForeground(Theme.BLUE);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(Theme.NORMAL_FONT);
        textLabel.setForeground(Theme.TEXT);

        card.add(numberLabel, BorderLayout.CENTER);
        card.add(textLabel, BorderLayout.SOUTH);
        return card;
    }
}
