package com.gestionhandicap.view;

import javax.swing.*;
import java.awt.*;

public class DemandeView extends JPanel {
    private final JComboBox<String> typeBox;
    private final JTextField descriptionField;
    private final JComboBox<String> statusBox;

    public DemandeView() {
        setBackground(Theme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));
        setLayout(new BorderLayout(0, 25));

        JLabel title = new JLabel("Gestion des demandes");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.BLUE_DARK);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 2, 15, 15));
        form.setBackground(Theme.WHITE);

        typeBox = new JComboBox<>(new String[]{"Aménagement d'examen", "Accessibilité", "Accompagnement"});
        descriptionField = new JTextField();
        statusBox = new JComboBox<>(new String[]{"En cours", "Acceptée", "Refusée"});

        form.add(createLabel("Type de demande"));
        form.add(typeBox);
        form.add(createLabel("Description"));
        form.add(descriptionField);
        form.add(createLabel("Pièce justificative"));
        form.add(new JButton("Choisir un fichier"));
        form.add(createLabel("Statut"));
        form.add(statusBox);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(Theme.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(Theme.LABEL_FONT);
        saveButton.setFocusPainted(false);

        form.add(new JLabel());
        form.add(saveButton);

        add(form, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.LABEL_FONT);
        label.setForeground(Theme.TEXT);
        return label;
    }
}
