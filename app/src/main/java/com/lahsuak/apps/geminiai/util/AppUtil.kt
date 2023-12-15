package com.lahsuak.apps.geminiai.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.lahsuak.apps.geminiai.R
import java.util.Locale

@SuppressLint("QueryPermissionsNeeded")
fun Context.speakToAdd(speakLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    intent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    if (intent.resolveActivity(packageManager) != null) {
        speakLauncher.launch(intent)
    } else {
        Toast.makeText(this,
            getString(R.string.speech_not_support)
        ,Toast.LENGTH_SHORT).show()
    }
}