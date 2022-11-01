package com.example.trial3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class viewmodel(private val repository: repository): ViewModel() {
    val allentries: Flow<List<entry>> = repository.allentries

    fun insert(entry: entry)=viewModelScope.launch {
        repository.insert(entry)
    }

}
class ViewModelFactory(private val repository: repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(viewmodel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return viewmodel(repository) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }
}