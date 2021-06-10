package com.project.testapplication

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.testapplication.databinding.ListRowLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListAdapter(var activity: MainActivity, var viewModel: MainViewModel) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var listRowItem = emptyList<ListDetailModelItem>()

    class ListViewHolder(private val binding: ListRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            result: ListDetailModelItem, activity: MainActivity, viewModel: MainViewModel
        ) {
            binding.result = result
            binding.rowCardView.setOnClickListener {
                alertShow(activity, result.title, result.description, viewModel)
            }
            binding.executePendingBindings()
        }

        private fun alertShow(
            activity: MainActivity,
            titleVal: String,
            descriptionVal: String,
            viewModel: MainViewModel
        ) {
            val child = activity.layoutInflater.inflate(R.layout.postdatalayout, null)
            val description = child.findViewById<EditText>(R.id.description)
            val title = child.findViewById<EditText>(R.id.title)

            description.setText(descriptionVal)
            title.setText(titleVal)

            val layout = LinearLayout(activity)
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


            val builder = AlertDialog.Builder(activity, R.style.Theme_AppCompat_Light_Dialog_Alert)
            layout.removeAllViews()
            layout.addView(child, tv1Params)
            builder.setView(layout)

            builder.setTitle("POST DATA")

            builder.setPositiveButton(android.R.string.cancel) { dialog, which ->
            }
            builder.setNeutralButton("POST") { dialog, which ->
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.postListData(title.text.toString(), description.text.toString())
                        .observe(activity, { response ->
                            when (response) {
                                is NetworkResult.Success -> {
                                    if (response.data!!) {
                                        Toast.makeText(
                                            activity,
                                            "Posted successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                                is NetworkResult.Error -> {

                                    Toast.makeText(
                                        activity,
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


        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListRowLayoutBinding.inflate(layoutInflater, parent, false)

                return ListViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        return ListViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return listRowItem.size
    }

    fun setData(newData: List<ListDetailModelItem>) {
        val recipesDiffUtil =
            DiffUtility(listRowItem, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        listRowItem = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentRecipe = listRowItem[position]
        holder.bind(currentRecipe, activity, viewModel)
    }
}


