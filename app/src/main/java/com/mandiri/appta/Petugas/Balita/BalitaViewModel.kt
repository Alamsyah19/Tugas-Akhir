package com.mandiri.appta.Petugas.Balita

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class BalitaViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _balitaList = MutableLiveData<List<Balita>>()
    val balitaList: LiveData<List<Balita>> get() = _balitaList

    private var listenerRegistration: ListenerRegistration? = null

    fun startListening() {
        listenerRegistration = db.collection("balita")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val list = mutableListOf<Balita>()
                value?.forEach { doc ->
                    val balita = doc.toObject(Balita::class.java)
                    balita.id = doc.id
                    list.add(balita)
                }
                _balitaList.value = list
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}