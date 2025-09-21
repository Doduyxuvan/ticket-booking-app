package com.donisw.pemesanantiket.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.Tiket

class TiketAdapter(private val list: List<Tiket>) :
    RecyclerView.Adapter<TiketAdapter.TiketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tiket, parent, false)
        return TiketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TiketViewHolder, position: Int) {
        val tiket = list[position]
        holder.tvTujuan.text = "Tujuan: ${tiket.tujuan}"
        holder.tvTanggal.text = "Tanggal: ${tiket.tanggal}"
        holder.tvJumlah.text = "Jumlah Tiket: ${tiket.jumlah}"
        holder.tvStatus.text = "Status: ${tiket.status}"
    }

    override fun getItemCount(): Int = list.size

    class TiketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTujuan: TextView = itemView.findViewById(R.id.tvTujuan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }
}
