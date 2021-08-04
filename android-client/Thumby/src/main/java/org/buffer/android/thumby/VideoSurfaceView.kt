package org.buffer.android.thumby

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View.getDefaultSize


class VideoSurfaceView @kotlin.jvm.JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SurfaceView(context, attrs), MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Set video size.
     *
     * @see MediaPlayer.getVideoWidth
     * @see MediaPlayer.getVideoHeight
     */
    init {
        holder.addCallback(this)
    }

    fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        mVideoWidth = videoWidth
        mVideoHeight = videoHeight
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //Log.i("@@@@", "onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", "
        //        + MeasureSpec.toString(heightMeasureSpec) + ")");
        var width = getDefaultSize(mVideoWidth, widthMeasureSpec)
        var height = getDefaultSize(mVideoHeight, heightMeasureSpec)
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            val widthSpecMode: Int = MeasureSpec.getMode(widthMeasureSpec)
            val widthSpecSize: Int = MeasureSpec.getSize(widthMeasureSpec)
            val heightSpecMode: Int = MeasureSpec.getMode(heightMeasureSpec)
            val heightSpecSize: Int = MeasureSpec.getSize(heightMeasureSpec)
            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize
                height = heightSpecSize

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize
                height = width * mVideoHeight / mVideoWidth
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize
                width = height * mVideoWidth / mVideoHeight
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth
                height = mVideoHeight
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize
                    width = height * mVideoWidth / mVideoHeight
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize
                    height = width * mVideoHeight / mVideoWidth
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height)
    }
    fun start(){
        mediaPlayer?.start()
    }

    fun seekTo(milliseconds: Int) {
        mediaPlayer?.seekTo(milliseconds)
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun setDataSource(context: Context, uri: Uri) {
        initPlayer()
        mediaPlayer?.setDataSource(context, uri)
        prepare()
    }

    fun setDataSource( s: String) {
        initPlayer()
        mediaPlayer?.setDataSource(s)
        prepare()
    }

    private fun prepare() {
        mediaPlayer?.setOnVideoSizeChangedListener(this)
        mediaPlayer?.prepareAsync()
    }

    override fun onVideoSizeChanged(mp: MediaPlayer, width: Int, height: Int) {
        mVideoWidth = mp.getVideoWidth()
        mVideoHeight = mp.getVideoHeight()
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight)
            requestLayout()
        }
        seekTo(0)
    }

    private fun initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {

    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        mediaPlayer?.setSurface(p0?.surface)
    }
}