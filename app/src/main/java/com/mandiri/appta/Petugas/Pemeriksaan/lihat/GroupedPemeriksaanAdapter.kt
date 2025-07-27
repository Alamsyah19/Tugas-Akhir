package com.mandiri.appta.Petugas.Pemeriksaan.lihat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandiri.appta.databinding.ItemPemeriksaanGroupBinding

class GroupedPemeriksaanAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val groups = mutableListOf<PemeriksaanGroup>()

    fun submitList(newList: List<PemeriksaanGroup>) {
        groups.clear()
        groups.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (groups[position].isExpanded) 1 else 0
    }

    override fun getItemCount(): Int = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPemeriksaanGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GroupViewHolder).bind(groups[position])
    }

    inner class GroupViewHolder(private val binding: ItemPemeriksaanGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: PemeriksaanGroup) {
            binding.tvNamaBalita.text = group.balita
            binding.tvTotal.text = "${group.listPemeriksaan.size} riwayat"

            val detailText = group.listPemeriksaan.joinToString("\n\n") {
                """
                ➤ ${it.pemeriksaan.tanggal_pemeriksaan}
                Nama Balita: ${it.balita?.nama}
                tinggi Badan: ${it.pemeriksaan?.tinggi_badan}
                berat badan: ${it.pemeriksaan?.berat_badan}
                lingkar kepala: ${it.pemeriksaan?.lingkar_kepala}
                lingkar lengan: ${it.pemeriksaan?.lingkar_lengan}
                keterangan: ${it.pemeriksaan?.keteranganTimbangan}
                Vitamin A: ${it.pelayanan?.vitamin_a.let { if (it == true) "Ya" else "Tidak" }}
                Oralit: ${it.pelayanan?.oralit.let { if (it == true) "Ya" else "Tidak" }}
                Polio: ${it.imunisasi?.polio.let { if (it == true) "Ya" else "Tidak" }}
                DPT-HB: ${it.imunisasi?.dpt_hb.let { if (it == true) "Ya" else "Tidak" }}
                Campak: ${it.imunisasi?.campak.let { if (it == true) "Ya" else "Tidak" }}
                ASI Eksklusif 1: ${it.asi?.e1.let { if (it == true) "Ya" else "Tidak" }}
                ASI Eksklusif 2: ${it.asi?.e2.let { if (it == true) "Ya" else "Tidak" }}
                ASI Eksklusif 3: ${it.asi?.e3.let { if (it == true) "Ya" else "Tidak" }}
                ASI Eksklusif 4: ${it.asi?.e4.let { if (it == true) "Ya" else "Tidak" }}
                ASI Eksklusif 5: ${it.asi?.e5.let { if (it == true) "Ya" else "Tidak" }}
                ASI Eksklusif 6: ${it.asi?.e6.let { if (it == true) "Ya" else "Tidak" }}
                """.trimIndent()

            }

            binding.tvDetails.text = if (group.isExpanded) detailText else ""
            binding.root.setOnClickListener {
                group.isExpanded = !group.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }
}