import com.example.kebabapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = BuildConfig.API_ADDRESS

    // Placeholder for the token, ideally stored in SharedPreferences or a secure location
    private var authToken: String? = null

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    // Interceptor to add the Bearer token to specific requests
    private val tokenInterceptor =
        Interceptor { chain ->
            val request = chain.request()
            val url = request.url.toString()

            // List of endpoints that require the token, with placeholders
            val endpointsRequiringToken =
                listOf(
                    "/api/logout",
                    "/api/profile",
                    "/api/kebab/showAll",
                    "/api/kebab/show",
                    "/api/kebab/details",
                    "/api/kebab/{id}/fav",
                    "/api/kebab/favorites",
                    "/api/kebab/{id}/favdelete",
                    "/api/suggest/add"
                )

            // Check if the URL matches any endpoint pattern
            val requiresToken =
                endpointsRequiringToken.any { pattern ->
                    matchEndpointPattern(pattern, url)
                }

            val requestBuilder = request.newBuilder()

            if (requiresToken && authToken != null) {
                requestBuilder.addHeader("Authorization", "Bearer $authToken")
            }

            chain.proceed(requestBuilder.build())
        }

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    // Helper function to match endpoint patterns with placeholders
    private fun matchEndpointPattern(
        pattern: String,
        url: String,
    ): Boolean {
        // Replace `{id}` placeholders with a regex wildcard (e.g., matching numeric IDs)
        val regexPattern = pattern.replace("{id}", "\\d+")
        return Regex(regexPattern).containsMatchIn(url)
    }

    fun setAuthToken(token: String) {
        authToken = token
    }

    fun getAuthToken(): String? {
        return authToken
    }
}
