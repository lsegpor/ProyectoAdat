package com.lsegura.proyectoadat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Note (
    val id: Int?,
    val title: String,
    val description: String,
    val category: String,
    val user: User
): Parcelable