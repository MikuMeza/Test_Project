package com.project.testapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.testapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val listAdapter by lazy { ListAdapter(this,viewModel) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.getListData().observe(this@MainActivity, { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let { listAdapter.setData(it) }
                    }
                }
            })
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = listAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

    }
}