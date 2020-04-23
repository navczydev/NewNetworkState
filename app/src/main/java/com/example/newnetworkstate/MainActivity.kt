package com.example.newnetworkstate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectivityLiveData = ConnectivityLiveData(application)
        connectivityLiveData.observe(this, Observer {
            it?.let {
                if (it) {
                    val snackbar: Snackbar = Snackbar
                        .make(parentLayout, "connected", Snackbar.LENGTH_LONG)
                    snackbar.setBackgroundTint(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_green_dark
                        )
                    )
                    snackbar.show()
                } else {
                    val snackbar: Snackbar = Snackbar
                        .make(parentLayout, "connection lost", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        })
    }
}
