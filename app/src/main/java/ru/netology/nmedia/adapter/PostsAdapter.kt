package ru.netology.nmedia.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import kotlin.math.floor

interface onListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
}

class PostsAdapter(private val listener: onListener, private val intent: Intent) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener, intent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: onListener,
    private val intent: Intent
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesButton.isChecked = post.likedByMe
            likesButton.text = amountShow(post.likes)
            sharesButton.text = amountShow(post.shares)
            viewsButton.text = post.views.toString()
            likesButton.setOnClickListener {
                listener.onLike(post)
            }
            sharesButton.setOnClickListener {
                listener.onShare(post)
            }
            if (post.video != null) {
                play.setOnClickListener {
                    intent.data = Uri.parse(post.video)
                    startActivity(play.context, intent, null)
                }
                preview.visibility = View.VISIBLE
                play.visibility = View.VISIBLE
            } else {
                preview.visibility = View.GONE
                play.visibility = View.GONE
            }
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                listener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

private fun amountShow(number: Int): String =
    when {
        number >= 1_000_000 && (number / 100_000) % 10 == 0 -> (number / 1_000_000).toString() + "M"
        number >= 1_000_000 -> (floor(number.toDouble() / 100_000) / 10).toString() + "M"
        number >= 10_000 || (number >= 1_000 && (number / 100) % 10 == 0) -> (number / 1_000).toString() + "K"
        number >= 1_000 -> (floor(number.toDouble() / 100) / 10).toString() + "K"
        else -> number.toString()
    }

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}