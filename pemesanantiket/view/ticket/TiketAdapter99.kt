package com.donisw.pemesanantiket.view.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.TiketModel

class TiketAdapter99(private var tiketList: MutableList<TiketModel>) :
    RecyclerView.Adapter<TiketAdapter99.ViewHolder>(), Filterable {

    // Simpan full data untuk search
    var tiketListFull: List<TiketModel> = ArrayList(tiketList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tiket, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tiket = tiketList[position]
        holder.bind(tiket)
    }

    override fun getItemCount(): Int = tiketList.size

    // 🔍 Implementasi Filterable
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    tiketListFull
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    tiketListFull.filter {
                        it.nama.lowercase().contains(filterPattern) ||
                                it.asal.lowercase().contains(filterPattern) ||
                                it.tujuan.lowercase().contains(filterPattern) ||
                                it.status.lowercase().contains(filterPattern) ||
                                it.statusPembayaran.lowercase().contains(filterPattern)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tiketList = (results?.values as? List<TiketModel>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        private val tvAsalTujuan: TextView = itemView.findViewById(R.id.tvAsalTujuan)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        private val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvStatusPembayaran: TextView = itemView.findViewById(R.id.tvStatusPembayaran)

        fun bind(tiket: TiketModel) {
            tvNama.text = tiket.nama
            tvAsalTujuan.text = "${tiket.asal} → ${tiket.tujuan}"
            tvTanggal.text = tiket.tanggal
            tvJumlah.text = "Harga: Rp ${tiket.hargaTotal}"
            tvStatus.text = "Status: ${tiket.status}"
            tvStatusPembayaran.text = "Pembayaran: ${tiket.statusPembayaran}"

            // 🎨 Warna sesuai status pembayaran
            val colorRes = when (tiket.statusPembayaran.lowercase()) {
                "pending" -> android.R.color.holo_orange_dark
                "paid", "success", "lunas" -> android.R.color.holo_green_dark
                "expired", "failed" -> android.R.color.holo_red_dark
                else -> android.R.color.darker_gray
            }

            val color = ContextCompat.getColor(itemView.context, colorRes)
            tvStatus.setTextColor(color)
            tvStatusPembayaran.setTextColor(color)
        }
    }
}
