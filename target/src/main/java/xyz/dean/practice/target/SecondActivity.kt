package xyz.dean.practice.target

import android.os.Bundle
import xyz.dean.practice.common.BaseActivity

class SecondActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}