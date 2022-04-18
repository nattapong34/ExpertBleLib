package com.expert.expert_ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import java.util.*


interface TEMPHTD8808EListener {
    fun tempReadName(name:String?)
    fun tempReadValue(value:String?) // pass any parameter in your onCallBack which you want to return
}
class TEMPHTD8808E(val listener:TEMPHTD8808EListener, context: Context): ExpDevice(context) {

    override var SERVICE_UUID: UUID =convertFromInteger(0xFFE0)
    override var CHAR_UUID: UUID =convertFromInteger(0xFFE1)
    override var advertise_name="HC-08"
//    private var listener : TEMPHTD8808EListener? = null


    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data = characteristic.value.decodeToString()
        values=data.substring(4,9)
        listener?.tempReadValue(values)
    }

    public fun pairDevice()
    {
        scanDevice(callbackScan)
    }

    private val callbackScan: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            with(result.device) {
                if (result?.device != null && result.device.address != null  && result.device.name != null) {
                    Log.d("SCAN ", result.device.name.toString())
//                    DEVICE = result.device
                    listener?.tempReadName(connect(result.device,connectCallback))
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
            gatt!!.setCharacteristicNotification(getCharacteristic(gatt), true)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)

            if (gatt != null) {
                if (characteristic != null) {
                    onCharacteristicRead(gatt,characteristic)
                    val d = Log.d("temp ", values)
                }
            }

        }
    }
}