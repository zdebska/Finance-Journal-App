/*
* @author Kambulat Alakaev (xalaka00)
* @brief Class for transferring categories through fragments
* */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.journalapp.models.CategoryModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    var selectedCategory: CategoryModel? = null

    companion object {
        @Volatile
        private var instance: SharedViewModel? = null

        fun getInstance(application: Application): SharedViewModel =
            instance ?: synchronized(this) {
                instance ?: SharedViewModel(application).also { instance = it }
            }
    }
}



