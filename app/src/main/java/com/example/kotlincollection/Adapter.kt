package com.example.kotlincollection

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_card_view.view.*
import org.joda.time.LocalDate
import org.joda.time.Period

class Adapter(val list: MutableList<PostCard>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.post_card_view, parent, false)
        return PostViewHolder(this, view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as PostViewHolder) {
            bind(list[position])
        }
    }
}

class PostViewHolder(private val adapter: Adapter, view: View) : RecyclerView.ViewHolder(view) {

    fun bind(post: PostCard) {
        with(itemView) {
            username_txt.text = post.username
            post_txt.text = post.post
            date_txt.text = getTimePeriod(post)

            if (post.liked) {
                like_btn.setImageResource(R.drawable.ic_baseline_favorite_24_f10f0a)
                like_counts_txt.text = post.likeCounts.toString()
                like_counts_txt.visibility = View.VISIBLE
            } else {
                like_counts_txt.visibility = View.GONE
            }

            if (post.commented) {
                comments_counts_txt.text = post.commentCounts.toString()
                comments_counts_txt.visibility = View.VISIBLE
            } else {
                comments_counts_txt.visibility = View.GONE
            }

            if (post.shared) {
                share_counts_txt.text = post.shareCounts.toString()
                share_counts_txt.visibility = View.VISIBLE
            } else {
                share_counts_txt.visibility = View.GONE
            }

            if (post.postType == PostType.YOUTUBE_VIDEO) {
                youtube_btn.visibility = View.VISIBLE
            } else {
                youtube_btn.visibility = View.GONE
            }

            if (post.postType == PostType.EVENT) {
                location_btn.visibility = View.VISIBLE
            } else {
                location_btn.visibility = View.GONE
            }

            post_visible.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]

                    adapter.list.removeAt(adapterPosition)
                    adapter.notifyDataSetChanged()
                }
            }

            like_btn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]

                    item.liked = !item.liked
                    if (item.liked) {
                        like_btn.setImageResource(R.drawable.ic_baseline_favorite_24_f10f0a)
                        item.likeCounts += 1
                        like_counts_txt.text = item.likeCounts.toString()
                        like_counts_txt.visibility = View.VISIBLE
                    } else {
                        like_btn.setImageResource(R.drawable.ic_baseline_favorite_24_808080)
                        item.likeCounts -= 1
                        like_counts_txt.text = item.likeCounts.toString()
                        if (item.likeCounts <= 0) {
                            like_counts_txt.visibility = View.INVISIBLE
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            comment_btn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]

                    item.commented = !item.commented
                    if (item.commented) {
                        item.commentCounts += 1
                        comments_counts_txt.text = item.commentCounts.toString()
                        comments_counts_txt.visibility = View.VISIBLE
                    } else {
                        item.commentCounts -= 1
                        comments_counts_txt.text = item.commentCounts.toString()
                        if (item.commentCounts <= 0) {
                            comments_counts_txt.visibility = View.INVISIBLE
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            share_btn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]

                    item.shared = !item.shared
                    if (item.shared) {
                        item.shareCounts += 1
                        share_counts_txt.text = item.shareCounts.toString()
                        share_counts_txt.visibility = View.VISIBLE
                    } else {
                        item.shareCounts -= 1
                        share_counts_txt.text = item.shareCounts.toString()
                        if (item.shareCounts <= 0) {
                            share_counts_txt.visibility = View.INVISIBLE
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            location_btn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]

                    if (item.postType == PostType.EVENT) {
                        val geoLocation = GeoLocation()
                        val intentUri = Uri.parse(
                            if (geoLocation.coordinate != "") {
                                "geo:${geoLocation.coordinate}"
                            } else {
                                "geo:0,0?q=${geoLocation.address}"
                            }
                        )
                        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri).apply {
                            setPackage("com.google.android.apps.maps")
                            itemView.context.startActivity(this)
                        }
                    } else {
                        Toast.makeText(itemView.context, "Post is not event", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            youtube_btn.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]

                    if (item.postType == PostType.YOUTUBE_VIDEO) {
                        val intentUri = Uri.parse("https://www.youtube.com/watch?v=WhWc3b3KhnY")
                        val youtubeIntent = Intent(Intent.ACTION_VIEW, intentUri).apply {
                            setPackage("com.google.android.youtube")
                            itemView.context.startActivity(this)
                        }
                    }
                }
            }
        }
    }

    private fun getTimePeriod(post: PostCard): String {
        val currentDate = LocalDate()
        val postDate = LocalDate()
        val period = Period(postDate, currentDate)

        return "hour " + period.hours + " minutes " + period.minutes + " ago"
    }
}