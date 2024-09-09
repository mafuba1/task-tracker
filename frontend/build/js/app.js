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
            $('#task-manager').show();
            fetchTasks();
        },
        error: function(xhr) {
            // Если сервер возвращает ошибку, считаем, что пользователь не авторизован
            if (xhr.status === 401) {
                localStorage.removeItem('jwtToken');
                $('#auth-buttons').show();
                $('#welcome-message').hide();
                $('#task-manager').hide();
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

    const taskApiUrl = '/api/tasks';

    // Fetch all tasks when page is loaded
    function fetchTasks() {
        $.ajax({
            url: taskApiUrl,
            type: 'GET',
            success: function (response) {
                displayTasks(response.tasks);
            },
            error: function (error) {
                console.error('Error fetching tasks:', error);
            }
        });
    }

    // Display tasks in the task list
    function displayTasks(tasks) {
        const taskList = $('#taskList');
        taskList.empty();

        tasks.forEach(addTaskToUI);
    }

    // Create new task and immediately add it to the UI
    $('#createTaskForm').submit(function (event) {
        event.preventDefault();

        const taskHeader = $('#taskHeader').val();
        const taskDescription = $('#taskDescription').val();
        const taskDeadline = $('#taskDeadline').val();

        if (taskHeader === '') {
            alert('Task header is required');
            return;
        }

        const newTask = {
            header: taskHeader,
            description: taskDescription,
            deadline_timestamp: taskDeadline ? new Date(taskDeadline).toISOString() : null
        };

        $.ajax({
            url: taskApiUrl,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(newTask),
            success: function (response) {
                $('#taskHeader').val('');
                $('#taskDescription').val('');
                $('#taskDeadline').val('');

                // Добавляем созданную задачу в UI
                addTaskToUI(response);
            },
            error: function (error) {
                console.error('Error creating task:', error);
            }
        });
    });

    // Add task to UI
    function addTaskToUI(task) {
        const taskList = $('#taskList');
        const deadline = task.deadline_timestamp ? new Date(task.deadline_timestamp).toLocaleString() : 'No deadline';
        const done = task.done_timestamp ? `Done at: ${new Date(task.done_timestamp).toLocaleString()}` : 'Not done yet';

        const taskItem = `
            <li class="list-group-item d-flex justify-content-between align-items-center" id="task-${task.id}">
                <div>
                    <h5>${task.header}</h5>
                    <p>${task.description || 'No description'}</p>
                    <p><strong>Deadline:</strong> ${deadline}</p>
                    <p><strong>Status:</strong> ${done}</p>
                </div>
                <div>
                    <button class="btn btn-success btn-sm mr-2" onclick="markTaskDone(${task.id})">Mark Done</button>
                    <button class="btn btn-warning btn-sm mr-2" onclick="unmarkTaskDone(${task.id})">Unmark</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteTask(${task.id})">Delete</button>
                </div>
            </li>
        `;
        taskList.append(taskItem);
    }

    // Mark task as done
    window.markTaskDone = function (taskId) {
        $.ajax({
            url: `${taskApiUrl}/${taskId}/mark`,
            type: 'PATCH',
            success: function () {
                $(`#task-${taskId} .status`).text('Done');
            },
            error: function (error) {
                console.error('Error marking task as done:', error);
            }
        });
    };

    // Unmark task as done
    window.unmarkTaskDone = function (taskId) {
        $.ajax({
            url: `${taskApiUrl}/${taskId}/unmark`,
            type: 'PATCH',
            success: function () {
                $(`#task-${taskId} .status`).text('Not done yet');
            },
            error: function (error) {
                console.error('Error unmarking task:', error);
            }
        });
    };

    // Delete task
    window.deleteTask = function (taskId) {
        $.ajax({
            url: `${taskApiUrl}/${taskId}`,
            type: 'DELETE',
            success: function () {
                $(`#task-${taskId}`).remove(); // Удаляем задачу из UI
            },
            error: function (error) {
                console.error('Error deleting task:', error);
            }
        });
    };

});
