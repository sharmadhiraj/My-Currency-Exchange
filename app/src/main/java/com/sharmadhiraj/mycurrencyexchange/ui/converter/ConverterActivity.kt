package com.sharmadhiraj.mycurrencyexchange.ui.converter

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Filterable
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.R as appCompatR
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.sharmadhiraj.mycurrencyexchange.R
import com.sharmadhiraj.mycurrencyexchange.databinding.ActivityConverterBinding
import com.sharmadhiraj.mycurrencyexchange.domain.model.Currency
import com.google.android.material.color.MaterialColors
import com.sharmadhiraj.mycurrencyexchange.ui.util.formatTimestamp
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.R as materialR

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConverterBinding
    private val viewModel: CurrencyConverterViewModel by viewModels()
    private val ratesAdapter = ConvertedAmountsAdapter()

    private var shownCurrencies: List<Currency>? = null
    private var shownSelectedCode: String? = null
    private var shownStale: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupInsets()
        setupAmountInput()
        setupCurrencyDropdown()
        setupRatesList()
        setupRefresh()

        viewModel.uiState.observe(this, ::render)
    }

    private fun setupInsets() {
        val listBottomPadding = binding.recyclerRates.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            binding.recyclerRates.setPadding(
                binding.recyclerRates.paddingLeft,
                binding.recyclerRates.paddingTop,
                binding.recyclerRates.paddingRight,
                listBottomPadding + bars.bottom,
            )
            binding.errorGroup.setPadding(0, 0, 0, bars.bottom)
            insets
        }
    }

    private fun setupAmountInput() {
        binding.editAmount.doAfterTextChanged { viewModel.onAmountChanged(it.toString()) }
    }

    private fun setupCurrencyDropdown() {
        val dropdown = binding.dropdownCurrency
        dropdown.setOnItemClickListener { parent, _, position, _ ->
            val label = parent.getItemAtPosition(position) as String
            viewModel.onCurrencySelected(CurrencyDropdownAdapter.codeOf(label))
            dropdown.clearFocus()
            hideKeyboard()
        }
        dropdown.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) openCurrencyDropdown() else restoreSelectedCurrencyText()
        }
        dropdown.setOnClickListener { openCurrencyDropdown() }
    }

    private fun openCurrencyDropdown() {
        val dropdown = binding.dropdownCurrency
        dropdown.selectAll()
        dropdown.adapter?.let { (it as Filterable).filter.filter(null) }
        dropdown.showDropDown()
    }

    private fun setupRatesList() {
        val spanCount = (resources.configuration.screenWidthDp / MIN_CARD_WIDTH_DP).coerceAtLeast(2)
        binding.recyclerRates.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerRates.adapter = ratesAdapter
    }

    private fun setupRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        binding.buttonRetry.setOnClickListener { viewModel.retry() }
    }

    private fun render(state: ConverterUiState) {
        binding.progressIndicator.isVisible = state is ConverterUiState.Loading
        binding.errorGroup.isVisible = state is ConverterUiState.Error
        binding.contentGroup.isVisible = state is ConverterUiState.Content

        when (state) {
            ConverterUiState.Loading -> Unit
            is ConverterUiState.Error -> binding.textError.setText(state.messageRes)
            is ConverterUiState.Content -> renderContent(state)
        }
    }

    private fun renderContent(content: ConverterUiState.Content) {
        binding.swipeRefresh.isRefreshing = content.isRefreshing
        showCurrencies(content)

        if (binding.editAmount.text.toString() != content.amountInput) {
            binding.editAmount.setText(content.amountInput)
        }
        binding.inputLayoutAmount.error =
            if (content.isAmountValid) null else getString(R.string.error_invalid_amount)

        renderStatus(content)
        ratesAdapter.submitList(content.results)
    }

    private fun showCurrencies(content: ConverterUiState.Content) {
        if (shownCurrencies != content.currencies) {
            shownCurrencies = content.currencies
            binding.dropdownCurrency.setAdapter(CurrencyDropdownAdapter(this, content.currencies))
            shownSelectedCode = null
        }
        if (shownSelectedCode != content.selectedCode) {
            shownSelectedCode = content.selectedCode
            restoreSelectedCurrencyText()
        }
    }

    private fun restoreSelectedCurrencyText() {
        val selected = shownCurrencies?.firstOrNull { it.code == shownSelectedCode } ?: return
        binding.dropdownCurrency.setText(CurrencyDropdownAdapter.label(selected), false)
    }

    private fun renderStatus(content: ConverterUiState.Content) {
        val updatedAt = content.lastUpdatedMillis?.let(::formatTimestamp)
        val status = when {
            content.isStale && updatedAt != null -> getString(R.string.status_stale, updatedAt)
            content.isStale -> getString(R.string.status_stale_unknown)
            updatedAt != null -> getString(R.string.status_updated, updatedAt)
            else -> null
        }
        binding.textStatus.isVisible = status != null
        if (status != null && binding.textStatus.text.toString() != status) {
            binding.textStatus.text = status
        }
        if (shownStale != content.isStale) {
            shownStale = content.isStale
            binding.textStatus.setTextColor(
                MaterialColors.getColor(
                    binding.textStatus,
                    if (content.isStale) appCompatR.attr.colorError else materialR.attr.colorOnSurfaceVariant
                )
            )
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private companion object {
        const val MIN_CARD_WIDTH_DP = 180
    }
}
