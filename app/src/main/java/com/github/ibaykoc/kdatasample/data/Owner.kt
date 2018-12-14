package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData
import com.google.gson.annotations.SerializedName

data class Owner(
    @KData.Field("avatarUrl")
    @SerializedName("avatar_url")
    val avatarUrl: String?, // https://avatars0.githubusercontent.com/u/15071688?v=4
    @SerializedName("events_url")
    val eventsUrl: String?, // https://api.github.com/users/ibaykoc/events{/privacy}
    @SerializedName("followers_url")
    val followersUrl: String?, // https://api.github.com/users/ibaykoc/followers
    @SerializedName("following_url")
    val followingUrl: String?, // https://api.github.com/users/ibaykoc/following{/other_user}
    @SerializedName("gists_url")
    val gistsUrl: String?, // https://api.github.com/users/ibaykoc/gists{/gist_id}
    @SerializedName("gravatar_id")
    val gravatarId: String?,
    @SerializedName("html_url")
    val htmlUrl: String?, // https://github.com/ibaykoc
    @SerializedName("id")
    val id: Int?, // 15071688
    @SerializedName("login")
    @KData.Field("name")
    val login: String?, // ibaykoc
    @SerializedName("node_id")
    val nodeId: String?, // MDQ6VXNlcjE1MDcxNjg4
    @SerializedName("organizations_url")
    val organizationsUrl: String?, // https://api.github.com/users/ibaykoc/orgs
    @SerializedName("received_events_url")
    val receivedEventsUrl: String?, // https://api.github.com/users/ibaykoc/received_events
    @SerializedName("repos_url")
    val reposUrl: String?, // https://api.github.com/users/ibaykoc/repos
    @SerializedName("site_admin")
    val siteAdmin: Boolean?, // false
    @SerializedName("starred_url")
    val starredUrl: String?, // https://api.github.com/users/ibaykoc/starred{/owner}{/repo}
    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String?, // https://api.github.com/users/ibaykoc/subscriptions
    @SerializedName("type")
    val type: String?, // User
    @SerializedName("url")
    val url: String? // https://api.github.com/users/ibaykoc
)