package br.com.promo.panfleteiro.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @RequestMapping("/**")
    public ResponseEntity<String> handleUnknownPaths() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint não encontrado");
    }
}
