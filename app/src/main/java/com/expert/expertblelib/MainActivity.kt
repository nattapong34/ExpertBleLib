package com.expert.expertblelib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
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

    override fun tempReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtTempName)
        txt.text=name!!
    }

    override fun tempReadValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtTempVal)
        txt.text=value!!
    }

    override fun AccuReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtAccuName)
        txt.text=name!!
    }

    override fun AccuReadValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtAccuVal)
        txt.text=value!!
    }

    override fun bluedotReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtSATName)
        txt.text=name!!
    }

    override fun bluedotValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtSATVal)
        txt.text=value!!
    }

    override fun miscaleReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtScaleName)
        txt.text=name!!
    }

    override fun miscaleValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtScaleVal)
        txt.text=value!!
    }

    override fun bpReadName(name: String?) {
        val txt = findViewById<TextView>(R.id.txtBPName)
        txt.text=name!!
    }

    override fun bpReadValue(value: String?) {
        val txt = findViewById<TextView>(R.id.txtBPVal)
        txt.text=value!!
    }


}