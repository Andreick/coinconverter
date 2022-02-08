package com.andreick.coinconverter.presentation

import androidx.lifecycle.*
import com.andreick.coinconverter.data.model.ExchangeResponseValue
import com.andreick.coinconverter.domain.ListExchangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val listExchangeUseCase: ListExchangeUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getExchanges()
    }

    private fun getExchanges() {
        viewModelScope.launch {
            viewModelScope.launch {
                listExchangeUseCase()
                    .flowOn(Dispatchers.Main)
                    .onStart {
                        _state.value = State.Loading
                    }
                    .catch {
                        _state.value = State.Error(it)
                    }
                    .collect {
                        _state.value = State.Success(it)
                    }
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val exchanges: List<ExchangeResponseValue>) : State()
        data class Error(val throwable: Throwable) : State()
    }
}