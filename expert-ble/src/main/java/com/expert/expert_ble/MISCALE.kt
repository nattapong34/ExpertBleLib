package com.expert.expert_ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.TextView
import java.lang.Math.abs
import java.util.*
interface MISCALEListener {
    fun miscaleReadName(name:String?)
    fun miscaleValue(value:String?) // pass any parameter in your onCallBack which you want to return
    fun miscaleStatus(state:String?)
}
class MISCALE(val listener:MISCALEListener, context: Context): ExpDevice(context) {

    override var ADV_UUID: UUID =convertFromInteger(0x181b)
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
        listener?.miscaleValue(values)
    }
    public fun startConnect(addr:String){
        MACADDR=addr
        //var dv: BluetoothDevice? =readDevice()
        Log.d("temp ", MACADDR)
        var device:BluetoothDevice  = connectMacaddr(MACADDR);
        device?.let { listener?.miscaleReadName(connect(it,connectCallback)) }
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
                    listener?.miscaleReadName(connect(result.device,connectCallback))
                }
                //     Log.d("DEVICE", mTemp.names.toString())
            }
        }
    }

    private val connectCallback: BluetoothGattCallback = object: BluetoothGattCallback(){
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.d("SCALE STATE",newState.toString())
            if (newState== BluetoothAdapter.STATE_CONNECTED)
            {
                listener?.miscaleStatus("Connected")
                gatt!!.discoverServices()
            }else if (newState==BluetoothAdapter.STATE_DISCONNECTED)
            {
                Log.d(DEVICE.name,"DISCONNECTED")
                listener?.miscaleStatus("Disconnected")
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            gatt!!.setCharacteristicNotification(getCharacteristic(gatt), true)
            var characteristic=getCharacteristic(gatt)
            var descriptor=characteristic!!.getDescriptor(mCONFIG)
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            descriptor.value= BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            if (gatt.writeDescriptor(descriptor))
            {
                Log.d("DESCRIPTOR","Write ok")
            }else
            {
                Log.e("DESCRIPTOR Error","Write")
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
                }
            }

        }
    }

}