package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sharmadhiraj.mycurrencyexchange.databinding.ActivityConverterBinding

class ConverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}