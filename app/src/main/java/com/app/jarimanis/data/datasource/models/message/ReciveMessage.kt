package com.app.jarimanis.data.datasource.models.message

import android.os.Parcelable
import com.app.jarimanis.data.datasource.models.chats.UserChat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReciveMessage(
    @DocumentId
     val id : String?=null,
     val _id : String?=null,
     val _image :String?= null,
     val _message :String?= null,
     val _user :String?=null,
     val _video : String?=null,
     val timestamp :Timestamp? =null,
     var status : Int? =null
): Parcelable
