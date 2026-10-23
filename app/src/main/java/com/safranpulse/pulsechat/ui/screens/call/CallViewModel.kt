package com.safranpulse.pulsechat.ui.screens.call

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.safranpulse.pulsechat.data.CallRepository
import com.safranpulse.pulsechat.model.Call
import com.safranpulse.pulsechat.webrtc.WebRtcManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

@HiltViewModel
class CallViewModel @Inject constructor(
    private val repository: CallRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    var call by mutableStateOf<Call?>(null)
    var callId by mutableStateOf("")

    val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private val manager = WebRtcManager(context)
    private var remoteOfferSet = false
    private var remoteAnswerSet = false
    private val appliedIce = mutableSetOf<String>()

    init { manager.init() }

    fun startCall(calleeId: String, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val id = repository.createCall(calleeId, currentUserId)
            callId = id
            manager.createPeerConnection()
            manager.onLocalSdp = { sdp ->
                viewModelScope.launch { repository.sendOffer(id, sdp.description) }
            }
            manager.onIceCandidate = { candidate ->
                viewModelScope.launch { repository.addIceCandidate(id, serialize(candidate)) }
            }
            manager.createOffer()
            listen(id)
            onCreated(id)
        }
    }

    fun listen(callId: String) {
        this.callId = callId
        repository.listenToCall(callId) { updated ->
            call = updated ?: return@listenToCall
            val c = updated
            if (c.callerId != currentUserId && c.offer.isNotBlank() && !remoteOfferSet) {
                remoteOfferSet = true
                manager.createPeerConnection()
                manager.onLocalSdp = { sdp ->
                    viewModelScope.launch { repository.sendAnswer(callId, sdp.description) }
                }
                manager.onIceCandidate = { candidate ->
                    viewModelScope.launch { repository.addIceCandidate(callId, serialize(candidate)) }
                }
                manager.setRemoteDescription(SessionDescription(SessionDescription.Type.OFFER, c.offer))
                manager.createAnswer()
            }
            if (c.callerId == currentUserId && c.answer.isNotBlank() && !remoteAnswerSet) {
                remoteAnswerSet = true
                manager.setRemoteDescription(SessionDescription(SessionDescription.Type.ANSWER, c.answer))
            }
            c.iceCandidates.forEach { candidate ->
                if (candidate !in appliedIce) {
                    appliedIce.add(candidate)
                    parse(candidate)?.let { manager.addIceCandidate(it) }
                }
            }
        }
    }

    fun accept() {
        viewModelScope.launch { if (callId.isNotBlank()) repository.acceptCall(callId) }
    }

    fun reject() {
        viewModelScope.launch { if (callId.isNotBlank()) repository.rejectCall(callId) }
    }

    fun end() {
        viewModelScope.launch {
            if (callId.isNotBlank()) repository.endCall(callId)
            manager.close()
        }
    }

    override fun onCleared() {
        super.onCleared()
        manager.close()
    }

    private fun serialize(candidate: IceCandidate): String =
        "${candidate.sdpMid}|${candidate.sdpMLineIndex}|${candidate.sdp}"

    private fun parse(value: String): IceCandidate? {
        val parts = value.split("|", limit = 3)
        if (parts.size != 3) return null
        return IceCandidate(parts[0], parts[1].toIntOrNull() ?: 0, parts[2])
    }
}
