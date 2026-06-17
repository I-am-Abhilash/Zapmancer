package com.smach.zapmancer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val id: Int,
    val title: String,
    val content: String,
    val authorName: String,
    val authorAvatarUrl: String? = null,
    val thumbnailUrl: String? = null,
    val readingTimeMinutes: Int = 5,
    val publishedDate: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val viewsCount: Int = 0,
    val status: String = "PUBLISHED",
    val authorId: String,
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isNewUser: Boolean = false
)

@Serializable
data class UserProfileDto(
    val id: String,
    val name: String,
    val bio: String,
    val avatarUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val articlesCount: Int = 0,
)

@Serializable
data class SignupRequestDto(
    val email: String,
    val username: String,
    val password: String,
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class CreateArticleDto(
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val thumbnailUrl: String? = null,
)


@Serializable
data class OtpRequestDto(
    val identifier: String,
    val type: String // "PHONE" or "EMAIL"
)

@Serializable
data class OtpVerifyRequestDto(
    val identifier: String,
    val code: String,
    val sessionInfo: String
)
