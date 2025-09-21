package com.donisw.pemesanantiket.view.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.LaporanModel
import java.text.NumberFormat
import java.util.*

class LaporanAdapter(private val laporanList: MutableList<LaporanModel>) :
    RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder>() {

    class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvJenis: TextView = itemView.findViewById(R.id.tvJenis)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = laporanList[position]

        // Format angka ke Rupiah
        val formattedTotal = NumberFormat.getNumberInstance(Locale("in", "ID"))
            .format(laporan.total.toIntOrNull() ?: 0)

        holder.tvNama.text = laporan.nama.ifEmpty { "-" }
        holder.tvTanggal.text = laporan.tanggal
        holder.tvJenis.text = laporan.jenis.ifEmpty { "-" }
        holder.tvTotal.text = "Rp $formattedTotal"
    }

    override fun getItemCount(): Int = laporanList.size

    // 🔄 Fungsi update data (untuk search/filter)
    fun updateData(newList: List<LaporanModel>) {
        laporanList.clear()
        laporanList.addAll(newList)
        notifyDataSetChanged()
    }
}
