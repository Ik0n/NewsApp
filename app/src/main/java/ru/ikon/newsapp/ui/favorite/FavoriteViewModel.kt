package ru.ikon.newsapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ikon.newsapp.data.api.NewsRepository
import ru.ikon.newsapp.models.Article
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: NewsRepository): ViewModel() {

    init {

    }

    fun getSavedArticles() = viewModelScope.async(Dispatchers.IO) {
        repository.getFavoriteArticles()
    }

    fun deleteArticleFromFavorite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFromFavorite(article = article)
    }

}