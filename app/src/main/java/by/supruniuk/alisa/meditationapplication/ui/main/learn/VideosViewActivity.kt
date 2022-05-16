package by.supruniuk.alisa.meditationapplication.ui.main.learn

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import by.supruniuk.alisa.meditationapplication.databinding.ActivityVideoBinding
import by.supruniuk.alisa.meditationapplication.models.Videos

const val PLAY_VIDEO = "play_video"

class VideosViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private var mediaController: MediaController? = null
    private lateinit var videos: Videos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videos = intent.getSerializableExtra(PLAY_VIDEO) as Videos

        configureVideoView(videos)

        binding.textVideoTitle.text = videos.title
        binding.textVideoDescription.text = videos.description
    }

    private fun configureVideoView(video: Videos) {
        binding.videoView.setVideoURI(Uri.parse(video.url))
        mediaController = MediaController(this)
        binding.videoView.setMediaController(mediaController)
        binding.videoView.requestFocus(0)
        binding.videoView.start()
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.stopPlayback()
        binding.videoView.resume()
    }
}