package com.expert.expertblelib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.expert.expert_ble.TEMPHTD8808E

class MainActivity : AppCompatActivity() {

    var mTemp:TEMPHTD8808E= TEMPHTD8808E(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTemp.checkAdapter()

    }
}