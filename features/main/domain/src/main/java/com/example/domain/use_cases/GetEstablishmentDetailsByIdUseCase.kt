package com.example.domain.use_cases

import com.example.domain.repositories.EstablishmentRepository

class GetEstablishmentDetailsByIdUseCase(
    private val repo: EstablishmentRepository
) {
    operator fun invoke(id: Int) = repo.getEstablishmentDetailsById(id)
}