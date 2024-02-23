package com.lsegura.proyectoadat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lsegura.proyectoadat.databinding.ViewNoteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotesAdapter(
    val notes: List<Note>,
    private val apiService: ApiInterface,
    val listener: (Note) -> Unit
) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])

        holder.itemView.setOnClickListener {
            val noteId = notes[position].id
            if (noteId != null) {
                apiService.getNoteById(noteId).enqueue(object : Callback<Note> {
                    override fun onResponse(call: Call<Note>, response: Response<Note>) {
                        if (response.isSuccessful) {
                            val note = response.body()
                            if (note != null) {
                                listener(note)
                            }
                        } else {
                            Log.e("Cagada", "Efectivamente Lucía la has cagado")
                        }
                    }

                    override fun onFailure(call: Call<Note>, t: Throwable) {
                        Log.e("Cagada", "Efectivamente Lucía la has cagado")
                    }
                })
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewNoteBinding.bind(view)

        fun bind(note: Note) {
            binding.nombre.text = note.title
        }
    }
}