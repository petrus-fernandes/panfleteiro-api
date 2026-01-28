package br.com.promo.panfleteiro.integration.scheduler;

import br.com.promo.panfleteiro.v1.flyer.validation.FlyerValidation;
import br.com.promo.panfleteiro.integration.service.ChatGptService;
import br.com.promo.panfleteiro.integration.service.GoogleDriveService;
import br.com.promo.panfleteiro.v1.flyer.validation.FlyerValidationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Component
public class ImageProcessingScheduler {

    @Autowired
    private GoogleDriveService driveService;

    @Autowired
    private ChatGptService chatGptService;

    @Autowired
    private FlyerValidationService flyerValidationService;

    private final String pastaRaizId;

    private final String processedFolderId;

    public ImageProcessingScheduler(@Value("${google.drive.mercadao.main.directory.id}") String pastaRaizId,
                                    @Value("${google.drive.mercadao.processed.directory.id}") String processedFolderId) {
        this.pastaRaizId = pastaRaizId;
        this.processedFolderId = processedFolderId;
    }


    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void processarImagens() {
        log.info("Iniciando verificação de panfletos no Google Drive...");

        try {
            Drive drive = driveService.getDriveService();
            List<File> pastasDeMercado = driveService.listarPastasDeMercado(pastaRaizId);

            for (File pastaMercado : pastasDeMercado) {
                String mercado = pastaMercado.getName();
                String pastaId = pastaMercado.getId();
                List<File> pastasDeURL = driveService.listarPastasDeMercado(pastaId);

                for (File pastaURL : pastasDeURL) {
                    List<File> imagens = driveService.listarImagensNaPasta(pastaURL.getId());

                    for (File imagem : imagens) {
                        String nomeImagem = imagem.getName();
                        String urlImagem = pastaURL.getName();
                        java.io.File imagemArquivo = baixarImagemDoDrive(drive, imagem.getId(), nomeImagem);
                        log.info("Baixado panfleto do Google Drive: '{}', mercado: '{}', url: '{}'", nomeImagem, mercado, urlImagem);

                        log.info("Enviando panfleto ao ChatGPT: '{}', mercado: '{}', url: '{}'", nomeImagem, mercado, urlImagem);
                        JsonNode gptResponse = chatGptService.enviarImagem(imagemArquivo, urlImagem, mercado);
                        log.info("Panfleto processado com sucesso: '{}', mercado: '{}', url: '{}'", nomeImagem, mercado, urlImagem);

                        String respostaContent = gptResponse.get("choices").get(0).get("message").get("content").asText();

                        FlyerValidation flyerValidation = new FlyerValidation(Files.readAllBytes(imagemArquivo.toPath()), respostaContent);
                        flyerValidation = flyerValidationService.save(flyerValidation);
                        log.info("Validação salva com sucesso: '{}', mercado: '{}', url: '{}'", flyerValidation.getId(), mercado, urlImagem);

                        this.moverArquivoParaPastaProcessados(imagem.getId(), pastaURL.getId(), drive);
                        Files.deleteIfExists(imagemArquivo.toPath());
                        log.info("Imagem do panfleto deletada com sucesso: '{}', mercado: '{}', url: '{}'", nomeImagem, mercado, urlImagem);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao processar imagens: ", e);
        }
    }

    private java.io.File baixarImagemDoDrive(Drive drive, String fileId, String nomeArquivoLocal) throws IOException {
        java.io.File arquivoLocal = new java.io.File("/tmp/" + nomeArquivoLocal);
        try (FileOutputStream outputStream = new FileOutputStream(arquivoLocal)) {
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        }
        return arquivoLocal;
    }

    private void moverArquivoParaPastaProcessados(String fileId, String idPastaAtual, Drive drive) throws IOException {
        File fileMetadata = new File();

        drive.files().update(fileId, fileMetadata)
                .setAddParents(processedFolderId)
                .setRemoveParents(idPastaAtual)
                .setFields("id, parents")
                .execute();
    }
}
