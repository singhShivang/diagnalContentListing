package com.diagnal.contentlisting.repo

import com.diagnal.contentlisting.model.PageData

interface IPageRepository {
    fun loadJson(pageNum: Int): PageData?
}
