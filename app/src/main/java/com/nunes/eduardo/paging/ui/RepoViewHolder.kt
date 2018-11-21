package com.nunes.eduardo.paging.ui

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.nunes.eduardo.paging.R
import com.nunes.eduardo.paging.model.Repo

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.repo_name)
    private val description: TextView = view.findViewById(R.id.repo_description)
    private val stars: TextView = view.findViewById(R.id.repo_stars)
    private val language: TextView = view.findViewById(R.id.repo_language)
    private val forks: TextView = view.findViewById(R.id.repo_forks)

    private var repo: Repo? = null

    init {
        view.setOnClickListener(::onItemClickListener)
    }

    private fun onItemClickListener(view: View) {
        repo?.url?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view.context.startActivity(intent)
        }
    }

    fun bindItem(repo: Repo?) {
        repo?.let(::showItemData) ?: showItemPlaceholder()
    }

    private fun showItemPlaceholder() {
        val resources = itemView.resources
        name.text = resources.getString(R.string.loading)
        stars.text = resources.getString(R.string.unknown)
        forks.text = resources.getString(R.string.unknown)
    }

    private fun showItemData(repo: Repo) {
        this.repo = repo
        name.text = repo.fullName
        stars.text = repo.stars.toString()
        forks.text = repo.forks.toString()

        repo.description?.let {
            description.text = it
            description.visibility = VISIBLE
        }

        repo.language?.let {
            val resources = itemView.resources
            language.text = resources.getString(R.string.language, repo.language)
            language.visibility = VISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.repo_view_item, parent, false)
            return RepoViewHolder(view)
        }
    }
}