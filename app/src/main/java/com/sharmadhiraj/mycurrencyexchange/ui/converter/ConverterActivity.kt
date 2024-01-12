package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.sharmadhiraj.mycurrencyexchange.R
import com.sharmadhiraj.mycurrencyexchange.databinding.ActivityConverterBinding
import com.sharmadhiraj.mycurrencyexchange.util.CommonUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding
    private val viewModel: CurrencyConverterViewModel by viewModels()
    private val exchangeRateRecyclerViewAdapter = ExchangeRateAdapter()
    private lateinit var exchangeRateSpinnerAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        setupViewModel()
    }

    private fun initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_converter)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initEditText()
        initSpinner()
        initRecyclerView()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(
            this
        ) { viewState ->
            if (viewState is ConverterViewState.Success) {
                exchangeRateSpinnerAdapter.clear()
                exchangeRateSpinnerAdapter.addAll(viewState.exchangeRates.rates.keys)
                exchangeRateSpinnerAdapter.notifyDataSetChanged()
                binding.textLastRefreshed.text =
                    getString(
                        R.string.exchange_rate_of,
                        CommonUtil.formatDateTime(viewState.exchangeRates.timestamp)
                    )
            } else if (viewState is ConverterViewState.Error) {
                binding.txtErrorMessage.text = viewState.errorMessage
            }
        }

        viewModel.convertedAmounts.observe(
            this
        ) { exchangeRates ->
            exchangeRateRecyclerViewAdapter.setExchangeRates(exchangeRates)
        }
        viewModel.fetchExchangeRates()
    }


    private fun initEditText() {
        binding.editTextAmount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                convertCurrency()
            }
        })
    }

    private fun initSpinner() {
        exchangeRateSpinnerAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        exchangeRateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = exchangeRateSpinnerAdapter

        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    convertCurrency()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun initRecyclerView() {
        binding.recyclerViewCurrencies.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewCurrencies.adapter = exchangeRateRecyclerViewAdapter
    }

    private fun convertCurrency() {
        val amount: Double = CommonUtil.stringToDouble(binding.editTextAmount.text.toString())
        if (amount == 0.0) return
        viewModel.convertCurrency(amount, binding.spinnerCurrency.selectedItem.toString())
    }

}