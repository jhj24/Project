package com.zgdj.project.ui.work.jkjc

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.glide
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_video_monitor.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch

class VideoMonitorActivity : DefaultTopBarActivity() {

    var isPause = false

    override val title: String
        get() = intent.getStringExtra(Config.TITLE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_monitor)

        GlobalScope.launch(Dispatchers.Main) {
            speed()
        }
        touchEvent()
        val uri = Uri.parse("android.resource://com.zgdj.project/" + R.raw.video)
        video_view.setVideoURI(uri);
        //video_view.setMediaController(MediaController(this))
        video_view.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                video_view.start()
            }
        });
        video_view.start()

        btn_play.onClick {
            if (video_view.isPlaying) {
                btn_play.setImageResource(R.mipmap.play)
                video_view.pause()
            } else {
                btn_play.setImageResource(R.mipmap.stop)
                video_view.start()
            }
        }

        var isSound = true
        btn_sound.onClick {
            if (isSound) {
                isSound = false
                btn_sound.setImageResource(R.mipmap.sound_minus)
            } else {
                isSound = true
                btn_sound.setImageResource(R.mipmap.sound_plus)
            }
        }

        if ("叠梁门桥头" == title) {
            video_view.visibility = View.VISIBLE
            image_view.visibility = View.GONE
        } else {
            video_view.visibility = View.GONE
            image_view.visibility = View.VISIBLE
            when (title) {
                "5#泵组上方" -> image_view.glide(R.mipmap.bzsf)
                "厂房中控室" -> image_view.glide(R.mipmap.cfzks)
                "叠梁门桥头" -> image_view.glide(R.mipmap.qlmqt)
                "水闸上游左岸" -> image_view.glide(R.mipmap.szzasy)
                "水闸下游全景" -> image_view.glide(R.mipmap.szsyqj)
                "水闸下游左岸" -> image_view.glide(R.mipmap.szxyza)
            }
        }

    }

    suspend fun speed() {
        if (isPause) return
        delay((500..2000).random().toLong())
        text_flow_rate.text = if (video_view.isPlaying) "${(50..300).random()}k/s" else "0k/s"
        speed()
    }


    private fun touchEvent() {
        iv_previous.onClick { }
        iv_talk.onClick { }
        iv_video.onClick { }
        ptz_left_btn.onTouch { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_left_sel)
            } else {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_bg)
            }
        }
        ptz_top_btn.onTouch { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_up_sel)
            } else {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_bg)
            }
        }

        ptz_right_btn.onTouch { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_right_sel)
            } else {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_bg)
            }
        }
        ptz_bottom_btn.onTouch { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_bottom_sel)
            } else {
                ptz_control_ly.setBackgroundResource(R.mipmap.ptz_bg)
            }
        }
    }

    override fun onPause() {
        isPause = true
        super.onPause()
    }

}