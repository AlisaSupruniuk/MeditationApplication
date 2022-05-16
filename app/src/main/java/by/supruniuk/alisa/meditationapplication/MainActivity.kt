package by.supruniuk.alisa.meditationapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.supruniuk.alisa.meditationapplication.databinding.ActivityMainBinding
import by.supruniuk.alisa.meditationapplication.ui.main.meditation.MeditationFragment
import by.supruniuk.alisa.meditationapplication.ui.main.home.HomeFragment
import by.supruniuk.alisa.meditationapplication.ui.main.learn.LearnFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(LearnFragment(), "Learn")
        adapter.addFragment(MeditationFragment(), "Songs")

        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tabs, position ->
            when (position) {
                0 -> tabs.setIcon(R.drawable.home)
                1 -> tabs.setIcon(R.drawable.learn)
                2 -> tabs.setIcon(R.drawable.songs)
                else -> {}
            }
        }.attach()
        if (intent.hasExtra(ACTION)) {
            val action = intent.getIntExtra(ACTION, 0)
            if (action == 2) {
                binding.viewPager.currentItem = 2
//                val fragmentTransaction = this.supportFragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id., MeditationFragment())
//                fragmentTransaction.commit()
            }
        }
    }

    companion object {
        const val ACTION = "ACTION"
        @JvmStatic
        fun newIntent(context: Context, action: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(ACTION, action)
            }
        }
    }
}