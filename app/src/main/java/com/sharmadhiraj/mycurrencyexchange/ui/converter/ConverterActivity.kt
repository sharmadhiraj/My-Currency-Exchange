package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sharmadhiraj.mycurrencyexchange.databinding.ActivityConverterBinding
import com.sharmadhiraj.mycurrencyexchange.domain.model.ExchangeRates

class ConverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding
    private val viewModel: CurrencyConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        setupViewModel()
    }


    private fun initUI() {
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        viewModel.exchangeRates.observe(this, Observer { exchangeRates ->
            updateUI(exchangeRates)
        })
        viewModel.error.observe(this, Observer { errorMessage ->
            showError(errorMessage)
        })
        viewModel.fetchExchangeRates()
    }

    private fun updateUI(exchangeRates: ExchangeRates) {
        // Update your UI with the exchange rates data
        // For example, update TextViews, RecyclerView, etc.
    }

    private fun showError(errorMessage: String) {
        // Handle and display the error message
    }

}