package com.example.telaslivros

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val context: Context, private val messages: MutableList<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {


    private val viewTypeUser = 1
    private val viewTypeBot = 2

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.tvMessageContent)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) viewTypeUser else viewTypeBot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]


        holder.messageText.text = message.text

        val isUser = message.isUser
        val colorBackgroundResId = if (isUser) R.color.chat_user_background else R.color.chat_bot_background
        val colorTextResId = if (isUser) R.color.chat_user_text else R.color.chat_bot_text

        val originalDrawable = ContextCompat.getDrawable(context, R.drawable.bg_message_bubble)
        val wrappedDrawable = DrawableCompat.wrap(originalDrawable!!.mutate())
        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(context, colorBackgroundResId)
        )
        holder.messageText.background = wrappedDrawable


        holder.messageText.setTextColor(ContextCompat.getColor(context, colorTextResId))


        val textViewParams = holder.messageText.layoutParams as FrameLayout.LayoutParams

        textViewParams.gravity = if (isUser) Gravity.END else Gravity.START

        holder.messageText.layoutParams = textViewParams


    }
}