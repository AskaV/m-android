package com.spp.android.myapplication.xmlscreens.fragment.contacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spp.android.myapplication.databinding.ActivityMainXmlHostBinding

class ContactsFragmentActivityXml : AppCompatActivity() {
    private lateinit var binding: ActivityMainXmlHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainXmlHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}