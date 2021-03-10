package ru.netology

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.databinding.ActivityMainBinding
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    var counterShares = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
                id = 1,
                author = "Нетология. Университет интернет-профессий будущего",
                published = "21 мая в 18:36",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу." +
                        " Затем появились курсы по дизайну, разработке, аналитике и управлению. " +
                        "Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                        "Но самое важно остается с нами: мы верим, что в каждом уже есть сила, которая " +
                        "заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать" +
                        " на путь роста и начать цепочку перемен → http://netolo.gy//fyb",
                likeByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (post.likeByMe) {
                like?.setImageResource(R.drawable.ic_no_liked_24)
            }
            like?.setOnClickListener {
                post.likeByMe = !post.likeByMe
                like.setImageResource(
                    if (post.likeByMe) R.drawable.ic_liked_24 else R.drawable.ic_no_liked_24
                )
                likes.setText(
                    if (post.likeByMe) "1" else "0"
                )
            }
            share?.setOnClickListener {
                shares.setText(totalizerSmartFeed(++counterShares))

            }
        }
    }

    fun counterOverThousand(feed: Int): Int {
        return when(feed) {
            in 1_000..999_999 -> feed/100
            else -> feed/100_000
        }
    }

    fun totalizerSmartFeed(feed: Int): String {
        return when(feed) {
            in 0..999 -> "$feed"
            in 1_000..999_999 -> "${ (counterOverThousand(feed).toDouble() / 10) }K"
            else -> "${ (counterOverThousand(feed).toDouble() / 10) }M"
        }
    }
}