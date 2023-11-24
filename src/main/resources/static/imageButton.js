    const upload1 = document.querySelector(".upload1");
    const uploadBtn1 = document.querySelector(".clickUpload1");

    uploadBtn1.addEventListener('click', () => upload1.click());

    upload1.addEventListener('change', function () {
        const files = this.files;

        if (files.length > 0) {
            const file = files[0];
            const fileName = file.name;
            const reader = new FileReader();

            reader.onload = function (e) {
                uploadBtn1.src = e.target.result;
            };

            reader.readAsDataURL(file);
            document.querySelector('#fileName1').innerText = fileName;
        } else {
            // 파일이 선택되지 않은 경우의 처리
            uploadBtn1.src = "/image/inputImage.jpg";
            document.querySelector('#fileName1').innerText = "선택된 파일 없음";
        }
    });


        const upload2 = document.querySelector(".upload2");
        const uploadBtn2 = document.querySelector(".clickUpload2");

        uploadBtn2.addEventListener('click', () => upload2.click());

        upload2.addEventListener('change', function () {
            const files = this.files;

        if (files.length > 0) {
            const file = files[0];
            const fileName = file.name;
            const reader = new FileReader();

            reader.onload = function (e) {
                uploadBtn2.src = e.target.result;
            };

            reader.readAsDataURL(file);
            document.querySelector('#fileName2').innerText = fileName;
        } else {
            uploadBtn2.src = "/image/inputImage.jpg";
            document.querySelector('#fileName2').innerText = "선택된 파일 없음";
        }
        });

        const upload3 = document.querySelector(".upload3");
        const uploadBtn3 = document.querySelector(".clickUpload3");

        uploadBtn3.addEventListener('click', () => upload3.click());

        upload3.addEventListener('change', function () {
            const files = this.files;

        if (files.length > 0) {
            const file = files[0];
            const fileName = file.name;
            const reader = new FileReader();

            reader.onload = function (e) {
                uploadBtn3.src = e.target.result;
            };

            reader.readAsDataURL(file);
            document.querySelector('#fileName3').innerText = fileName;
        } else {
            uploadBtn1.src = "/image/inputImage.jpg";
            document.querySelector('#fileName3').innerText = "선택된 파일 없음";
        }
        });