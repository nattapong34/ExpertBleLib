package com.expert.expert_ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*


open class ExpDevice :ExpBle{
    constructor(context: Context):super(context){}

    var values: String = ""
    open lateinit var advertise_name:String

    open lateinit var CHAR_UUID:UUID
    open lateinit var MSMCONTEXT:UUID
    open lateinit var FEATURE:UUID
    open lateinit var RECCACCESS:UUID

    private val DEVICE_REGISTED=100

    lateinit var DEVICE:BluetoothDevice

    lateinit var _setting:SharedPreferences

    public var MACADDR: String=""



    fun getCharacteristic(gatt: BluetoothGatt?): BluetoothGattCharacteristic? {
      //  Log.d("UUID",SERVICE_UUID.toString() + " " + CHAR_UUID.toString())
        return gatt!!.getService(SERVICE_UUID)
            .getCharacteristic(CHAR_UUID)
    }

    fun getCharacteristicRECACCESS(gatt: BluetoothGatt?): BluetoothGattCharacteristic? {
      //  Log.d("RECACCESS UUID",SERVICE_UUID.toString() + " " + RECCACCESS.toString())
        return gatt!!.getService(SERVICE_UUID)
            .getCharacteristic(RECCACCESS)
    }

    fun getCharacteristicContext(gatt: BluetoothGatt?): BluetoothGattCharacteristic? {
      //  Log.d("MSMCONTEXT UUID",SERVICE_UUID.toString() + " " + MSMCONTEXT.toString())
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



    @SuppressLint("MissingPermission")
    public fun connect(dv:BluetoothDevice, callback:BluetoothGattCallback):String
    {
        if (setDevice(dv)) {
            try {
                DEVICE.connectGatt(context, true, callback)
                return DEVICE.name
            }catch (e:Exception)
            {

            }
        }
        return  "อุปกรณ์ไม่ได้ลงทะเบียน!!"
    }



    // rest api expert
    public fun checkDevice(dv:BluetoothDevice):Boolean
    {
        if (readSetting(dv.address)) {
            Log.d("DEVICES is registed:","Ready to connect")
            return true
        }

        Log.d("CHECK REGISTER",dv.address)

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
        //Log.d("RETURN API",res)
        var obj=JSONObject(res)
        var code=obj.getInt("code")
        //Log.d("VALUES API",code.toString())
        if (code==DEVICE_REGISTED) {
            // DEVICE = dv
            saveSetting(dv.address,true)
            this.MACADDR=dv.address;
            return true
        }
        else
        {
            Toast.makeText(context,"This device is not registered.", Toast.LENGTH_LONG).show()
            return false
        }


    }

    private fun saveSetting(macaddr: String,status:Boolean){
        _setting= context.getSharedPreferences("DEVICE",Context.MODE_PRIVATE)
        var edit = _setting.edit()
        edit.putBoolean(macaddr,status)
        edit.commit()
    }
    private fun readSetting(macaddr:String):Boolean
    {
        _setting= context.getSharedPreferences("DEVICE",Context.MODE_PRIVATE)
        return _setting.getBoolean(macaddr,false)
    }


    fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }

}