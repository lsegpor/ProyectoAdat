package com.lsegura.proyectoadat

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("/notes")
    fun getNotes(): Call<List<Note>>

    @GET("/notes/{id}")
    fun getNoteById(@Path("id") id: Int): Call<Note>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @DELETE("/notes/{id}")
    fun deleteNote(@Path("id") id: Int): Call<Response<Note>>

    @DELETE("/users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<String>

    @PUT("/notes/{id}")
    fun updateNote(@Path("id") id: Int, @Body newData: Note): Call<Note>

    @PUT("/users/{id}")
    fun updateUser(@Path("id") id: Int, @Body newData: User): Call<User>

    @POST("/notes")
    fun createNote(@Body newData: Note): Call<Note>

    @POST("users")
    fun createUser(@Body newData: User): Call<User>
}
