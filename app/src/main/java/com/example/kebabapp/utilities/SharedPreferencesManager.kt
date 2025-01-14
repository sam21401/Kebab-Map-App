import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    companion object {
        private const val PREF_NAME = "user_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val NAME = "name"
        private const val ISLOGGED = "is_logged"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ---USER NAME FUNCTIONS
    fun saveName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(NAME, name)
        editor.apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString(NAME, null)
    }

    fun clearName() {
        val editor = sharedPreferences.edit()
        editor.remove(NAME)
        editor.apply()
    }

    // TOKEN NAME FUNCTIONS
    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_AUTH_TOKEN)
        editor.apply()
    }

    fun login() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(ISLOGGED, true)
        editor.apply()
    }

    fun checkStatus(): Boolean {
        return sharedPreferences.getBoolean(ISLOGGED, false)
    }

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(ISLOGGED, false)
        editor.apply()
    }
}
