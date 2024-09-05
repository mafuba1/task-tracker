$(document).ready(function() {

    $.ajaxSetup({
        beforeSend: function(xhr) {
            const token = localStorage.getItem('jwtToken');
            console.log('Before send:', token)
            if (token) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token)
            } else {
                console.warn('JWT token not found in localStorage');
            }
        }
    });

    $.ajax({
        method: 'GET',
        url: '/api/user',
        success: function(response) {
            // Если сервер подтверждает авторизацию, отображаем интерфейс для авторизованного пользователя
            $('#auth-buttons').hide();
            $('#welcome-message').show();
            $('#user-email').text(response.email);
        },
        error: function(xhr) {
            // Если сервер возвращает ошибку, считаем, что пользователь не авторизован
            if (xhr.status === 401) {
                localStorage.removeItem('jwtToken');
                $('#auth-buttons').show();
                $('#welcome-message').hide();
            } else {
                console.error('Ошибка при проверке аутентификации:', xhr.responseText);
            }
        }
    });

    // Показ модального окна регистрации
    $('#register-btn').click(function() {
        $('#authModalLabel').text('Регистрация');
        $('#repeat-password-field').show()
        $('#auth-error').hide();
        $('#authModal').modal('show');
    });

    // Показ модального окна авторизации
    $('#login-btn').click(function() {
        $('#authModalLabel').text('Авторизация');
        $('#repeat-password-field').hide()
        $('#auth-error').hide();
        $('#authModal').modal('show');
    });

    // Обработка формы регистрации/авторизации
    $('#auth-form').submit(function(event) {
        event.preventDefault();

        const isRegistration = $('#repeat-password-field').is(':visible');
        const email = $('#auth-email').val();
        const password = $('#auth-password').val();
        const repeatPassword = $('#auth-repeat-password').val();

        if (isRegistration && password !== repeatPassword) {
            $('#auth-error').text('Пароли не совпадают.').show();
            return;
        }

        const url = isRegistration ? '/api/user' : '/api/auth/login';

        $.ajax({
            method: 'POST',
            url: url,
            data: JSON.stringify({ email, password }),
            dataType: 'json',
            contentType: 'application/json',
            success: function(response) {
                if (response.token) {
                    console.log('Токен:', response.token);
                    localStorage.setItem('jwtToken', response.token); // Сохраняем JWT
                    $('#authModal').modal('hide');
                    $('#auth-buttons').hide();
                    $('#task-manager').show();
                    $('#user-info').html(`Добро пожаловать, ${email}`);
                } else {
                    $('#auth-error').text(response.error).show();
                    console.warn('Токен не найден:', response.error);
                }
            },
            error: function() {
                $('#auth-error').text('Ошибка сервера. Попробуйте позже.').show();
            }
        });
    });

    // Обработка выхода
    $('#logout-btn').click(function() {
        localStorage.removeItem('jwtToken');
        console.log('Выход из сессии');
        $.ajax({
            type: 'POST',
            url: '/api/auth/logout'
        });
        $('#task-manager').hide();
        $('#auth-buttons').show();
        $('#user-info').html('');
    });
});
