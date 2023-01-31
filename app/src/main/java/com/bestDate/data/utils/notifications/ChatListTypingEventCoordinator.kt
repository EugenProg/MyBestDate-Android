package com.bestDate.data.utils.notifications

import com.bestDate.data.extension.orZero
import java.util.Timer
import java.util.TimerTask

class ChatListTypingEventCoordinator(private var offAction: (senderId: Int?) -> Unit) {
    private var typingList: HashMap<Int, PingEventListener> = hashMapOf()

    fun setTypingEvent(senderId: Int?) {
        if (typingList[senderId] == null) {
            typingList[senderId.orZero] =
                PingEventListener { typingFinishAction(senderId) }
        } else {
            typingList[senderId]?.pingAction()
        }
    }

    private fun typingFinishAction(senderId: Int?) {
        offAction.invoke(senderId)
        typingList.remove(senderId)
    }
}

class PingEventListener(var offAction: () -> Unit) {
    private var lastUpdate: Long = System.currentTimeMillis()
    private var timer: Timer = Timer()
    private var task: TimerTask = object : TimerTask() {
        override fun run() {
            if (System.currentTimeMillis() - lastUpdate >= 3000) {
                offAction.invoke()
                timer.cancel()
            }
        }
    }

    init {
        timer.scheduleAtFixedRate(task, 0, 1000)
    }

    fun pingAction() {
        lastUpdate = System.currentTimeMillis()
    }
}