package com.nunes.eduardo.codelabs.paging.data

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.nunes.eduardo.codelabs.paging.api.GithubService
import com.nunes.eduardo.codelabs.paging.api.searchRepos
import com.nunes.eduardo.codelabs.paging.db.GithubLocalCache
import com.nunes.eduardo.codelabs.paging.model.RepoSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(
        private val service: GithubService,
        private val cache: GithubLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")
        lastRequestedPage = 1
        requestAndSaveData(query)

        // Get data from the local cache
        val data = cache.reposByName(query)

        return RepoSearchResult(data, networkErrors)
    }

    fun requestMore(query: String) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}