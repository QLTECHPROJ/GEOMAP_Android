package com.geomap.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GeoMapDetailsDao {

    @Query("SELECT * FROM UnderGroundMappingReport ORDER BY uid DESC")
    fun geAllData1ForAll(): List<UnderGroundMappingReport?>

    @Insert
    fun insertUGReport(underGroundMappingReport: UnderGroundMappingReport?)

    @Query("SELECT * FROM UnderGroundMappingReport")
    fun geAllUnderGroundMappingReport(): LiveData<List<UnderGroundMappingReport>>

    @Query("DELETE FROM UnderGroundMappingReport")
    fun deleteAllUnderGroundMappingReport()

    @Insert
    fun insertOCReport(openCastMappingReport: OpenCastMappingReport?)

    @Query("SELECT * FROM OpenCastMappingReport")
    fun geAllOpenCastMappingReport(): LiveData<List<OpenCastMappingReport>>

    @Query("DELETE FROM OpenCastMappingReport")
    fun deleteAllOpenCastMappingReport()

    @Insert
    fun insertAttributeData(attributeData: AttributeData?)

    @Query("SELECT * FROM AttributeData")
    fun geAllAttributeData(): LiveData<List<AttributeData>>

    @Insert
    fun insertNos(nos: Nos?)

    @Query("SELECT * FROM Nos WHERE attributeId =:attributeId")
    fun geAllNos(attributeId: String?): LiveData<List<Nos?>?>?

    @Insert
    fun insertSampleCollected(sampleCollected: SampleCollected?)

    @Query("SELECT * FROM SampleCollected")
    fun geAllSampleCollected(): LiveData<List<SampleCollected>>

    @Insert
    fun insertWeatheringData(weathering: WeatheringData?)

    @Query("SELECT * FROM WeatheringData")
    fun geAllWeatheringData(): LiveData<List<WeatheringData>>

    @Insert
    fun insertRockStrength(rockStrength: RockStrength?)

    @Query("SELECT * FROM RockStrength")
    fun geAllRockStrength(): LiveData<List<RockStrength>>

    @Insert
    fun insertWaterCondition(waterCondition: WaterCondition?)

    @Query("SELECT * FROM WaterCondition")
    fun geAllWaterCondition(): LiveData<List<WaterCondition>>

    @Insert
    fun insertTypeOfGeologicalStructures(typeOfGeologicalStructures: TypeOfGeologicalStructures?)

    @Query("SELECT * FROM TypeOfGeologicalStructures")
    fun geAllTypeOfGeologicalStructures(): LiveData<List<TypeOfGeologicalStructures>>

    @Insert
    fun insertTypeOfFaults(typeOfFaults: TypeOfFaults?)

    @Query("SELECT * FROM TypeOfFaults")
    fun geAllTypeOfFaults(): LiveData<List<TypeOfFaults>>

}