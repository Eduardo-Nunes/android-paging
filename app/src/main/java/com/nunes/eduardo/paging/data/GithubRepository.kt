package com.nunes.eduardo.paging.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.nunes.eduardo.paging.api.GithubService
import com.nunes.eduardo.paging.api.searchRepos
import com.nunes.eduardo.paging.db.GithubLocalCache
import com.nunes.eduardo.paging.model.RepoSearchResult

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

        // Get the data source factory from the local cache
        val dataSourceFactory = cache.reposByName(query)

        //Get the paged List
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()

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
        private const val DATABASE_PAGE_SIZE = 20
    }
}