package com.expert.expert_ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import java.lang.Math.abs
import java.util.*

class MISCALE : ExpDevice{
    constructor(context: Context):super(context){}
    override var SERVICE_UUID: UUID =convertFromInteger(0x181b)
    override var CHAR_UUID: UUID =convertFromInteger(0x2a9c)
    override var advertise_name="MIBFS"
    var mCONFIG=convertFromInteger(0x2902)

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data  = characteristic.value
        Log.e("onCharacteristicRead", "${data.toHexString()}")
        Log.e("onCharacteristicRead", "${data[11]} ${data[12]}")
        var p11=when(data[11].toInt()<0){true->256+data[11].toInt()
        false->data[11].toInt()}
        var sum: Double = (p11 + abs((data[12].toInt())*256.0))/200.0
        Log.d("WEIGHT",sum.toString())
        values=String.format("%.2f",sum)
    }


}