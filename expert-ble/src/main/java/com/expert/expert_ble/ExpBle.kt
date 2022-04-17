package com.expert.expert_ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import java.util.*
import kotlin.collections.ArrayList


open class ExpBle(context: Context){
    private var m_Adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val m_Scanner = m_Adapter.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler()

    open lateinit var SERVICE_UUID:UUID

    lateinit var mListdevice:ArrayList<BluetoothDevice>


    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 5000

    var context=context
    fun checkAdapter(){
        if (m_Adapter==null){
            Toast.makeText(this.context,"Bluetooth is not available", Toast.LENGTH_LONG).show()
        }else
        {
            Toast.makeText(this.context,"Bluetooth is available", Toast.LENGTH_LONG).show()
        }
    }
    fun checkOn(){
        if (m_Adapter.isEnabled)
        {
            Toast.makeText(this.context,"Bluetooth is on", Toast.LENGTH_LONG).show()
        }else
        {
            Toast.makeText(this.context,"Bluetooth is off", Toast.LENGTH_LONG).show()
        }

    }
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)

    fun scanLeDevice(name:String?,callback:ScanCallback) {

        var filters: List<ScanFilter>? = null
        if (name!=null)
        {
            var ft= ScanFilter.Builder()
                .setDeviceName(name)
                .build()
            filters = listOf(ft)
        }

        mListdevice = ArrayList()

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
//            .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
            .setCallbackType(ScanSettings.MATCH_MODE_STICKY)
//            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
//            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .setReportDelay(0)
            .build()


        if (!scanning){
            try {
                handler.postDelayed({
                    scanning=false
                    m_Scanner.stopScan(callback)
                }, SCAN_PERIOD)
                m_Scanner.startScan(filters, scanSettings, callback)
            }catch (e:IllegalStateException) {
                Log.e("ERROR", "Cannot start scan. Bluetooth may be turned off.");
            } catch (npe:NullPointerException ) {
                // Necessary because of https://code.google.com/p/android/issues/detail?id=160503
                Log.e("ERROR", "Cannot start scan. Unexpected NPE.");
            } catch (e:SecurityException) {
                // Thrown by Samsung Knox devices if bluetooth access denied for an app
                Log.e("ERROR", "Cannot start scan.  Security Exception");
            }
        }else
        {
            scanning=false

            m_Scanner.stopScan(callback)
        }
    }


    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun scanDevice(callback:ScanCallback) {

        var filters: List<ScanFilter>? = null

        if (SERVICE_UUID!=null)
        {
            Log.d("SERVICE UUID : ", SERVICE_UUID.toString())
            var ft= ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(SERVICE_UUID))
                .build()
            filters = listOf(ft)

        }

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
//            .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
            .setCallbackType(ScanSettings.MATCH_MODE_STICKY)
//            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
//            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .setReportDelay(0)
            .build()


        if (!scanning){
            try {
                handler.postDelayed({
                    scanning=false
                    m_Scanner.stopScan(callback)
                }, SCAN_PERIOD)
                Log.d("Start Scan","Ok")
                m_Scanner.startScan(filters, scanSettings, callback)
            }catch (e:IllegalStateException) {
                Log.e("ERROR", "Cannot start scan. Bluetooth may be turned off.");
            } catch (npe:NullPointerException ) {
                // Necessary because of https://code.google.com/p/android/issues/detail?id=160503
                Log.e("ERROR", "Cannot start scan. Unexpected NPE.");
            } catch (e:SecurityException) {
                // Thrown by Samsung Knox devices if bluetooth access denied for an app
                Log.e("ERROR", "Cannot start scan.  Security Exception");
            }
        }else
        {
            scanning=false

            m_Scanner.stopScan(callback)
        }
    }

    //example only not use
    private val leScanCallback: ScanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            with(result.device) {
                if (result?.device != null && result.device.address != null  && result.device.name != null) {
                    //(result.device)
                }
            }
        }
    }



    // util
    open fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

}
