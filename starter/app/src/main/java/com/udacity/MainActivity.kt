@file:Suppress("DEPRECATION")

package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var status: String
    private lateinit var downloadManager: DownloadManager

    private lateinit var url: String
    private lateinit var currentFileName: String


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        //Create a notification channel
        createChannel(getString(R.string.download_channel_id), getString(R.string.download_channel_name))

        notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager

        //Using Kotlin android extensions library to avoid using findViewById method
        custom_button.setOnClickListener {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected = activeNetwork?.isConnectedOrConnecting == true
            if (isConnected) {
                if (::url.isInitialized) {
                    custom_button.updateButtonState(ButtonState.Clicked)
                    download()
                } else {
                    Toast.makeText(applicationContext, getString(R.string.download_toast), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                checkDownloadStatus()
                notificationManager.sendNotification(currentFileName, status, applicationContext)
            }
        }
    }

    private fun download() {
        val request =
                DownloadManager.Request(Uri.parse(url))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun checkDownloadStatus() {

        val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
        if (cursor.moveToFirst()) {
            val statusId = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (statusId) {
                DownloadManager.STATUS_FAILED -> status = "FAILED"
                DownloadManager.STATUS_SUCCESSFUL -> status = "SUCCESS"
                else -> status = "IN PROGRESS"
            }
        }

    }

    companion object {
        private const val URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        //private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            //Is the button now checked
            val checked = view.isChecked
            //Check which radio button is clicked

            when (view.id) {
                R.id.radio_glide ->
                    if (checked) {
                        url = "https://github.com/bumptech/glide"
                        currentFileName = applicationContext.getString(R.string.glide_radio_string)
                    }
                R.id.radio_load_app ->
                    if (checked) {
                        url = URL
                        currentFileName = applicationContext.getString(R.string.load_app_radio_string)
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        url = "https://github.com/square/retrofit"
                        currentFileName = applicationContext.getString(R.string.retrofit_radio_string)
                    }
            }
        }

    }

    //Create a channel
    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                    channelId, channelName,
                    NotificationManager.IMPORTANCE_HIGH
            ).apply { setShowBadge(false) }

            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    //unregister the broadcast receiver
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
