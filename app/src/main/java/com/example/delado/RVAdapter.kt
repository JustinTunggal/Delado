package com.example.delado

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import java.util.HashMap

class RVAdapter(options: FirebaseRecyclerOptions<RVModel>, private val userId: String) :
    FirebaseRecyclerAdapter<RVModel, RVAdapter.MyViewHolder>(options) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: RVModel) {
        holder.namakegiatan.text = model.namakegiatan
        holder.deskripsi.text = model.desc
        holder.deadline.text = model.deadline

        holder.btnedit.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.itemView.context)
                .setContentHolder(ViewHolder(R.layout.popup_update)).setExpanded(true, 1200).create()

            val view = dialogPlus.holderView

            val namakegiatan = view.findViewById<EditText>(R.id.editnamakegiatan)
            val desc = view.findViewById<EditText>(R.id.editdesc)
            val deadline = view.findViewById<EditText>(R.id.editdeadline)

            val btnUpdate = view.findViewById<Button>(R.id.btnupdate)

            namakegiatan.setText(model.namakegiatan)
            desc.setText(model.desc)
            deadline.setText(model.deadline)

            dialogPlus.show()

            btnUpdate.setOnClickListener {
                val map: MutableMap<String, Any> = HashMap()
                map["namakegiatan"] = namakegiatan.text.toString()
                map["desc"] = desc.text.toString()
                map["deadline"] = deadline.text.toString()

                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("todo")
                    .child(getRef(position).key ?: "").updateChildren(map)
                    .addOnSuccessListener {
                        Toast.makeText(holder.namakegiatan.context, "Data Updated", Toast.LENGTH_SHORT).show()
                        dialogPlus.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(holder.namakegiatan.context, "Update Failed", Toast.LENGTH_SHORT).show()
                        dialogPlus.dismiss()
                    }
            }
        }

        holder.btndelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.namakegiatan.context)
            builder.setTitle("Are You Sure?")
            builder.setMessage("Deleted Data Cannot Be Undone")

            builder.setPositiveButton("Delete") { _, _ ->
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("todo")
                    .child(getRef(position).key ?: "").removeValue()

                Toast.makeText(holder.namakegiatan.context, "Deleted", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(holder.namakegiatan.context, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namakegiatan: TextView = itemView.findViewById(R.id.namakegiatan)
        val deskripsi: TextView = itemView.findViewById(R.id.desc)
        val deadline: TextView = itemView.findViewById(R.id.deadline)
        val btnedit: Button = itemView.findViewById(R.id.btnedit)
        val btndelete: Button = itemView.findViewById(R.id.btndelete)
    }
}
