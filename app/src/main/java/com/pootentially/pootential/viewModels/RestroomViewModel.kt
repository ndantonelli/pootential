package com.pootentially.pootential.viewModels

import android.arch.lifecycle.*
import android.arch.lifecycle.Transformations.switchMap
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.mapbox.mapboxsdk.geometry.VisibleRegion
import com.pootentially.pootential.utility.Models.ChangedRestroom
import com.pootentially.pootential.utility.Models.Enumerations
import com.pootentially.pootential.utility.firebase.FirebaseRepository
import com.pootentially.pootential.utility.firebase.models.Restroom
import javax.inject.Inject

/**
 * Created by nick on 2/6/18.
 */
class RestroomViewModel: ViewModel() {
    private val firestoreRef by lazy { FirebaseFirestore.getInstance() }
    private var restroomListener: ListenerRegistration? = null

    private var restroomChanges: MutableLiveData<List<ChangedRestroom>> = MutableLiveData()
    private val displayedRestrooms: MutableList<Restroom> = mutableListOf()

    fun getChanges(): LiveData<List<ChangedRestroom>>{
        return restroomChanges
    }

    private fun loadRestrooms(lowerGeoPoint: GeoPoint, greaterGeoPoint: GeoPoint){
        if(restroomListener != null){
            restroomListener?.remove()
        }
        restroomListener = firestoreRef.collection("restrooms")
                .whereGreaterThanOrEqualTo("lon", greaterGeoPoint.longitude)
                .whereLessThanOrEqualTo("lon", lowerGeoPoint.longitude)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(firebaseFirestoreException != null){
                //TODO: manage the error
            }
            val changedRestrooms: MutableList<ChangedRestroom> = mutableListOf()
            querySnapshot.documentChanges.forEach{
                val restroom = it.document.toObject(Restroom::class.java)
                if(restroom.lat < greaterGeoPoint.latitude && restroom.lat > lowerGeoPoint.latitude) {
                    val diffedRestroom = diffRestroom(restroom, it.type)
                    if (diffedRestroom != null) {
                        changedRestrooms.add(diffedRestroom)
                    }
                }
            }
            restroomChanges.value = changedRestrooms
        }
    }

    fun diffRestroom(restroom: Restroom, changeType: DocumentChange.Type? = null): ChangedRestroom?{
        when(changeType){
            DocumentChange.Type.ADDED -> {
                if(!displayedRestrooms.contains(restroom)){
                    displayedRestrooms.add(restroom)
                    return ChangedRestroom(Enumerations.ChangedStatus.ADDED, restroom)
                }
            }
            DocumentChange.Type.REMOVED -> {
                if(displayedRestrooms.contains(restroom)){
                    val index: Int = displayedRestrooms.indexOf(restroom)
                    restroom.marker = displayedRestrooms[index].marker
                    displayedRestrooms.remove(restroom)
                    return ChangedRestroom(Enumerations.ChangedStatus.REMOVED, restroom)
                }
            }
            else -> {
                //Todo: add some checking for if its not one of those types.  what should we do if its not?
                //would we ever modify some of the info for a restroom on the fly?  Is it that important?
            }
        }
        return null
    }

    override fun onCleared() {
        super.onCleared()
        if(restroomListener != null){
            restroomListener?.remove()
            restroomListener = null
        }
    }

    fun updateVisibleRegion(visibleRegion: VisibleRegion){
        val higherGeoPoint = GeoPoint(visibleRegion.farLeft.latitude, visibleRegion.farLeft.longitude)
        val lowerGeoPoint = GeoPoint(visibleRegion.nearRight.latitude, visibleRegion.nearRight.longitude)
        val changedRestrooms: MutableList<ChangedRestroom> = mutableListOf()
        val restroomsToRemove: MutableList<Restroom> = mutableListOf()
        displayedRestrooms.forEach {
            if(it.lon < higherGeoPoint.longitude
                || it.lon > lowerGeoPoint.longitude
                || it.lat > higherGeoPoint.latitude
                || it.lat < lowerGeoPoint.latitude
            ){
                changedRestrooms.add(ChangedRestroom(Enumerations.ChangedStatus.REMOVED, it))
                restroomsToRemove.add(it)
            }
        }
        displayedRestrooms.removeAll(restroomsToRemove)
        restroomChanges.value = changedRestrooms
        loadRestrooms(lowerGeoPoint, higherGeoPoint)
    }

    companion object{
        fun create(activity: AppCompatActivity): RestroomViewModel{
            return ViewModelProviders.of(activity).get(RestroomViewModel::class.java)
        }
    }

}