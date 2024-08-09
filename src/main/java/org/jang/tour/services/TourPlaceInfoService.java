package org.jang.tour.services;

import lombok.RequiredArgsConstructor;
import org.jang.global.rests.gov.api.ApiItem;
import org.jang.global.rests.gov.api.ApiResult;
import org.jang.tour.controllers.TourPlaceSearch;
import org.jang.tour.entities.QTourPlace;
import org.jang.tour.entities.TourPlace;
import org.jang.tour.repositories.TourPlaceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
public class TourPlaceInfoService {

    private final RestTemplate restTemplate;
    private final TourPlaceRepository repository;
    private String serviceKey = "kEX9PvC20lICTKmXbOsb%2FEyOzf7hTpVMkJNtoPoYz6kH%2BAS9LaKoqZ3M02TrNqSLvFP19TNHkWvnBgfdwXz9ig%3D%3D";

    public List<TourPlace> getList(TourPlaceSearch search) {
        double lat = search.getLatitude();
        double lon = search.getLongitude();
        int radius = search.getRadius();

        String url = String.format("https://apis.data.go.kr/B551011/KorService1/locationBasedList1?MobileOS=AND&MobileApp=test&mapX=%f&mapY=%f&radius=%d&numOfRows=1000&serviceKey=%s&_type=json", lon, lat, radius, serviceKey);

        //위치 기반으로 검색한거 다 가져오기
        try {
            ResponseEntity<ApiResult> response = restTemplate.getForEntity(URI.create(url), ApiResult.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResponse().getHeader().getResultCode().equals("0000")) {

                List<Long> ids = response.getBody().getResponse().getBody().getItems().getItem().stream().map(ApiItem::getContentid).toList();
                if(ids.isEmpty()) {
                    QTourPlace tourPlace = QTourPlace.tourPlace;
                    List<TourPlace> items =(List<TourPlace>) repository.findAll(tourPlace.contentId.in(ids),Sort.by(asc("contendId")));
                    return items;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}