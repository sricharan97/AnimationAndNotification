package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var fileName: String
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("fileName")) {
                fileName = bundle.getString("fileName")!!
            }

            if (bundle.containsKey("status")) {
                status = bundle.getString("status")!!
            }
        }

        filename_text.text = fileName
        status_text.text = status

        //navigate to main screen on click of button
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


}
