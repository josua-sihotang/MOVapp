package com.josua.movapp.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.josua.movapp.R
import com.josua.movapp.home.HomeActivity
import kotlinx.android.synthetic.main.activity_checkout_sukses.*

class CheckoutSuksesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_sukses)

        btn_home.setOnClickListener {
            finishAffinity()

            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
