package com.example.metro.data.repository

import com.example.metro.data.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository {

    private val db = Firebase.database.reference.child("users")

    /**
     * Returns (exists, User?) — checks if a user node already exists in Firebase.
     */
    suspend fun checkUserExists(email: String): Pair<Boolean, User?> {
        return try {
            val userId = email.toUserId()
            val snapshot = db.child(userId).get().await()
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                Pair(true, user)
            } else {
                Pair(false, null)
            }
        } catch (e: Exception) {
            Pair(false, null)
        }
    }

    /**
     * Saves a new user to Firebase Realtime Database.
     * Uses setValue so the full node is written atomically.
     */
    suspend fun saveNewUser(user: User) {
        val userId = user.email.toUserId()
        db.child(userId).setValue(user).await()
    }

    /**
     * Updates only specific fields for an existing user (e.g. lastLogin, status).
     */
    suspend fun updateUserFields(email: String, fields: Map<String, Any>) {
        val userId = email.toUserId()
        db.child(userId).updateChildren(fields).await()
    }

    /**
     * Fetches a user from Firebase by email.
     */
    suspend fun getUser(email: String): User? {
        return try {
            val snapshot = db.child(email.toUserId()).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun String.toUserId(): String = replace(".", "_").replace("@", "_at_")
}

