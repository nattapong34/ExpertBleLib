package com.expert.expert_ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.TextView
import java.lang.Exception
import java.lang.Math.abs
import java.util.*
interface SATBLUEDOTEListener {
    fun bluedotReadName(name:String?)
    fun bluedotValue(value:String?) // pass any parameter in your onCallBack which you want to return
}
class SATBLUEDOT(val listener:SATBLUEDOTEListener, context: Context): ExpDevice(context) {
    override var ADV_UUID: UUID =convertFromInteger(0xFFB0)
    override var SERVICE_UUID: UUID =convertFromInteger(0xFFF0)
    override var CHAR_UUID: UUID=convertFromInteger(0xFFF1)
    override var advertise_name="HJ-Narigmed"
    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
            var data:ByteArray = characteristic.value
          // Log.e("onCharacteristicRead", "${abs(data[2].toInt()).toString()}")
            if (abs(data[2].toInt())==127)
            {
                Log.d("value ", "${data[3]}")
                values=data[3].toString()
                listener?.bluedotValue(values)
            }
    }

    public fun pairDevice()
    {
        scanDevice(callbackScan)
    }
    private val callbackScan: ScanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            with(result.device) {
                if (result?.device != null && result.device.address != null  && result.device.name != null) {
                    Log.d("SCAN ", result.device.name.toString())
//                    DEVICE = result.device
                    listener?.bluedotReadName(connect(result.device,connectCallback))
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
            }else if (newState==BluetoothAdapter.STATE_DISCONNECTED)
            {
                Log.d(DEVICE.name,"DISCONNECTED")
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            gatt!!.setCharacteristicNotification(getCharacteristic(gatt), true)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            try {
                if (gatt != null) {
                    if (characteristic != null) {
                        onCharacteristicRead(gatt, characteristic)
                    }
                }
            }catch (e: Exception)
            {
                Log.e("ERROR SAT",e.toString())
            }

        }
    }

}