package br.com.promo.panfleteiro.v1.flyer.validation;

import br.com.promo.panfleteiro.infra.security.TokenService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping(path = "/v1/validacoes", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
public class FlyerValidationController {

    @Autowired
    private FlyerValidationService service;

    @GetMapping
    public ResponseEntity<?> getNextValidation() throws BadRequestException {
        String user = TokenService.getCurrentUser();
        FlyerValidation flyerValidation = service.reserveForUser(user);
        return flyerValidation != null ? ResponseEntity.ok(toFlyerValidationResponse(flyerValidation)) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws BadRequestException {
        service.deleteValidation(id);
        return ResponseEntity.ok().build();
    }

    private FlyerValidationResponse toFlyerValidationResponse(FlyerValidation flyerValidation) {
        String imageBase64 = Base64.getEncoder().encodeToString(flyerValidation.getImage());
        return new FlyerValidationResponse(flyerValidation.getId(), imageBase64, flyerValidation.getResponse());
    }
}

