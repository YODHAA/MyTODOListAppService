package com.saurabh.mytodolistappservice.fragments.update

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.saurabh.mytodolistappservice.R


class UpdateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Set Menu Option
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
        //super.onCreateOptionsMenu(menu, inflater)
    }

}