package com.donisw.pemesanantiket.view.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.Tiket10

class TiketAdapter5(
    private var tiketList: List<Tiket10>,
    private val context: Context
) : RecyclerView.Adapter<TiketAdapter5.ViewHolder>(), Filterable {

    private var tiketListFiltered: List<Tiket10> = tiketList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvAsalTujuan: TextView = itemView.findViewById(R.id.tvAsalTujuan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvStatusPembayaran: TextView = itemView.findViewById(R.id.tvStatusPembayaran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tiket, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tiketListFiltered.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tiket = tiketListFiltered[position]

        holder.tvNama.text = tiket.nama
        holder.tvAsalTujuan.text = "${tiket.asal} → ${tiket.tujuan}"
        holder.tvTanggal.text = tiket.tanggal
        holder.tvJumlah.text = "Harga: Rp ${tiket.hargaTotal}"

        // 🔧 Ganti metode jadi status, karena dari API field metode kosong
        holder.tvStatus.text = "Metode: ${tiket.status}"

        holder.tvStatusPembayaran.text = "Pembayaran: ${tiket.statusPembayaran}"

        // 🎨 Warna sesuai status pembayaran
        when (tiket.statusPembayaran.lowercase()) {
            "pending" -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            )
            "paid" -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.holo_green_dark)
            )
            "expired" -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.holo_red_dark)
            )
        }

        // Klik item → buka EditTicketActivity2
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditTicketActivity2::class.java)
            intent.putExtra("id", tiket.id.toInt())
            context.startActivity(intent)
        }
    }

    // 🔍 Implementasi Search/Filter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence?.toString()?.lowercase() ?: ""
                tiketListFiltered = if (query.isEmpty()) {
                    tiketList
                } else {
                    tiketList.filter {
                        it.nama.lowercase().contains(query) ||
                                it.asal.lowercase().contains(query) ||
                                it.tujuan.lowercase().contains(query) ||
                                it.statusPembayaran.lowercase().contains(query)
                    }
                }
                val results = FilterResults()
                results.values = tiketListFiltered
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tiketListFiltered = results?.values as List<Tiket10>
                notifyDataSetChanged()
            }
        }
    }
}
