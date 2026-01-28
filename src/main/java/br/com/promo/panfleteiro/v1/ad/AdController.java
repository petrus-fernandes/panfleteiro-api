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
    public ResponseEntity<AdResponse> create(@Valid @RequestBody AdRequest adRequest) {
        logger.info("Creating AdRequest: {}", adRequest);
        AdResponse adsResponse = adMarketHelper.createAd(adRequest);
        logger.info("Created Ad successfully");
        return ResponseEntity.status(201).body(adsResponse);
    }

    @PostMapping("/lot")
    public ResponseEntity<List<AdResponse>> createLot(@Valid @RequestBody AdLotRequest adLotRequest) {
        logger.info("Creating AdRequest: {}", adLotRequest);
        List<AdResponse> adsResponse = adMarketHelper.createAdLot(adLotRequest);
        logger.info("Created Ad successfully");
        return ResponseEntity.status(201).body(adsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> findById(@PathVariable Long id) {
        logger.info("Looking for AdResponse with ID: {}", id);
        return ResponseEntity.ok(adMarketHelper.convertToAdResponse(adService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdResponse> update(@PathVariable Long id, @RequestBody AdRequest adRequest) {
        logger.info("Updating AdRequest with ID: {}", id);
        return ResponseEntity.ok(adMarketHelper.updateAd(id, adRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting AdResponse with ID: {}", id);
        adMarketHelper.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscaPorNome", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Page<AdResponse>> searchAdsByProductName(@RequestParam String productName, @RequestParam int page, @RequestParam int size) {
        logger.info("Searching for ads by product name: {}", productName);
        Pageable pageable = PageRequest.of(page, size, Sort.by("active").descending());
        Page<Ad> adsPage = adService.findAdsByProductName(productName, pageable);
        List<AdResponse> adsResponseList = getAdsResponseListSorted(adsPage);
        Page<AdResponse> adsResponsePage = new PageImpl<>(manualPageableAdResponse(page, size, adsResponseList), pageable, adsResponseList.size());

        logger.info("Found {} ads by distance.", adsResponsePage.getTotalElements());
        return ResponseEntity.ok(adsResponsePage);
    }

    @GetMapping("/buscaPorDistanciaENome")
    public ResponseEntity<Page<AdResponse>> searchAdsByProductNameAndDistance(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Long rangeInKm,
            @RequestParam String productName,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        logger.info("Searching for ads by product name: {} and distance: {} km using latitude: {} and longitude: {}",
                productName, rangeInKm, latitude, longitude);

        Pageable pageable = PageRequest.of(page, size);
        Page<Ad> adsPage = adService.findAdsByProductNameAndDistance(latitude, longitude, rangeInKm, pageable, productName);
        List<AdResponse> adsResponseList = getAdsResponseListSorted(latitude, longitude, rangeInKm, adsPage);
        Page<AdResponse> adsResponsePage = new PageImpl<>(adsResponseList, pageable, adsResponseList.size());

        logger.info("Found {} ads by product name and distance.", adsResponsePage.getTotalElements());
        return ResponseEntity.ok(adsResponsePage);
    }

    @GetMapping
    public ResponseEntity<Page<AdResponse>> searchAds(
            @ModelAttribute AdSearchRequest request,
            Pageable pageable
    ) {
        validateAdSearchFields(request);
        return ResponseEntity.ok(adMarketHelper.searchAds(request, pageable));
    }

    private void validateAdSearchFields(AdSearchRequest request) {
        if (request.hasCep() == request.hasLatitudeAndLongitude()) {
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


    @NotNull
    private static List<AdResponse> manualPageableAdResponse(Integer page, Integer size, List<AdResponse> adsResponseList) {
        int start = page * size;
        int end = Math.min(start + size, adsResponseList.size());
        return adsResponseList.subList(start, end);
    }

    @PostMapping("/desativarAnunciosExpirados")
    public ResponseEntity<Void> desativarAnunciosExpirados() {
        logger.info("Desativar Anuncios Expirados");
        adMarketHelper.deactivateEntitiesByExpiratedDate();
        return ResponseEntity.noContent().build();
    }

    private List<AdResponse> getAdsResponseListSorted(Double latitude, Double longitude, Long rangeInKm, Page<Ad> adsPage) {
        List<AdResponse> adsResponseList = adsPage.stream().map(ad -> {
            AdResponse adResponse = adMarketHelper.convertToAdResponseWithMarkets(ad, latitude, longitude);
            adResponse.orderMarketsByDistanceInRange(rangeInKm);
            return adResponse;
        }).collect(Collectors.toList());


        Comparator<AdResponse> comparator =
                Comparator.comparing(AdResponse::getActive, Comparator.reverseOrder())
                        .thenComparing(AdResponse::getCreationDate, Comparator.reverseOrder())
                        .thenComparing(AdResponse::getNearestMarketDistance)
                        .thenComparing(AdResponse::getProductName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(AdResponse::getExpirationDate);

        return adsResponseList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<AdResponse> getAdsResponseListSorted(Page<Ad> adsPage) {
        return adsPage.stream().map(adMarketHelper::convertToAdResponse)
                .sorted(Comparator.comparing(AdResponse::getActive).reversed()
                        .thenComparing(AdResponse::getCreationDate)
                        .thenComparing(AdResponse::getProductName)
                        .thenComparing(Comparator.comparing(AdResponse::getExpirationDate).reversed()))
                .collect(Collectors.toList());
    }
}
