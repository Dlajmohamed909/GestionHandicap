package com.gestionhandicap.view;

import javax.swing.*;
import java.awt.*;

public class ArchivageView extends JPanel {
    public ArchivageView() {
        setBackground(Theme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));
        setLayout(new BorderLayout(0, 25));

        JLabel title = new JLabel("Archivage");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.BLUE_DARK);
        add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Type", "Date", "Statut"};
        Object[][] data = {
                {"1", "Demande", "2026-05-13", "Acceptée"},
                {"2", "Réclamation", "2026-05-12", "En cours"},
                {"3", "Demande", "2026-05-10", "Refusée"}
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(28);
        table.setFont(Theme.NORMAL_FONT);
        table.getTableHeader().setFont(Theme.LABEL_FONT);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
