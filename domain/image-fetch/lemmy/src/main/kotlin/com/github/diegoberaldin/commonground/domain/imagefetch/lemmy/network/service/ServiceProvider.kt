package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.service

interface ServiceProvider {

    val currentInstance: String
    val post: PostService

    fun changeInstance(value: String)
}

