package com.promo.panfleteiro.service;


import br.com.promo.panfleteiro.PanfleteiroApplication;
import br.com.promo.panfleteiro.integration.service.ChatGptService;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PanfleteiroApplication.class)
class ChatGptServiceTest {

    @Autowired
    private ChatGptService chatGptService;

    @Test
    @Ignore
    void enviarImagemEReceberRespostaDoChatGpt() {
        try {
            File imagem = new File("src/test/resources/imagem_panfleto_teste.png");
            assertTrue(imagem.exists(), "Imagem de teste não encontrada");

            String nomeDiretorio = "Chama Supermercados";
            String url = "https://www.grupochama.com.br/ofertas-do-chama-supermercados/";
            JsonNode resposta = chatGptService.enviarImagem(imagem, url, nomeDiretorio);
            String respostaContent = resposta.get("choices").get(0).get("message").get("content").asText();

            assertNotNull(resposta);
            System.out.println("Resposta do ChatGPT:");
            System.out.println(respostaContent);

        } catch (Exception e) {
            fail("Erro ao enviar imagem para o ChatGPT: " + e.getMessage());
        }
    }
}

