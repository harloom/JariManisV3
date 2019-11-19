package com.app.jarimanis.ui.thread.detail.comentar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.diskusi.paging.Doc

import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import com.snov.timeagolibrary.PrettyTimeAgo
import kotlinx.android.synthetic.main.item_comentar.view.*
import java.lang.Exception


class ComentarAdapter constructor(private val interaction: ComentarAdapter.Interaction? = null) : PagedListAdapter<Doc, RecyclerView.ViewHolder>(
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
        return CommentarHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comentar,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder){
            is CommentarHolder ->{
                item?.let {
                    holder.bind(it)
                }
            }
        }

    }


    class CommentarHolder(
        view: View,
        private val interaction: ComentarAdapter.Interaction?
    ) : RecyclerView.ViewHolder(view) {


        fun bind(item: Doc) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }

            itemView.iv_more.setOnClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
            }

            itemView.btnComentar.setOnLikeListener(object  : OnLikeListener{
                override fun liked(likeButton: LikeButton?) {

                }

                override fun unLiked(likeButton: LikeButton?) {

                }

            })

            itemView.btnLike.setOnLikeListener(object  : OnLikeListener{
                override fun liked(likeButton: LikeButton?) {
                    interaction?.onBtnLikeClick(adapterPosition,item)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    interaction?.onBtnLikeClick(adapterPosition,item)
                }

            })



            itemView.text_commentar_name.text = item.user?.nameUser?.capitalize()
            itemView.text_commentar_body.text = item.content?.capitalize()
//
            try {
                itemView.btnLike.isLiked = item.isLikes!!
                itemView.text_commentar_time.text = PrettyTimeAgo.getTimeAgo(item.updateAt!!)
                Glide.with(itemView.context).load(item.user?.thumbail)
                    .into(image_commentar_profile)
            }catch (e : Exception){

            }
//
//            itemView.cv_thumbail.setOnClickListener {
//                interaction?.onProfileSelected(adapterPosition, item)
//            }

        }
    }
    interface Interaction {
        fun onItemSelected(position: Int, item: Doc)
        fun onItemLongSelected(position: Int , item: Doc)
        fun onBtnLikeClick(position: Int,item: Doc)

    }
}