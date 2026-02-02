package com.example.core_common.usecase

abstract class UseCase<in P, out R> {
    abstract suspend fun run(params: P): R
    suspend operator fun invoke(params: P): R = run(params)
}

abstract class FlowUseCase<in P, out R> {
    abstract fun run(params: P): kotlinx.coroutines.flow.Flow<R>
    operator fun invoke(params: P) = run(params)
}
