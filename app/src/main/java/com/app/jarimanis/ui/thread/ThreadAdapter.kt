package com.app.jarimanis.ui.thread

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.datasource.models.thread.Like
import com.app.jarimanis.data.datasource.models.thread.UserT
import com.app.jarimanis.utils.debounce.onClickDebounced
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import com.snov.timeagolibrary.PrettyTimeAgo
import kotlinx.android.synthetic.main.item_thread.view.*
import java.lang.Exception


class ThreadAdapter constructor(private val interaction: ThreadAdapter.Interaction? = null) : PagedListAdapter<Doc, RecyclerView.ViewHolder>(
    object  :  DiffUtil.ItemCallback<Doc>(){
        override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return  oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThreadHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_thread,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder){
            is ThreadHolder ->{
                item?.let {
                    holder.bind(it)
                }
            }
        }

    }

//    fun dataChange(item: Doc,position: Int){
//        val oldItem = getItem(position)
//        oldItem?.likes?.add(Like(System.currentTimeMillis(), UserT(TokenUser.idUser),System.currentTimeMillis()))
//        this.notifyItemChanged(position)
//    }

    class ThreadHolder(
        view: View,
        private val interaction: ThreadAdapter.Interaction?
    ) : RecyclerView.ViewHolder(view) {


        fun bind(item: Doc) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }

            itemView.iv_more.setOnClickListener {
                interaction?.onItemLongSelected(adapterPosition,item)
            }

            itemView.imageView.onClickDebounced {
                interaction?.onBottomSheetUp(adapterPosition,item)
            }

            itemView.imageView3.setOnLikeListener(object : OnLikeListener{
                override fun liked(likeButton: LikeButton?) {
//                    itemView.imageView3?.isLiked = true
                    interaction?.onLike(adapterPosition,item)
                }

                override fun unLiked(likeButton: LikeButton?) {
//                    itemView.imageView3?.isLiked = false
                    interaction?.onUnlike(adapterPosition,item)
                }

            })

            itemView.tv_judul.text = item.title?.capitalize()
            itemView.tv_user.text = item.user?.nameUser?.capitalize()


            try {
                itemView.tv_time.text = PrettyTimeAgo.getTimeAgo(item.updateAt!!)
                itemView.imageView3.setLiked(item.isLikes)
                Glide.with(itemView.context).load(item.user?.thumbail)
                    .into(itemView.cv_thumbail)
            }catch (e : Exception){

            }

            itemView.cv_thumbail.setOnClickListener {
                interaction?.onProfileSelected(adapterPosition, item)
            }

        }
    }
    interface Interaction {
        fun onBottomSheetUp(position: Int,item: Doc)
        fun onItemSelected(position: Int, item: Doc)
        fun onProfileSelected(position: Int , item: Doc)
        fun onItemLongSelected(position: Int,item: Doc)
        fun onLike(position: Int,item: Doc)
        fun onUnlike(position: Int,item: Doc)
    }
}