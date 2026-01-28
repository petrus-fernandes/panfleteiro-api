package br.com.promo.panfleteiro.v1.flyer.validation;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlyerValidationService {

    @Autowired
    private FlyerValidationRepository repository;

    private static final Duration DURACAO_RESERVA = Duration.ofMinutes(15);

    public FlyerValidation reserveForUser(String usuario) throws BadRequestException {
        LocalDateTime expiracao = LocalDateTime.now().minus(DURACAO_RESERVA);
        Optional<FlyerValidation> optional = findFirstInQueue(expiracao);
        FlyerValidation firstFlyerValidation;
        if (optional.isPresent()) {
            firstFlyerValidation = optional.get();
            firstFlyerValidation.setReservedBy(usuario);
            firstFlyerValidation.setReserveDate(LocalDateTime.now());
        } else {
            throw new BadRequestException("Não há panfletos disponíveis para reserva");
        }

        return repository.save(firstFlyerValidation);
    }

    public void deleteValidation(Long id) throws BadRequestException {
        Optional<FlyerValidation> flyerValidation = repository.findById(id);
        flyerValidation.ifPresent(validation -> repository.delete(validation));
        if (flyerValidation.isEmpty()) {
            throw new BadRequestException("Validação nao encontrada");
        }
    }

    public FlyerValidation save(FlyerValidation flyerValidation) {
        return repository.save(flyerValidation);
    }

    private Optional<FlyerValidation> findFirstInQueue(LocalDateTime expiracao) {
        List<FlyerValidation> disponiveis = repository.findAvaiablesValidation(expiracao);
        return disponiveis.stream().findFirst();
    }

}

