package com.lahsuak.apps.geminiai.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.lahsuak.apps.geminiai.R
import java.util.Locale

fun Context.setClipboard(text: String) {
    val clipboard =
        getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copy", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, this.getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
}

fun Context.textToSpeech(inputText: String) {
    var textToSpeech: TextToSpeech? = null
    textToSpeech = TextToSpeech(this) { i ->
        // if No error is found then only it will run
        if (i != TextToSpeech.ERROR) {
            // To Choose language of speech
            textToSpeech?.setLanguage(Locale.getDefault())
        }
    }
    textToSpeech.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, null)
}

fun Context.shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.share))
    intent.putExtra(Intent.EXTRA_TEXT, text)
    this.startActivity(
        Intent.createChooser(
            intent,
            this.getString(R.string.share_by)
        )
    )
}

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
        Toast.makeText(
            this,
            getString(R.string.speech_not_support), Toast.LENGTH_SHORT
        ).show()
    }
}