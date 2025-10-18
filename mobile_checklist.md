# Mobile Implementation Checklist (Kotlin + Jetpack Compose)
## Time Estimate: 5-6 hours

---

## Phase 1: Project Setup (30 mins)

### Step 1.1: Create Android Project
- [ ] Open Android Studio
- [ ] New Project → Empty Activity (Compose)
- [ ] Name: CarEcommerce
- [ ] Package: com.yourname.carecommerce
- [ ] Minimum SDK: 24 (Android 7.0)
- [ ] Language: Kotlin
- [ ] Build configuration: Kotlin DSL

### Step 1.2: Update build.gradle.kts (Module level)

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-work:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    
    // Coil for images
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // DataStore for preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
```

### Step 1.3: Update build.gradle.kts (Project level)
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
```

### Step 1.4: Create Package Structure
```
com.yourname.carecommerce/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── entity/
│   │   └── database/
│   ├── remote/
│   │   ├── api/
│   │   ├── dto/
│   │   └── interceptor/
│   ├── repository/
│   └── worker/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── presentation/
│   ├── auth/
│   ├── home/
│   ├── detail/
│   ├── favorites/
│   ├── chat/
│   ├── profile/
│   ├── common/
│   └── navigation/
├── di/
└── util/
```

### Step 1.5: Setup Application Class
- [ ] Create CarEcommerceApp.kt
- [ ] Add @HiltAndroidApp annotation
- [ ] Update AndroidManifest.xml with android:name

---

## Phase 2: Clean Architecture - Data Layer (60 mins)

### Step 2.1: Remote API Setup

#### DTOs (data/remote/dto/)
- [ ] **LoginRequest.kt**
- [ ] **RegisterRequest.kt**
- [ ] **AuthResponse.kt** (token, user)
- [ ] **CarDto.kt** (all car fields)
- [ ] **FavoriteRequest.kt**
- [ ] **AiChatRequest.kt**
- [ ] **AiChatResponse.kt**
- [ ] **ApiResponse.kt** (generic wrapper)

#### API Interface (data/remote/api/ApiService.kt)
```kotlin
interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    @GET("auth/me")
    suspend fun getCurrentUser(): UserDto
    
    // Cars
    @GET("cars")
    suspend fun getCars(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("make") make: String?,
        @Query("bodyType") bodyType: String?,
        @Query("minPrice") minPrice: Int?,
        @Query("maxPrice") maxPrice: Int?
    ): CarsResponse
    
    @GET("cars/{id}")
    suspend fun getCarById(@Path("id") id: String): CarDto
    
    @GET("cars/search")
    suspend fun searchCars(@Query("q") query: String): CarsResponse
    
    // Favorites
    @GET("favorites")
    suspend fun getFavorites(): List<CarDto>
    
    @POST("favorites")
    suspend fun addFavorite(@Body request: FavoriteRequest): ApiResponse
    
    @DELETE("favorites/{carId}")
    suspend fun removeFavorite(@Path("carId") carId: String): ApiResponse
    
    // AI
    @POST("ai/chat")
    suspend fun chat(@Body request: AiChatRequest): AiChatResponse
}
```

#### Auth Interceptor (data/remote/interceptor/AuthInterceptor.kt)
- [ ] Read JWT token from DataStore
- [ ] Add "Authorization: Bearer {token}" header
- [ ] Handle 401 responses

### Step 2.2: Local Database (Room)

#### Entities (data/local/entity/)
- [ ] **CarEntity.kt** (mirror CarDto, add @Entity)
- [ ] **FavoriteEntity.kt** (id, carId, userId, timestamp)

#### DAOs (data/local/dao/)
- [ ] **CarDao.kt**
  - insertCars(cars: List<CarEntity>)
  - getAllCars(): Flow<List<CarEntity>>
  - getCarById(id: String): Flow<CarEntity?>
  - clearCars()
  
