package com.cars.cars_marketplace.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.usecase.RefreshCarsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncCarsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val refreshCarsUseCase: RefreshCarsUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val res = refreshCarsUseCase()
            when (res) {
                is Resource.Success -> Result.success()
                is Resource.Error -> Result.retry()
                else -> Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

