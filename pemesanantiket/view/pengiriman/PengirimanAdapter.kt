package com.donisw.pemesanantiket.view.pengiriman

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.PengirimanModel

class PengirimanAdapter(private val list: List<PengirimanModel>) :
    RecyclerView.Adapter<PengirimanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaPengirim: TextView = itemView.findViewById(R.id.tvNamaPengirim)
        val tvJenisBarang: TextView = itemView.findViewById(R.id.tvJenisBarang)
        val tvAlamatPenerima: TextView = itemView.findViewById(R.id.tvAlamatPenerima)
        val tvNoResi: TextView = itemView.findViewById(R.id.tvNoResi)
        val tvLokasiBarang: TextView = itemView.findViewById(R.id.tvLokasiBarang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengiriman, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.tvNamaPengirim.text = data.nama_pengirim
        holder.tvJenisBarang.text = "Barang: ${data.jenis_barang}"
        holder.tvAlamatPenerima.text = "Ke: ${data.alamat_penerima}"
        holder.tvNoResi.text = "Resi: ${data.no_resi}"
        holder.tvLokasiBarang.text = "Lokasi Barang: ${data.lokasi_barang}"

        // Klik item untuk buka detail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPengirimanActivity::class.java)
            intent.putExtra("id_pengiriman", data.id ?: 0)
            context.startActivity(intent)
        }
    }
}
