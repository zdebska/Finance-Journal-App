/*
* @author Kambulat Alakaev (xalaka00)
* @brief Class for transferring categories through fragments
* */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.journalapp.models.CategoryModel
import com.example.journalapp.models.GoalModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    var selectedCategory: CategoryModel? = null
    val selectedGoal = MutableLiveData<GoalModel?>()

    companion object {
        @Volatile
        private var instance: SharedViewModel? = null

        fun getInstance(application: Application): SharedViewModel =
            instance ?: synchronized(this) {
                instance ?: SharedViewModel(application).also { instance = it }
            }
    }

    fun selectGoal(goal: GoalModel?) {
        if(goal == null){
            selectedGoal.value == null
        }
        else{
            selectedGoal.value = goal
        }

    }
}



