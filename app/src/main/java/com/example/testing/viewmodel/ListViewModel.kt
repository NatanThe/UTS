package com.example.testing.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.testing.model.HobbyList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListViewModel(application: Application):AndroidViewModel(application) {
    val hobbyLD = MutableLiveData<ArrayList<HobbyList>>()
    val loadingLD = MutableLiveData<Boolean>()
    val hobbyLoadErrorLD = MutableLiveData<Boolean>()

    private val TAG = "volleyTag"
    private var queue:RequestQueue ?= null
    fun refresh(){
        loadingLD.value = true
        hobbyLoadErrorLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/cars/cars.json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                val sType = object : TypeToken<List<HobbyList>>() { }.type
                val result = Gson().fromJson<List<HobbyList>>(it, sType)
                hobbyLD.value = result as ArrayList<HobbyList>?

                loadingLD.value = false
                Log.d("showvoley", result.toString())
            },
            {
                Log.d("showvoley", it.toString())
                hobbyLoadErrorLD.value = false
                loadingLD.value = false
            })
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }

}