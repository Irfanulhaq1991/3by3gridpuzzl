package com.irfan.gridpuzzl

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GridAdaptor(private val itemLayoutManger: ItemLayoutManger) :
    RecyclerView.Adapter<GridViewHolder>() {

    private var data = Array<Int>(9){-1}

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: Array<Int>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return itemLayoutManger.getLayoutId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        itemLayoutManger.bindView(holder, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun set(position: Int, item: Int) {
        if(position > data.size-1 || position < 0) return
        data[position] = item
    }

    fun get(position: Int): Int? {
        if (position > data.size - 1 || position < 0) return null
        return data[position]
    }
}

class GridViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(position: Int) {
        // this is not required as the functionality is delegated to ItemLayoutManger
    }
}


interface ItemLayoutManger {
    fun getLayoutId(position: Int): Int
    fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
    }
}