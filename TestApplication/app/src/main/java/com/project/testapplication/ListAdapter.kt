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
//            binding.rowCardView.setOnClickListener {
//                alertShow(activity, result.title, result.description, viewModel)
//            }
            binding.executePendingBindings()
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


