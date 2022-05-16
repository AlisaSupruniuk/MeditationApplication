package by.supruniuk.alisa.meditationapplication.ui.main.meditation

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.supruniuk.alisa.meditationapplication.R
import by.supruniuk.alisa.meditationapplication.databinding.ActivityMeditationBinding
import by.supruniuk.alisa.meditationapplication.ui.main.home.HomeSharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException
import java.util.concurrent.TimeUnit

class MeditationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeditationBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var isFullscreen: Boolean = false
    private lateinit var values: Array<String>
    private var minutes = 20L
    private var startMin = 20L
    private lateinit var timer: CountDownTimer
    private val sharedPref = HomeSharedPref()
    lateinit var currentUser: FirebaseUser
    private var isRunning: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true

        val audio = intent.getStringExtra(URL)

        currentUser = FirebaseAuth.getInstance().currentUser!!
        val uid = FirebaseAuth.getInstance().uid
        sharedPref.init(applicationContext, uid!!)

        values = resources.getStringArray(R.array.minutes);
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, values)
        binding.spinner.adapter = adapter;
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                minutes = if (position == 0) 20L else 10L
                startMin = minutes
                binding.indicator.text = "$minutes:00"
                timer.cancel()
                timer = createTimer()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


            //mediaPlayer.start()
//        } catch (exception: IOException) {
//            Toast.makeText(applicationContext, "Something went wrong...", Toast.LENGTH_LONG).show()
//        }
        timer = createTimer()

        binding.close.setOnClickListener {
            showDialog(this)
        }

        binding.start.setOnClickListener {
            if (!isRunning) initMediaPlayer(audio ?: "")
            if (mediaPlayer.isPlaying){
                timer.cancel()
                mediaPlayer.stop()
                binding.start.text = "Play"
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer.setDataSource(audio)
               // mediaPlayer.prepare()
                timer.start()
                mediaPlayer.start()
                isRunning = true
                binding.start.text = "Stop"
            }
        }

        binding.sound.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                binding.sound.setImageResource(R.drawable.sound_no)
                mediaPlayer.setVolume(0f, 0f)
            } else {
                binding.sound.setImageResource(R.drawable.sound)
                mediaPlayer.setVolume(100f, 100f)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        timer.cancel()
        if (isRunning) {
            sharedPref.updateMeditationMinutes((startMin - minutes).toInt())
            sharedPref.updateMeditationCount()
        }
    }

    private  fun initMediaPlayer(url: String){
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(applicationContext, Uri.parse(url))
        //    prepare()
            start()
        }
    }

    override fun onBackPressed() {
        showDialog(this)
    }

    private fun createTimer(): CountDownTimer {
        return object : CountDownTimer(minutes * 60000, 1000) {
            var sec = 0L
            override fun onTick(ms: Long) {
                minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(ms)
                )
                sec = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(ms)
                )
                binding.indicator.text =
                    "${minutes.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
            }

            override fun onFinish() {
                sharedPref.updateMeditationMinutes(20)
                sharedPref.updateMeditationCount()
            }
        }
    }

    private fun showDialog(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Do you want to stop meditating ?").setCancelable(true)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            finish()
        })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val alert = builder.create()
        alert.setTitle("Are you sure")
        alert.show()
    }

    companion object {
        const val URL = "audio_url"

        @JvmStatic
        fun newIntent(context: Context, url: String): Intent =
            Intent(context, MeditationActivity::class.java).apply {
                this.putExtra(URL, url)
            }
    }
}