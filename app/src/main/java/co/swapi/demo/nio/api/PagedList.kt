package co.swapi.demo.nio.api

import android.text.TextUtils


class PagedList<T> {
    var count: Int? = 0
    var next: String? = null
    var previous: String? = null
    var results: List<T>? = emptyList()

    fun hasMore(): Boolean {
        return !TextUtils.isEmpty(next)
    }
}