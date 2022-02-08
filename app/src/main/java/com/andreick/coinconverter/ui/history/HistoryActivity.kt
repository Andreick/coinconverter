package com.andreick.coinconverter.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.andreick.coinconverter.core.extensions.createDialog
import com.andreick.coinconverter.core.extensions.createProgressDialog
import com.andreick.coinconverter.databinding.ActivityHistoryBinding
import com.andreick.coinconverter.presentation.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<HistoryViewModel>()
    private val dialog by lazy { createProgressDialog() }
    private val adapter by lazy { HistoryListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setRecyclerView()
        setObservers()
        lifecycle.addObserver(viewModel)
    }

    private fun setRecyclerView() {
        binding.rvHistory.adapter = adapter
        binding.rvHistory.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        )
    }

    private fun setObservers() {
        viewModel.state.observe(this) {
            when (it) {
                HistoryViewModel.State.Loading -> dialog.show()
                is HistoryViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog { setMessage(it.throwable.message) }.show()
                }
                is HistoryViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(it.exchanges)
                }
            }
        }
    }
}