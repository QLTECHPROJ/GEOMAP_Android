package com.geomap.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GeoMapDetailsDao {

    @Query("SELECT * FROM undergroundmappingreport ORDER BY uid DESC")
    fun geAllData1ForAll(): List<UnderGroundMappingReport?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUGReport(underGroundMappingReport: UnderGroundMappingReport?)

    @Query("SELECT * FROM undergroundmappingreport")
    fun geAllUnderGroundMappingReport(): List<UnderGroundMappingReport>

    @Query("SELECT * FROM undergroundmappingreport ORDER BY uid ASC")
    fun geAllUnderGroundMappingReportASC(): LiveData<List<UnderGroundMappingReport>>

    @Query("SELECT * FROM undergroundmappingreport ORDER BY uid DESC")
    fun geAllUnderGroundMappingReport1(): LiveData<List<UnderGroundMappingReport>>

    @Query("DELETE FROM UnderGroundMappingReport")
    fun deleteUnderGroundMappingReport()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOCReport(openCastMappingReport: OpenCastMappingReport?)

    @Query("SELECT * FROM opencastmappingreport")
    fun geAllOpenCastMappingReport(): List<OpenCastMappingReport>

    @Query("SELECT * FROM opencastmappingreport  ORDER BY uid ASC")
    fun geAllOpenCastMappingReportASC(): LiveData<List<OpenCastMappingReport>>

    @Query("SELECT * FROM opencastmappingreport  ORDER BY uid DESC")
    fun geAllOpenCastMappingReport1(): LiveData<List<OpenCastMappingReport>>

    @Query("DELETE FROM OpenCastMappingReport")
    fun deleteOpenCastMappingReport()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttributeData(attributeData: AttributeData?)

    @Query("SELECT * FROM attributdata")
    fun geAllAttributeData(): List<AttributeData>

    @Query("DELETE FROM attributdata")
    fun deleteAttributeData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNos(nos: Nos?)

    @Query("SELECT * FROM nos WHERE attributeId =:attributeId")
    fun geAllNos(attributeId: Int?): List<Nos>

    @Query("DELETE FROM nos")
    fun deleteNos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSampleCollected(sampleCollected: SampleCollected?)

    @Query("SELECT * FROM samplecollected")
    fun geAllSampleCollected(): List<SampleCollected>

    @Query("DELETE FROM samplecollected")
    fun deleteSampleCollected()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatheringData(weathering: WeatheringData?)

    @Query("SELECT * FROM weatheringdata")
    fun geAllWeatheringData(): List<WeatheringData>

    @Query("DELETE FROM weatheringdata")
    fun deleteWeatheringData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRockStrength(rockStrength: RockStrength?)

    @Query("SELECT * FROM rocksStrength")
    fun geAllRockStrength(): List<RockStrength>

    @Query("DELETE FROM rocksStrength")
    fun deleteRockStrength()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWaterCondition(waterCondition: WaterCondition?)

    @Query("SELECT * FROM watercondition")
    fun geAllWaterCondition(): List<WaterCondition>

    @Query("DELETE FROM watercondition")
    fun deleteWaterCondition()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTypeOfGeologicalStructures(typeOfGeologicalStructures: TypeOfGeologicalStructures?)

    @Query("SELECT * FROM typeofgeologicalstructures")
    fun geAllTypeOfGeologicalStructures(): List<TypeOfGeologicalStructures>

    @Query("DELETE FROM typeofgeologicalstructures")
    fun deleteTypeOfGeologicalStructures()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTypeOfFaults(typeOfFaults: TypeOfFaults?)

    @Query("SELECT * FROM typeoffaults")
    fun geAllTypeOfFaults():List<TypeOfFaults>

    @Query("DELETE FROM typeoffaults")
    fun deleteTypeOfFaults()

// ORDER BY uid ASC
}