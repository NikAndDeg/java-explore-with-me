# java-explore-with-me
Template repository for ExploreWithMe project.

Пул-реквест: https://github.com/NikAndDeg/java-explore-with-me/pull/3

Реализовал фичу с комментариями. Эндпоинты:

Private:

    POST /users/{userId}/comments/{eventId}
        Добавление комментария любым зарегистрированным пользователем к любому опубликованному событию.

    DELETE /users/{userId}/comments/{commentId}
        Удаление пользователем своего комментария.

    GET /users/{userId}/comments/{commentId}
        Получение комментария любым зарегестрированным пользователем.

    GET /users/{userId}/comments
        Получение комментариев к событию любым зарегестрированным пользователем с возможностью поиска.

Admin:

    PATCH /admin/comments/{commentId}
        Обновление комментария админом.

    DELETE /admin/comments/{commentId}
        Удаление комментария админом.

    GET /admin/comments/{commentId}
        Получение комментария админом.

    GET /admin/comments
        Получение комментариев к событию админом с возможностью поиска.

P.S. Я сознательно не дал возможности пользователям редактировать свои комментарии.