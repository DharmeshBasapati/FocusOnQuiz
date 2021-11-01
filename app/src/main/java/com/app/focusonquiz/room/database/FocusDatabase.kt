package com.app.focusonquiz.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.focusonquiz.room.dao.FocusDao
import com.app.focusonquiz.room.entity.Quiz

@Database(entities = [Quiz::class],version = 1)
abstract class FocusDatabase : RoomDatabase() {

    abstract fun focusDao() : FocusDao
}