package by.supruniuk.alisa.meditationapplication.ui.main.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.supruniuk.alisa.meditationapplication.databinding.FragmentMeditationBinding
import by.supruniuk.alisa.meditationapplication.models.MeditationModel

class MeditationFragment : Fragment(), MeditationView {
    private lateinit var binding: FragmentMeditationBinding
    private val presenter = MeditationPresenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeditationBinding.inflate(layoutInflater)
        val view = binding.root
        presenter.attachView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        presenter.getMeditationList()
    }

    private fun initAdapter() {
        binding.listRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = MeditationAdapter { startMeditationActivity(it.url) }
        }
    }

    private fun startMeditationActivity(url: String) {
        startActivity(MeditationActivity.newIntent(requireContext(), url))
    }

    override fun showMeditationList(list: List<MeditationModel>) {
        (binding.listRecyclerView.adapter as MeditationAdapter).submitList(list)
    }

    override fun showErrorToast() {
        Toast.makeText(requireContext(), "Please check your network connection", Toast.LENGTH_LONG).show()
    }
}
