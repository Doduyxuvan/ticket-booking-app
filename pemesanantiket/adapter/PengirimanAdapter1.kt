package com.donisw.pemesanantiket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.PengirimanModel
import java.util.*

class PengirimanAdapter1(
    private val listPengiriman: List<PengirimanModel>,
    private val onItemClick: (PengirimanModel) -> Unit
) : RecyclerView.Adapter<PengirimanAdapter1.ViewHolder>(), Filterable {

    private var filteredList: MutableList<PengirimanModel> = listPengiriman.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pengiriman1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pengiriman = filteredList[position]

        // ✅ pakai nama_pengirim & no_resi sesuai model
        holder.nama.text = pengiriman.nama_pengirim ?: "-"
        holder.resi.text = "Resi: ${pengiriman.no_resi ?: "-"}"

        holder.itemView.setOnClickListener { onItemClick(pengiriman) }
    }

    override fun getItemCount(): Int = filteredList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvNama)
        val resi: TextView = view.findViewById(R.id.tvResi)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint.toString().lowercase(Locale.getDefault())
                val results = FilterResults()
                results.values = if (filterString.isEmpty()) {
                    listPengiriman
                } else {
                    listPengiriman.filter {
                        (it.nama_pengirim ?: "").lowercase(Locale.getDefault()).contains(filterString) ||
                                (it.no_resi ?: "").lowercase(Locale.getDefault()).contains(filterString)
                    }
                }
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<PengirimanModel>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}
