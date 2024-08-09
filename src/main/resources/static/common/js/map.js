const mapLib = {
    /**
     * 지도 로드
     *
     * @param mapId : 지도를 출력할 요소 id 이름
     * @param width : 지도 너비
     * @param height: 지도 높이
     * @param options - 옵션
     *               - center: { lat: 위도, lng: 경도} - 필수
     *               - zoom : 확대 정도(1~10) / 숫자가 작을 수록 확대
     - markerImage: 공통 마커 이미지 주소, 개별 마커 이미지가 있는 경우는 그걸로 대체,
     *               - marker : [{ lat: 위도, lng: 경도,
     info: { content: html 데이터(인포윈도우), clickable: true|false - true(마커 클릭시 노출) }, image: 이미지 주소 - 마커이미지}]
     */
    load(mapId ,options) {
        const mapEl = document.getElementById('map');
        if (!mapEl || !options?.center) return;
        mapEl.style.width = `px`;
        mapEl.style.height = `600px`;
        let { center} = options;
        // 지도 가운데 좌표 처리
        const zoom = options?.zoom ?? 3; // 기본값 3
        const position = new kakao.maps.LatLng(center.lat, center.lng);
        const map = new kakao.maps.Map(mapEl, {
            center: position,
            level: zoom,
        });
    },
    loadByKeyword( mapId,options) {
        if (!keyword?.trim()) return;
        const markers = [];
        const ps = new kakao.maps.services.Places();
        const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 })
        function searchPlaces() {
            let keyword = document.getElementById('keyword').value;

            if (!keyword.replace(/^\s+|\s+$/g, '')) {
                alert('키워드를 입력해주세요!');
                return false;
            }

            ps.keywordSearch(keyword, placesSearchCB);
        }
        function placesSearchCB(data, status, pagination) {
            if (status === kakao.maps.services.Status.OK) {
                displayPlaces(data);
                displayPagination(pagination);
            } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                alert('검색 결과가 존재하지 않습니다.');
                return;
            } else if (status === kakao.maps.services.Status.ERROR) {
                alert('검색 결과 중 오류가 발생했습니다.');
                return;
            }
        }
        function displayPlaces(places) {
            var listEl = document.getElementById('placesList');
            var menuEl = document.getElementById('menu_wrap');
            var fragment = document.createDocumentFragment();
            var bounds = new kakao.maps.LatLngBounds();

            removeAllChildNods(listEl);
            removeMarker();
            for (var i = 0; i < places.length; i++) {
                var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x);
                var marker = addMarker(placePosition, i);
                var itemEl = getListItem(i, places[i]);

                bounds.extend(placePosition);

                (function(marker, title) {
                    kakao.maps.event.addListener(marker, 'mouseover', function() {
                        displayInfowindow(marker, title);
                    });

                    kakao.maps.event.addListener(marker, 'mouseout', function() {
                        infowindow.close();
                    });

                    itemEl.onmouseover = function() {
                        displayInfowindow(marker, title);
                    };

                    itemEl.onmouseout = function() {
                        infowindow.close();
                    };
                })(marker, places[i].place_name);

                fragment.appendChild(itemEl);
            }
            listEl.appendChild(fragment);
            menuEl.scrollTop = 0;
            map.setBounds(bounds);
        }
        function getListItem(index, places) {
            var el = document.createElement('li');
            var itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
                '<div class="info">' +
                '   <h5>' + places.place_name + '</h5>';

            if (places.road_address_name) {
                itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' + places.address_name + '</span>';
            } else {
                itemStr += '    <span>' + places.address_name + '</span>';
            }

            itemStr += '  <span class="tel">' + places.phone + '</span>' +
                '</div>';

            el.innerHTML = itemStr;
            el.className = 'item';

            return el;
        }
        function addMarker(position, idx, title) {
            var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';
            var imageSize = new kakao.maps.Size(36, 37);
            var imgOptions = {
                spriteSize: new kakao.maps.Size(36, 691),
                spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10),
                offset: new kakao.maps.Point(13, 37)
            };
            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
            var marker = new kakao.maps.Marker({
                position: position,
                image: markerImage
            });

            marker.setMap(map);
            markers.push(marker);

            return marker;
        }
        function removeMarker() {
            for (var i = 0; i < markers.length; i++) {
                markers[i].setMap(null);
            }
        }
        function displayPagination(pagination) {
            var paginationEl = document.getElementById('pagination');
            var fragment = document.createDocumentFragment();

            while (paginationEl.hasChildNodes()) {
                paginationEl.removeChild(paginationEl.lastChild);
            }

            for (var i = 1; i <= pagination.last; i++) {
                var el = document.createElement('a');
                el.href = "#";
                el.innerHTML = i;

                if (i === pagination.current) {
                    el.className = 'on';
                } else {
                    el.onclick = (function(index) {
                        return function() {
                            pagination.gotoPage(index);
                        };
                    })(i);
                }

                fragment.appendChild(el);
            }

            paginationEl.appendChild(fragment);
        }
        function displayInfowindow(marker, title) {
            var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

            infowindow.setContent(content);
            infowindow.open(map, marker);
        }

        function removeAllChildNods(el) {
            while (el.hasChildNodes()) {
                el.removeChild(el.lastChild);
            }
        }

        window.searchPlaces = searchPlaces;


        mapLib.load(mapId, width, height, options);

    },
    loadCurrentLocation(mapId, width = 300, height = 300, options) {
        navigator.geolocation.getCurrentPosition(pos => {
            const { latitude, longitude } = pos.coords;
            options = options ?? {};
            options.center = { lat: latitude, lng: longitude };

            mapLib.load(mapId, width, height, options);
        });
    },
    /**
     * 키워드로 지도 출력
     *
     */

    /**
     * 주소로 지도 검색
     *
     */
    loadByAddress(address, cnt = 0, mapId, width = 300, height = 300, options) {

        if (!address?.trim()) return;

        const geocoder = new kakao.maps.services.Geocoder();

        geocoder.addressSearch(address, (items, status) => {
            if (status === kakao.maps.services.Status.OK) {
                // cnt가 0이면 전체 목록, 1 이상이면 갯수 제한
                items = cnt > 0 ? items.slice(0, cnt + 1) : items;

                options = options ?? {};
                options.center = { lat: items[0].y, lng: items[0].x };
                options.marker = [];

                items.forEach(item => {
                    options.marker.push({lat: item.y, lng: item.x});
                });
            }

            mapLib.load(mapId, width, height, options);
        });
    },
    /**
     * 분류별로 지도 조회
     *
     */
    loadByCategory(category, cnt = 0, mapId, width = 300, height = 300, options) {
        if (!category?.trim()) return;

        const ps = new kakao.maps.services.Places();
        ps.categorySearch(category.trim(), placesSearchCB, {useMapBounds:true});

        function placesSearchCB (items, status, pagination) {
            if (status === kakao.maps.services.Status.OK) {
                // cnt가 0이면 전체 목록, 1 이상이면 갯수 제한
                items = cnt > 0 ? items.slice(0, cnt + 1) : items;

                options = options ?? {};
                options.center = { lat: items[0].y, lng: items[0].x };
                options.marker = [];

                items.forEach(item => {
                    options.marker.push({lat: item.y, lng: item.x});
                });
            }

            mapLib.load(mapId, width, height, options);
        }
    }
};