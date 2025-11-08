package edu.kinoko.kidsbankingandroid.data.enums

enum class GlobalError(
    val header: String,
    val message: String
) {

    NETWORK_ERROR(
        header = "Ошибка сети",
        message = "Ошибка подключения к интернету. Проверьте свое подключение и попробуйте еще раз",
    ),

    SERVER_ERROR(
        header = "Ошибка сервера",
        message = "Сервер недоступен. Попробуйте еще раз через некоторое время",
    ),

    UNKNOWN_ERROR(
        header = "Неизвестная ошибка",
        message = "Случилась неизведанная ошибка. Скорее всего ты влез в код и что-то не то натыкал. Не делай так больше позязя)",
    ),
}