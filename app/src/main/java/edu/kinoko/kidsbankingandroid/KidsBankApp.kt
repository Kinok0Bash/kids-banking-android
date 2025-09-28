package edu.kinoko.kidsbankingandroid

import android.app.Application
import edu.kinoko.kidsbankingandroid.data.config.Network

class KidsBankApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Network.init(
            appContext = this,
            baseUrl = "https://kinoko.su/api/"
        )
    }
}