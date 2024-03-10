package com.learning.observable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.learning.observable.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLivedata.setOnClickListener {
            viewModel.triggerLiveData()
        }
        binding.btnStateFlow.setOnClickListener {
            viewModel.triggerStateFlow()
        }
        binding.btnFlow.setOnClickListener {
            lifecycleScope.launch {
                viewModel.triggerFlow().collectLatest {
                    binding.tv3.text = it
                }
            }
        }
        binding.btnSharedFlow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }

        subcribeToObservable()
    }

    // Mengetahui apakah berpengaruh pada lifecycle
    private fun subcribeToObservable(){
        viewModel.liveData.observe(this){
            binding.tv1.text = it
        }

        lifecycleScope.launch {
            viewModel.stateFlow.collectLatest {
                binding.tv2.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.sharedFlow.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}