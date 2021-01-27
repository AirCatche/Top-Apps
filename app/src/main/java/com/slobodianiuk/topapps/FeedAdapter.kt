package com.slobodianiuk.topapps

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val FEED_TYPE_PAID: Int = 0
private const val FEED_TYPE_FREE: Int = 1


class FeedAdapter<T : FeedEntry>(private val application: List<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    object FeedAConstants {
        const val TAG = "Feed Adapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FEED_TYPE_PAID -> PaidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_paid_list_record, parent, false))
            else -> FreeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_free_list_record, parent, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentApp = application[position]
        Log.d(FeedAConstants.TAG, "onBindViewHolder: ($position)")
        when(holder.itemViewType) {
            FEED_TYPE_PAID -> {
                val viewHolder0: PaidViewHolder = holder as PaidViewHolder
                viewHolder0.name.text = "${position+1}. ${currentApp.name}"
                viewHolder0.artist.text = (currentApp.artist)
                viewHolder0.summary.text = (currentApp.summary)
                viewHolder0.price.text = (currentApp.price)
            }
            FEED_TYPE_FREE -> {
                val viewHolder1: FreeViewHolder = holder as FreeViewHolder
                viewHolder1.name.text = "${position+1}. ${currentApp.name}"
                viewHolder1.artist.text = (currentApp.artist)
                viewHolder1.summary.text = (currentApp.summary)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (application[position].entryType == 0) {
            FEED_TYPE_PAID
        } else {
            FEED_TYPE_FREE
        }
    }

    override fun getItemCount(): Int {
        return application.size
    }

    class PaidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tvName)
        val artist: TextView = itemView.findViewById(R.id.tvArtist)
        val summary: TextView = itemView.findViewById(R.id.tvSummary)
        val price: TextView = itemView.findViewById(R.id.tvPrice)


//        init {
//            makeScrollable(summary)
//        }

    }


    class FreeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tvName)
        val artist: TextView = itemView.findViewById(R.id.tvArtist)
        val summary: TextView = itemView.findViewById(R.id.tvSummary)
        private val scroll: ScrollView = itemView.findViewById(R.id.summaryScroll)

        init {
            makeScrollable(scroll)
        }

    }

}

@SuppressLint("ClickableViewAccessibility")
fun makeScrollable(scroll: ScrollView) {

    scroll.setOnTouchListener(View.OnTouchListener() {
        view, _ ->
        view.parent.requestDisallowInterceptTouchEvent(true)
        false
    })
}
