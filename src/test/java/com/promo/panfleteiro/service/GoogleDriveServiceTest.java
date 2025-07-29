package com.promo.panfleteiro.service;

import br.com.promo.panfleteiro.PanfleteiroApplication;
import br.com.promo.panfleteiro.integration.service.GoogleDriveService;
import com.google.api.services.drive.model.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PanfleteiroApplication.class)
public class GoogleDriveServiceTest {

    @Autowired
    private GoogleDriveService googleDriveService;

    private final String pastaRaizId;

    public GoogleDriveServiceTest(@Value("${google.drive.mercadao.main.directory.id}") String pastaRaizId) {
        this.pastaRaizId = pastaRaizId;
    }

    @Test
    public void listarPastasDoDrive() throws Exception {
        List<File> pastas = googleDriveService.listarPastasDeMercado(pastaRaizId);

        assertNotNull(pastas, "A lista de pastas não deve ser nula");
        assertFalse(pastas.isEmpty(), "Nenhuma pasta de mercado encontrada");

        for (File pasta : pastas) {
            System.out.println("Pasta: " + pasta.getName() + " - ID: " + pasta.getId());
        }
    }

    @Test
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

