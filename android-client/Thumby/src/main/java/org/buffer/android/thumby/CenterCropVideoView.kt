package org.buffer.android.thumby

import android.app.Activity
import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView

class CenterCropVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextureView(context, attrs), TextureView.SurfaceTextureListener {

    private var mediaPlayer: MediaPlayer? = null

    private var videoHeight = 0f
    private var videoWidth = 0f
    private var videoSizeDivisor = 1

    init {
        surfaceTextureListener = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSurfaceTextureSizeChanged(
        surface: SurfaceTexture?,
        width: Int,
        height: Int
    ) { }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) { }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean  = false

    override fun onSurfaceTextureAvailable(
        surface: SurfaceTexture?,
        width: Int,
        height: Int
    ) {
        mediaPlayer?.setSurface(Surface(surfaceTexture))
    }

    fun setDataSource(context: Context, uri: Uri, videoSizeDivisor: Int = 1) {
        this.videoSizeDivisor = videoSizeDivisor
        initPlayer()
        mediaPlayer?.setDataSource(context, uri)
        prepare()
    }

    fun seekTo(milliseconds: Int) {
        mediaPlayer?.seekTo(milliseconds)
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    private fun prepare() {
        mediaPlayer?.setOnVideoSizeChangedListener { _, width, height ->
            videoWidth = width.toFloat()
            videoHeight = height.toFloat()

//            setFitToFillAspectRatio(mediaPlayer, width, height)

//            layoutParams.width = (videoWidth.roundToInt()/1.25).roundToInt()
//            layoutParams.height= videoHeight.roundToInt()
//            videoWidth = width.toFloat() / videoSizeDivisor
//            videoHeight = height.toFloat()  / videoSizeDivisor
//            updateTextureViewSize()
            seekTo(0)
        }
        mediaPlayer?.prepareAsync()
    }

    private fun setFitToFillAspectRatio(mp: MediaPlayer?, videoWidth: Int, videoHeight: Int) {
        if (mp != null) {
            val screenWidth: Int = (context as Activity).windowManager.defaultDisplay.width
            val screenHeight: Int = (context as Activity).windowManager.defaultDisplay.height
            val videoParams = layoutParams
            if (videoWidth > videoHeight) {
                videoParams.width = screenWidth
                videoParams.height = screenWidth * videoHeight / videoWidth
            } else {
                videoParams.width = screenHeight * videoWidth / videoHeight
                videoParams.height = screenHeight
            }
            layoutParams = videoParams
        }
    }

    private fun updateTextureViewSize() {
        var scaleX = 0.75f
        var scaleY = 1.00f

//        if (videoWidth != videoHeight) {
//            if (videoWidth > width && videoHeight > height) {
//                scaleX = videoWidth / width
//                scaleY = videoHeight / height
//            } else if (videoWidth < width && videoHeight < height) {
//                scaleY = width / videoWidth
//                scaleX = height / videoHeight
//            } else if (width > videoWidth) {
//                scaleY = width / videoWidth / (height / videoHeight)
//            } else if (height > videoHeight) {
//                scaleX = height / videoHeight / (width / videoWidth)
//            }
//        }

        val matrix = Matrix().apply {
            setScale(scaleX, scaleY, (width / 2).toFloat(), (height / 2).toFloat())
        }

        setTransform(matrix)
    }

    private fun initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }
}