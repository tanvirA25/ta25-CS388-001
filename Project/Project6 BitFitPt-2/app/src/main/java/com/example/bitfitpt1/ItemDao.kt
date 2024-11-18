package com.example.bitfitpt1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item_table")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Insert
    suspend fun insert(item: ItemEntity)


    @Delete
    suspend fun delete(item: ItemEntity)
}