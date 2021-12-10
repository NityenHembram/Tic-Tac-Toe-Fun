package com.nityen.tictactoefun

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import java.util.concurrent.Delayed

class SplashScrren : AppCompatActivity() {
    lateinit var img:ImageView
    lateinit var text:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_scrren)

        img = findViewById(R.id.imageView)
        text = findViewById(R.id.textView)

        val anim = AnimationUtils.loadAnimation(this,R.anim.my_animation)
        val animText = AnimationUtils.loadAnimation(this,R.anim.text_anim)

        animText.duration  = 1000

        anim.duration = 2000
        anim.repeatCount = 5

        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                MediaPlayer.create(this@SplashScrren,R.raw.start).start()


            }

            override fun onAnimationEnd(animation: Animation?) {
                text.visibility = View.VISIBLE
                animText.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val intent = Intent(this@SplashScrren,MainActivity::class.java)
                        val handel = Handler().postDelayed(Runnable {
                            startActivity(intent)
                            overridePendingTransition(R.anim.actvity_enter,R.anim.activity_exit)
                            finish()
                        },4000)



                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })
                text.startAnimation(animText)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

        img.startAnimation(anim)
    }
}