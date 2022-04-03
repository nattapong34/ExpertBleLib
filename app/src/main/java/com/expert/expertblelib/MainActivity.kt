package com.expert.expertblelib

import android.Manifest
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import com.expert.expert_ble.ExpBle
import com.expert.expert_ble.TEMPHTD8808E

class MainActivity : AppCompatActivity() {

    private lateinit var m_ble: ExpBle
     var mTemp:TEMPHTD8808E= TEMPHTD8808E(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_ble = ExpBle(this)

        mTemp.checkAdapter()

        mTemp.clear()

        findViewById<Button>(R.id.bnDevice).apply{
            setOnClickListener{
                mTemp.scanLeDevice(null,tempCallback)
               // mTemp.scanLeDevice(mTemp.advertise_name,tempCallback)
            }
        }

    }

    private val tempCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            with(result.device) {


                if (result?.device != null && result.device.address != null  && result.device.name != null) {
                    Log.d("SCAN ", result.device.name.toString())
                    mTemp.add(result.device)
                }
           //     Log.d("DEVICE", mTemp.names.toString())
            }
        }
    }
}