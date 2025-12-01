package com.example.telaslivros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ModerationAdapter(private var commentList: MutableList<Comment>, private val onModerationAction: (String, Boolean) -> Unit) :
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

        val comment = commentList[position]


        holder.bookTitle.text = "Livro: ${comment.bookTitle}"
        holder.commentContent.text = comment.commentContent
        holder.authorName.text = "Por: ${comment.authorName}"
        holder.ratingContent.rating = comment.ratingContent.toFloat()
        holder.ratingPhysical.rating = comment.ratingPhysical.toFloat()


        holder.approveButton.setOnClickListener {
            onModerationAction(comment.id.toString(), true)
            removeItem(position)
        }

        holder.rejectButton.setOnClickListener {
            onModerationAction(comment.id.toString(), false)
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        if (position in commentList.indices) {
            commentList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, commentList.size)
        }
    }

    fun updateList(newList: MutableList<Comment>) {
        commentList = newList
        notifyDataSetChanged()
    }
}