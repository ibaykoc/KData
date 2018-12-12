package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.GithubReposResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubAPI {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<GithubReposResponse>>

}