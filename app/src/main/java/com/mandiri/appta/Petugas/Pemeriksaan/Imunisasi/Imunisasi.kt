package com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi

data class Imunisasi(
    val id_imunisasi: String = "",
    val polio: Boolean = false,
    val dpt_hb: Boolean = false,
    val campak: Boolean = false
)