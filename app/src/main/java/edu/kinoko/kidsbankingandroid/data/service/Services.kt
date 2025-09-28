package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.data.config.Network

object Services {
    val auth by lazy { AuthService(Network.authClientAuthed, Network.access) }
}
