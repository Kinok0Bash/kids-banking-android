package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.data.config.Network

object Services {
    val auth by lazy { AuthService(Network.authClientAuthed, Network.access) }
    val balance by lazy { BalanceService(Network.balanceClient) }
    val limit by lazy { LimitService(Network.limitClient) }
    val parent by lazy { ParentService(Network.parentClient) }
    val transaction by lazy { TransactionService(Network.transactionClient) }
}
