package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.adapter.onListener
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.*
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.textArg: String? by AndroidUtils.StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : onListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }

                val chooser =
                    Intent.createChooser(shareIntent, getString(R.string.chooser_share_post))
                startActivity(chooser)
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                if (viewModel.edited.value?.id == post.id) {
                    viewModel.clear()
                }
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onVideo(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(post.video)
                }
                startActivity(intent, null)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
}
