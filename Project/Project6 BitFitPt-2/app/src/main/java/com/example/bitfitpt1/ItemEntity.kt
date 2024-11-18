package com.example.bitfitpt1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "item_table")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "food") val name: String,
    @ColumnInfo(name = "calorie") val calorie: Int,
    @ColumnInfo(name = "date") val date: String
)
