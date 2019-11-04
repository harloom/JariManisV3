package com.app.jarimanis.ui.thread.detail.comentar

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.diskusi.paging.Doc
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_thread_more.*

class ComentarInfo (private val item  : Doc, private val position: Int,  private val callback  : InteractionEditClick): BottomSheetDialogFragment() {

    private lateinit var btnHapus: LinearLayout
    private lateinit var btnEdit: LinearLayout
    private lateinit var btnLapor: LinearLayout


    private val TAG = "DialogProfile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_BottomSheetDialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_thread_more, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true)
        return dialog
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnHapus =txtHapus
        btnLapor = txtReport
        btnEdit = txtEdit
        btnHapus.setOnClickListener {
            val dialog =
                MaterialAlertDialogBuilder(activity).setMessage("Apakah Anda Ingin Menghapus!")
                    .setPositiveButton("OK") { dialogInterface, i ->
                        callback.onDeleteListener(item, position)
                        dismiss()
                    }.setCancelable(false).setNegativeButton("Batal") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }


            dialog.show()
        }

        if (item.user?.id != TokenUser.idUser) {
            btnEdit.visibility = View.GONE
            btnHapus.visibility = View.GONE
        }

        action_dismiss.setOnClickListener {
            dismiss()
        }

        btnEdit.setOnClickListener {
            val newFragment = DialogEditComentar(item,callback)
            if (fragmentManager != null) {
                newFragment.show(fragmentManager!!, "morePost")
            }

        }
        btnLapor.setOnClickListener {
            val dialog =
                MaterialAlertDialogBuilder(activity).setMessage("Apakah Anda Ingin Melapor!")
                    .setPositiveButton("OK") { dialogInterface, i ->
                    }.setCancelable(true).setNegativeButton("Batal") { dialogInterface, i ->
                        callback.onLaporListener(item)
                        dialogInterface.dismiss()
                    }


            dialog.show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun log(s: String) {
        Log.d(TAG, "Log : $s")
    }

    private fun deletePost(itemId: String) {



    }
}
