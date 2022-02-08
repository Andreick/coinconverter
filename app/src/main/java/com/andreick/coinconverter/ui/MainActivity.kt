package com.andreick.coinconverter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import com.andreick.coinconverter.R
import com.andreick.coinconverter.core.extensions.*
import com.andreick.coinconverter.data.model.Coin
import com.andreick.coinconverter.databinding.ActivityMainBinding
import com.andreick.coinconverter.presentation.MainViewModel
import com.andreick.coinconverter.ui.history.HistoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MainViewModel>()
    private val dialog by lazy { createProgressDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setArrayAdapters()
        setListeners()
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setArrayAdapters() {
        val list = Coin.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.atvFrom.apply {
            setAdapter(adapter)
            setText(Coin.BRL.name, false)
        }
        binding.atvTo.apply {
            setAdapter(adapter)
            setText(Coin.USD.name, false)
        }
    }

    private fun setListeners() {
        binding.ietValue.doAfterTextChanged {
            binding.btnConvert.isEnabled = it != null && it.toString().isNotEmpty()
            binding.btnSave.isEnabled = false
        }
        binding.btnConvert.setOnClickListener {
            it.hideSoftKeyboard()
            val search = "${binding.atvFrom.text}-${binding.atvTo.text}"
            viewModel.getExchangeValue(search)
        }
        binding.btnSave.setOnClickListener {
            val state = viewModel.state.value
            (state as? MainViewModel.State.Success)?.let {
                val bid = it.exchange.bid * binding.ietValue.text.toDouble()
                viewModel.saveExchange(it.exchange.copy(bid = bid))
            }
        }
    }

    private fun setObservers() {
        viewModel.state.observe(this) {
            when (it) {
                MainViewModel.State.Loading -> dialog.show()
                is MainViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog { setMessage(it.throwable.message) }.show()
                }
                is MainViewModel.State.Success -> onSuccess(it)
                MainViewModel.State.Saved -> {
                    dialog.dismiss()
                    createDialog { setMessage("Item salvo com sucesso!") }.show()
                }
            }
        }
    }

    private fun onSuccess(it: MainViewModel.State.Success) {
        dialog.dismiss()
        val selectedCoin = binding.atvTo.text.toString()
        val coin = Coin.getByName(selectedCoin)
        val result = it.exchange.bid * binding.ietValue.text.toDouble()
        binding.tvResult.text = result.formatCurrency(coin.locale)
        binding.btnSave.isEnabled = true
    }
}