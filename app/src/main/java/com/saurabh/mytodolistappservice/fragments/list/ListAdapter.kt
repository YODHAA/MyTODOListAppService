package com.saurabh.mytodolistappservice.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.saurabh.mytodolistappservice.R
import com.saurabh.mytodolistappservice.data.models.Priority
import com.saurabh.mytodolistappservice.data.models.ToDoData
import com.saurabh.mytodolistappservice.databinding.RowLayoutBinding
import com.saurabh.mytodolistappservice.databinding.RowLayoutBindingImpl
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

   private var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//       val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
//        return MyViewHolder(view)

         return  MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = dataList[position]
        holder.bind(currentItem)

//       holder.itemView.title_txt.text = dataList[position].title
//        holder.itemView.description_txt.text= dataList[position].description
//
//        holder.itemView.row_background.setOnClickListener {
//
//            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
//
//            // holder.itemView.findNavController().navigate(R.id.action_listFragment_to_updateFragment)
//             holder.itemView.findNavController().navigate(action)
//        }
//
//
//        val priority = dataList[position].priority
//        when(priority){
//            Priority.HIGH -> holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,
//            R.color.red))
//
//            Priority.MEDIUM -> holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,
//                R.color.yellow))
//
//            Priority.LOW -> holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,
//                R.color.green))
//        }

    }

     fun setdata(toDoData: List<ToDoData>) {
         this.dataList=toDoData
         notifyDataSetChanged()
     }

    class MyViewHolder(private val binding : RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoData: ToDoData){
            binding.toDodata = toDoData
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent:ViewGroup) : MyViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflator,parent,false)
                return MyViewHolder(binding)
            }
        }

    }

}