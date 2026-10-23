package com.safranpulse.pulsechat.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.safranpulse.pulsechat.model.Call
import kotlinx.coroutines.tasks.await

class CallRepository {
    private val db = Firebase.firestore

    suspend fun createCall(calleeId: String, callerId: String): String {
        val call = Call(callerId = callerId, calleeId = calleeId, status = "ringing")
        val ref = db.collection("calls").add(call).await()
        return ref.id
    }

    suspend fun acceptCall(callId: String) {
        db.collection("calls").document(callId).update("status", "accepted").await()
    }

    suspend fun rejectCall(callId: String) {
        db.collection("calls").document(callId).update("status", "rejected").await()
    }

    suspend fun endCall(callId: String) {
        db.collection("calls").document(callId).update("status", "ended").await()
    }

    suspend fun sendOffer(callId: String, sdp: String) {
        db.collection("calls").document(callId).update("offer", sdp).await()
    }

    suspend fun sendAnswer(callId: String, sdp: String) {
        db.collection("calls").document(callId).update("answer", sdp).await()
    }

    suspend fun addIceCandidate(callId: String, candidate: String) {
        db.collection("calls").document(callId).update("iceCandidates", com.google.firebase.firestore.FieldValue.arrayUnion(candidate)).await()
    }

    fun listenToCall(callId: String, onCall: (Call?) -> Unit) {
        db.collection("calls").document(callId).addSnapshotListener { snapshot, _ ->
            onCall(snapshot?.toObject(Call::class.java)?.copy(id = snapshot.id))
        }
    }
}
