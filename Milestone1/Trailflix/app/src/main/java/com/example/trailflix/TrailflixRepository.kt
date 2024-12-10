package com.example.trailflix

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TrailflixRepository(context: Context) {
    private val db: AppDatabase = AppDatabase.getInstance(context)
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tag = "TrailflixRepository"


    suspend fun addToMovieList(movieId: String, title: String) = withContext(Dispatchers.IO){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User is not logged in")

        try {
            // Check if a movie with the same title exists for the user
            val snapshot = firestore.collection("movieList")
                .whereEqualTo("userId", userId)
                .whereEqualTo("title", title)
                .get()
                .await()

            if (snapshot.isEmpty) {
                val movieEntry = hashMapOf(
                    "userId" to userId,
                    "movieId" to movieId,
                    "title" to title,
                    "addedDate" to com.google.firebase.Timestamp.now()
                )
                firestore.collection("movieList").add(movieEntry).await()
                Log.d(tag, "Added movie $title to movieList for user $userId")
                return@withContext true // Return true if added successfully
            } else {
                Log.d(tag, "Movie with title $title already exists in the movieList")
                return@withContext false // Return false if it already exists
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to add movie to movieList: ${e.message}", e)
            throw e
        }
    }

    suspend fun addToTvShowList(tvShowId: String, name: String) = withContext(Dispatchers.IO) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User is not logged in")

        try {
            // Check if a TV show with the same name exists for the user
            val snapshot = firestore.collection("tvShowList")
                .whereEqualTo("userId", userId)
                .whereEqualTo("name", name)
                .get()
                .await()

            if (snapshot.isEmpty) {
                val tvShowEntry = hashMapOf(
                    "userId" to userId,
                    "tvShowId" to tvShowId,
                    "name" to name,
                    "addedDate" to com.google.firebase.Timestamp.now()
                )
                firestore.collection("tvShowList").add(tvShowEntry).await()
                Log.d(tag, "Added TV show $name to tvShowList for user $userId")
                return@withContext true // Return true if added successfully
            } else {
                Log.d(tag, "TV show with name $name already exists in the tvShowList")
                return@withContext false // Return false if it already exists
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to add TV show to tvShowList: ${e.message}", e)
            throw e
        }
    }

    // Fetch pending todos
    fun getPendingTrailflixs(): Flow<List<Trailflix>> {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in")
        return db.trailflixDao().getPendingTrailflixs(userId).onEach { trailflixs ->
            Log.d(tag, "Syncing pending trailflix from Firestore for user: $userId")
            try {
                val collection = firestore.collection("trailflix")
                val snapshot = collection
                    .whereEqualTo("isDone", false)
                    .whereEqualTo("userId", userId)
                    .get().await()

                val fetchedTrailflix = snapshot.documents.map { document ->
                    document.toObject(Trailflix::class.java)!!.apply { firestoreId = document.id }
                }
                db.trailflixDao().insertAll(fetchedTrailflix)
                Log.d(tag, "Synced pending todos from Firestore to Room")
            } catch (e: Exception) {
                Log.e(tag, "Failed to sync pending todos from Firestore: ${e.message}", e)
            }
        }
    }

        // Fetch completed todos
        fun getCompletedTrailflixs(): Flow<List<Trailflix>> {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in")
            return db.trailflixDao().getCompletedTrailflixs(userId).onEach { trailflixs ->
                Log.d(tag, "Syncing completed todos from Firestore for user: $userId")
                try {
                    val collection = firestore.collection("todos")
                    val snapshot = collection
                        .whereEqualTo("isDone", true)
                        .whereEqualTo("userId", userId)
                        .get().await()

                    val fetchedTrailflix = snapshot.documents.map { document ->
                        document.toObject(Trailflix::class.java)!!.apply { firestoreId = document.id }
                    }
                    db.trailflixDao().insertAll(fetchedTrailflix)
                    Log.d(tag, "Synced completed todos from Firestore to Room")
                } catch (e: Exception) {
                    Log.e(tag, "Failed to sync completed todos from Firestore: ${e.message}", e)
                }
            }
        }

// Add new todo
suspend fun addTrailflix(trailflix: Trailflix) = withContext(Dispatchers.IO) {
    val userId = auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in")
    val newTrailflix = trailflix.copy(userId = userId)
    val newId = db.trailflixDao().insert(newTrailflix)
    Log.d(tag, "Todo added to Room with local ID $newId")

    try {
        val document = firestore.collection("todos").add(newTrailflix).await()
        newTrailflix.firestoreId = document.id
        db.trailflixDao().update(newTrailflix)
        Log.d(tag, "Synced new todo to Firestore with Firestore ID ${newTrailflix.firestoreId}")
    } catch (e: Exception) {
        Log.e(tag, "Failed to sync new todo to Firestore: ${e.message}", e)
    }
}
suspend fun updateTrailflixStatus(trailflix: Trailflix) = withContext(Dispatchers.IO) {
    db.trailflixDao().update(trailflix) // Update in Room
    Log.d(tag, "Updated TODO status in Room with ID ${trailflix.localId}")

    // Sync the updated status with Firestore if possible
    try {
        val docId = trailflix.firestoreId
        if (docId != null) {
            firestore.collection("todos").document(docId).set(trailflix).await()
            Log.d(tag, "Synced TODO status to Firestore with ID $docId")
        } else {
            Log.w(tag, "Firestore ID is null for the TODO item")
        }
    } catch (e: Exception) {
        Log.e(tag, "Failed to sync TODO status to Firestore: ${e.message}", e)
    }
}


}
