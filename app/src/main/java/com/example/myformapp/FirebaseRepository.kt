package com.example.myformapp

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseRepository {
    private val db = FirebaseDatabase.getInstance().getReference("forms")

    fun addform(form: Form /*, onResult: (Boolean) -> Unit*/) {
        // Create a new push ID for the form
        val id = db.push().key!!
        // Save the form with the generated ID
        db.child(id).setValue(form.copy(id = id))
        /*.addOnCompleteListener {
            onResult(it.isSuccessful)
        }*/
    }
    fun updateform(form: Form) {
        db.child(form.id).setValue(form) //Update the form with the given ID
    }

    fun deleteform(formId: String) {
        db.child(formId).removeValue() // Remove the form with the given ID
    }

    fun getforms(onDataChange: (List<Form>) -> Unit) {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val formList = mutableListOf<Form>()
                snapshot.children.forEach { child ->
                    val form = child.getValue(Form::class.java)
                    if (form != null) {
                        formList.add(form)
                    }
                }
                onDataChange(formList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional: log or handle the error
            }
        })
    }
}
