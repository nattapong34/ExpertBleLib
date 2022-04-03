package com.expert.expert_ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import java.util.*

class TEMPHTD8808E(context: Context): ExpDevice(context) {
    override var SERVICE_UUID: UUID =convertFromInteger(0xFFE0)
    override var CHAR_UUID: UUID =convertFromInteger(0xFFE1)
    override var advertise_name="HC-08"
    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data = characteristic.value.decodeToString()
         Log.e("onCharacteristicRead", "${data}")
        values=data.substring(4,9)
    }
}