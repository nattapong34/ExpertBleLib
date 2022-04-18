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
import com.expert.expert_ble.*
import java.util.*

class MainActivity : AppCompatActivity() ,TEMPHTD8808EListener,ACCUCHECKListener {

    private lateinit var m_ble: ExpBle
     var mTemp:TEMPHTD8808E= TEMPHTD8808E(this,this)
    var mAccu:ACCUCHECK= ACCUCHECK(this,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTemp.checkAdapter()

        findViewById<Button>(R.id.bnDevice).apply{
            setOnClickListener{
                mTemp.pairDevice()

            }
        }

        findViewById<Button>(R.id.bnAccu).apply{
            setOnClickListener{
                mAccu.pairDevice()

            }
        }

    }

    override fun tempReadName(name: String?) {
        val temp = findViewById<TextView>(R.id.txtTempName)
        temp.text=name!!
    }

    override fun tempReadValue(value: String?) {
            Log.d("VALUE FROM CALLBACK", value!!)
        val temp = findViewById<TextView>(R.id.txtTempVal)
        temp.text=value!!
    }

    override fun AccuReadName(name: String?) {
        val acc = findViewById<TextView>(R.id.txtAccuName)
        acc.text=name!!
    }

    override fun AccuReadValue(value: String?) {
        val acc = findViewById<TextView>(R.id.txtAccuVal)
        acc.text=value!!
    }


}