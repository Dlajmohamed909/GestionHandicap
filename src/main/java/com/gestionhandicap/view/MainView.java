package com.gestionhandicap.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private final JPanel contentPanel;

    public MainView() {
        setTitle("GestionHandicap — Accueil");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createMenu(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Theme.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        changePage(new DashboardView());
    }

    private JPanel createMenu() {
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(260, 700));
        menu.setBackground(Theme.BLUE_DARK);
        menu.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        menu.setLayout(new GridLayout(8, 1, 0, 10));

        JLabel logo = new JLabel("♿  GestionHandicap");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        menu.add(logo);

        menu.add(createMenuButton("Tableau de bord", new DashboardView()));
        menu.add(createMenuButton("Demandes", new DemandeView()));
        menu.add(createMenuButton("Réclamations", new ReclamationView()));
        menu.add(createMenuButton("Archivage", new ArchivageView()));

        return menu;
    }

    private JButton createMenuButton(String title, JPanel page) {
        JButton button = new JButton(title);
        button.setFocusPainted(false);
        button.setBackground(Theme.BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(Theme.LABEL_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> changePage(page));
        return button;
    }

    private void changePage(JPanel page) {
        contentPanel.removeAll();
        contentPanel.add(page, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
