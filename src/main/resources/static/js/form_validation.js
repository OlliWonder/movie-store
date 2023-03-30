function validateForm() {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation');
    const genre = document.getElementById("genre");
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                // TODO: Проверка на жанр (что жанр выбран)
                // if (genre.value === 'default') {
                //     alert("Пожалуйста, выберете жанр!");
                //     event.preventDefault()
                //     event.stopPropagation()
                //     return false;
                // }
                form.classList.add('was-validated')
            }, false)
        })
}