package com.mandiri.appta.Petugas.OrangTua

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class OrangtuaViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _orangTuaList = MutableLiveData<List<OrangTua>>()
    val orangTuaList: LiveData<List<OrangTua>> = _orangTuaList

    private var listener: ListenerRegistration? = null

    fun fetchData() {
        listener?.remove()
        listener = db.collection("orangtua").addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val list = snapshot?.documents?.mapNotNull {
                it.toObject(OrangTua::class.java)
            } ?: emptyList()
            _orangTuaList.postValue(list)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}