- [ ] **FavoriteDao.kt**
  - insertFavorite(favorite: FavoriteEntity)
  - deleteFavorite(carId: String)
  - getAllFavorites(): Flow<List<FavoriteEntity>>
  - isFavorite(carId: String): Flow<Boolean>

#### Database (data/local/database/AppDatabase.kt)
- [ ] Create RoomDatabase with CarEntity, FavoriteEntity
- [ ] Version 1
- [ ] Export DAO methods

### Step 2.3: DataStore for Token
- [ ] Create DataStoreManager in util/
- [ ] saveToken(token: String)
- [ ] getToken(): Flow<String?>
- [ ] clearToken()

### Step 2.4: Repositories Implementation (data/repository/)

#### AuthRepositoryImpl.kt
- [ ] login(email, password): Resource<AuthResponse>
- [ ] register(name, email, password): Resource<AuthResponse>
- [ ] getCurrentUser(): Resource<User>
- [ ] Use try-catch with Resource wrapper

#### CarRepositoryImpl.kt
- [ ] getCars(filters): Flow<Resource<List<Car>>>
- [ ] getCarById(id): Flow<Resource<Car>>
- [ ] searchCars(query): Flow<Resource<List<Car>>>
- [ ] Cache cars in Room
- [ ] Emit from local first, then fetch from remote

#### FavoriteRepositoryImpl.kt
- [ ] getFavorites(): Flow<List<Car>>
- [ ] addFavorite(carId): Resource<Unit>
- [ ] removeFavorite(carId): Resource<Unit>
- [ ] isFavorite(carId): Flow<Boolean>
- [ ] Sync with local database

#### AiRepositoryImpl.kt
- [ ] sendChatMessage(message): Resource<AiChatResponse>

---

## Phase 3: Clean Architecture - Domain Layer (30 mins)

### Step 3.1: Domain Models (domain/model/)
- [ ] **User.kt**
- [ ] **Car.kt**
- [ ] **ChatMessage.kt**
- [ ] **Resource.kt** (sealed class: Success, Error, Loading)

### Step 3.2: Repository Interfaces (domain/repository/)
- [ ] **AuthRepository.kt**
- [ ] **CarRepository.kt**
- [ ] **FavoriteRepository.kt**
- [ ] **AiRepository.kt**

### Step 3.3: Use Cases (domain/usecase/)
- [ ] **LoginUseCase.kt**
- [ ] **RegisterUseCase.kt**
- [ ] **GetCarsUseCase.kt**
- [ ] **GetCarByIdUseCase.kt**
- [ ] **ToggleFavoriteUseCase.kt**
- [ ] **SendChatMessageUseCase.kt**
- [ ] **GetFavoritesUseCase.kt**

Each use case:
```kotlin
class GetCarsUseCase(private val repository: CarRepository) {
    operator fun invoke(filters: CarFilters) = repository.getCars(filters)
}
```

---

## Phase 4: Dependency Injection (30 mins)

### Step 4.1: Modules (di/)

#### NetworkModule.kt
- [ ] Provide OkHttpClient (with AuthInterceptor)
- [ ] Provide Retrofit
- [ ] Provide ApiService
- [ ] @Singleton scope

#### DatabaseModule.kt
- [ ] Provide AppDatabase
- [ ] Provide CarDao
- [ ] Provide FavoriteDao
- [ ] @Singleton scope

#### RepositoryModule.kt
- [ ] Bind AuthRepository to AuthRepositoryImpl
- [ ] Bind CarRepository to CarRepositoryImpl
- [ ] Bind FavoriteRepository to FavoriteRepositoryImpl
- [ ] Bind AiRepository to AiRepositoryImpl

#### DataStoreModule.kt
- [ ] Provide DataStore<Preferences>
- [ ] Provide DataStoreManager

---

## Phase 5: WorkManager (30 mins)

