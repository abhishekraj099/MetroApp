package com.example.metro.domain.usecase

import com.example.metro.data.model.RouteResult
import com.example.metro.data.repository.HomeRepository

/**
 * Finds route between two stations and optionally saves it.
 */
class FindRouteUseCase(private val repository: HomeRepository) {

    operator fun invoke(from: String, to: String): Result<RouteResult> {
        if (from.isBlank() || to.isBlank()) {
            return Result.failure(IllegalArgumentException("Please select both stations"))
        }
        if (from.equals(to, ignoreCase = true)) {
            return Result.failure(IllegalArgumentException("From and To stations cannot be the same"))
        }
        val result = repository.findRoute(from, to)
            ?: return Result.failure(IllegalArgumentException("No route found between $from and $to"))
        return Result.success(result)
    }
}

