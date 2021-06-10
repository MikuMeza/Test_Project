package com.project.testapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.testapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val listAdapter by lazy { ListAdapter(this, viewModel) }
    var isFirst=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()

        binding.addButton.setOnClickListener {
            alertShow()
        }
        populateList()

    }

    fun populateList() {

        lifecycleScope.launch {
            viewModel.getListData().observe(this@MainActivity, { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        binding.progressBar.visibility= View.GONE
                        if (isFirst == 0){
                            isFirst++
                            response.data?.let { listAdapter.setData(it) }
                        }

                        else
                        {
                            response.data?.let {
                                var lastindex=it.size-1
                                val item=it[lastindex]
                                val list=it
                                list.removeAt(lastindex)
                                list.add(0,item)
                                listAdapter.setData(list) }
                        }
                    }

                    is NetworkResult.Loading->{
                        binding.progressBar.visibility= View.VISIBLE
                    }
                }
            })
        }
    }

    private fun alertShow() {
        val child = layoutInflater.inflate(R.layout.postdatalayout, null)
        val description = child.findViewById<EditText>(R.id.description)
        val title = child.findViewById<EditText>(R.id.title)

//        description.setText(descriptionVal)
//        title.setText(titleVal)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val ll: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ll.setMargins(20, 20, 20, 20)
        layout.layoutParams = ll

        layout.gravity = Gravity.CLIP_VERTICAL
        layout.setPadding(2, 2, 2, 2)

        val tv1Params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tv1Params.bottomMargin = 10
        layout.addView(child, tv1Params)


        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
        layout.removeAllViews()
        layout.addView(child, tv1Params)
        builder.setView(layout)

        builder.setTitle("POST DATA")

        builder.setPositiveButton(android.R.string.cancel) { dialog, which ->
        }
        builder.setNeutralButton("POST") { dialog, which ->
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.postListData(title.text.toString(), description.text.toString())
                    .observe(this@MainActivity, { response ->
                        when (response) {
                            is NetworkResult.Success -> {
                                if (!response.data.isNullOrEmpty()) {
                                    okAlert(response.data)
                                }
                            }
                            is NetworkResult.Error -> {

                                Toast.makeText(
                                    this@MainActivity,
                                    response.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    })
            }
        }
        val d = builder.create()
        d.show()
    }

    fun okAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)

        builder.setPositiveButton("Ok") { dialogInterface, which ->
            populateList()
        }

        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = listAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

    }
}