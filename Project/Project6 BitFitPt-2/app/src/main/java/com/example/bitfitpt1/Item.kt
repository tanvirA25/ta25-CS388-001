package com.example.bitfitpt1

class Item(
    val id: Long,
    val name: String,
    val calorie: Int,
    val date: String
){
    fun toEntity(): ItemEntity{
        return ItemEntity(
            id = this.id,
            name = this.name,
            calorie = this.calorie,
            date = this.date
        )
    }
}

