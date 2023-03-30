package com.dicoding.fundamentalsatu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.fundamentalsatu.data.remote.ApiClient
import com.dicoding.fundamentalsatu.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val resultUser = MutableLiveData<Result>()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getUserGithub()

                emit(response)

            }.onStart {
                resultUser.value = Result.Loading(true)
            }.onCompletion {
                resultUser.value = Result.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultUser.value = Result.Error(it)
            }.collect { it ->
                resultUser.value = Result.Success(it)
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .searchUserGithub(
                        mapOf(
                            "q" to username,
                            "per_page" to 10
                        )
                    )

                emit(response)

            }.onStart {
                resultUser.value = Result.Loading(true)
            }.onCompletion {
                resultUser.value = Result.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultUser.value = Result.Error(it)
            }.collect { it ->
                resultUser.value = Result.Success(it.items)
            }
        }
    }
}