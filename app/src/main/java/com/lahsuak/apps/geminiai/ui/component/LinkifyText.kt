package com.lahsuak.apps.geminiai.ui.component

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.util.LinkifyCompat

@Composable
fun LinkifyText(
    text: AnnotatedString?, color: Color,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val customLinkifyTextView = remember {
        TextView(context)
    }
    AndroidView(modifier = modifier, factory = { customLinkifyTextView }) { textView ->
        textView.text = text ?: ""

        textView.setTextColor(color.hashCode())
        textView.setLinkTextColor(Color.Blue.hashCode())
        LinkifyCompat.addLinks(textView, Linkify.ALL)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}