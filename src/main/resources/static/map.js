    var inputAddress = document.querySelector('#searchAddress').value;

    var mapContainer = document.getElementById('map'),
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    var overlay = new kakao.maps.CustomOverlay({
        yAnchor: 1.4
    });

    resArr.forEach(function(element) {
        var marker = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(element.x, element.y),
            clickable: true
        });
        console.log(typeof(element.y));

        var tmpOverlay = new kakao.maps.CustomOverlay({
            content: '<div class="card">'+element.name+'</div>',
            position: marker.getPosition(),
            map: map,
            yAnchor: 2.6
        });

        var content =
        '<div class="card">'+
            '<div class="text-end pb-0">'+
               '<button type="button" class="btn-close mt-2 me-2" aria-label="Close" onclick="closeOverlay();"></button>'+
            '</div>'+
            ((element.image != null) ? ('<img th:src="@{${'+element.image+'}}" class="card-img-top" alt="">') : (""))+
            '<div class="card-body pt-0">'+
                '<a href="/restaurant/detail/'+element.id+'" class="card-title text-center">'+
                    '<h5>'+
                        element.name +
                    '</h5>'+
                '</a>'+
                '<p class="card-text text-secondary">'+
                    element.address +
                '</p>'+
            '</div>'+
        '</div>'
        ;

    kakao.maps.event.addListener(marker, 'click', function() {
            infowindow.close();
            overlay.setContent(content);
            overlay.setPosition(marker.getPosition());
            overlay.setMap(map);
            tmpOverlay.setMap(null);
        });
    });

    if (inputAddress == 'aroundMe') {
    var infowindow = new kakao.maps.InfoWindow({zIndex:1});
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {

                var lat = position.coords.latitude,
                    lon = position.coords.longitude;

                var locPosition = new kakao.maps.LatLng(lat, lon);

                displayMarker_myLocation(locPosition);

              });
        }

    } else {
        var infowindow = new kakao.maps.InfoWindow({zIndex:1});

        var geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(inputAddress, function(result, status) {

             if (status === kakao.maps.services.Status.OK) {

                var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                var marker = new kakao.maps.Marker({
                    map: map,
                    position: coords
                });

                var infowindow = new kakao.maps.InfoWindow({
                    content: '<div style="width:150px;text-align:center;padding:6px 0;">' + inputAddress + '</div>'
                });

                infowindow.open(map, marker);

                map.setCenter(coords);

            }
        });
    }

    function displayMarker_myLocation(locPosition) {

        var imageSrc = '/image/myLocation.png',
            imageSize = new kakao.maps.Size(45, 50),
            imageOption = {offset: new kakao.maps.Point(21, 47)};

        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption)

        var marker = new kakao.maps.Marker({
            map: map,
            position: locPosition,
            image: markerImage
        });

        map.setCenter(locPosition);
    }

    function closeOverlay() {
        overlay.setMap(null);
    }



