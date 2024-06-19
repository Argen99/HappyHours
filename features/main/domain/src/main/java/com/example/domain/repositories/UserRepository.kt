package com.example.domain.repositories

import com.example.core.either.Either
import com.example.core.either.NetworkError
import com.example.domain.models.UpdateUserDataRequest
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    fun getUser(): Flow<Either<NetworkError, User>>
    fun updateUserData(userData: UpdateUserDataRequest): Flow<Either<NetworkError, User>>
    fun logout(): Flow<Either<String, Unit>>
}