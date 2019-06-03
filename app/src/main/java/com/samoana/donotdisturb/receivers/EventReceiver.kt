package com.samoana.donotdisturb.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.media.AudioManager



class EventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("EventReceiver", "Broadcast message received")
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = intent.getIntExtra("mode", AudioManager.RINGER_MODE_VIBRATE)
    }

}