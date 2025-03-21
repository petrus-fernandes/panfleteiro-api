package br.com.promo.panfleteiro.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.response.AdResponse;
import br.com.promo.panfleteiro.service.AdService;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping(path = "v1/anuncios", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class AdController {

    private final AdService adService;

    private final FlyerOrchestrator flyerOrchestrator;

    private static final Logger logger = LoggerFactory.getLogger(AdController.class);

    public AdController(AdService adService, FlyerOrchestrator flyerOrchestrator) {
        this.adService = adService;
        this.flyerOrchestrator = flyerOrchestrator;
    }

    @PostMapping
    public ResponseEntity<AdResponse> create(@Valid @RequestBody AdRequest adRequest) {
        logger.info("Creating AdRequest: {}", adRequest);
        AdResponse adResponse = adService.convertToAdResponse(flyerOrchestrator.createAd(adRequest));
        logger.info("Created AdResponse: {}", adResponse.getId());
        return ResponseEntity.status(201).body(adResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> findById(@PathVariable Long id) {
        logger.info("Looking for AdResponse with ID: {}", id);
        return ResponseEntity.ok(adService.convertToAdResponse(adService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<AdResponse>> list() {
        logger.info("Listing all AdResponses");
        return ResponseEntity.ok(adService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdResponse> update(@PathVariable Long id, @RequestBody AdRequest adRequest) {
        logger.info("Updating AdRequest with ID: {}", id);
        return ResponseEntity.ok(adService.convertToAdResponse(flyerOrchestrator.updateAd(id, adRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting AdResponse with ID: {}", id);
        flyerOrchestrator.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscaPorNome", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Page<AdResponse>> searchAdsByProductName(@RequestParam String productName, @RequestParam int page, @RequestParam int size) {
        logger.info("Searching for ads by product name: {}", productName);
        Pageable pageable = PageRequest.of(page, size, Sort.by("active", "product.name").ascending());
        Page<Ad> adsPage = adService.findAdsByProductName(productName, pageable);
        Page<AdResponse> adsResponsePage = adsPage.map(adService::convertToAdResponse);
        return ResponseEntity.ok(adsResponsePage);
    }

    @GetMapping("/buscaPorDistancia")
    public ResponseEntity<Page<AdResponse>> searchAdsByDistance(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Long rangeInKm, @RequestParam Integer page, @RequestParam Integer size) {
        logger.info("Searching for ads by distance: {} km using latitude: {} and longitude: {}", rangeInKm, latitude, longitude);
        Pageable pageable = PageRequest.of(page, size, Sort.by("active").ascending());
        Page<Ad> adsPage = adService.findAdsByDistance(latitude, longitude,rangeInKm, pageable);
        Page<AdResponse> adsResponsePage = new PageImpl<>(getAdsResponseListSorted(latitude, longitude, rangeInKm, adsPage));
        logger.info("Found {} ads by distance.", adsResponsePage.getTotalElements());
        return ResponseEntity.ok(adsResponsePage);
    }


    @GetMapping("/buscaPorDistanciaENome")
    public ResponseEntity<Page<AdResponse>> searchAdsByProductNameAndDistance(@RequestParam Double latitude,
                                                                              @RequestParam Double longitude,
                                                                              @RequestParam Long rangeInKm,
                                                                              @RequestParam String productName,
                                                                              @RequestParam Integer page,
                                                                              @RequestParam Integer size) {
        logger.info("Searching for ads by product name: {} and distance: {} km using latitude: {} and longitude: {}", productName, rangeInKm, latitude, longitude);
        Pageable pageableQuery = PageRequest.of(page, size, Sort.by("active").ascending());
        Page<Ad> adsPage = adService.findAdsByProductNameAndDistance(latitude, longitude,rangeInKm, pageableQuery, productName);
        List<AdResponse> adsResponseListSorted = getAdsResponseListSorted(latitude, longitude, rangeInKm, adsPage);
        List<AdResponse> paginatedList = getPaginatedList(adsResponseListSorted, (int) pageableQuery.getOffset(), pageableQuery.getPageSize());
        Pageable pageableResponse = PageRequest.of(page, size);
        Page<AdResponse> adsResponsePage = new PageImpl<>(paginatedList, pageableResponse, size);
        logger.info("Found {} ads by product name and distance.", adsResponsePage.getTotalElements());
        return ResponseEntity.ok(adsResponsePage);
    }

    @NotNull
    private static List<AdResponse> getPaginatedList(List<AdResponse> adsResponseListSorted, Integer offSet, Integer pageSize) {
        Integer totalElements = adsResponseListSorted.size();
        Integer start = offSet;
        Integer end = Math.min((start + pageSize), totalElements);

        if (start > totalElements) {
            start = totalElements;
            end = totalElements;
        }
        return adsResponseListSorted.subList(start, end);
    }

    private List<AdResponse> getAdsResponseListSorted(Double latitude, Double longitude, Long rangeInKm, Page<Ad> adsPage) {
        return adsPage.stream().filter(ad -> ad.getFlyerSection() != null && ad.getFlyerSection().getMarkets() != null)
                .flatMap(ad -> getAdResponseStreamForAllMarketsInRange(rangeInKm, ad, latitude, longitude))
                .sorted(Comparator.comparing(AdResponse::getActive)
                        .thenComparing(AdResponse::getDistance)
                        .thenComparing(AdResponse::getProductName)
                        .thenComparing(AdResponse::getExpirationDate)
                )
                .collect(Collectors.toList());
    }

    private Stream<AdResponse> getAdResponseStreamForAllMarketsInRange(Long rangeInKm, Ad ad, Double latitude, Double longitude) {
        return ad.getFlyerSection().getMarkets().stream()
                .filter(market -> market.getLocation().calculateDistanceInKm(latitude, longitude) <= rangeInKm)
                .map(market -> adService.convertToAdResponseWithUniqueMarketAndDistance(ad, market.getId(), market.getLocation().calculateDistanceInKm(latitude, longitude)));
    }
}
