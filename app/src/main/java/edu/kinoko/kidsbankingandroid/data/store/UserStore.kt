package edu.kinoko.kidsbankingandroid.data.store

import android.util.Log
import edu.kinoko.kidsbankingandroid.data.dto.User
import edu.kinoko.kidsbankingandroid.data.enums.Role
import kotlinx.datetime.LocalDate
import java.util.UUID

object UserStore {
    private var _userData: User? = null

    var userData: User
        get() {
            if (_userData == null) {
                Log.e("UserStore", "Информация о пользователе отсутствует")
            }

            return _userData ?: User(
                id = UUID.randomUUID(),
                fullName = "ОШИБКА",
                lastname = "ОШИБКА",
                name = "ОШИБКА",
                fatherName = "ОШИБКА",
                username = "ОШИБКА",
                birthDate = LocalDate.parse("2000-01-01"),
                city = "ОШИБКА",
                role = Role.PARENT,
                isGetKid = false
            )
        }
        set(value) {
            _userData = value
        }
}
