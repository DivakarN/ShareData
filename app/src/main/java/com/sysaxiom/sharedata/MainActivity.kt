package com.sysaxiom.sharedata

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when {
            intent.type == PLAIN_TEXT_MIME -> handleSendText(intent)
            intent.type?.startsWith(MEDIA_IMAGE_MIME) == true -> handleSendImage(intent)
            intent.type?.startsWith(MEDIA_Audio_MIME) == true -> handleSendAudio(intent)
        }

        button.setOnClickListener {
            shareImage(uri)
        }

    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            editText.setText(it)
        }
    }

    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            imageView.setImageURI(it)
            uri = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleSendAudio(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            editText.setText("Playing Audio")
            MediaPlayer().apply {
                setDataSource(applicationContext, it)
                prepare()
                start()
            }
        }
    }

    companion object {
        private const val PLAIN_TEXT_MIME = "text/plain"
        private const val MEDIA_IMAGE_MIME = "image/"
        private const val MEDIA_Audio_MIME = "audio/"
    }

    private fun shareImage(uri: Uri?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        if(!editText.text.isNullOrEmpty()){
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT,editText.text.toString())
        }
        if(uri != null){
            sharingIntent.type = "image/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(sharingIntent, "Share Data"))
    }

}


