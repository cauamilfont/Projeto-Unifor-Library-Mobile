package com.example.telaslivros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ModerationAdapter(private val commentList: MutableList<Comment>) :
    RecyclerView.Adapter<ModerationAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookTitle: TextView = view.findViewById(R.id.textViewBookTitle)
        val commentContent: TextView = view.findViewById(R.id.textViewCommentContent)
        val authorName: TextView = view.findViewById(R.id.textViewCommentAuthor)
        val ratingContent: RatingBar = view.findViewById(R.id.ratingBarContent)
        val ratingPhysical: RatingBar = view.findViewById(R.id.ratingBarPhysical)
        val approveButton: Button = view.findViewById(R.id.buttonApprove)
        val rejectButton: Button = view.findViewById(R.id.buttonReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comentario_moderacao, parent, false)
        return CommentViewHolder(view)
    }


    override fun getItemCount() = commentList.size


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comments = commentList[position]


        holder.bookTitle.text = "Livro: ${comments.bookTitle}"
        holder.commentContent.text = comments.commentContent
        holder.authorName.text = "Por: ${comments.authorName}"
        holder.ratingContent.rating = comments.ratingContent
        holder.ratingPhysical.rating = comments.ratingPhysical


        val removeClickListener = View.OnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                commentList.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                notifyItemRangeChanged(currentPosition, commentList.size)
            }
        }

        holder.approveButton.setOnClickListener(removeClickListener)
        holder.rejectButton.setOnClickListener(removeClickListener)
    }
}