### Step 5.1: Create Worker (data/worker/SyncCarsWorker.kt)
- [ ] Extend CoroutineWorker
- [ ] Fetch latest cars from API
- [ ] Update local database
- [ ] Return Result.success() or Result.retry()
- [ ] Add @HiltWorker annotation

### Step 5.2: Schedule Work
- [ ] Create WorkManager setup in Application class
- [ ] PeriodicWorkRequest (repeat every 6 hours)
- [ ] Constraints: NetworkType.CONNECTED
- [ ] enqueueUniquePeriodicWork()

---

## Phase 6: Presentation Layer - UI Screens (2.5 hours)

### Step 6.1: Navigation Setup (presentation/navigation/)

#### Screen.kt
```kotlin
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object CarDetail : Screen("car_detail/{carId}")
    object Favorites : Screen("favorites")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
}
```

#### NavGraph.kt
- [ ] Setup NavHost
- [ ] Define all routes
- [ ] Handle navigation arguments

### Step 6.2: Auth Screens (presentation/auth/)

#### LoginScreen.kt
- [ ] Email + Password fields
- [ ] Login button
- [ ] Navigate to Register
- [ ] Show loading state
- [ ] Handle errors
- [ ] Navigate to Home on success

#### LoginViewModel.kt
- [ ] LoginUseCase injection
- [ ] UIState (idle, loading, success, error)
- [ ] login() function
- [ ] Save token on success

#### RegisterScreen.kt
- [ ] Name, Email, Password fields
- [ ] Register button
- [ ] Similar to Login

### Step 6.3: Home Screen (presentation/home/)

#### HomeScreen.kt
- [ ] Top bar with search icon
- [ ] Filter chips (body type, price range)
- [ ] LazyVerticalGrid of cars
- [ ] Car card composable
- [ ] Pull to refresh
- [ ] Navigate to detail on click
- [ ] Bottom navigation bar

#### HomeViewModel.kt
- [ ] GetCarsUseCase injection
- [ ] StateFlow<List<Car>>
- [ ] Filter state
- [ ] Load cars on init
- [ ] applyFilters() function

#### CarCard composable
- [ ] AsyncImage (Coil)
- [ ] Make, Model, Year
- [ ] Price
- [ ] Favorite icon button

### Step 6.4: Car Detail Screen (presentation/detail/)

#### CarDetailScreen.kt
- [ ] Image carousel (HorizontalPager)
- [ ] Car info sections
- [ ] Features list
- [ ] Favorite button
- [ ] Contact seller button (optional)
- [ ] Back button

#### CarDetailViewModel.kt
- [ ] GetCarByIdUseCase
- [ ] ToggleFavoriteUseCase
- [ ] StateFlow<Car?>
- [ ] StateFlow<Boolean> (isFavorite)
- [ ] loadCarDetails(carId)
- [ ] toggleFavorite()

### Step 6.5: Favorites Screen (presentation/favorites/)

#### FavoritesScreen.kt
- [ ] Similar to Home but shows favorites
- [ ] Empty state ("No favorites yet")
- [ ] LazyColumn of favorite cars

#### FavoritesViewModel.kt
- [ ] GetFavoritesUseCase
- [ ] StateFlow<List<Car>>
- [ ] Load favorites on init

### Step 6.6: AI Chat Screen (presentation/chat/)

#### ChatScreen.kt
- [ ] LazyColumn for messages
- [ ] User messages (right-aligned)
- [ ] AI messages (left-aligned)
- [ ] Recommended cars list below AI message
- [ ] Text input field at bottom
- [ ] Send button

#### ChatViewModel.kt
- [ ] SendChatMessageUseCase
- [ ] StateFlow<List<ChatMessage>>
- [ ] StateFlow<List<Car>> (recommendations)
- [ ] sendMessage(text: String)
- [ ] Add user message immediately
- [ ] Show loading
- [ ] Add AI response

