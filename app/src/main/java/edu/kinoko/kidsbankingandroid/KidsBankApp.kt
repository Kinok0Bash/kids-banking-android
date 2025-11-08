package edu.kinoko.kidsbankingandroid

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import edu.kinoko.kidsbankingandroid.data.config.Network

class KidsBankApp : Application(), CameraXConfig.Provider {
    override fun getCameraXConfig(): CameraXConfig = Camera2Config.defaultConfig()

    override fun onCreate() {
        super.onCreate()
        Network.init(
            appContext = this,
            baseUrl = "https://kinoko.su/api/"
        )
    }
}