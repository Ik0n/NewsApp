package ru.ikon.newsapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import okhttp3.internal.notify
import ru.ikon.newsapp.R
import ru.ikon.newsapp.databinding.FragmentDetailsBinding
import ru.ikon.newsapp.databinding.FragmentFavoriteBinding
import ru.ikon.newsapp.models.Article
import ru.ikon.newsapp.ui.adapters.NewsAdapter

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteViewModel>()
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        MainScope().launch {
            newsAdapter.differ.submitList(viewModel.getSavedArticles().await())
        }

        newsAdapter.setOnFavoriteClickLister {
            MainScope().launch {
                viewModel.deleteArticleFromFavorite(it)
                launch {
                    newsAdapter.differ.submitList(newsAdapter.differ.currentList.toMutableList().apply {
                        remove(it)
                    })
                    newsAdapter.notifyItemRemoved(newsAdapter.differ.currentList.indexOf(it))
                }
            }
        }

        newsAdapter.setOnItemClickListener {
            val bundle = bundleOf("article" to it)
            view.findNavController().navigate(
                R.id.action_favoriteFragment_to_detailsFragment,
                bundle
            )
        }

    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        favorite_news_adapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}