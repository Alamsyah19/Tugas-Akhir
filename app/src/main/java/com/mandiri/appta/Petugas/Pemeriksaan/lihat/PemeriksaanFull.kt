package com.mandiri.appta.Petugas.Pemeriksaan.lihat

import com.mandiri.appta.Petugas.Balita.Balita
import com.mandiri.appta.Petugas.Pemeriksaan.Asi.PemberianAsi
import com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi.Imunisasi
import com.mandiri.appta.Petugas.Pemeriksaan.Pelayanan.Pelayanan
import com.mandiri.appta.Petugas.Pemeriksaan.Pemeriksaan

data class PemeriksaanFull(
    val pemeriksaan: Pemeriksaan,
    val balita: Balita?,
    val pelayanan: Pelayanan?,
    val imunisasi: Imunisasi?,
    val asi: PemberianAsi?
)
data class PemeriksaanGroup(
    val balita: String,
    val listPemeriksaan: List<PemeriksaanFull>,
    var isExpanded: Boolean = false
)