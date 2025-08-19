package com.promo.panfleteiro.service;

import br.com.promo.panfleteiro.PanfleteiroApplication;
import br.com.promo.panfleteiro.integration.service.GoogleDriveService;
import com.google.api.services.drive.model.File;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GoogleDriveService.class)
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
public class GoogleDriveServiceTest {

    @Autowired
    private GoogleDriveService googleDriveService;

    @Value("${google.drive.mercadao.main.directory.id}")
    private String pastaRaizId;

    @Test
    @EnabledIfEnvironmentVariable(named = "GOOGLE_APPLICATION_CREDENTIALS", matches = ".+")
    public void listarPastasDoDrive() throws Exception {
        List<File> pastas = googleDriveService.listarPastasDeMercado(pastaRaizId);

        assertNotNull(pastas, "A lista de pastas não deve ser nula");
        assertFalse(pastas.isEmpty(), "Nenhuma pasta de mercado encontrada");

        for (File pasta : pastas) {
            System.out.println("Pasta: " + pasta.getName() + " - ID: " + pasta.getId());
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "GOOGLE_APPLICATION_CREDENTIALS", matches = ".+")
    public void listarImagensDeUmaPasta() throws Exception {
        // Primeiro, lista as pastas
        List<File> pastas = googleDriveService.listarPastasDeMercado(pastaRaizId);
        assertFalse(pastas.isEmpty(), "Nenhuma pasta de mercado encontrada");

        File primeiraPasta = pastas.get(0);

        List<File> imagens = googleDriveService.listarImagensNaPasta(primeiraPasta.getId());
        assertNotNull(imagens);
        System.out.println("Imagens da pasta " + primeiraPasta.getName() + ":");

        for (File imagem : imagens) {
            System.out.println(" - " + imagem.getName() + " (ID: " + imagem.getId() + ")");
        }
    }
}

