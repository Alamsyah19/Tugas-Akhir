package com.mandiri.appta.Petugas.Balita

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mandiri.appta.R

class BalitaAdapter(
    private var list: MutableList<Balita> = mutableListOf(),
    private val onItemClick: (Balita) -> Unit
) : RecyclerView.Adapter<BalitaAdapter.BalitaViewHolder>() {

    inner class BalitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvNik: TextView = itemView.findViewById(R.id.tv_nik)
        val tvTglLahir: TextView = itemView.findViewById(R.id.tv_tgl_lahir)
        val tvBerat: TextView = itemView.findViewById(R.id.tv_berat)
        val tvTinggi: TextView = itemView.findViewById(R.id.tv_tinggi)
        val tvJenisKelamin: TextView = itemView.findViewById(R.id.tv_jenis_kelamin)
        val tvAyah: TextView = itemView.findViewById(R.id.tv_ayah)
        val tvIbu: TextView = itemView.findViewById(R.id.tv_ibu)
        val tvKelompok: TextView = itemView.findViewById(R.id.tv_kelompok)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_balita, parent, false)
        return BalitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BalitaViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = "Nama: ${item.nama}"
        holder.tvNik.text = "NIK: ${item.nik}"
        holder.tvTglLahir.text = "Tanggal Lahir: ${item.tgl_lahir}"
        holder.tvBerat.text = "Berat Badan Lahir: ${item.berat_badan_lahir}"
        holder.tvTinggi.text = "Tinggi Badan: ${item.tinggi_badan}"
        holder.tvJenisKelamin.text = "Jenis Kelamin: ${item.jenis_kelamin}"
        holder.tvKelompok.text = "Kelompok Dasawisma: ${item.kelompok_dasawisma.ifBlank { "-" }}"

        //Data Orangtua
        val idOrtu = item.id_orang_tua
        if (!idOrtu.isNullOrBlank()) {
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("orangtua")
                .document(idOrtu)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val namaAyah = document.getString("nama_ayah") ?: "-"
                        val namaIbu = document.getString("nama_ibu") ?: "-"
                        holder.tvAyah.text = "Nama Ayah: $namaAyah"
                        holder.tvIbu.text = "Nama Ibu: $namaIbu"
                    } else {
                        holder.tvAyah.text = "Nama Ayah: -"
                        holder.tvIbu.text = "Nama Ibu: -"
                    }
                }
                .addOnFailureListener {
                    holder.tvAyah.text = "Nama Ayah: -"
                    holder.tvIbu.text = "Nama Ibu: -"
                }
        } else {
            holder.tvAyah.text = "Nama Ayah: -"
            holder.tvIbu.text = "Nama Ibu: -"
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = list.size


    fun updateData(newList: List<Balita>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}