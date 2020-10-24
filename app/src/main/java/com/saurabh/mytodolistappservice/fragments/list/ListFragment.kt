package com.saurabh.mytodolistappservice.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.saurabh.mytodolistappservice.R
import com.saurabh.mytodolistappservice.data.models.ToDoData
import com.saurabh.mytodolistappservice.data.viewmodel.ToDoViewModel
import com.saurabh.mytodolistappservice.databinding.FragmentListBinding
import com.saurabh.mytodolistappservice.fragments.SharedViewModel
import com.saurabh.mytodolistappservice.fragments.list.adapter.ListAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Data Binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        binding?.mSharedViewModel = mSharedViewModel

        // Set Menu
        setHasOptionsMenu(true)

        // Setup  Recycler View
        setUpRecyclerView()

        // Observe Livedata

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
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
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        recyclerView?.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }

        // Swipe to Delete
        recyclerView?.let { swipeToDelete(it) }
    }



    // Swipe functionality callback abstract class Impl

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                // Delete Item
                mToDoViewModel.deleteData(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // Restore Deleted Item
                restoreDeletedData(viewHolder.itemView,deletedItem = itemToDelete,position = viewHolder.adapterPosition)
                // as snackbar is used
//                Toast.makeText(
//                    requireContext(), " Successfully Removed : '${itemToDelete.title}' !!!",
//                    Toast.LENGTH_LONG
//                ).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    // Restore Functionality

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make( view , " Deleted '${deletedItem.title}'  ",Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }

        snackBar.show()
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
        inflater.inflate(R.menu.list_fragment_menu, menu)
        //super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoveAll()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer {
                list -> adapter.setdata(list) }
            )
            R.id.menu_priority_low -> mToDoViewModel.sortbyLowProirity.observe(this, Observer {
                    list -> adapter.setdata(list) }
            )
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
       if(query!= null) {
           searchThroughDatabase(query)
       }
        return true
    }

    private fun searchThroughDatabase(query: String) {
         var searchQuery = query
        searchQuery = "%$searchQuery%"

        mToDoViewModel.searchDatabase(searchQuery = searchQuery).observe(this, Observer {
            list -> adapter.setdata(list)

        })

    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query!= null) {
            searchThroughDatabase(query)
        }
        return true
    }


    // Show AlertDialogBox to confirm Removal of All Items from Database Table
    private fun confirmRemoveAll() {
        val bulder = AlertDialog.Builder(context)
        bulder.setPositiveButton("Yes, Delete") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(), " Successfully Removed Everything !!!",
                Toast.LENGTH_LONG
            ).show()
        }
        bulder.setNegativeButton("No") { _, _ -> }
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