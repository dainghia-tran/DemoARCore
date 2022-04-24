package com.dainghia.demoarcore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ModelListAdapter(private val list: List<String>, private val listener: OnClickItemListener) :
    RecyclerView.Adapter<ModelListAdapter.ViewHolder>() {

    interface OnClickItemListener {
        fun onClickItem(fileName: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.model_item_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = list[position].split(".")[0]

        holder.itemView.setOnClickListener {
            listener.onClickItem(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}