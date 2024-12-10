package com.example.trailflix

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trailflix::class], version =1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun trailflixDao(): TrailflixDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE}

            }
        private fun buildDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "trailflix-database"
            ).build()
    }
}