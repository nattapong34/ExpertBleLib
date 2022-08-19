package com.expert.expertblelib

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.expert.expert_ble.*


class MainActivity : AppCompatActivity() ,TEMPHTD8808EListener,ACCUCHECKListener,SATBLUEDOTEListener
,MISCALEListener,BPUA651Listener{



    private lateinit var m_ble: ExpBle
     var mTemp:TEMPHTD8808E= TEMPHTD8808E(this,this)
    var mAccu:ACCUCHECK= ACCUCHECK(this,this)
    var mSAT:SATBLUEDOT= SATBLUEDOT(this,this)
    var mMIScale:MISCALE = MISCALE(this,this)
    var mBP:BPUA651 = BPUA651(this,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTemp.checkAdapter()

        // Start auto connect when device address is recorded
        var temp=readDevice("TEMP")
        if (temp != null && temp!="") {
            Log.d("temp ", temp)
            mTemp.startConnect(temp)
        }
        var sat=readDevice("SAT")!!
        if (sat != null && sat!="") {
            Log.d("SAT ", sat)
            mSAT.startConnect(sat)
        }
        var miscale=readDevice("SCALE")
        if (miscale != null && miscale!="") {
            Log.d("SCALE ", miscale)
            mSAT.startConnect(miscale)
        }
        var bp=readDevice("BP")
        if (bp != null && bp!="") {
            Log.d("BP ", bp)
            mSAT.startConnect(bp)
        }
        var accu=readDevice("ACCU")
        if (accu != null && accu!="") {
            Log.d("ACCU ", accu)
            mSAT.startConnect(accu)
        }
        // end auto connect


        // button
        findViewById<Button>(R.id.bnDevice).apply{
            setOnClickListener{
                mTemp.pairDevice()
            }
        }

        findViewById<Button>(R.id.bnAccu).apply{
            setOnClickListener{
                mAccu.pairDevice()

            }
        }

        findViewById<Button>(R.id.bnSAT).apply{
            setOnClickListener{
                mSAT.pairDevice()

            }
        }

        findViewById<Button>(R.id.bnScale).apply{
            setOnClickListener{
                mMIScale.pairDevice()

            }
        }
        findViewById<Button>(R.id.bnBP).apply{
            setOnClickListener{
                mBP.pairDevice()

            }
        }
    }

    // Save,read,delete device
    fun saveDevice(name:String,addr:String){
        getPreferences(MODE_PRIVATE).edit().putString(name,addr).commit();
    }
    fun readDevice(name:String): String? {
        return getPreferences(MODE_PRIVATE).getString(name,"")
    }
    fun deleteDevice(name:String){
        getPreferences(MODE_PRIVATE).edit().remove(name)
    }
    //


    // Event
    override fun tempReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtTempName)
        txt.text=name!!

        if (name!=null)
            mTemp.MACADDR?.let { saveDevice("TEMP", it) }
    }

    override fun tempReadValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtTempVal)
        txt.text=value!!
    }

    override fun tempStatus(state: String?) {
        Log.d("TEMP STATE ",state!!)
    }

    override fun AccuReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtAccuName)
        txt.text=name!!
        if (name!=null)
            mTemp.MACADDR?.let { saveDevice("ACCU", it) }
    }

    override fun AccuReadValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtAccuVal)
        txt.text=value!!
    }

    override fun AccuStatus(state: String?) {
        Log.d("ACCU STATE ",state!!)
    }

    override fun bluedotReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtSATName)
        txt.text=name!!
        if (name!=null)
            mTemp.MACADDR?.let { saveDevice("SAT", it) }
    }

    override fun bluedotValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtSATVal)
        txt.text=value!!
    }

    override fun bluedotStatus(state: String?) {
        Log.d("BLUEDOT STATE ",state!!)
    }

    override fun miscaleReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtScaleName)
        txt.text=name!!
        if (name!=null)
            mTemp.MACADDR?.let { saveDevice("SCALE", it) }
    }

    override fun miscaleValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtScaleVal)
        txt.text=value!!
    }

    override fun miscaleStatus(state: String?) {
        Log.d("MISCALE STATE ",state!!)
    }

    override fun bpReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtBPName)
        txt.text=name!!
        if (name!=null)
            mTemp.MACADDR?.let { saveDevice("BP", it) }
    }

    override fun bpReadValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtBPVal)
        txt.text=value!!
    }

    override fun bpStatus(state: String?) {
        Log.d("BP STATE ",state!!)
    }


}