package com.nunes.eduardo.paging.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * RepoSearchResult from a search, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class RepoSearchResult(
        val data: LiveData<PagedList<Repo>>,
        val networkErrors: LiveData<String>
)