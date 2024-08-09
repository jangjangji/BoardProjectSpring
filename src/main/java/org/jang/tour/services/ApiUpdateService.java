package org.jang.tour.services;


import lombok.RequiredArgsConstructor;
import org.jang.global.rests.gov.api.ApiItem;
import org.jang.global.rests.gov.api.ApiResult;
import org.jang.tour.entities.TourPlace;
import org.jang.tour.repositories.TourPlaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ApiUpdateService {

    private final RestTemplate restTemplate;
    private final TourPlaceRepository repository;


    private String serviceKey = "kEX9PvC20lICTKmXbOsb%2FEyOzf7hTpVMkJNtoPoYz6kH%2BAS9LaKoqZ3M02TrNqSLvFP19TNHkWvnBgfdwXz9ig%3D%3D";

    //@Scheduled(fixedRate = 1L, timeUnit = TimeUnit.DAYS)
    public void update() {

        for (int i = 1; i <= 60; i++) {
            String url = String.format("https://apis.data.go.kr/B551011/KorService1/areaBasedList1?MobileOS=AND&MobileApp=test&numOfRows=10&pageNo=%d&serviceKey=%s&_type=json", i, serviceKey );

            ResponseEntity<ApiResult> response = null;
            try {
                response = restTemplate.getForEntity(URI.create(url), ApiResult.class);
            } catch (Exception e) {
                break;
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                List<ApiItem> items = response.getBody().getResponse().getBody().getItems().getItem();

                for(ApiItem item : items) {
                    try {
                        String address = item.getAddr1();
                        address += StringUtils.hasText(item.getAddr2()) ? " " + item.getAddr2() : "";

                        TourPlace tourPlace = TourPlace.builder()
                                .contentId(item.getContentid())
                                .contentTypeId(item.getContenttypeid())
                                .category1(item.getCat1())
                                .category2(item.getCat2())
                                .category3(item.getCat3())
                                .title(item.getTitle())
                                .tel(item.getTel())
                                .address(address)
                                .areaCode(item.getAreacode())
                                .bookTour(item.getBooktour().equals("1"))
                                .distance(item.getDist())
                                .firstImage(item.getFirstimage())
                                .firstImage2(item.getFirstimage2())
                                .cpyrhtDivCd(item.getCpyrhtDivCd())
                                .latitude(item.getMapy())
                                .longitude(item.getMapx())
                                .mapLevel(item.getMlevel())
                                .sigugunCode(item.getSigungucode())
                                .build();
                        repository.saveAndFlush(tourPlace);

                    } catch (Exception e) {
                        // 예외 발생하면 이미 등록된 여행지
                    }
                }
            } // endif
        }



    }
}