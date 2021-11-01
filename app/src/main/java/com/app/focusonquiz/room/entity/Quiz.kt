package com.app.focusonquiz.room.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(

    @PrimaryKey val q_id: Int,

    @ColumnInfo(name = "question")
    val question: String,

    @ColumnInfo(name = "opt1")
    val opt1: String,

    @ColumnInfo(name = "opt2")
    val opt2: String,

    @ColumnInfo(name = "opt3")
    val opt3: String?,

    @ColumnInfo(name = "opt4")
    val opt4: String?
)