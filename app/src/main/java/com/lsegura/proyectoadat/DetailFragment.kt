package com.lsegura.proyectoadat

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.lsegura.proyectoadat.databinding.FragmentDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {

    companion object{
        const val NOTE = "note"
    }

    private lateinit var binding: FragmentDetailBinding
    private lateinit var apiService: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val note = arguments?.getParcelable<Note>(NOTE)

        binding.nota.text = note?.title
        binding.user.text = note?.user?.name
        binding.user.paintFlags = binding.user.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.category.text = note?.category
        binding.description.text = note?.description

        apiService = RetrofitClient.api

        binding.modify.setOnClickListener {
            if (note != null) {
                findNavController().navigate(
                    R.id.action_detailFragment_to_modifyNoteFragment,
                    bundleOf(ModifyNoteFragment.POS to note.id, ModifyNoteFragment.NOTE to note)
                )
            }
        }

        binding.eliminar.setOnClickListener {
            note?.id?.let { it1 ->
                apiService.deleteNote(it1).enqueue(object : Callback<Response<Note>> {
                    override fun onResponse(call: Call<Response<Note>>, response: Response<Response<Note>>) {
                        if (response.isSuccessful) {
                            findNavController().navigate(R.id.action_detailFragment_to_notesFragment)
                        }
                    }

                    override fun onFailure(call: Call<Response<Note>>, t: Throwable) {
                        Log.d("API Response", "Error: ${t.message}")
                    }
                })
            }
        }

        binding.volver.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_notesFragment)
        }

        return binding.root
    }

}