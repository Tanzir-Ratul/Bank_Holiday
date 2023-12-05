package com.example.bankholiday.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankholiday.api.models.HolidayData
import com.example.bankholiday.api.models.Login
import com.example.bankholiday.api.models.Registration
import com.example.bankholiday.ui.repository.HolidayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(private val repository: HolidayRepository) :
    ViewModel() {


    private var _loginResponse = MutableLiveData<Login?>()
    val loginResponse: LiveData<Login?> = _loginResponse

    private var _registrationResponse = MutableLiveData<Registration?>()
    val registrationResponse: LiveData<Registration?> = _registrationResponse

    private var _getHolidayData = MutableLiveData<HolidayData?>()
    val getHolidayData: LiveData<HolidayData?> = _getHolidayData

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var messageCode = MutableLiveData<Int?>()

    init {
        getHolidaysData("2021")
    }

    fun registerUser(name: String?, email: String?, password: String?) {
        try {
            _isLoading.value = true
            viewModelScope.launch {
                val response = repository.registerUser(name, email, password)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _registrationResponse.value = response.body()
                    }
                } else messageCode.value = response.code()
                Log.d("responseCode", "${response.code()}")
                _isLoading.value = false
            }
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    fun loginUser(userName: String?, password: String?) {
        try {
            _isLoading.value = true
            viewModelScope.launch {
                val response = repository.loginUser(userName, password)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _loginResponse.value = response.body()
                    }
                } else {
                    messageCode.value = response.code()
                }



                _isLoading.value = false
            }
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }


    fun getHolidaysData(year: String?) {
        try {
            _isLoading.value = true
            viewModelScope.launch {
                val response = repository.getHolidaysData(year)
                if (response.isSuccessful) {
                    if (response.body() != null && response.body()?.meta?.code == 200) {
                        _getHolidayData.value = response.body()
                        Log.d("_getHolidayData", "${response.body()}")
                    } else {
                        _getHolidayData.value = null
                    }
                } else {
                    _getHolidayData.value = null
                }
                _isLoading.value = false
            }
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    fun resetValues() {
        _registrationResponse.value = null
        _loginResponse.value = null
        messageCode.value = null
        //_getHolidayData.value = null
    }
}