package com.donisw.pemesanantiket.view.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.TiketModel

class TiketAdapter1(private val tiketList: List<TiketModel>) :
    RecyclerView.Adapter<TiketAdapter1.TiketViewHolder>() {

    class TiketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvNama)
        val asal: TextView = view.findViewById(R.id.tvAsal)
        val tujuan: TextView = view.findViewById(R.id.tvTujuan)
        val harga: TextView = view.findViewById(R.id.tvHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tiket1, parent, false)
        return TiketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TiketViewHolder, position: Int) {
        val tiket = tiketList[position]
        holder.nama.text = tiket.nama
        holder.asal.text = "Dari: ${tiket.asal}"
        holder.tujuan.text = "Tujuan: ${tiket.tujuan}"
        holder.harga.text = "Rp ${tiket.hargaTotal}"  // ✅ sesuai field baru
    }

    override fun getItemCount(): Int = tiketList.size
}
