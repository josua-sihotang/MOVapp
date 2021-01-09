package com.josua.movapp.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.josua.movapp.R
import kotlinx.android.synthetic.main.activity_onboarding_one.*
import com.josua.movapp.sign.singin.SinginActivity
import com.josua.movapp.utils.Preferences

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preferences:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")){
            finishAffinity()

            var intent = Intent(this@OnboardingOneActivity, SinginActivity::class.java)
            startActivity(intent)
        }
        btn_home.setOnClickListener {
            var intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }
        btn_daftar.setOnClickListener {
            preferences.setValues("onboarding", "1")
            finishAffinity()

            var intent = Intent(this@OnboardingOneActivity, SinginActivity::class.java)
            startActivity(intent)
        }
    }
}
