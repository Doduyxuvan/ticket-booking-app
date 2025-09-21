package com.donisw.pemesanantiket.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.PelangganModel
import com.donisw.pemesanantiket.view.user.EditProfilActivity
import android.widget.Filter
import android.widget.Filterable

class PelangganAdapter(
    private val context: Context,
    private var pelangganList: List<PelangganModel>
) : RecyclerView.Adapter<PelangganAdapter.ViewHolder>(), Filterable {

    private var pelangganListFull: List<PelangganModel> = ArrayList(pelangganList)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaPelanggan)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pelangganList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = pelangganList[position]
        holder.tvNama.text = pelanggan.nama_lengkap

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, EditProfilActivity::class.java)
            intent.putExtra("id", pelanggan.id)
            context.startActivity(intent)
        }
    }

    // 🔍 Implementasi filter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    pelangganListFull
                } else {
                    pelangganListFull.filter {
                        it.nama_lengkap.lowercase().contains(query)
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pelangganList = results?.values as List<PelangganModel>
                notifyDataSetChanged()
            }
        }
    }

    // Fungsi update kalau load data baru dari server
    fun updateData(newList: List<PelangganModel>) {
        pelangganList = newList
        pelangganListFull = ArrayList(newList)
        notifyDataSetChanged()
    }
}
