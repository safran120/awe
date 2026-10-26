package com.safranpulse.pulsechat.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.safranpulse.pulsechat.model.User
import com.safranpulse.pulsechat.util.Clock
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserRepository @Inject constructor(
    private val clock: Clock
) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun signUp(
        email: String,
        password: String,
        displayName: String,
        bio: String,
        avatarUri: Uri?
    ): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("Missing user"))
            val avatarUrl = avatarUri?.let { uploadAvatar(uid, it) } ?: ""
            val user = User(
                uid = uid,
                username = email.substringBefore("@"),
                email = email.trim(),
                displayName = displayName.trim(),
                bio = bio.trim(),
                avatarUrl = avatarUrl
            )
            db.collection("users").document(uid).set(user).await()
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("Missing user"))
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun setPresence(status: String) {
        val uid = currentUserId ?: return
        try {
            db.collection("users").document(uid).update(
                mapOf("status" to status, "lastSeen" to clock.now())
            ).await()
        } catch (_: Exception) {
        }
    }

    suspend fun getCurrentUser(): User? {
        val uid = currentUserId
        if (uid == null) {
            return null
        }
        return db.collection("users").document(uid).get().await().toObject(User::class.java)
    }

    suspend fun getUser(uid: String): User? =
        db.collection("users").document(uid).get().await().toObject(User::class.java)

    suspend fun updateProfile(
        displayName: String,
        bio: String,
        avatarUri: Uri?
    ): Result<Unit> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            val updates = mutableMapOf<String, Any>(
                "displayName" to displayName.trim(),
                "bio" to bio.trim()
            )
            if (avatarUri != null) updates["avatarUrl"] = uploadAvatar(uid, avatarUri)
            db.collection("users").document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchUsers(query: String): Result<List<User>> {
        return try {
            if (query.isBlank()) {
                Result.success(emptyList())
            } else {
                val term = query.trim().lowercase()
                val current = currentUserId
                val users = db.collection("users").get().await()
                    .toObjects(User::class.java)
                    .filter { user ->
                        user.uid != current && (
                            user.username.lowercase().contains(term) ||
                            user.displayName.lowercase().contains(term) ||
                            user.email.lowercase().contains(term)
                        )
                    }
                    .take(20)
                Result.success(users)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addContact(contactUid: String): Result<Unit> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            db.collection("users").document(uid)
                .update("contacts", FieldValue.arrayUnion(contactUid)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeContact(contactUid: String): Result<Unit> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            db.collection("users").document(uid)
                .update("contacts", FieldValue.arrayRemove(contactUid)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getContacts(): Result<List<User>> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            val snapshot = db.collection("users").document(uid).get().await()
            val ids = (snapshot.get("contacts") as? List<*>)?.filterIsInstance<String>().orEmpty()
            val users = ids.mapNotNull { getUser(it) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---- Blocked users ----

    suspend fun blockUser(uidToBlock: String): Result<Unit> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            db.collection("users").document(uid)
                .update("blocked", FieldValue.arrayUnion(uidToBlock)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unblockUser(uidToUnblock: String): Result<Unit> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            db.collection("users").document(uid)
                .update("blocked", FieldValue.arrayRemove(uidToUnblock)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBlockedUsers(): Result<List<User>> {
        return try {
            val uid = currentUserId ?: return Result.failure(IllegalStateException("Not signed in"))
            val snapshot = db.collection("users").document(uid).get().await()
            val ids = (snapshot.get("blocked") as? List<*>)?.filterIsInstance<String>().orEmpty()
            val users = ids.mapNotNull { getUser(it) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isBlocked(blockerUid: String, targetUid: String): Boolean {
        return false // check at query time in ChatRepository
    }

    private suspend fun uploadAvatar(uid: String, uri: Uri): String {
        val ref = storage.reference.child("users/$uid/avatar.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }
}