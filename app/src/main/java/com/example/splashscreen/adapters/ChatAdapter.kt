package com.example.splashscreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splashscreen.R
import com.example.splashscreen.models.MessageModels
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val mList: ArrayList<MessageModels>, private val context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    view type for sender and receiver ....
    val SENDER_VIEW_TYPE = 1
    val RECEIVER_VIEW_TYPE = 0

//    viewHolder for receiver .....
    class ViewHolderReceiver(view: View) : RecyclerView.ViewHolder(view) {
        val receiverMessageTextView: TextView = view.findViewById(R.id.receiverMessageTextView)
        val receiverTimeStampTextView: TextView = view.findViewById(R.id.receiverTimeTextView)
    }

//    viewHolder for sender .....
    class ViewHolderSender(view: View) : RecyclerView.ViewHolder(view) {
        val senderMessageTextView: TextView = view.findViewById(R.id.senderMessageTextView)
        val senderTimeStampTextView: TextView = view.findViewById(R.id.senderTimeTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return if(mList[position].userId == FirebaseAuth.getInstance().uid) {
            SENDER_VIEW_TYPE
        } else {
            RECEIVER_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if(viewType == SENDER_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.user_sample_sender, parent, false)
            ViewHolderSender(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.user_sample_receiver, parent, false)
            ViewHolderReceiver(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: MessageModels = mList[position]

        if(holder.javaClass == ViewHolderSender::class.java) {
            (holder as ViewHolderSender).senderMessageTextView.text = message.message
        } else {
            (holder as ViewHolderReceiver).receiverMessageTextView.text = message.message
        }

//        TIME STAMP ......
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}