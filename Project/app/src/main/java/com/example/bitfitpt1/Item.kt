package com.example.bitfitpt1

class Item(
    val name: String,
    val calorie: Int
){
    fun toEntity(): ItemEntity{
        return ItemEntity(
            name = this.name,
            calorie = this.calorie
        )
    }
}

