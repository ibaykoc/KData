package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData
import com.google.gson.annotations.SerializedName

@KData("GithubRepo")
data class GithubReposResponse(
    @SerializedName("archive_url")
    val archiveUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/{archive_format}{/ref}
    @SerializedName("archived")
    val archived: Boolean?, // false
    @SerializedName("assignees_url")
    val assigneesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/assignees{/user}
    @SerializedName("blobs_url")
    val blobsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/git/blobs{/sha}
    @SerializedName("branches_url")
    val branchesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/branches{/branch}
    @SerializedName("clone_url")
    val cloneUrl: String?, // https://github.com/ibaykoc/WordReceiverKBBI.git
    @SerializedName("collaborators_url")
    val collaboratorsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/collaborators{/collaborator}
    @SerializedName("comments_url")
    val commentsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/comments{/number}
    @SerializedName("commits_url")
    val commitsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/commits{/sha}
    @SerializedName("compare_url")
    val compareUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/compare/{base}...{head}
    @SerializedName("contents_url")
    val contentsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/contents/{+path}
    @SerializedName("contributors_url")
    val contributorsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/contributors
    @SerializedName("created_at")
    val createdAt: String?, // 2017-08-24T11:00:50Z
    @SerializedName("default_branch")
    val defaultBranch: String?, // master
    @SerializedName("deployments_url")
    val deploymentsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/deployments
    @SerializedName("description")
    val description: String?, // Little program that receive all the word in KBBI.co.id site
    @SerializedName("downloads_url")
    val downloadsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/downloads
    @SerializedName("events_url")
    val eventsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/events
    @SerializedName("fork")
    val fork: Boolean?, // false
    @SerializedName("forks")
    val forks: Int?, // 0
    @SerializedName("forks_count")
    val forksCount: Int?, // 0
    @SerializedName("forks_url")
    val forksUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/forks
    @SerializedName("full_name")
    val fullName: String?, // ibaykoc/WordReceiverKBBI
    @SerializedName("git_commits_url")
    val gitCommitsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/git/commits{/sha}
    @SerializedName("git_refs_url")
    val gitRefsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/git/refs{/sha}
    @SerializedName("git_tags_url")
    val gitTagsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/git/tags{/sha}
    @SerializedName("git_url")
    val gitUrl: String?, // git://github.com/ibaykoc/WordReceiverKBBI.git
    @SerializedName("has_downloads")
    val hasDownloads: Boolean?, // true
    @SerializedName("has_issues")
    val hasIssues: Boolean?, // true
    @SerializedName("has_pages")
    val hasPages: Boolean?, // false
    @SerializedName("has_projects")
    val hasProjects: Boolean?, // true
    @SerializedName("has_wiki")
    val hasWiki: Boolean?, // true
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("hooks_url")
    val hooksUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/hooks
    @SerializedName("html_url")
    val htmlUrl: String?, // https://github.com/ibaykoc/WordReceiverKBBI
    @SerializedName("id")
    val id: Int?, // 101285717
    @SerializedName("issue_comment_url")
    val issueCommentUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/issues/comments{/number}
    @SerializedName("issue_events_url")
    val issueEventsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/issues/events{/number}
    @SerializedName("issues_url")
    val issuesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/issues{/number}
    @SerializedName("keys_url")
    val keysUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/keys{/key_id}
    @SerializedName("labels_url")
    val labelsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/labels{/name}
    @SerializedName("language")
    val language: String?, // Python
    @SerializedName("languages_url")
    val languagesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/languages
    @SerializedName("license")
    val license: Any?, // null
    @SerializedName("merges_url")
    val mergesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/merges
    @SerializedName("milestones_url")
    val milestonesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/milestones{/number}
    @SerializedName("mirror_url")
    val mirrorUrl: Any?, // null
    @SerializedName("name")
    @KData.Field("repoName")
    val name: String?, // WordReceiverKBBI
    @SerializedName("node_id")
    val nodeId: String?, // MDEwOlJlcG9zaXRvcnkxMDEyODU3MTc=
    @SerializedName("notifications_url")
    val notificationsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/notifications{?since,all,participating}
    @SerializedName("open_issues")
    val openIssues: Int?, // 0
    @SerializedName("open_issues_count")
    val openIssuesCount: Int?, // 0
    @SerializedName("owner")
    val owner: Owner?,
    @SerializedName("private")
    val `private`: Boolean?, // false
    @SerializedName("pulls_url")
    val pullsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/pulls{/number}
    @SerializedName("pushed_at")
    val pushedAt: String?, // 2017-08-26T11:19:40Z
    @SerializedName("releases_url")
    val releasesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/releases{/id}
    @SerializedName("size")
    val size: Int?, // 2
    @SerializedName("ssh_url")
    val sshUrl: String?, // git@github.com:ibaykoc/WordReceiverKBBI.git
    @SerializedName("stargazers_count")
    val stargazersCount: Int?, // 0
    @SerializedName("stargazers_url")
    val stargazersUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/stargazers
    @SerializedName("statuses_url")
    val statusesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/statuses/{sha}
    @SerializedName("subscribers_url")
    val subscribersUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/subscribers
    @SerializedName("subscription_url")
    val subscriptionUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/subscription
    @SerializedName("svn_url")
    val svnUrl: String?, // https://github.com/ibaykoc/WordReceiverKBBI
    @SerializedName("tags_url")
    val tagsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/tags
    @SerializedName("teams_url")
    val teamsUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/teams
    @SerializedName("trees_url")
    val treesUrl: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI/git/trees{/sha}
    @SerializedName("updated_at")
    val updatedAt: String?, // 2017-08-26T11:21:16Z
    @SerializedName("url")
    val url: String?, // https://api.github.com/repos/ibaykoc/WordReceiverKBBI
    @SerializedName("watchers")
    val watchers: Int?, // 0
    @SerializedName("watchers_count")
    val watchersCount: Int? // 0
)