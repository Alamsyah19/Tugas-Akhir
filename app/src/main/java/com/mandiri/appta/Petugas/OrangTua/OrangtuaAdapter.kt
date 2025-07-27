package com.mandiri.appta.Petugas.OrangTua

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandiri.appta.databinding.ItemOrangtuaBinding

class OrangtuaAdapter (
    private val list: List<OrangTua>,
    private val onItemClick: (OrangTua) -> Unit
) : RecyclerView.Adapter<OrangtuaAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemOrangtuaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrangTua) {
            binding.txtNamaAyah.text = "Ayah: ${data.nama_ayah}"
            binding.txtNamaIbu.text = "Ibu: ${data.nama_ibu}"
            binding.txtNoHp.text = "No HP: ${data.no_hp}"
            binding.txtAlamat.text = "Alamat: ${data.alamat}"
            binding.txtKb.text = "Ibu KB: ${data.ibu_kb.ifBlank { "-" }}"
            binding.root.setOnClickListener { onItemClick(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrangtuaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}