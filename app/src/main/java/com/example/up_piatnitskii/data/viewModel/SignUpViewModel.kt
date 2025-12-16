
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.up_piatnitskii.data.RetrofitInstance
import com.example.up_piatnitskii.data.Model.SignUpRequest
import com.example.up_piatnitskii.data.Model.User
import com.example.up_piatnitskii.data.Model.UserDAO
import kotlinx.coroutines.launch
import okhttp3.Request

class SignUpViewModel(private val userDao: UserDAO): ViewModel() {
    var email: String = ""
    var password: String = ""

    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit){
        viewModelScope.launch{

            val signUpData = SignUpRequest(email, password)
            val response =  RetrofitInstance.userManagementService.signUp(signUpData)

            if (response.isSuccessful){

                response.body()?.let {
                    Log.v("signUp - if", "Пользователь зарегистрирован: ${it.email}")
                    onSuccess()
                }
            }
            else{
                val errorMessage = when (response.code()) {
                    422 -> "Пользователь уже существует или неверные данные"
                    500 -> "Ошибка сервера"
                    else -> "Ошибка регистрации: ${response.message()}"
                }
                onError(errorMessage)
            }
        }
    }
}