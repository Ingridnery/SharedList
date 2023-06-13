package br.edu.scl.ifsp.inery.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Task (
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    @NonNull var title: String = "",
    @NonNull var description: String = "",
    @NonNull var dateCreate: String = "",
    @NonNull var datePreviousFinish: String = "",
    @NonNull var user: String = "",
    @NonNull var isCompleted: Boolean = false,
    var userConcluded: String = "",
): Parcelable