package com.spp.android.myapplication.presentation.xml.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.databinding.ActivityMainXmlHostBinding

class MainActivityXml : AppCompatActivity() {
    private lateinit var binding: ActivityMainXmlHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainXmlHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
