package com.saurabh.mytodolistappservice.fragments.update

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
         if(item.itemId == R.id.menu_save) {
             updateItem()
         }
        return super.onOptionsItemSelected(item)
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