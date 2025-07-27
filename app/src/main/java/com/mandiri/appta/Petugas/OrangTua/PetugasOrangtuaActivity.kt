package com.mandiri.appta.Petugas.OrangTua

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mandiri.appta.R
import com.mandiri.appta.databinding.ActivityPetugasOrangtuaBinding

class PetugasOrangtuaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetugasOrangtuaBinding
    private val viewModel: OrangtuaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPetugasOrangtuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerOrangtua.layoutManager = LinearLayoutManager(this)

        viewModel.orangTuaList.observe(this) { list ->
            val adapter = OrangtuaAdapter(list) { orangTua ->
                val detail = DetailOrangtuaFragment(orangTua)
                detail.show(supportFragmentManager, detail.tag)
            }
            binding.recyclerOrangtua.adapter = adapter
        }

        viewModel.fetchData()

        binding.fabAdd.setOnClickListener {
            val bottomSheetFragment = TambahOrangtuaFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}