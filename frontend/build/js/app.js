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

    function displayAuthorized(email) {
        if (email) {
            $('#auth-buttons').hide();
            $('#welcome-message').show();
            $('#user-email').text(email);
            $('#task-manager').show();
        } else {
            $('#auth-buttons').show();
            $('#welcome-message').hide();
            $('#task-manager').hide();
        }
    }

    $.ajax({
        method: 'GET',
        url: '/api/user',
        success: function(response) {
            // Если сервер подтверждает авторизацию, отображаем интерфейс для авторизованного пользователя
            displayAuthorized(response.email)
            fetchTasks();
        },
        error: function(xhr) {
            // Если сервер возвращает ошибку, считаем, что пользователь не авторизован
            if (xhr.status === 401) {
                localStorage.removeItem('jwtToken');
                displayAuthorized(false)
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
                    displayAuthorized(email)
                    fetchTasks();
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
        displayAuthorized(false)
        localStorage.removeItem('jwtToken');
        console.log('Выход из сессии');
        $.ajax({
            type: 'POST',
            url: '/api/auth/logout'
        });
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
                handleError('Error fetching tasks:', error);
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

        const submitButton = $('#createTaskForm button[type="submit"]');
        submitButton.prop('disabled', true);

        const taskHeader = $('#taskHeader').val();
        const taskDescription = $('#taskDescription').val();
        const taskDeadline = $('#taskDeadline').val();

        if (taskHeader === '') {
            alert('Task header is required');
            submitButton.prop('disabled', false);
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

                addTaskToUI(response);
                submitButton.prop('disabled', false);
            },
            error: function (error) {
                handleError('Error creating task', error);
                submitButton.prop('disabled', false);
            }
        });
    });

    // Add task to UI
    function addTaskToUI(task) {
        const taskList = $('#taskList');
        const deadline = task.deadline_timestamp ? new Date(task.deadline_timestamp).toLocaleString() : 'No deadline';
        const done = task.done_timestamp ? `Done at: ${new Date(task.done_timestamp).toLocaleString()}` : 'Not done yet';

        const taskItem = $(`
            <li class="list-group-item d-flex justify-content-between align-items-center" id="task-${task.id}">
                <div>
                    <h5>${task.header}</h5>
                    <p>${task.description || 'No description'}</p>
                    <p><strong>Deadline:</strong> ${deadline}</p>
                    <p class="status"><strong>Status:</strong> ${done}</p>
                </div>
                <div>
                    <button class="btn btn-success btn-sm mr-2"">Mark Done</button>
                    <button class="btn btn-warning btn-sm mr-2">Unmark</button>
                    <button class="btn btn-info btn-sm mr-2">Edit</button>
                    <button class="btn btn-danger btn-sm"">Delete</button>
                </div>
            </li>
        `);

        const markButton = taskItem.find('button.btn-success');
        const unmarkButton = taskItem.find('button.btn-warning');
        const editButton = taskItem.find('button.btn-info');
        const deleteButton = taskItem.find('button.btn-danger');

        // Управляем видимостью кнопок
        if (task.done_timestamp) {
            markButton.hide();
            unmarkButton.show();
        } else {
            markButton.show();
            unmarkButton.hide();
        }

        // Добавляем обработчики событий
        markButton.click(() => markTaskDone(task.id));
        unmarkButton.click(() => unmarkTaskDone(task.id));
        editButton.click(() => editTask(task.id, task)); // При нажатии "Edit" открываем форму редактирования
        deleteButton.click(() => deleteTask(task.id));

        taskList.append(taskItem);
    }

    function handleError(message = 'An error occurred', error) {
        console.error(message, error);
        // TODO Добавить отображение ошибки в UI для пользователя
    }


    function updateTaskStatus(taskId, isDone) {
        const statusText = isDone ? `Done at: ${new Date().toLocaleString()}` : 'Not done yet';

        $.ajax({
            url: `${taskApiUrl}/${taskId}/${isDone ? 'mark' : 'unmark'}`,
            type: 'PATCH',
            success: function () {
                const task = $(`#task-${taskId}`)
                task.find('.status').html(`<strong>Status:</strong> ${statusText}`);
                task.find('button.btn-success').toggle(!isDone);
                task.find('button.btn-warning').toggle(isDone);
            },
            error: function (error) {
                handleError(isDone ? 'Error marking task as done' : 'Error unmarking task', error);
            }
        });
    }

// Использование
    window.markTaskDone = function (taskId) {
        updateTaskStatus(taskId, true);
    };

    // Unmark task as done
    window.unmarkTaskDone = function (taskId) {
        updateTaskStatus(taskId, false);
    };

    function editTask(taskId) {
        $.ajax({
            url: `${taskApiUrl}/${taskId}`,
            type: 'GET',
            success: function (task) {
                // Заполняем форму последними данными
                $('#editTaskHeader').val(task.header);
                $('#editTaskDescription').val(task.description);
                $('#editTaskDeadline').val(task.deadline_timestamp ? new Date(task.deadline_timestamp).toISOString().slice(0, 16) : '');

                // Показываем модальное окно
                $('#editTaskModal').modal('show');
            },
            error: function (error) {
                console.error('Error fetching task:', error);
            }
        });

        // Обрабатываем отправку формы
        $('#editTaskForm').off('submit').on('submit', function (event) {
            event.preventDefault();

            const deadline = $('#editTaskDeadline').val()

            const updatedTask = {
                header: $('#editTaskHeader').val(),
                description: $('#editTaskDescription').val(),
                deadline_timestamp: deadline ? new Date(deadline).toISOString() : null
            };

            $.ajax({
                url: `${taskApiUrl}/${taskId}`,
                type: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(updatedTask),
                success: function (response) {
                    const task = $(`#task-${taskId}`)
                    // Обновляем задачу в UI
                    task.find('h5').text(response.header);
                    task.find('p:eq(0)').text(response.description || 'No description');
                    task.find('p:eq(1)').html(`<strong>Deadline:</strong> ${response.deadline_timestamp ? new Date(response.deadline_timestamp).toLocaleString() : 'No deadline'}`);

                    // Закрываем модальное окно
                    $('#editTaskModal').modal('hide');
                },
                error: function (error) {
                    handleError('Error updating task:', error);
                }
            });
        });
    }



    // Delete task
    window.deleteTask = function (taskId) {
        $.ajax({
            url: `${taskApiUrl}/${taskId}`,
            type: 'DELETE',
            success: function () {
                $(`#task-${taskId}`).remove(); // Удаляем задачу из UI
            },
            error: function (error) {
                handleError('Error deleting task:', error);
            }
        });
    };

});
