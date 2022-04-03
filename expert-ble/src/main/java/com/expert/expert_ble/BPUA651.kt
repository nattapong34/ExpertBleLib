package com.expert.expert_ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import java.util.*

class BPUA651: ExpDevice {
    constructor(context: Context):super(context){}
    override var SERVICE_UUID: UUID =convertFromInteger(0x1810)
    override var CHAR_UUID: UUID =convertFromInteger(0x2a35)
    override var advertise_name="A&D_UA-651BLE"
    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data:ByteArray = characteristic.value
         Log.e("onCharacteristicRead", "${data.decodeToString()}")
        var sys=data[1]
        var dia=data[3]
        var pul=data[7]
        values= "$sys,$dia,$pul"
    }
}