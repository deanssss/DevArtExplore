package xyz.dean.practice.devartexplore.ipc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import xyz.dean.practice.devartexplore.R
import xyz.dean.practice.devartexplore.ipc.binderpool.BINDER_COMPUTE
import xyz.dean.practice.devartexplore.ipc.binderpool.BINDER_SECURITY_CENTER
import xyz.dean.practice.devartexplore.ipc.binderpool.ICompute
import xyz.dean.practice.devartexplore.ipc.binderpool.ISecurityCenter
import xyz.dean.practice.devartexplore.ipc.binderpool.client.BinderPool

class BinderPoolActivity : AppCompatActivity() {
    private val binderPool = BinderPool.instance
    private val handler = Handler()
    private lateinit var computeService: ICompute
    private lateinit var securityService: ISecurityCenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_pool)

        val plusBt = findViewById<Button>(R.id.plus_bt)
        val encryptBt = findViewById<Button>(R.id.encrypt_bt)
        val decryptBt = findViewById<Button>(R.id.decrypt_bt)
        val numAEt = findViewById<EditText>(R.id.num_a_et)
        val numBEt = findViewById<EditText>(R.id.num_b_et)
        val resultTv = findViewById<TextView>(R.id.result_tv)
        val encryptEt = findViewById<EditText>(R.id.encrypt_content_et)
        val decryptEt = findViewById<EditText>(R.id.decrypt_content_et)

        plusBt.isEnabled = false
        encryptBt.isEnabled = false
        decryptBt.isEnabled = false

        plusBt.setOnClickListener {
            val a = numAEt.text.toString().toInt()
            val b = numBEt.text.toString().toInt()
            resultTv.text = computeService.add(a, b).toString()
        }

        encryptBt.setOnClickListener {
            val content = encryptEt.text.toString()
            decryptEt.setText(securityService.encrypt(content))
        }

        decryptBt.setOnClickListener {
            val content = decryptEt.text.toString()
            encryptEt.setText(securityService.decrypt(content))
        }

        Thread {
            binderPool.connectBinderPoolService(this)
            computeService = binderPool.queryBinder(BINDER_COMPUTE, ICompute.Stub::asInterface)
                ?: error("can not found service.")
            securityService = binderPool.queryBinder(BINDER_SECURITY_CENTER, ISecurityCenter.Stub::asInterface)
                ?: error("cannot found service")

            handler.post {
                plusBt.isEnabled = true
                encryptBt.isEnabled = true
                decryptBt.isEnabled = true
            }
        }.start()
    }

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, BinderPoolActivity::class.java)
    }
}