    var inputAddress = document.querySelector('#searchAddress').value;
    var infowindow = new kakao.maps.InfoWindow({zIndex:1});
    var locPosition = new kakao.maps.LatLng(myY, myX);

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
            position: new kakao.maps.LatLng(element.y, element.x),
            clickable: true
        });

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
        displayMarker_myLocation(locPosition);
    } else {

        var marker = new kakao.maps.Marker({
            map: map,
            position: locPosition
        });

        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:6px 0;">' + inputAddress + '</div>'
        });

        infowindow.open(map, marker);
    }

    map.setCenter(locPosition);

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
    }

    function closeOverlay() {
        overlay.setMap(null);
    }