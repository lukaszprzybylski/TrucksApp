package data.model

class TruckResponse : ArrayList<TruckResponse.TruckResponseItem>(){
    data class TruckResponseItem(
        val vehicleId: String,
        val location: List<Double>,
    )
}