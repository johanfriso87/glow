package org.buffer.android.thumby

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_thumby.*
import org.buffer.android.thumby.listener.SeekListener

class ThumbyActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_THUMBNAIL_POSITION = "org.buffer.android.thumby.EXTRA_THUMBNAIL_POSITION"
        const val EXTRA_URI = "org.buffer.android.thumby.EXTRA_URI"

        fun getStartIntent(context: Context, uri: Uri, thumbnailPosition: Long = 0): Intent {
            val intent = Intent(context, ThumbyActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            intent.putExtra(EXTRA_THUMBNAIL_POSITION, thumbnailPosition)
            return intent
        }
    }

    private lateinit var videoUri: Uri

    private lateinit var cancel: TextView
    private lateinit var done: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumby)
        title = getString(R.string.picker_title)

//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        videoUri = intent.getParcelableExtra(EXTRA_URI) as Uri

        cancel = findViewById(R.id.cancel)
        done = findViewById(R.id.done)

        cancel.setOnClickListener {
            finish()
        }
        done.setOnClickListener {
            finishWithData()
        }

        setupVideoContent()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.thumbnail, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                true
//            }
//            R.id.action_menu_done -> {
//                finishWithData()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun setupVideoContent() {
        var s = getRealPathFromURI(videoUri)
        view_thumbnail.setDataSource(s.toString())
//        view_thumbnail.setVideoPath(s)
//        view_thumbnail.start()
        thumbs.seekListener = seekListener
        thumbs.currentSeekPosition = intent.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0).toFloat()
        thumbs.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                thumbs.viewTreeObserver.removeOnGlobalLayoutListener(this)
                thumbs.uri = videoUri
            }
        })
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra(EXTRA_THUMBNAIL_POSITION,
                ((view_thumbnail.getDuration() / 100) * thumbs.currentProgress).toLong() * 1000)
        intent.putExtra(EXTRA_URI, videoUri)
        setResult(RESULT_OK, intent)
        finish()
    }

    private val seekListener = object : SeekListener {
        override fun onVideoSeeked(percentage: Double) {
            val duration = view_thumbnail.getDuration()
            view_thumbnail.seekTo((percentage.toInt() * view_thumbnail.getDuration()) / 100)
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}