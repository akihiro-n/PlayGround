package com.example.akihiro.playground

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.akihiro.playground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.ediText
            .toStringObservable(TextChanged.Before::class.java)
            .subscribe {
                Log.v("ああ",it )
        }
        binding.ediText.beforeTextChangedListener { s, start, count, after ->
            Log.v("ああ1","${s}, ${start}, ${count}, ${after}" )
        }

        binding.ediText
            .toStringObservable(TextChanged.On::class.java)
            .subscribe {
                Log.v("ああ2",it )
            }

        binding.ediText.onTextChangedListener { s, start, before, count ->
            Log.v("ああ3","${s}, ${start}, ${count}, ${before}" )
        }
    }
}
