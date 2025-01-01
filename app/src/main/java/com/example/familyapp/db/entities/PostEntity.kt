package com.example.instaclone.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val postId: Int = 0,

    @ColumnInfo val caption: String,
    val likes: Int,

    @ColumnInfo(name = "profile_pic_url")
    val profilePicture: String,

    @ColumnInfo val username: String
)
