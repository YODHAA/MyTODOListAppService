package com.saurabh.mytodolistappservice.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saurabh.mytodolistappservice.R
import com.saurabh.mytodolistappservice.data.viewmodel.ToDoViewModel
import com.saurabh.mytodolistappservice.databinding.FragmentListBinding
import com.saurabh.mytodolistappservice.fragments.SharedViewModel
import com.saurabh.mytodolistappservice.fragments.list.adapter.ListAdapter


class ListFragment : Fragment() {

    private val adapter : ListAdapter by lazy { ListAdapter() }

    private val mSharedViewModel : SharedViewModel by viewModels()
    private val mToDoViewModel : ToDoViewModel  by viewModels()

    private var _binding : FragmentListBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Data Binding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        binding?.lifecycleOwner = this
        binding?.mSharedViewModel = mSharedViewModel

        // Set Menu
        setHasOptionsMenu(true)

        // Setup  Recycler View
        setUpRecyclerView()

        // Observe Livedata

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
             adapter.setdata(data)
        })

//        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
//            showEmptyDatabaseView(it)
//        })

//        view.floatingActionButton.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_addFragment)
//        }  now implmented DataBinding Adapter for its click

//        view.listLayout.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
//        }



        return binding?.root
    }

    private fun setUpRecyclerView() {
        var recyclerView = binding?.recyclerView
        recyclerView?.adapter =adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())

        // Swipe to Delete
        recyclerView?.let { swipeToDelete(it) }
    }

    // Swipe functionality callback abstract class Impl

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteData(itemToDelete)
                Toast.makeText(requireContext()," Successfully Removed : '${itemToDelete.title}' !!!",
                    Toast.LENGTH_LONG).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

//    private fun showEmptyDatabaseView(emptyDatabase:Boolean) {
//
//        if(emptyDatabase) {
//            view?.no_data_imageView?.visibility = View.VISIBLE
//            view?.no_data_textView?.visibility = View.VISIBLE
//        }
//        else{
//            view?.no_data_imageView?.visibility = View.INVISIBLE
//            view?.no_data_textView?.visibility = View.INVISIBLE
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
        //super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_delete_all ->  confirmRemoveAll()
        }
        return super.onOptionsItemSelected(item)
    }

    // Show AlertDialogBox to confirm Removal of All Items from Database Table
    private fun confirmRemoveAll() {
        val bulder = AlertDialog.Builder(context)
        bulder.setPositiveButton("Yes, Delete"){
                _,_ ->  mToDoViewModel.deleteAll()
            Toast.makeText(requireContext()," Successfully Removed Everything !!!",
                Toast.LENGTH_LONG).show()
        }
        bulder.setNegativeButton("No") { _,_ -> }
        bulder.setTitle("Delete Everything ? ")
        bulder.setMessage(" Are you sure you want to remove Everything ?")
        bulder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // to avoid memeory leak
        _binding = null

    }

}