package br.com.promo.panfleteiro.integration.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class GoogleDriveService {

    private final String applicationName;

    public GoogleDriveService(@Value("${google.drive.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    public Drive getDriveService() throws Exception {
        try (InputStream credentialsStream = getCredentialsStream()) {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(credentialsStream)
                    .createScoped(List.of("https://www.googleapis.com/auth/drive"));

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(applicationName)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar credenciais do Google", e);
        }
    }

    private InputStream getCredentialsStream() throws IOException {
        String credPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        if (credPath != null) {
            java.io.File file = new java.io.File(credPath);
            if (!file.exists()) {
                throw new IllegalStateException("Arquivo de credenciais não encontrado em: " + credPath);
            }
            return new java.io.FileInputStream(file);

        } else {
            InputStream stream = GoogleDriveService.class.getResourceAsStream("/drive-service-account.json");
            if (stream == null) {
                throw new IllegalStateException("drive-service-account.json não encontrado no classpath");
            }
            return stream;
        }
    }


    public List<File> listarPastasDeMercado(String pastaId) throws Exception {
        Drive drive = getDriveService();

        FileList result = drive.files().list()
                .setQ(String.format("'%s' in parents and mimeType = 'application/vnd.google-apps.folder' and trashed = false", pastaId))
                .setFields("files(id, name)")
                .execute();

        return result.getFiles();
    }

    public List<File> listarImagensNaPasta(String pastaId) throws Exception {
        Drive drive = getDriveService();

        FileList result = drive.files().list()
                .setQ(String.format("'%s' in parents and mimeType contains 'image/' and trashed = false", pastaId))
                .setFields("files(id, name, mimeType)")
                .execute();

        return result.getFiles();
    }
}

