package com.josua.movapp.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.josua.movapp.R
import kotlinx.android.synthetic.main.activity_onboarding_three.*
import com.josua.movapp.sign.singin.SinginActivity

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_home.setOnClickListener {
            finishAffinity()

            var intent = Intent(this@OnboardingThreeActivity, SinginActivity::class.java)
            startActivity(intent)
        }
    }
}
