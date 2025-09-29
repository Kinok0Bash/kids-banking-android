package edu.kinoko.kidsbankingandroid.data.store

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import edu.kinoko.kidsbankingandroid.data.dto.User

object UserStore {
    private var _userData: User? = null

    var userData: User
        get() {
            if (_userData == null) {
                Log.e("UserStore", "Информация о пользователе отсутствует")
            }

            return _userData ?: throw UserNotAuthenticatedException(
                "Пользователь не авторизован"
            )
        }
        set(value) {
            _userData = value
        }
}