#### ChatMessage composable
- [ ] Bubble design
- [ ] Different colors for user/ai
- [ ] Timestamp

### Step 6.7: Profile Screen (presentation/profile/)

#### ProfileScreen.kt
- [ ] User info display
- [ ] Logout button
- [ ] App version
- [ ] Simple settings (optional)

#### ProfileViewModel.kt
- [ ] Get current user info
- [ ] Logout function (clear token)

### Step 6.8: Common Components (presentation/common/)
- [ ] **LoadingIndicator.kt**
- [ ] **ErrorMessage.kt**
- [ ] **EmptyState.kt**
- [ ] **FilterChip.kt**
- [ ] **SearchBar.kt** (optional)

---

## Phase 7: Integration & Testing (45 mins)

### Step 7.1: Connect Everything
- [ ] Test login flow
- [ ] Verify JWT token storage
- [ ] Test car list loading
- [ ] Test offline caching (Room)
- [ ] Test favorites sync
- [ ] Test AI chat with real API
- [ ] Test WorkManager (check logs)
- [ ] Test navigation between screens

### Step 7.2: Handle Edge Cases
- [ ] Network errors
- [ ] Empty states
- [ ] Loading states
- [ ] Token expiration
- [ ] Image loading failures

### Step 7.3: UI Polish
- [ ] Add proper spacing
- [ ] Consistent colors
- [ ] Material 3 theme
- [ ] Smooth animations
- [ ] Proper error messages

---

## Phase 8: Final Touches (30 mins)

### Step 8.1: App Configuration
- [ ] Update AndroidManifest.xml
  - Internet permission
  - Network state permission
- [ ] Update strings.xml
- [ ] Add app icon (optional)

### Step 8.2: Code Cleanup
- [ ] Remove unused imports
- [ ] Format code
- [ ] Add comments where needed
- [ ] Check for hardcoded values

### Step 8.3: Documentation
- [ ] README.md with:
  - Features implemented
  - Architecture explanation
  - Screenshots (if time)
  - Setup instructions

---

## Quick Reference: Key Patterns

### ViewModel Pattern
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCarsUseCase: GetCarsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadCars()
    }
    
    fun loadCars() {
        viewModelScope.launch {
            getCarsUseCase().collect { resource ->
                _uiState.value = when(resource) {
                    is Resource.Success -> UiState.Success(resource.data)
                    is Resource.Error -> UiState.Error(resource.message)
                    is Resource.Loading -> UiState.Loading
                }
            }
        }
    }
}
```

### Room Flow Pattern
```kotlin
@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAllCars(): Flow<List<CarEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCars(cars: List<CarEntity>)
}
```

### Compose Navigation
```kotlin
NavHost(navController, startDestination = Screen.Login.route) {
    composable(Screen.Login.route) {
        LoginScreen(navController)
    }
    composable(
        route = Screen.CarDetail.route,
        arguments = listOf(navArgument("carId") { type = NavType.StringType })
    ) { backStackEntry ->
        val carId = backStackEntry.arguments?.getString("carId")
        CarDetailScreen(navController, carId)
    }
}
```

---

## Completion Checklist
- [ ] All 7 screens implemented
- [ ] Clean Architecture (3 layers) ✓
- [ ] Hilt DI working ✓
- [ ] Retrofit + Coroutines for API calls ✓
- [ ] Room for offline caching ✓
- [ ] WorkManager scheduled ✓
- [ ] AI chat functional ✓
- [ ] Navigation working ✓
- [ ] App runs without crashes
- [ ] Ready for demo

**Estimated Total Time: 5-6 hours**

---

## Pro Tips
- Use Android Studio's code generation (Alt+Insert)
- Let GitHub Copilot/ChatGPT help with boilerplate
- Test on emulator frequently
- Focus on functionality first, polish later
- Keep build.gradle dependencies updated
- Use sealed classes for states
- Remember to handle loading states in UI