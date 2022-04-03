package com.expert.expert_ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import java.util.*

class ACCUCHECK(context: Context) : ExpDevice(context) {
    override var SERVICE_UUID: UUID =convertFromInteger(0x1808)
    override var CHAR_UUID: UUID =convertFromInteger(0x2a18)
    override var advertise_name="HJ-Narigmed"
    override var MSMCONTEXT:UUID =convertFromInteger(0x2a34)
    override var FEATURE:UUID =convertFromInteger(0x2a51)
    override var RECCACCESS:UUID =convertFromInteger(0x2a52)
    public var LASTREC: ByteArray = ByteArray(0x0106)

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data:ByteArray = characteristic.value
        Log.e("onCharacteristicRead", "${data.toHexString()}")
        var fbs=data[12]
        Log.d("FBS",fbs.toString())
        values= fbs.toString()
    }

}