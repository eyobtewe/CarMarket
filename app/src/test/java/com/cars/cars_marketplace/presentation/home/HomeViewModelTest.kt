package com.cars.cars_marketplace.presentation.home

import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.CarRepository
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import com.cars.cars_marketplace.domain.usecase.GetCarsUseCase
import com.cars.cars_marketplace.domain.usecase.GetFavoritesUseCase
import com.cars.cars_marketplace.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// Fake CarRepository that returns a successful list
class FakeCarRepository(private val cars: List<Car>) : CarRepository {
    override fun getCars(page: Int, limit: Int): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(cars))
    }

    override fun getCarById(id: String): Flow<Resource<Car>> = flow {
        emit(Resource.Error("Not implemented"))
    }
    override suspend fun refreshCars(page: Int, limit: Int): Resource<Unit> = Resource.Success(Unit)
}

// Fake FavoriteRepository with controllable behavior
class FakeFavoriteRepository(initial: List<Car> = emptyList(), var succeed: Boolean = true) :
        FavoriteRepository {
    private val state = MutableStateFlow(initial)

    override fun getFavorites(): Flow<List<Car>> = state

    override suspend fun addFavorite(carId: String): Resource<Unit> {
        return if (succeed) {
            val current = state.value.toMutableList()
            // add a placeholder Car with id
            current.add(Car(id = carId, make = "", model = "", year = 0, price = 0.0))
            state.value = current
            Resource.Success(Unit)
        } else Resource.Error("fail")
    }

    override suspend fun removeFavorite(carId: String): Resource<Unit> {
        return if (succeed) {
            state.value = state.value.filter { it.id != carId }
            Resource.Success(Unit)
        } else Resource.Error("fail")
    }

    override fun isFavorite(carId: String): Flow<Boolean> =
            flowOf(state.value.any { it.id == carId })
}

class HomeViewModelTest {

    private lateinit var sampleCars: List<Car>

    @Before
    fun setup() {
        sampleCars =
                listOf(
                        Car(
                                id = "1",
                                make = "Toyota",
                                model = "Corolla",
                                year = 2020,
                                price = 15000.0
                        ),
                        Car(id = "2", make = "Honda", model = "Civic", year = 2019, price = 14000.0)
                )
    }

    @Test
    fun toggleFavorite_optimistic_and_revert_on_error() = runBlocking {
        val carRepo = FakeCarRepository(sampleCars)
        val favRepo = FakeFavoriteRepository(initial = emptyList(), succeed = false)

        val getCarsUseCase = GetCarsUseCase(carRepo)
        val getFavoritesUseCase = GetFavoritesUseCase(favRepo)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(favRepo)

        val vm = HomeViewModel(getCarsUseCase, getFavoritesUseCase, toggleFavoriteUseCase)

        // wait briefly for init flows
        kotlinx.coroutines.delay(50)

        // Initially no favorites
        assertEquals(emptySet<String>(), vm.favoriteIds.value)

        // Toggle favorite (will fail)
        vm.toggleFavorite("1")
        // optimistic: should be added immediately
        assertEquals(setOf("1"), vm.favoriteIds.value)

        // give time for coroutine to process and revert
        kotlinx.coroutines.delay(100)

        // since toggle failed, optimistic update should be reverted (favRepo didn't add)
        assertEquals(emptySet<String>(), vm.favoriteIds.value)
    }

    @Test
    fun toggleFavorite_success_keeps_favorite() = runBlocking {
        val carRepo = FakeCarRepository(sampleCars)
        val favRepo = FakeFavoriteRepository(initial = emptyList(), succeed = true)

        val getCarsUseCase = GetCarsUseCase(carRepo)
        val getFavoritesUseCase = GetFavoritesUseCase(favRepo)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(favRepo)

        val vm = HomeViewModel(getCarsUseCase, getFavoritesUseCase, toggleFavoriteUseCase)

        kotlinx.coroutines.delay(50)

        vm.toggleFavorite("2")
        // optimistic: should be added immediately
        assertEquals(setOf("2"), vm.favoriteIds.value)

        // give time for coroutine to finish and favRepo to update
        kotlinx.coroutines.delay(100)

        // favRepo succeed should keep it
        assertEquals(setOf("2"), vm.favoriteIds.value)
    }
}
