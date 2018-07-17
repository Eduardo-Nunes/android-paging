package com.nunes.eduardo.paging.data

import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.nunes.eduardo.paging.api.GithubService
import com.nunes.eduardo.paging.db.GithubLocalCache
import com.nunes.eduardo.paging.model.RepoSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(
        private val service: GithubService,
        private val cache: GithubLocalCache
) {
    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")

        // Get the data source factory from the local cache
        val dataSourceFactory = cache.reposByName(query)

        //contruct the repoBoundaryCallback
        val boundaryCallback = RepoBoundaryCallback(query, service, cache)

        //Get the paged List
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        return RepoSearchResult(data, boundaryCallback.networkErrors)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}