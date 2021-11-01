package com.app.focusonquiz.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.app.focusonquiz.room.entity.Quiz

@Dao
interface FocusDao {

    @Query("SELECT * from quiz")
    fun getQuestions(): List<Quiz>

}