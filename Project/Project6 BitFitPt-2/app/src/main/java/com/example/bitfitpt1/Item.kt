package com.example.bitfitpt1

class Item(
    val name: String,
    val calorie: Int,
    val date: String
){
    fun toEntity(): ItemEntity{
        return ItemEntity(
            name = this.name,
            calorie = this.calorie,
            date = this.date
        )
    }
}

