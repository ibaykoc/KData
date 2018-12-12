package com.github.ibaykoc.kdatasample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.ibaykoc.kdatasample.data.GithubReposResponse
import com.github.ibaykoc.kdatasample.data.validate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val githubApi: GithubAPI = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        githubApi.listRepos("ibaykoc").enqueue(
            object : Callback<List<GithubReposResponse>> {
                override fun onResponse(
                    call: Call<List<GithubReposResponse>>,
                    response: Response<List<GithubReposResponse>>
                ) {
                    response.body()?.forEach { raw ->
                        raw.validate()?.let { validData ->
                            Log.d("MainActivity", validData.repoName)
                        }
                    }
                }

                override fun onFailure(call: Call<List<GithubReposResponse>>, t: Throwable) {
                    Log.d("MainActivity", "API Call Failed: ${t.localizedMessage}")
                }

            }
        )
    }
}
