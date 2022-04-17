package com.expert.expertblelib

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import com.expert.expert_ble.ExpBle
import com.expert.expert_ble.TEMPHTD8808E
import com.expert.expert_ble.TEMPHTD8808EListener
import java.util.*

class MainActivity : AppCompatActivity() ,TEMPHTD8808EListener {

    private lateinit var m_ble: ExpBle
     var mTemp:TEMPHTD8808E= TEMPHTD8808E(this,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_ble = ExpBle(this)

        mTemp.checkAdapter()

        mTemp.clear()

        findViewById<Button>(R.id.bnDevice).apply{
            setOnClickListener{
//                mTemp.scanLeDevice(null,tempCallback)
//                mTemp.scanDevice(tempCallback)
//                mTemp.scanLeDevice(mTemp.advertise_name,tempCallback)
                mTemp.pairDevice()

            }
        }

    }

    override fun onCallBack(value: String?) {
            Log.d("VALUE FROM CALLBACK", value!!)
        val temp = findViewById<TextView>(R.id.txtTempVal)
        temp.text=value!!
    }


}