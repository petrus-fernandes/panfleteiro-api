package br.com.promo.panfleteiro.v1.ad;

import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;


    public Ad create(AdRequest adRequest) {
        Ad ad = new Ad();
        ad.setProductName(adRequest.getProductName());
        ad.setExpirationDate(adRequest.getExpirationDate());
        ad.setInitialDate(adRequest.getInitialDate());
        ad.setProductCategory(ProductCategory.fromName(adRequest.getProductCategory()));
        ad.setPrice(adRequest.getPrice());
        ad.setActive(adRequest.getActive());
        ad.setUrl(adRequest.getUrl());
        return adRepository.save(ad);
    }

    public List<Ad> list() {
        return adRepository.findAll();
    }
    
    public void delete(Ad ad) {
        adRepository.delete(ad);
    }

    @NotNull
    public AdResponse convertToAdResponse(Ad ad) {
        AdResponse adResponse = new AdResponse();
        adResponse.setId(ad.getId());
        adResponse.setUrl(ad.getUrl());
        adResponse.setPrice(ad.getPrice());
        adResponse.setActive(ad.getActive());
        adResponse.setProductName(ad.getProductName());
        adResponse.setProductCategory(ad.getProductCategory().getName());
        adResponse.setInitialDate(ad.getInitialDate());
        adResponse.setExpirationDate(ad.getExpirationDate());
        adResponse.setCreationDate(ad.getCreationDate());
        return adResponse;
    }

    public Ad findById(Long id) {
        return adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found with ID: " + id));
    }

    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    public List<Ad> getActiveAds() {
        return adRepository.findByActive(true);
    }

    public Page<Ad> search(AdSearchRequest adSearchRequest, Pageable pageable) {
        Specification<Ad> specification = Specification.where(null);

        specification = specification.and(AdSpecification.productNameLike(adSearchRequest.getProductName()));

        specification = specification.and(
                AdSpecification.withinDistanceUsingGist(
                        adSearchRequest.getLatitude(),
                        adSearchRequest.getLongitude(),
                        adSearchRequest.getRangeInKm()
                )
        );

        specification = specification.and(
                AdSpecification.orderBySearchRanking(
                        adSearchRequest.getLatitude(),
                        adSearchRequest.getLongitude()
                )
        );

        return adRepository.findAll(specification, pageable);
    }
}
