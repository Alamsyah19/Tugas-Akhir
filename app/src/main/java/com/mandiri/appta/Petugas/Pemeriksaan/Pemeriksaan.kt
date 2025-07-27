package com.mandiri.appta.Petugas.Pemeriksaan

data class Pemeriksaan(
    val id_pemeriksaan: String = "",
    val nik_balita: String = "",
    val nama_balita: String = "",
    val berat_badan: Double?=0.0,
    val tinggi_badan: Double?= 0.0,
    val lingkar_kepala: Double?=0.0,
    val lingkar_lengan: Double?=0.0,
    val keteranganTimbangan: String = "",
    val umur_balita: Int = 0,
    val tanggal_pemeriksaan: String = "",
    val id_pelayanan: String = "",
    val id_imunisasi: String = "",
    val id_pemberian_asi: String = ""

)