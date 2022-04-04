package com.expert.expert_ble

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import org.json.JSONObject
import java.lang.Exception
import java.util.*


open class ExpDevice :ExpBle{
    constructor(context: Context):super(context){}

    var values: String = ""
    lateinit var names : ArrayList<String>
    lateinit var devices :ArrayList<BluetoothDevice>
    open lateinit var advertise_name:String

    open lateinit var SERVICE_UUID:UUID
    open lateinit var CHAR_UUID:UUID
    open lateinit var CONTROL_UUID: UUID
    open lateinit var MSMCONTEXT:UUID
    open lateinit var FEATURE:UUID
    open lateinit var RECCACCESS:UUID

    private val DEVICE_REGISTED=100

    lateinit var DEVICE:BluetoothDevice

    fun clear()
    {
        names= ArrayList()
        devices= ArrayList()
    }
    fun add(dv:BluetoothDevice)
    {
        for(d in devices)
        {
            if (d.address.equals(dv.address))
            {
                return
            }
        }
        names.add(dv.name + " " + dv.address)
        devices.add(dv)
    }

    fun getCharacteristic(gatt: BluetoothGatt?): BluetoothGattCharacteristic? {
        Log.d("UUID",SERVICE_UUID.toString() + " " + CHAR_UUID.toString())
        return gatt!!.getService(SERVICE_UUID)
            .getCharacteristic(CHAR_UUID)
    }

    fun getCharacteristicRECACCESS(gatt: BluetoothGatt?): BluetoothGattCharacteristic? {
        Log.d("RECACCESS UUID",SERVICE_UUID.toString() + " " + RECCACCESS.toString())
        return gatt!!.getService(SERVICE_UUID)
            .getCharacteristic(RECCACCESS)
    }

    fun getCharacteristicContext(gatt: BluetoothGatt?): BluetoothGattCharacteristic? {
        Log.d("MSMCONTEXT UUID",SERVICE_UUID.toString() + " " + MSMCONTEXT.toString())
        return gatt!!.getService(SERVICE_UUID)
            .getCharacteristic(MSMCONTEXT)
    }

    open fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        // read value
    }

    open fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    )
    {

    }

    private fun setDevice(dv:BluetoothDevice) : Boolean
    {
        if (checkDevice(dv)) {
            Log.d("DEVICE API","REGISTED")
            DEVICE = dv
            return true
        }
        return false
    }

    public fun connect(dv:BluetoothDevice,callback:BluetoothGattCallback)
    {
        if (setDevice(dv)) {
            try {
                DEVICE.connectGatt(context, true, callback)
            }catch (e:Exception)
            {

            }
        }
    }

    // rest api expert
    private fun checkDevice(dv:BluetoothDevice):Boolean
    {
        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        var url= "https://smarteasyopd.com/experthealth/api/GetHardwareInfo"
        var addr=dv.address.replace(":","")

        val requestParams: MutableList<Pair<String, String>> = ArrayList(1)
        requestParams.add(Pair("macaddress", addr))

        val (request, response, result) =Fuel.post(url)
            .jsonBody("{\"macaddress\":\"${addr}\"}")
            .responseString()

        var res=result.toString().replace("Success:","")
        res=res.replace("[","")
        res=res.replace("]","")
        Log.d("RETURN API",res)
        var obj=JSONObject(res)
        var code=obj.getInt("code")
        Log.d("VALUES API",code.toString())
        if (code==DEVICE_REGISTED) {
            // DEVICE = dv
            return true
        }
        else
        {
            Toast.makeText(context,"This device is not registered.", Toast.LENGTH_LONG).show()
            return false
        }


    }


    // util
    open fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }
    fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }

}