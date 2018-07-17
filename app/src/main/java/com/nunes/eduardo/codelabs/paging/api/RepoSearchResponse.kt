package com.nunes.eduardo.codelabs.paging.api


import com.nunes.eduardo.codelabs.paging.model.Repo
import com.google.gson.annotations.SerializedName

/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class RepoSearchResponse(
        @SerializedName("total_count") val total: Int = 0,
        @SerializedName("items") val items: List<Repo> = emptyList(),
        val nextPage: Int? = null
)
