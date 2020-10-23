package com.saurabh.mytodolistappservice.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saurabh.mytodolistappservice.R
import com.saurabh.mytodolistappservice.data.models.Priority
import com.saurabh.mytodolistappservice.data.models.ToDoData
import com.saurabh.mytodolistappservice.data.viewmodel.ToDoViewModel
import com.saurabh.mytodolistappservice.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedViewModel :  SharedViewModel by viewModels()

    private val mToDoViewModel : ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_update, container, false)

        // Set Menu Option
        setHasOptionsMenu(true)

        view.current_title_et.setText(args.currentItem.title)
        view.current_description_et.setText(args.currentItem.description)
        view.current_priorities_spinner.setSelection(mSharedViewModel.parsePriority(args.currentItem.priority))

        view.current_priorities_spinner.onItemSelectedListener = mSharedViewModel.listener


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
        //super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.menu_save ->  updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    // Show AlertDialog to Confirm item Removal
    private fun confirmItemRemoval() {

        val bulder = AlertDialog.Builder(context)
          bulder.setPositiveButton("Yes, Delete"){
              _,_ ->  mToDoViewModel.deleteData(args.currentItem)
              Toast.makeText(requireContext()," Successfully Removed : '${args.currentItem.title}' !!!",Toast.LENGTH_LONG).show()
              findNavController().navigate(R.id.action_updateFragment_to_listFragment)
          }
         bulder.setNegativeButton("No") { _,_ -> }
        bulder.setTitle("Delete '${args.currentItem.title}' ? ")
        bulder.setMessage(" Are you sure you want to remove '${args.currentItem.title}' ?")
        bulder.create().show()

    }

    private fun updateItem() {
        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val getPriority = current_priorities_spinner.selectedItem.toString()
        val validation = mSharedViewModel.verifyDataFromUser(title,getPriority,description)

        if(validation){
            // Update Current Item
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext()," Successfully Updated !!!",Toast.LENGTH_LONG).show()

            // Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        }else{
            Toast.makeText(requireContext()," Please fill out all the fields. !!!",Toast.LENGTH_LONG).show()
        }


    }

}