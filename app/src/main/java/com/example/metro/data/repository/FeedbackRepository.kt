package com.example.metro.data.repository

import com.example.metro.data.model.Feedback
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FeedbackRepository {

    private val db = Firebase.database.reference.child("feedback")

    /**
     * Submits feedback to Firebase Realtime Database under /feedback/{pushId}.
     * Returns true on success, false on failure.
     */
    suspend fun submitFeedback(feedback: Feedback): Boolean {
        return try {
            val ref = db.push()
            ref.setValue(feedback).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}

