    var inputAddress = document.querySelector('#searchAddress').value;

    var mapContainer = document.getElementById('map'),
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    /* 마크 적용
    for(var i = 0; i < 10; i++) {
            var marker = new kakao.maps.Marker({
                map: map,
                position: new kakao.maps.LatLng(33.450701 + i * 0.001, 126.570667 + i * 0.001)
            });
        }
    */

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



