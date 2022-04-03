package com.expert.expert_ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat


open class ExpBle(context: Context){
    private var m_Adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val m_Scanner = m_Adapter.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler()

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
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
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
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            m_Scanner.stopScan(callback)
        }
    }


    //example only not use
    private val leScanCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            with(result.device) {
                if (result?.device != null && result.device.address != null  && result.device.name != null) {
                    //(result.device)
                }
            }
        }
    }


}
