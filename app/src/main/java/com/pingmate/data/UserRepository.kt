package com.pingmate.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pingmate.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun signUp(email: String, pass: String, displayName: String, avatarUrl: String): String? {
        return try {
            val result = auth.signUp(email, pass).await()
            val uid = result.user?.uid ?: return null
            val user = User(uid, email.split("@")[0], email, displayName, avatarUrl)
            db.collection("users").document(uid).set(user).await()
            uid
        } catch (e: Exception) {
            null
        }
    }

    suspend fun login(email: String, pass: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            result.user?.uid
        } catch (e: Exception) {
            null
        }
    }

    fun getUser(uid: String) = db.collection("users").document(uid).get()
}
