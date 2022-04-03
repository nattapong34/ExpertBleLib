package com.expert.expert_ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.lang.Math.abs
import java.util.*

class SATBLUEDOT(context: Context): ExpDevice(context) {
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
            }
    }
}