package com.lsegura.proyectoadat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lsegura.proyectoadat.databinding.FragmentNotesBinding
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var binding: FragmentNotesBinding
    private lateinit var apiService: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        binding = FragmentNotesBinding.bind(view)
        recyclerView = binding.recycler
        apiService = RetrofitClient.api
        recyclerView.layoutManager = LinearLayoutManager(activity)
        notesAdapter = NotesAdapter(emptyList(), apiService) { note ->
            findNavController().navigate(
                R.id.action_notesFragment_to_detailFragment,
                bundleOf(DetailFragment.NOTE to note)
            )
        }
        recyclerView.adapter = notesAdapter
        fetchDataFromApi()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_notesFragment_to_modifyNoteFragment,
                bundleOf(ModifyNoteFragment.POS to -1)
            )
            notesAdapter.notifyItemInserted(notesAdapter.itemCount)
        }
        return view
    }

    private fun fetchDataFromApi() {
        apiService.getNotes().enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val notes = response.body() ?: emptyList()
                    Log.d("API Response", "Number of notes: ${notes.size}")
                    notesAdapter = NotesAdapter(notes, apiService) { note ->
                        findNavController().navigate(
                            R.id.action_notesFragment_to_detailFragment,
                            bundleOf(DetailFragment.NOTE to note)
                        )
                    }
                    recyclerView.adapter = notesAdapter
                    notesAdapter.notifyDataSetChanged()
                } else {
                    Log.e("API Error", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}", t)
            }
        })
    }
}
