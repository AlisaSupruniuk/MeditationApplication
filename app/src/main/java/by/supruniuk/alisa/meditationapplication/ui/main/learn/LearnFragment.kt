package by.supruniuk.alisa.meditationapplication.ui.main.learn

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.supruniuk.alisa.meditationapplication.databinding.FragmentLearnBinding
import by.supruniuk.alisa.meditationapplication.models.VideoModel

class LearnFragment : Fragment(), LearnView {
    lateinit var binding: FragmentLearnBinding
    private val presenter = LearnPresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        binding.recyclerView1.layoutManager = LinearLayoutManager(context)
        val adapter = LearnAdapter { onItemClick(it) }
        adapter.submitList(presenter.getData())
        binding.recyclerView1.adapter = adapter
    }

    private fun onItemClick(item: VideoModel) {
        val intent = Intent(context, VideosViewActivity::class.java)
        intent.putExtra(PLAY_VIDEO, item)
        startActivity(intent)
    }
}