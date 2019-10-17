package com.app.jarimanis.ui.thread

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_edit_post.*

class DialogEditThread(
    private val item: Doc,
     private val callback: InteractionEditClick
) : DialogFragment() {


    private lateinit var _title: EditText
    private lateinit var _content: EditText


    private var tempTitle = ""
    private var tempConten =  ""

    private val TAG = "DialogProfile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_edit_post, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        return dialog
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _title = view.findViewById(R.id.txt_edit_title)
        _content = view.findViewById(R.id.txt_edit_content)
        _title.setText(item.title)
        _content.setText(item.content)

        tempTitle = _title.text.toString()
        tempConten = _content.text.toString()
        action_dismiss.setOnClickListener {

            if(tempConten != _content.text.toString() || tempTitle != _title.text.toString()){
                val dialogSimpan =
                    MaterialAlertDialogBuilder(activity).setMessage("Apakah Anda Ingin Menyimpan!")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            callback.onEditListerner(item.id!!,_title.text.toString() ,_content.text.toString())
                            this.dismiss()
                        }.setCancelable(true).setNegativeButton("Batal") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            this.dismiss()
                        }


                dialogSimpan.show()
            }else{
                dismiss()
            }

        }

    }

    private fun editPost() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun log(s: String) {
        Log.d(TAG, "Log : $s")
    }
}
