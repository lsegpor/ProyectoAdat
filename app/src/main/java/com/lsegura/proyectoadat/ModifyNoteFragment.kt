package com.lsegura.proyectoadat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lsegura.proyectoadat.databinding.FragmentModifyNoteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyNoteFragment : Fragment() {

    companion object {
        const val POS = "pos"
        const val NOTE = "note"
    }

    private lateinit var binding: FragmentModifyNoteBinding
    private lateinit var apiService: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentModifyNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentModifyNoteBinding.bind(view)

        apiService = RetrofitClient.api

        val position = arguments?.getInt(POS)

        if (position != -1) {
            val note = arguments?.getParcelable<Note>(NOTE)
            binding.title.setText(note?.title)
            binding.description.setText(note?.description)
            binding.category.setText(note?.category)
            binding.user.setText(note?.user?.id.toString())
        }

        binding.cancelar.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.guardar.setOnClickListener {
            if (position == -1) {
                val userName = binding.user.text.toString().toInt()
                if (userName != null) {
                    apiService.getUserById(userName).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                val user = response.body()!!
                                val note =
                                    Note(
                                        0,
                                        binding.title.text.toString(),
                                        binding.description.text.toString(),
                                        binding.category.text.toString(),
                                        user
                                    )
                                createNote(note)
                            } else {
                                Toast.makeText(
                                    context,
                                    "El usuario no existe",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.e("Cagada", "Efectivamente Lucía la has cagado")
                        }
                    })
                }
            } else {
                val userName = binding.user.text.toString().toInt()
                if (userName != null) {
                    apiService.getUserById(userName).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                val user = response.body()!!
                                val note =
                                    Note(
                                        0,
                                        binding.title.text.toString(),
                                        binding.description.text.toString(),
                                        binding.category.text.toString(),
                                        user
                                    )
                                if (position != null) {
                                    updateNote(position, note)
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "El usuario no existe",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.e("Cagada", "Efectivamente Lucía la has cagado")
                        }
                    })
                }
            }
        }

    }

    private fun createNote(note: Note) {
        apiService.createNote(note).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    activity?.onBackPressed()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(
                        "Cagada1",
                        "Respuesta no exitosa: ${response.code()}, ${response.message()}, Body: $errorBody"
                    )
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.e("Cagada2", "Efectivamente Lucía la has cagado")
            }
        })
    }

    private fun updateNote(id: Int, note: Note) {
        apiService.updateNote(id, note).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    val note = response.body()!!
                    findNavController().navigate(
                        R.id.action_modifyNoteFragment_to_detailFragment,
                        bundleOf(DetailFragment.NOTE to note)
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(
                        "Cagada1",
                        "Respuesta no exitosa: ${response.code()}, ${response.message()}, Body: $errorBody"
                    )
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.e("Cagada2", "Efectivamente Lucía la has cagado")
            }
        })
    }

}