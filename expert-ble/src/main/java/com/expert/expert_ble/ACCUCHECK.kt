package com.expert.expert_ble

import android.R
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import java.util.*

interface ACCUCHECKListener {
    fun AccuReadName(name:String?)
    fun AccuReadValue(value:String?) // pass any parameter in your onCallBack which you want to return
}
class ACCUCHECK(val listener:ACCUCHECKListener, context: Context) : ExpDevice(context) {
    override var ADV_UUID: UUID =convertFromInteger(0x1808)
    override var SERVICE_UUID: UUID =convertFromInteger(0x1808)
    override var CHAR_UUID: UUID =convertFromInteger(0x2a18)
    override var advertise_name="HJ-Narigmed"
    override var MSMCONTEXT:UUID =convertFromInteger(0x2a34)
    override var FEATURE:UUID =convertFromInteger(0x2a51)
    override var RECCACCESS:UUID =convertFromInteger(0x2a52)
    private var LASTREC: ByteArray = ByteArray(0x0106)

    public fun pairDevice()
    {
        scanDevice(callbackScan)
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data:ByteArray = characteristic.value
       // Log.e("onCharacteristicRead", "${data.toHexString()}")
        var fbs=data[12]
        Log.d("FBS",fbs.toString())
        values= fbs.toString()
        listener?.AccuReadValue(values)
    }
    private val callbackScan: ScanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            with(result.device) {
                if (result?.device != null && result.device.address != null  && result.device.name != null) {
                    Log.d("SCAN ", result.device.name.toString())
//                    DEVICE = result.device
                    listener?.AccuReadName(connect(result.device,connectCallback))
                }
                //     Log.d("DEVICE", mTemp.names.toString())
            }
        }
    }
    private val connectCallback: BluetoothGattCallback = object: BluetoothGattCallback(){
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState== BluetoothAdapter.STATE_CONNECTED)
            {
                gatt!!.discoverServices()
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (!gatt!!.setCharacteristicNotification(getCharacteristic(gatt), true)) {
                Log.e("NOTIFY FIELD", "characteristic")
            }else
            {
                var char = getCharacteristicRECACCESS(gatt)
                Log.d("ACCESS", char.toString())
                char!!.value =LASTREC
                if (gatt!!.writeCharacteristic(char)) {
                    Log.d("Write ", "characteristic lastrec")
                }
            }

        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            if (gatt != null) {
                if (characteristic != null) {
                    onCharacteristicRead(gatt,characteristic)
                    Log.d("accu ", values)
                }
            }

        }
    }


}