package br.com.promo.panfleteiro.integration.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Slf4j
@Service
public class ChatGptService {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private final String openaiApiKey;

    private static final String PROMPT_INSTRUCOES = """
        Transforme a imagem enviada em um JSON no seguinte padrão usando todos os produtos enviados na imagem:
        {
            "url": "%s",
            "marketExternalCode": "%s",
            "initialDate": "",
            "expirationDate": "",
            "ads": [
                {
                    "productName": "",
                    "productCategory": "",
                    "price": 0.0,
                    "priceWithDiscount": 0.0,
                    "active": true
                }
            ]
        }

        O productCategory deve ser catalogado como:
        Açougue; Frios; Laticínios; Adega; Bebidas; Higiene; Limpeza; Hortifruti;
        Mercearia; Padaria; Enlatados; Cereais; Rotisseria; Petshop; Peixaria; Auto-peças;
        O nome do produto dever possuir o nome completo como está no panfleto incluindo a grandeza de medida.
        Os produtos alcoolicos devem ser catalogados com productCategory de Adega
        O price deve registrar o valor sem o desconto adicional de cartão da loja ou outro tipo de desconto enquanto o priceWithDiscount deve registrar o valor com o desconto adicional de cartão da loja ou outro tipo de desconto.
        Após criar o JSON, quero que revise o resultado alterando algumas informações conforme as seguintes regras:
        As datas devem estar no padrão dd/MM/YYYY.
        Todo productName deve conter a primeira letra sempre maiúscula como formatação padrão.
        As gradezas de medida como: quilograma, mililitros, litros devem ser abreviadas como: kg,ml,L.
        """;

    public ChatGptService(@Value("${openai.api.key}") String openaiKey) {
        openaiApiKey = openaiKey;
    }

    public JsonNode enviarImagem(File imagem, String url, String nomeDiretorio) throws IOException {
        String imagemBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(imagem.toPath()));
        String prompt = PROMPT_INSTRUCOES.formatted(url, nomeDiretorio);

        // Monta o JSON corretamente usando ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Prompt como texto
        JsonNode textNode = mapper.createObjectNode()
                .put("type", "text")
                .put("text", prompt);

        // Imagem como base64
        JsonNode imageNode = mapper.createObjectNode()
                .put("type", "image_url")
                .set("image_url", mapper.createObjectNode()
                        .put("url", "data:image/jpeg;base64," + imagemBase64));

        // Lista content (text + image)
        JsonNode contentArray = mapper.createArrayNode()
                .add(textNode)
                .add(imageNode);

        String content = """
                Você é um extrator de ofertas de panfletos de supermercados. Use OCR e devolva os dados em formato JSON no padrão especificado pelo usuário.
                Sempre responda com JSON válido, sem comentários, sem blocos de markdown. Comece a resposta diretamente com '{'.
                Cada anuncio terá seu preço e pode ter um preço com desconto, caso não possua desconto o priceWithDiscount pode ser nulo.
                """;
        // Contexto do ChatGPT
        ObjectNode systemMessage = mapper.createObjectNode()
                .put("role", "system")
                .put("content", content);

        // Mensagem
        JsonNode message = mapper.createObjectNode()
                .put("role", "user")
                .set("content", contentArray);

        // Corpo final
        JsonNode root = mapper.createObjectNode()
                .put("model", "gpt-5-mini")
                .put("max_tokens", 2000)
                .set("messages", mapper.createArrayNode().add(systemMessage).add(message));


        String requestBody = mapper.writeValueAsString(root);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(OPENAI_URL);
            post.setHeader("Authorization", "Bearer " + openaiApiKey);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

            return mapper.readTree(client.execute(post).getEntity().getContent());
        }
    }

}
