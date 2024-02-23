package com.lsegura.proyectoadat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val email: String,
    val name: String,
    val telephone: String,
    val notes: List<Note>
): Parcelable