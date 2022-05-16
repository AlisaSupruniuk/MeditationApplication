package by.supruniuk.alisa.meditationapplication.ui.main.home

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import by.supruniuk.alisa.meditationapplication.MainActivity
import by.supruniuk.alisa.meditationapplication.NotificationReceiver
import by.supruniuk.alisa.meditationapplication.R
import by.supruniuk.alisa.meditationapplication.databinding.FragmentHomeBinding
import by.supruniuk.alisa.meditationapplication.models.ProfileModel
import by.supruniuk.alisa.meditationapplication.ui.main.meditation.BreathActivity
import by.supruniuk.alisa.meditationapplication.ui.main.meditation.MeditationActivity
import by.supruniuk.alisa.meditationapplication.ui.main.meditation.MeditationFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import loader.ImageLoader
import java.util.*

class HomeFragment : Fragment(), HomeView {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var calendar: Calendar
    private lateinit var picker: MaterialTimePicker
    private val homeSharedPref = HomeSharedPref()
    private val presenter = HomePresenter()

    private val imageLoader = ImageLoader()

    private val contentResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            presenter.updateProfileImage(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        val uid = FirebaseAuth.getInstance().uid
        homeSharedPref.init(requireContext(), uid!!)
        presenter.attachView(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        createNotificationChannel()
        alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager

        binding.startMeditation.setOnClickListener {
            goToMeditation()
        }
        binding.startBreathe.setOnClickListener {
            goToBreath()
        }
        binding.remind.setOnClickListener { setRemind() }
        binding.profile.addAvatarButton.setOnClickListener { contentResult.launch(INPUT_TYPE) }
        binding.logout.setOnClickListener { presenter.logout() }
        presenter.showUserContent()
        updateReportTexts()
        updateReportProgress()
    }

    private fun setRemind() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Reminder time")
            .build()

        activity?.let { it1 -> picker.show(it1.supportFragmentManager, "GENO") }

        picker.addOnPositiveButtonClickListener {
            cancelAlarm()
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            calendar.set(Calendar.MINUTE, picker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val intent = Intent(context, NotificationReceiver::class.java)

            val pendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this.context, 200, intent, 0)

            val alarmManager: AlarmManager =
                context?.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Toast.makeText(
                context,
                "Successfully set reminder at: ${calendar.get(Calendar.HOUR_OF_DAY)}:${
                    calendar.get(Calendar.MINUTE)
                } every day.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateReportTexts() {
        binding.report.valMeditateTimes.text = homeSharedPref.getMeditationCount().toString()
        binding.report.valMeditate.text = homeSharedPref.getMeditationMin().toString()
        binding.report.valBreatheTimes.text = homeSharedPref.getBreatheCount().toString()
        binding.report.valBreathe.text = homeSharedPref.getBreatheMin().toString()
    }

    private fun updateReportProgress() {
        val medMin = homeSharedPref.getMeditationMin()
        val breMin = homeSharedPref.getBreatheMin()
        val medCount = homeSharedPref.getMeditationCount()
        val breCount = homeSharedPref.getBreatheCount()
        var percentage = 1
        if (medCount > 0 && breCount > 0) {
            percentage = (medMin + breMin) * 100 / (medCount * 20) + (breCount * 3)
        }
        binding.report.statusBar.progress = percentage
        binding.report.valMeditateTimes.text = medCount.toString()
        binding.report.valMeditate.text = medMin.toString()
        binding.report.valBreatheTimes.text = breCount.toString()
        binding.report.valBreathe.text = breMin.toString()
    }

    override fun onResume() {
        super.onResume()
        updateReportProgress()
        updateReportTexts()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Meditation Channel"
            val description = "Channel for meditation remainder"

            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("NOTIFICATION", name, importance)
            channel.description = description

            val notificationManager: NotificationManager? =
                context?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun cancelAlarm() {
        val intent = Intent(context, MeditationActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 200, intent, 0)

        alarmManager.cancel(pendingIntent)
    }

    private fun goToMeditation() {
        startActivity(MainActivity.newIntent(requireContext(), 2))
    }

    private fun goToBreath() {
        startActivity(
            Intent(
                context,
                BreathActivity::class.java
            )
        )
    }

    override fun defaultImage() {
        TODO("Not yet implemented")
    }

    override fun showUserData(profileModel: ProfileModel) {
        with(binding.profile) {
            profileNameTextView.text = profileModel.name
            profileEmailTextView.text = profileModel.email
            if (profileModel.image != Uri.EMPTY) {
                imageLoader.load(profileModel.image.toString(), imageView)
            }
        }
    }

    override fun showProgressBar() {
        binding.profile.progressView.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.profile.progressView.visibility = View.GONE
    }

    companion object {
        const val INPUT_TYPE = "image/*"
    }
}
