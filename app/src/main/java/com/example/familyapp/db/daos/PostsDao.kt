package com.example.instaclone.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.instaclone.db.entities.PostEntity

@Dao
interface PostsDao {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<PostEntity>

    @Insert
    fun addPost(post: PostEntity)


    @Insert
    suspend fun addPosts(posts: List<PostEntity>)

    @Delete
    fun removePost(post: PostEntity)
}