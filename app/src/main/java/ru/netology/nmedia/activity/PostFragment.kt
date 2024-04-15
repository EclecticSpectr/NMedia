package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.util.amountShow
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.CompanionArg.Companion.longArg
import ru.netology.nmedia.util.CompanionArg.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(::requireParentFragment)
        val id = arguments?.longArg

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            binding.postContent.apply {
                posts.map { post ->
                    if (post.id == id) {
                        author.text = post.author
                        published.text = post.published
                        content.text = post.content
                        likesButton.isChecked = post.likedByMe
                        likesButton.text = amountShow(post.likes)
                        sharesButton.text = amountShow(post.shares)
                        viewsButton.text = post.views.toString()
                        likesButton.setOnClickListener {
                            viewModel.likeById(post.id)
                        }
                        sharesButton.setOnClickListener {
                            viewModel.shareById(post.id)
                        }

                        play.setOnClickListener {
                            if (post.video != null) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                                startActivity(intent)
                                preview.visibility = View.VISIBLE
                                play.visibility = View.VISIBLE
                            } else {
                                preview.visibility = View.GONE
                                play.visibility = View.GONE
                            }
                            menuButton.setOnClickListener {
                                androidx.appcompat.widget.PopupMenu(it.context, it).apply {
                                    inflate(R.menu.options_post)
                                    setOnMenuItemClickListener { item ->
                                        when (item.itemId) {
                                            R.id.remove -> {
                                                viewModel.removeById(post.id)
                                                findNavController().navigate(R.id.action_postFragment_to_feedFragment)
                                                findNavController().navigateUp()
                                                true
                                            }

                                            R.id.edit -> {
                                                viewModel.edit(post)
                                                findNavController().navigate(R.id.action_postFragment_to_newPostFragment,
                                                    Bundle().apply
                                                    {
                                                        textArg = post.content
                                                    }
                                                )
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
            }
        }
        return binding.root
    }
}