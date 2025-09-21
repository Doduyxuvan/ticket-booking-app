package com.donisw.pemesanantiket.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.databinding.ListItemHistoryBinding
import com.donisw.pemesanantiket.model.ModelDatabase
import com.donisw.pemesanantiket.utils.FunctionHelper.rupiahFormat

class HistoryAdapter(private val modelDatabase: MutableList<ModelDatabase>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    fun setDataAdapter(items: List<ModelDatabase>) {
        modelDatabase.clear()
        modelDatabase.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelDatabase[position]
        val binding = holder.binding

        binding.tvKode1.text = when (data.keberangkatan) {
            "Jakarta" -> "JKT"
            "Semarang" -> "SRG"
            "Surabaya" -> "SBY"
            "Bali" -> "DPS"
            else -> "-"
        }

        binding.tvKode2.text = when (data.tujuan) {
            "Jakarta" -> "JKT"
            "Semarang" -> "SRG"
            "Surabaya" -> "SBY"
            "Bali" -> "DPS"
            else -> "-"
        }

        binding.tvKelas.text = data.kelas
        binding.tvDate.text = data.tanggal
        binding.tvNama.text = data.namaPenumpang
        binding.tvKeberangkatan.text = data.keberangkatan
        binding.tvTujuan.text = data.tujuan
        binding.tvHargaTiket.text = rupiahFormat(data.hargaTiket)

        // ❌ Hapus bagian Glide.load(gambar), karena field gambar sudah dihapus
        // Kalau imageProfile di layout masih ada, bisa kamu isi dengan placeholder:
        // binding.imageProfile.setImageResource(R.drawable.ic_launcher_foreground)
    }

    override fun getItemCount(): Int = modelDatabase.size

    inner class ViewHolder(val binding: ListItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    fun setSwipeRemove(position: Int): ModelDatabase {
        return modelDatabase.removeAt(position)
    }
}
