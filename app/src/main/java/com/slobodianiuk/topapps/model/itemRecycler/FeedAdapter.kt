package com.slobodianiuk.topapps.model.itemRecycler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.slobodianiuk.topapps.R
import com.slobodianiuk.topapps.model.parse.FeedEntry

private const val FEED_TYPE_PAID: Int = 0
private const val FEED_TYPE_FREE: Int = 1


class FeedAdapter<T : FeedEntry>(private val application: List<T>, private val itemRV: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    object FeedAConstants {
        const val TAG = "Feed Adapter"
    }

    private var isShownDescription = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FEED_TYPE_PAID -> PaidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_paid_list_record, parent, false))
            else -> FreeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_free_list_record, parent, false))
        }
    }


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentApp = application[position]
        Log.d(FeedAConstants.TAG, "onBindViewHolder: ($position)")
        when(holder.itemViewType) {
            FEED_TYPE_PAID -> {
                val paidVh: PaidViewHolder = holder as PaidViewHolder
                paidVh.name.text = "${position+1}. ${currentApp.name}"
                paidVh.artist.text = (currentApp.artist)
                paidVh.summary.text = currentApp.summary
                paidVh.price.text = (currentApp.price)
            }
            FEED_TYPE_FREE -> {
                val freeVh: FreeViewHolder = holder as FreeViewHolder
                freeVh.name.text = "${position+1}. ${currentApp.name}"
                freeVh.artist.text = currentApp.artist
                if (isShownDescription) {
                    freeVh.summary.text = currentApp.summary
                } else if (!isShownDescription){
                    freeVh.summary.text = null
                }
            }
        }

        val itemListener = RecyclerItemClickListener(holder.itemView.context, itemRV, object : ItemOnClick {
            override fun onDoubleItemClick(v: View?, position: Int) {
                //val viewType = itemRV.findViewHolderForAdapterPosition(position)?.itemViewType
                if (isShownDescription) {
                    isShownDescription = false
                    notifyItemChanged(position)
                }
                Toast.makeText(v?.context, "Hide desc for ${1 + position} item", Toast.LENGTH_SHORT).show()
            }

            override fun onLongItemClick(v: View?, position: Int) {
                if (!isShownDescription) {
                    Toast.makeText(v?.context, "Open desc for ${1 + position} item", Toast.LENGTH_SHORT).show()
                    isShownDescription = true
                    notifyItemChanged(position)
                }

            }
        })
        itemRV.setOnTouchListener{ _, event ->
            itemListener.gestureDetector!!.onTouchEvent(event)
            false
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

    abstract class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract val name: TextView
        abstract val artist: TextView
        abstract val summary: TextView
    }

    class PaidViewHolder(itemView: View) : ItemViewHolder(itemView) {
        override val name: TextView = itemView.findViewById(R.id.tvName)
        override val artist: TextView = itemView.findViewById(R.id.tvArtist)
        override val summary: TextView = itemView.findViewById(R.id.tvSummary)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
    }


    class FreeViewHolder(itemView: View) : ItemViewHolder(itemView) {
        override val name: TextView = itemView.findViewById(R.id.tvName)
        override val artist: TextView = itemView.findViewById(R.id.tvArtist)
        override val summary: TextView = itemView.findViewById(R.id.tvSummary)
        private val scroll: ScrollView = itemView.findViewById(R.id.summaryScroll)

        init {
            makeScrollable(scroll)
        }
    }

}

@SuppressLint("ClickableViewAccessibility")
fun makeScrollable(scroll: ScrollView) {

    scroll.setOnTouchListener {
        view, _ ->
        view.parent.requestDisallowInterceptTouchEvent(true)
        false
    }
}
