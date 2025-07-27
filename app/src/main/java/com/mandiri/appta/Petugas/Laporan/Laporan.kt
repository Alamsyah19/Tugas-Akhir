package com.mandiri.appta.Petugas.Laporan

data class Laporan(
    val id_laporan: String = "",
    val total_balita: Int = 0,
    val total_pemeriksaan: Int = 0,
    val tgl_laporan: String = "",
    val totalSetahun: Int = 0
)
