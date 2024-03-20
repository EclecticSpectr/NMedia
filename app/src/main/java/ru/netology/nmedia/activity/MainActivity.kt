package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getPost().observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likesAmount.text = amountShow(post.likes)
                sharesAmount.text = amountShow(post.sharesAmount)
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )

                likes.setOnClickListener {
                    viewModel.likePost(post)
                }

                shares.setOnClickListener {
                    viewModel.sharePost(post)
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
}