package br.com.promo.panfleteiro.v1.ad;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.promo.panfleteiro.util.MessageHelper;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping(path = "v1/anuncios", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private AdMarketHelper adMarketHelper;

    private static final Logger logger = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private MessageHelper messageHelper;


    @PostMapping
    public ResponseEntity<List<AdResponse>> post(@Valid @RequestBody AdLotRequest adRequest) {
        List<AdResponse> adsResponse = adMarketHelper.createAdLot(adRequest);
        List<Long> adsId = adsResponse.stream().map(AdResponse::getId).collect(Collectors.toList());
        logger.info("Created Ads {} successfully", adsId);
        return ResponseEntity.status(201).body(adsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> get(@PathVariable Long id) {
        logger.info("Looking for AdResponse with ID: {}", id);
        return ResponseEntity.ok(adMarketHelper.convertToAdResponse(adService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdResponse> put(@PathVariable Long id, @RequestBody AdRequest adRequest) {
        logger.info("Updating AdRequest with ID: {}", id);
        return ResponseEntity.ok(adMarketHelper.updateAd(id, adRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting AdResponse with ID: {}", id);
        adMarketHelper.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<AdResponse>> searchAds(@ModelAttribute AdSearchRequest request, Pageable pageable) {
        validateAdSearchFields(request);
        return ResponseEntity.ok(adMarketHelper.searchAds(request, pageable));
    }

    private void validateAdSearchFields(AdSearchRequest request) {
        if (request.hasCep() && request.hasLatitudeAndLongitude()) {
            throw new RuntimeException(
                    messageHelper.get("ad.exception.adSearchMustHaveCepOrLatitudeAndLongitude")
            );
        }

        if (request.hasCep() && request.getCep().length() != 8) {
            throw new RuntimeException(
                    messageHelper.get("ad.exception.adSearchCepMustHave8digits")
            );
        }
    }

    @PostMapping("/desativarAnunciosExpirados")
    public ResponseEntity<Void> desativarAnunciosExpirados() {
        logger.info("Deactivate expired ads");
        adMarketHelper.deactivateEntitiesByExpiratedDate();
        return ResponseEntity.noContent().build();
    }
}
