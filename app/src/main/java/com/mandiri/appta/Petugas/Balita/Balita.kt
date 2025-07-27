package com.mandiri.appta.Petugas.Balita

data class Balita(
    val nik: String = "",
    val nama: String = "",
    val tgl_lahir: String = "",
    val berat_badan_lahir: Double?=0.0,
    val tinggi_badan: Double?=0.0,
    val jenis_kelamin: String = "",
    val id_orang_tua:String?="",
    val kelompok_dasawisma: String = "",
    var id: String? = null
)