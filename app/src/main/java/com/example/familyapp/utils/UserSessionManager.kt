import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

// Extension property pour créer DataStore par Context
val Context.dataStore by preferencesDataStore(name = "user_session")

class UserSessionManager(private val context: Context) {

        private val prefs: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        fun saveToken(token: String) {
            prefs.edit().putString("USER_TOKEN", token).apply()
        }

        fun getToken(): String? {
            return prefs.getString("USER_TOKEN", null)
        }

        fun saveUserId(userId: Int) {
            prefs.edit().putInt("USER_ID", userId).apply()
        }

        fun getUserId(): Int {
            return prefs.getInt("USER_ID", -1)
        }

        fun clearSession() {
            prefs.edit().clear().apply()
        }
    }