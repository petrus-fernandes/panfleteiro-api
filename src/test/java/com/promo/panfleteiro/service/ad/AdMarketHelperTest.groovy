package com.promo.panfleteiro.service.ad

import br.com.promo.panfleteiro.v1.ad.AdSearchRequest
import br.com.promo.panfleteiro.v1.ad.Ad
import br.com.promo.panfleteiro.v1.location.Location
import br.com.promo.panfleteiro.v1.market.Market
import br.com.promo.panfleteiro.v1.ad.AdMarketHelper
import br.com.promo.panfleteiro.v1.market.MarketLocationHelper
import br.com.promo.panfleteiro.integration.service.GeocodingApiService
import br.com.promo.panfleteiro.v1.ad.AdResponse
import br.com.promo.panfleteiro.v1.location.LocationResponse
import br.com.promo.panfleteiro.v1.market.MarketResponse
import br.com.promo.panfleteiro.v1.ad.AdService
import br.com.promo.panfleteiro.v1.market.MarketService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class AdMarketHelperTest extends Specification {

    def geocodingApiService = Mock(GeocodingApiService)
    def marketService = Mock(MarketService)
    def adService = Mock(AdService)
    def marketLocationHelper = Mock(MarketLocationHelper)

    def adMarketHelper = new AdMarketHelper(
            adService: adService,
            marketService: marketService,
            geocodingApiService: geocodingApiService,
            marketLocationHelper: marketLocationHelper
    )


    def "should return ads in range with lat and lng informed"() {
        given:
        def lat1 = -23.553773
        def lng1 = -46.507487

        def location1 = new Location(
                latitude: lat1,
                longitude: lng1
        )

        def market1 = new Market(
                location: location1
        )

        def ad1 = new Ad(
                id: 1L,
                productName: "banana",
                active: true,
                markets: [market1]
        )

        def adResponse1 = new AdResponse(
                id: 1L,
                productName: "banana",
                active: true
        )

        adResponse1.creationDate = LocalDateTime.now()
        adResponse1.expirationDate = LocalDate.of(2024, 12, 31)

        def locationResponse1 = new LocationResponse(1L, lat1, lng1, "Rua abacaxi, 63", true)
        def marketResponse1 = new MarketResponse(1L, "Mercadinho da Dona Maria", locationResponse1, "MARIA", [], false)

        and:
        def lat2 = -23.515105
        def lng2 = -46.471096
        def location2 = new Location(
                latitude: lat2,
                longitude: lng2
        )

        def market2 = new Market(
                location: location2
        )

        def ad2 = new Ad(
                id: 2L,
                productName: "chocolate",
                active: true,
                markets: [market2]
        )

        def adResponse2 = new AdResponse(
                id: 2L,
                productName: "chocolate",
                active: true
        )

        adResponse2.creationDate = LocalDateTime.now()
        adResponse2.expirationDate = LocalDate.of(2024, 12, 31)

        def locationResponse2 = new LocationResponse(2L, lat2, lng2, "Rua uva, 13", true)
        def marketResponse2 = new MarketResponse(2L, "Mercadinho do Seu José", locationResponse2, "JOSE", [], false)

        and: "request parameters"
        def latRequest = -23.549843
        def lngRequest = -46.501571

        def request = new AdSearchRequest(
                rangeInKm: 4L,
                productName: "banana",
                latitude: latRequest,
                longitude: lngRequest
        )

        def pageable = Pageable.unpaged()

        and:
        def distance1 = market1.location.calculateDistanceInKm(request.latitude, request.longitude)
        marketResponse1.distance = distance1
        adResponse1.markets = [marketResponse1]

        and:
        def distance2 = market2.location.calculateDistanceInKm(request.latitude, request.longitude)
        marketResponse2.distance = distance2
        adResponse2.markets = [marketResponse2]

        and: "adService returns ads as if from database"
        def adsPage = new PageImpl<Ad>([ad1, ad2], pageable, 2)
        adService.search(_, _) >> adsPage

        and: "conversion to response"
        adService.convertToAdResponse(_ as Ad) >> { Ad ad ->
            if (ad.id == 1L) return adResponse1
            if (ad.id == 2L) return adResponse2
            return null
        }

        marketService.convertToMarketResponseWithDistance(request.latitude, request.longitude, market1) >> marketResponse1
        marketService.convertToMarketResponseWithDistance(request.latitude, request.longitude, market2) >> marketResponse2

        when:
        def result = adMarketHelper.searchAds(request, pageable)

        then:
        result.content.size() == 1
        result.content.first().productName == "banana"
        result.content.first().markets.first().distance.round(2) <= 0.74

        1 * adService.search(_, _) >> adsPage
        1 * adService.convertToAdResponse(ad1) >> adResponse1
        1 * marketService.convertToMarketResponseWithDistance(_, _, market1) >> marketResponse1
        0 * marketService.convertToMarketResponseWithDistance(_, _, market2)
    }
}
