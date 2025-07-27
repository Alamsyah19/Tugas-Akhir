package com.mandiri.appta.Petugas.Pemeriksaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mandiri.appta.Petugas.Balita.Balita
import com.mandiri.appta.Petugas.Pemeriksaan.Asi.PemberianAsi
import com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi.Imunisasi
import com.mandiri.appta.Petugas.Pemeriksaan.Pelayanan.Pelayanan

class PemeriksaanViewModel : ViewModel() {

    //Balita
    private val _selectedBalita = MutableLiveData<Balita>()
    val selectedBalita: LiveData<Balita> get() = _selectedBalita

    fun setSelectedBalita(balita: Balita) {
        _selectedBalita.value = balita
    }

    // Data Perkembangan Balita
    val beratBadan = MutableLiveData<Double>()
    val tinggiBadan = MutableLiveData<Double>()
    val lingkarKepala = MutableLiveData<Double>()
    val lingkarLengan = MutableLiveData<Double>()
    val keteranganTimbangan = MutableLiveData<String>()

    //Imunisasi
    private val _imunisasiData = MutableLiveData<Imunisasi>()
    val imunisasiData: LiveData<Imunisasi> get() = _imunisasiData

    fun setImunisasi(imunisasi: Imunisasi) {
        _imunisasiData.value = imunisasi
    }

    //Pelayanan
    private val _pelayananData = MutableLiveData<Pelayanan>()
    val pelayananData: LiveData<Pelayanan> get() = _pelayananData

    fun setPelayanan(pelayanan: Pelayanan) {
        _pelayananData.value = pelayanan
    }

    //ASI
    private val _asiData = MutableLiveData<PemberianAsi>()
    val asiData: LiveData<PemberianAsi> get() = _asiData

    fun setAsi(asi: PemberianAsi) {
        _asiData.value = asi
    }

}