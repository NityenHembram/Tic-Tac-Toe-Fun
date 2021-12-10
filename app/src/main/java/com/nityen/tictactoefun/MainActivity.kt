package com.nityen.tictactoefun


import android.content.Context
import android.content.SharedPreferences
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources

import android.media.AudioAttributes
import android.media.AudioManager



class MainActivity : AppCompatActivity(),View.OnClickListener{

    lateinit var img_1:ImageView
    lateinit var img_2:ImageView
    lateinit var img_3:ImageView
    lateinit var img_4:ImageView
    lateinit var img_5:ImageView
    lateinit var img_6:ImageView
    lateinit var img_7:ImageView
    lateinit var img_8:ImageView
    lateinit var img_9:ImageView

    private lateinit var reset_btn:Button
    lateinit var score_x:TextView
    lateinit var score_O:TextView
    lateinit var turn:TextView


   private lateinit var play:SoundPool
   var restartSound = 0
    var xTap = 0
    var oTap = 0
    var win = 0
    var gameOver = 0



    var xCount = 0
    var oCount = 0
    var ie = false
    private lateinit var save:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor
//    val play = MediaPlayer()

    lateinit var audioManger:AudioManager
    var volume:Float= 0F




    private var gameActive = false
    private var player = 1
    var state = arrayOf(2,2,2,2,2,2,2,2,2)
    private var winGame = arrayOf(arrayOf(0,1,2), arrayOf(3,4,5), arrayOf(6,7,8),
                                    arrayOf(0,3,6), arrayOf(1,4,7), arrayOf(2,5,8)
                                    , arrayOf(0,4,8), arrayOf(2,4,6))



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        save = getSharedPreferences("save",MODE_PRIVATE)
        initializing()
        save = getSharedPreferences("name", MODE_PRIVATE)
        editor= save.edit()

        xCount = save.getInt("xScore",0)
        oCount = save.getInt("oScore",0)
        score_x.text = xCount.toString()
        score_O.text = oCount.toString()

        audioManger = getSystemService(Context.AUDIO_SERVICE)  as AudioManager
        val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(
                AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        volume = audioManger.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()/audioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        play = SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build()
        xTap = play.load(this,R.raw.pop3,1)
        oTap = play.load(this,R.raw.pop1,1)
        win = play.load(this,R.raw.win,1)
        restartSound = play.load(this,R.raw.round_end,1)
        gameOver = play.load(this,R.raw.game_over,1)

    }



    private fun initializing() {
        img_1 = findViewById(R.id.img_1)
        img_2 = findViewById(R.id.img_2)
        img_3 = findViewById(R.id.img_3)
        img_4 = findViewById(R.id.img_4)
        img_5 = findViewById(R.id.img_5)
        img_6 = findViewById(R.id.img_6)
        img_7 = findViewById(R.id.img_7)
        img_8 = findViewById(R.id.img_8)
        img_9 = findViewById(R.id.img_9)
        reset_btn = findViewById(R.id.reset_btn)

        turn = findViewById(R.id.xTurn)


        setListener()


        score_O = findViewById(R.id.score_O)
        score_x = findViewById(R.id.score_X)
    }
    private fun setListener() {
        img_1.setOnClickListener(this)
        img_2.setOnClickListener(this)
        img_3.setOnClickListener(this)
        img_4.setOnClickListener(this)
        img_5.setOnClickListener(this)
        img_6.setOnClickListener(this)
        img_7.setOnClickListener(this)
        img_8.setOnClickListener(this)
        img_9.setOnClickListener(this)

//        reset_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        val img = v as ImageView
        val tappedImage  = Integer.parseInt(img.tag.toString())
        volume = audioManger.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()/audioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        if(state[tappedImage] == 2){
            player = if(player == 1){
                play.play(xTap,volume,volume,1,0,1F)
                img.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.x))
                0
            }else{
                play.play(oTap,volume,volume,1,0,1F)
                img.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.o))
                1
            }
//            editor.commit()
            if(player == 0){
                turn.text = "O"
            }else{
                turn.text = "X"
            }
            state[tappedImage] = player
            checkWinner()
        }
    }

    private fun checkWinner():Boolean {
        volume = audioManger.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()/audioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val editor:SharedPreferences.Editor = save.edit()
        for (i in winGame){
            if (state[i[0]] == state[i[1]] && state[i[1]] == state[i[2]] && state[i[0]] != 2){
                if (state[i[0]] == 1){
                    play.play(win,volume,volume,1,0,1F)

                    showMeDialog("O")
                    oCount = save.getInt("oScore",0)
                    oCount++

                    score_O.text = oCount.toString()
                    editor.putInt("oScore",oCount)
                    gameActive = true
                    ie = true
                }else{
                    play.play(win,volume,volume,1,0,1F)
                    showMeDialog("X")
                    xCount = save.getInt("xScore",0)
                    xCount++
                    score_x.text = xCount.toString()

                    editor.putInt("xScore",xCount)
                    gameActive = true
                    ie = true
                }
            }

            editor.apply()

        }

        if(!gameActive){
            volume = audioManger.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()/audioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            if(state[0] != 2 && state[1] != 2 && state[2] != 2
                && state[3] != 2 && state[4] != 2 && state[5] != 2
                && state[6] != 2 && state[7] != 2 && state[8] != 2){
                play.play(gameOver,volume,volume,1,0,1F)
                val dialog = AlertDialog.Builder(this).setMessage("No one Wins\ndo you want to play again")
                    .setCancelable(false).setPositiveButton("Ok"){dialog,which ->

                        resetGame()
                    }
                dialog.show()
            }
        }

        return ie
    }

    private fun showMeDialog(s: String) {
        val dialog = AlertDialog.Builder(this).setMessage("Player $s won")
            .setCancelable(false).setPositiveButton("Ok"){dialog,which ->
                resetGame()
            }
        dialog.show()
    }

    private fun resetGame() {

        volume = audioManger.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()/audioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        img_1.setImageDrawable(null)
        img_2.setImageDrawable(null)
        img_3.setImageDrawable(null)
        img_4.setImageDrawable(null)
        img_5.setImageDrawable(null)
        img_6.setImageDrawable(null)
        img_7.setImageDrawable(null)
        img_8.setImageDrawable(null)
        img_9.setImageDrawable(null)

        turn.text = "X"

        gameActive  = false
        player = 1
        for (i in state.indices){
            state[i] = 2
        }

        if(checkWinner()){
            play.play(restartSound,volume,volume,1,0,1F)
            ie = false
        }


    }

    private fun resetScore() {
        editor.clear()
        editor.apply()
        score_x.text = save.getInt("xScore",0).toString()
        score_O.text = save.getInt("oScore",0).toString()
        turn.text = "X"

    }

    fun resetbtn(view: android.view.View) {
        resetGame()
        resetScore()
    }
}