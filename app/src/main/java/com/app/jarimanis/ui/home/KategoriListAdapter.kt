package com.app.jarimanis.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.bumptech.glide.Glide


class KategoriListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultKategori>() {

        override fun areItemsTheSame(oldItem: ResultKategori, newItem: ResultKategori): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultKategori, newItem: ResultKategori): Boolean {
            return  oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return KategoriHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_kategori,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KategoriHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ResultKategori?>) {
        differ.submitList(list)
    }

    class KategoriHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ResultKategori) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
                tv_kategori.text  = item.category
            try {

                Glide.with(itemView.context).load(item.icon).into(itemView.civ_category)
            }catch (e : Exception){

            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ResultKategori)
    }
}
