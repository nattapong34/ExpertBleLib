package com.expert.expert_ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.TextView
import java.util.*
interface BPUA651Listener {
    fun bpReadName(name:String?)
    fun bpReadValue(value:String?) // pass any parameter in your onCallBack which you want to return
}
class BPUA651(val listener:BPUA651Listener, context: Context): ExpDevice(context) {

    override var ADV_UUID: UUID =convertFromInteger(0x1810)
    override var SERVICE_UUID: UUID =convertFromInteger(0x1810)
    override var CHAR_UUID: UUID =convertFromInteger(0x2a35)
    override var advertise_name="A&D_UA-651BLE"
    var mCONFIG=convertFromInteger(0x2902)

    @SuppressLint("MissingPermission")
    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        var data:ByteArray = characteristic.value
         Log.e("onCharacteristicRead", "${data.toHexString()}")

        var sys=when(data[1].toInt()<0){true->256+data[1].toInt()
            false->data[1].toInt()}

        var dia=data[3]
//        var pul=when(DEVICE.name == "BM57") {
//            true-> data[14]
//            false->data[7]
//        }
        var pul =data[7]
        if (DEVICE.name=="BM57")
        {
            pul=data[14]
        }
        values= "$sys,$dia,$pul"
        Log.d("BP Value:",values)
        listener?.bpReadValue(values)
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
                    listener?.bpReadName(connect(result.device,connectCallback))
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
                Log.d("BP:","CONNECTED")
                gatt!!.discoverServices()
            }else if (newState==BluetoothAdapter.STATE_DISCONNECTED)
            {
                Log.d(DEVICE.name,"DISCONNECTED")
            }

                Log.d("STATE",newState.toString())

        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            var characteristic=getCharacteristic(gatt)

//
//            if (gatt!!.writeDescriptor(descriptor)){
//                Log.d("writeDescriptor befor","OK")
//            }

            if (gatt!!.setCharacteristicNotification(getCharacteristic(gatt), true)){
                Log.d("setCharacteristicNotification","OK")
                Thread.sleep(1000)

                var descriptor= characteristic!!.getDescriptor(mCONFIG)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                descriptor.value= BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                if (gatt!!.writeDescriptor(descriptor)){
                    Log.d("writeDescriptor","OK")
                }else{
                    Log.e("writeDescriptor","error")
                }
            }

        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
           // Log.d("CHARACTERISTIC Change",characteristic!!.uuid.toString())
            if (gatt != null) {
                if (characteristic != null) {
                    onCharacteristicRead(gatt,characteristic)
                    Log.d("BP VALUE",values)
                }
            }

        }
    }
}