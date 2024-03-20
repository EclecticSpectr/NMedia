package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {
    private val postLiveData = MutableLiveData<Post>()

    override fun getPost(): LiveData<Post> {
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb\n",
            likes = 999999,
            sharesAmount = 1099999
        )
        postLiveData.value = post
        return postLiveData
    }

    override fun likePost(post: Post) {
        val updatedPost = post.copy(likes = if (post.likedByMe) post.likes - 1 else post.likes + 1, likedByMe = !post.likedByMe)
        postLiveData.value = updatedPost
    }

    override fun sharePost(post: Post) {
        val updatedPost = post.copy(sharesAmount = post.sharesAmount + 1)
        postLiveData.value = updatedPost
    }
}
