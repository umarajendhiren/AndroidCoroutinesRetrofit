package com.devtides.androidcoroutinesretrofit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devtides.androidcoroutinesretrofit.model.CountriesService
import com.devtides.androidcoroutinesretrofit.model.Country
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.coroutineContext

class ListViewModel : ViewModel() {

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    var job: Job? = null
    val countriesService = CountriesService.getCountriesService()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception:${throwable.localizedMessage}")
    }

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        loading.value = true

        /*here we are using coroutine to get response on background thread.
        (for network communication we need to use IO dispatcher).
        once the response available, we need  that result in the main thread to update UI*/
     job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response = countriesService.getCountries()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    countries.value = response.body()
                    countryLoadError.value = null
                    loading.value = false
                } else {
                    onError("Error:${response.message()}")
                }
            }
        }

        /*val dummyData = generateDummyCountries()

        countries.value = dummyData
        countryLoadError.value = ""
        loading.value = false*/
    }

    private fun generateDummyCountries(): List<Country> {
        val countries = arrayListOf<Country>()
        countries.add(Country("dummyCountry1", "dummyCapital1", ""))
        countries.add(Country("dummyCountry2", "dummyCapital2", ""))
        countries.add(Country("dummyCountry3", "dummyCapital3", ""))
        countries.add(Country("dummyCountry4", "dummyCapital4", ""))
        countries.add(Country("dummyCountry5", "dummyCapital5", ""))
        countries.add(Country("dummyCountry1", "dummyCapital1", ""))
        countries.add(Country("dummyCountry2", "dummyCapital2", ""))
        countries.add(Country("dummyCountry3", "dummyCapital3", ""))
        countries.add(Country("dummyCountry4", "dummyCapital4", ""))
        countries.add(Country("dummyCountry5", "dummyCapital5", ""))
        return countries
    }

    private fun onError(message: String) {
        countryLoadError.value = message
        loading.value = false
    }

    /*when this view model is terminated we need to cancel the job */
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}