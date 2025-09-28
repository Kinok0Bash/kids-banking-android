package edu.kinoko.kidsbankingandroid.data.service

import edu.kinoko.kidsbankingandroid.api.ParentClient
import edu.kinoko.kidsbankingandroid.api.request.RegistrationRequest

class ParentService(
    private val api: ParentClient
) {

    suspend fun createChildAccount(
        req: RegistrationRequest
    ) = api.createChildAccount(req)

    suspend fun getSalary(): Map<String, String> = api.getSalary()
}