package com.gestionhandicap.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static void copierFichier(String source, String destination) {

        try {

            File fichierSource = new File(source);
            File fichierDestination = new File(destination);

            Files.copy(
                    fichierSource.toPath(),
                    fichierDestination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("Fichier copié avec succès");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}