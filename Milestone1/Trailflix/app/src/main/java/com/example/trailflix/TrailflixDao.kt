package com.example.trailflix

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrailflixDao {

    @Insert
    suspend fun insert(trailflix: Trailflix): Long

    @Insert
    suspend fun insertAll(trailflixs: List<Trailflix>)

    @Update
    suspend fun update(trailflix: Trailflix)

    // Return pending todos as a Flow, so it will emit updates automatically
    @Query("SELECT * FROM Trailflix WHERE isDone = 0 AND userId = :userId")
    fun getPendingTrailflixs(userId: String): Flow<List<Trailflix>>

    // Return completed todos as a Flow, ordered by completion date
    @Query("SELECT * FROM Trailflix WHERE isDone = 1 AND userId = :userId ORDER BY completedDate DESC")
    fun getCompletedTrailflixs(userId: String): Flow<List<Trailflix>>
}