package com.example.instaclone.network.dto

import com.example.instaclone.network.dto.following_dto.FollowingDto
import com.example.instaclone.network.dto.messages_dto.MessageDto
import com.example.instaclone.network.dto.posts_dto.PostDto
import com.example.instaclone.network.dto.story_dto.StoryDto

data class GlobalModelDto(
    val followings: List<FollowingDto>,
    val messages: List<MessageDto>,
    val posts: List<PostDto>,
    val stories: List<StoryDto>
)