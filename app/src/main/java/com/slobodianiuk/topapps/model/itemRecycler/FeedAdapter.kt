package com.slobodianiuk.topapps.model.itemRecycler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.slobodianiuk.topapps.R
import com.slobodianiuk.topapps.model.itemRecycler.FeedAdapter.FeedAConstants.FEED_TYPE_FREE
import com.slobodianiuk.topapps.model.itemRecycler.FeedAdapter.FeedAConstants.FEED_TYPE_PAID
import com.slobodianiuk.topapps.model.parse.FeedEntry
import com.squareup.picasso.Picasso


class FeedAdapter<T : FeedEntry>(private val application: List<T>, private val itemRV: RecyclerView)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    object FeedAConstants {
        const val FEED_TYPE_PAID: Int = 0
        const val FEED_TYPE_FREE: Int = 1
        const val TAG = "Feed Adapter"
    }
    var isClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FEED_TYPE_PAID ->
                PaidViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.activity_paid_list_record, parent, false))
            else ->
                FreeViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.activity_free_list_record, parent, false))
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentApp = application[position]
        Log.d(FeedAConstants.TAG, "onBindViewHolder: ($position)")
        when(holder.itemViewType) {
            FEED_TYPE_PAID -> {
                val paidVh: PaidViewHolder = holder as PaidViewHolder
                paidVh.position.text = (position+1).toString()
                paidVh.name.text = currentApp.name
                paidVh.artist.text = (currentApp.artist)
                if (isClicked) {
                    paidVh.summary.text = currentApp.summary
                    }
                if (!isClicked) {
                    paidVh.summary.text = ""
                }
                paidVh.price.text = (currentApp.price)
                Picasso.get().load(currentApp.imageUrl).into(paidVh.image)
            }
            FEED_TYPE_FREE -> {
                val freeVh: FreeViewHolder = holder as FreeViewHolder
                freeVh.position.text = (position+1).toString()
                freeVh.name.text = currentApp.name
                freeVh.artist.text = currentApp.artist
                if (isClicked) {
                    freeVh.summary.text = currentApp.summary
                }
                if (!isClicked) {
                    freeVh.summary.text = ""
                }
                Picasso.get().load(currentApp.imageUrl).into(freeVh.image)
            }
        }
        val itemListener = RecyclerItemClickListener(holder.itemView.context, itemRV, object : ItemOnClick {
            override fun onLongItemClick(v: View?, position: Int) {

                Toast.makeText(v?.context, "Open desc for ${1 + position} item", Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(v: View?, position: Int) {
                isClicked = click(isClicked)
                notifyItemChanged(position)
                Toast.makeText(v?.context, "Hide desc for ${1 + position} item", Toast.LENGTH_SHORT).show()
            }
        })
        itemRV.setOnTouchListener{ _, event ->
            itemListener.gestureDetector!!.onTouchEvent(event)
            false
        }
        //                    val d = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
//                        override fun getOldListSize(): Int {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun getNewListSize(): Int {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//
//                        }
//
//                        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                            TODO("Not yet implemented")
//                        }
//
//                    })
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
        abstract val position: TextView
        abstract val image : ImageView

    }

    class PaidViewHolder(itemView: View) : ItemViewHolder(itemView) {
        override val position: TextView = itemView.findViewById(R.id.tvPosition)
        override val name: TextView = itemView.findViewById(R.id.tvName)
        override val artist: TextView = itemView.findViewById(R.id.tvArtist)
        override val summary: TextView = itemView.findViewById(R.id.tvSummary)
        override val image: ImageView = itemView.findViewById(R.id.paidAppImage)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
    }


    class FreeViewHolder(itemView: View) : ItemViewHolder(itemView) {
        override val position: TextView = itemView.findViewById(R.id.tvPosition)
        override val name: TextView = itemView.findViewById(R.id.tvName)
        override val artist: TextView = itemView.findViewById(R.id.tvArtist)
        override val summary: TextView = itemView.findViewById(R.id.tvSummary)
        override val image: ImageView = itemView.findViewById(R.id.freeAppImage)
        private val scroll: ScrollView = itemView.findViewById(R.id.summaryScroll)

        init {
            //makeScrollable(scroll)
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
fun click(click: Boolean) : Boolean {
    return !click
}
