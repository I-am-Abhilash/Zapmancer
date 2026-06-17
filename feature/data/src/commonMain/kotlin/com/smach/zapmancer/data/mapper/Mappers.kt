package com.smach.zapmancer.data.mapper

import com.smach.zapmancer.core.database.entity.ArticleEntity
import com.smach.zapmancer.data.model.ArticleDto
import com.smach.zapmancer.data.model.UserDto
import com.smach.zapmancer.data.model.UserProfileDto
import com.smach.zapmancer.domain.model.Article
import com.smach.zapmancer.domain.model.User
import com.smach.zapmancer.domain.model.UserProfile

fun ArticleDto.toDomain() = Article(
    id = id,
    title = title,
    content = content,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    thumbnailUrl = thumbnailUrl,
    readingTimeMinutes = readingTimeMinutes,
    publishedDate = publishedDate,
    category = category,
    likesCount = likesCount,
    isBookmarked = false,
)

fun ArticleEntity.toDomain() = Article(
    id = id,
    title = title,
    content = content,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    thumbnailUrl = thumbnailUrl,
    readingTimeMinutes = readingTimeMinutes,
    publishedDate = publishedDate,
    category = category,
    likesCount = likesCount,
    isBookmarked = isBookmarked,
)

fun ArticleDto.toEntity(feedType: String) = ArticleEntity(
    id = id,
    feedType = feedType,
    title = title,
    content = content,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    thumbnailUrl = thumbnailUrl,
    readingTimeMinutes = readingTimeMinutes,
    publishedDate = publishedDate,
    category = category,
    likesCount = likesCount,
    isBookmarked = false,
)

fun UserDto.toDomain() = User(
    id = id,
    email = email,
    accessToken = accessToken,
    refreshToken = refreshToken,
    isNewUser = isNewUser,
)

fun UserProfileDto.toDomain() = UserProfile(
    id = id,
    name = name,
    bio = bio,
    avatarUrl = avatarUrl,
    followersCount = followersCount,
    followingCount = followingCount,
    articlesCount = articlesCount,
)

fun Article.toDto() = ArticleDto(
    id = id,
    title = title,
    content = content,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    thumbnailUrl = thumbnailUrl,
    readingTimeMinutes = readingTimeMinutes,
    publishedDate = publishedDate,
    category = category,
    likesCount = likesCount,
    tags = tags,
    commentsCount = commentsCount,
    viewsCount = viewsCount,
    status = status,
    authorId = authorId,
)

fun Article.toEntity(feedType: String) = ArticleEntity(
    id = id,
    feedType = feedType,
    title = title,
    content = content,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    thumbnailUrl = thumbnailUrl,
    readingTimeMinutes = readingTimeMinutes,
    publishedDate = publishedDate,
    category = category,
    likesCount = likesCount,
    isBookmarked = isBookmarked,
    commentsCount = commentsCount,
    viewsCount = viewsCount,
    status = status,
    authorId = authorId,
)

