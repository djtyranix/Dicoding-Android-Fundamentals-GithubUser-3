package com.nixstudio.githubuser3.view.settings

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.databinding.SettingsActivityBinding
import com.nixstudio.githubuser3.dialog.TimePickerFragment
import com.nixstudio.githubuser3.model.ReminderModel
import com.nixstudio.githubuser3.model.UserPreferences
import com.nixstudio.githubuser3.services.AlarmReceiver
import com.nixstudio.githubuser3.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    companion object {
        const val TIME_PICKER_TAG = "TimePicker"
    }

    private lateinit var alarmReceiver: AlarmReceiver
    private var binding: SettingsActivityBinding? = null
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        alarmReceiver = AlarmReceiver()
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = dateFormat.format(calendar.time).toString()
        val message = resources.getString(R.string.app_comeback)
        val title = resources.getString(R.string.app_comeback_title)

        viewModel.setTimeString(timeString)

        alarmReceiver.setRepeatingAlarm(this@SettingsActivity, timeString, message, title)

    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private val viewModel: SettingsViewModel by activityViewModels()
        private lateinit var reminderModel: ReminderModel

        private var languageChange: Preference? = null
        private var reminderChange: Preference? = null
        private var turnOffReminder: Preference? = null
        private var doubleClicked: Boolean = false

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            languageChange = findPreference("select_language")
            reminderChange = findPreference("notification_switch")
            turnOffReminder = findPreference("notification_off")

            val languageIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            languageChange?.intent = languageIntent

            val mUserPreferences = activity?.let { UserPreferences(it) }

            if (mUserPreferences != null) {
                reminderModel = mUserPreferences.getReminder()
            }

            if (reminderModel.isReminderSet) {
                reminderChange?.summary = resources.getString(R.string.reminder_set, reminderModel.timeString)
                turnOffReminder?.isEnabled = true
            }

            val timePickerFragment = TimePickerFragment()

            reminderChange?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                timePickerFragment.show(parentFragmentManager, TIME_PICKER_TAG)

                viewModel.getTimeString().observe(this, { time ->
                    if (time != null) {
                        reminderModel = ReminderModel()
                        reminderChange?.summary = resources.getString(R.string.reminder_set, time)
                        val userPreferences = activity?.let { it1 -> UserPreferences(it1) }
                        reminderModel.isReminderSet = true
                        reminderModel.timeString = time

                        userPreferences?.setReminder(reminderModel)
                        turnOffReminder?.isEnabled = true
                    }
                })

                return@OnPreferenceClickListener(true)
            }

            turnOffReminder?.onPreferenceClickListener = Preference.OnPreferenceClickListener {

                if (doubleClicked) {
                    disableNotification()
                    return@OnPreferenceClickListener(true)
                }

                this.doubleClicked = true

                Toast.makeText(activity, resources.getString(R.string.notif_off_confirmation), Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    kotlin.run { doubleClicked = false }
                }, 2000)
            }
        }

        fun disableNotification() {
            activity?.let { AlarmReceiver().cancelAlarm(it) }

            reminderModel = ReminderModel()
            reminderChange?.summary = resources.getString(R.string.notification_summary)
            val userPreferences = activity?.let { it1 -> UserPreferences(it1) }
            reminderModel.isReminderSet = false
            reminderModel.timeString = "null"

            userPreferences?.setReminder(reminderModel)

            turnOffReminder?.isEnabled = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
