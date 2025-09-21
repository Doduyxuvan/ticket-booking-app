package com.donisw.pemesanantiket.view.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.PengirimanModel
import android.widget.Filter
import android.widget.Filterable

class PengirimanAdapter2(
    private var pengirimanList: List<PengirimanModel>
) : RecyclerView.Adapter<PengirimanAdapter2.ViewHolder>(), Filterable {

    private var pengirimanListFull: List<PengirimanModel> = ArrayList(pengirimanList)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvNoHp: TextView = itemView.findViewById(R.id.tvNoHp)
        val tvAlamatPengirim: TextView = itemView.findViewById(R.id.tvAlamatPengirim)
        val tvAlamatPenerima: TextView = itemView.findViewById(R.id.tvAlamatPenerima)
        val tvBerat: TextView = itemView.findViewById(R.id.tvBerat)
        val tvJenis: TextView = itemView.findViewById(R.id.tvJenis)
        val tvResi: TextView = itemView.findViewById(R.id.tvResi)
        val tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
        val tvStatusPembayaran: TextView = itemView.findViewById(R.id.tvStatusPembayaran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengiriman2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pengirimanList[position]
        holder.tvNama.text = "Nama: ${item.nama_pengirim ?: "-"}"
        holder.tvNoHp.text = "No HP: ${item.no_hp ?: "-"}"
        holder.tvAlamatPengirim.text = "Alamat Pengirim: ${item.alamat_pengirim ?: "-"}"
        holder.tvAlamatPenerima.text = "Alamat Penerima: ${item.alamat_penerima ?: "-"}"
        holder.tvBerat.text = "Berat: ${item.berat_barang?.toString() ?: "-"} Kg"
        holder.tvJenis.text = "Jenis: ${item.jenis_barang ?: "-"}"
        holder.tvResi.text = "Resi: ${item.no_resi ?: "-"}"
        holder.tvLokasi.text = "Lokasi: ${item.lokasi_barang ?: "-"}"

        // ✅ Status pembayaran dengan label jelas
        val status = item.status_pembayaran ?: "-"
        holder.tvStatusPembayaran.text = "Status Pembayaran: $status"

        // ✅ Warna sesuai status
        val context = holder.itemView.context
        when (status.lowercase()) {
            "pending" -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            )
            "paid" -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.holo_green_dark)
            )
            "expired" -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.holo_red_dark)
            )
            else -> holder.tvStatusPembayaran.setTextColor(
                ContextCompat.getColor(context, android.R.color.darker_gray)
            )
        }
    }

    override fun getItemCount(): Int = pengirimanList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().lowercase()
                val filtered = if (query.isEmpty()) {
                    pengirimanListFull
                } else {
                    pengirimanListFull.filter {
                        it.nama_pengirim?.lowercase()?.contains(query) == true ||
                                it.no_resi?.lowercase()?.contains(query) == true ||
                                it.status_pembayaran?.lowercase()?.contains(query) == true
                    }
                }
                val results = FilterResults()
                results.values = filtered
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pengirimanList = results?.values as List<PengirimanModel>
                notifyDataSetChanged()
            }
        }
    }
}
