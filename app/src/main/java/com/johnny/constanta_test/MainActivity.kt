package com.johnny.constanta_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.johnny.constanta_test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, MainFragment.getInstance(), "MainFragment")
                .commit()
    }

}