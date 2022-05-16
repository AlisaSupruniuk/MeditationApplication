package by.supruniuk.alisa.meditationapplication.ui.main.learn

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.supruniuk.alisa.meditationapplication.databinding.FragmentLearnBinding

class LearnFragment : Fragment(), LearnView, MyAdapter.ItemClickListener {
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
        val adapter = MyAdapter(presenter.getData(), this)
        binding.recyclerView1.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(context, VideosViewActivity::class.java)
        intent.putExtra(PLAY_VIDEO, presenter.getData()[position])
        startActivity(intent)
    }
}