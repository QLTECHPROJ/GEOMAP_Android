package com.geomap.roomDataBase

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GeoMapDetailsDao {
    @Query("SELECT * FROM undergroundmappingreport ORDER BY uid DESC")
    fun geAllData1ForAll(): List<UnderGroundMappingReport?>

    @TypeConverters(Converters::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUGReport(underGroundMappingReport: UnderGroundMappingReport?)

    @TypeConverters(Converters::class)
    @Query("UPDATE undergroundmappingreport set name =:name,mapSerialNo =:mapSerialNo,ugDate =:ugDate,attributes =:attributes,shift =:shift,mappedBy =:mappedBy,scale =:scale,location =:location,veinOrLoad =:veinOrLoad,xCordinate =:xCordinate,yCordinate =:yCordinate,zCordinate =:zCordinate,roofImage =:roofImage,faceImage =:faceImage,leftImage =:leftImage,rightImage =:rightImage,comment =:comment WHERE uid =:uid")
    fun updateUGReport(name: String?, mapSerialNo: String?, ugDate: String?, attributes: String?, shift: String?,
        mappedBy: String?, scale: String?, location: String?, veinOrLoad: String?,
        xCordinate: String?, yCordinate: String?, zCordinate: String?, roofImage: Bitmap?,
        faceImage: Bitmap?, leftImage: Bitmap?, rightImage: Bitmap?, comment: String?, uid: Int?)

    @Query("SELECT * FROM undergroundmappingreport WHERE userId =:userId")
    fun geAllUnderGroundMappingReport(userId: String?): List<UnderGroundMappingReport>

    @Query("SELECT * FROM undergroundmappingreport  WHERE userId =:userId ORDER BY uid ASC")
    fun geAllUnderGroundMappingReportASC(userId: String?): LiveData<List<UnderGroundMappingReport>>

    @Query("SELECT * FROM undergroundmappingreport  WHERE userId =:userId ORDER BY uid DESC")
    fun geAllUnderGroundMappingReport1(userId: String?): LiveData<List<UnderGroundMappingReport>>

    @Query("DELETE FROM UnderGroundMappingReport WHERE userId =:userId")
    fun deleteUnderGroundMappingReport(userId: String?)

    @Query("DELETE FROM UnderGroundMappingReport  WHERE uid =:uid")
    fun deleteUnderGroundMappingReportByUid(uid: Int?)

    @TypeConverters(Converters::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOCReport(openCastMappingReport: OpenCastMappingReport?)

    @TypeConverters(Converters::class)
    @Query("UPDATE opencastmappingreport set ocDate=:ocDate,mappingSheetNo=:mappingSheetNo,minesSiteName=:minesSiteName,pitName=:pitName,pitLocation=:pitLocation,shiftInChargeName=:shiftInChargeName,geologistName=:geologistName,shift=:shift,faceLocation=:faceLocation,faceLength=:faceLength,faceArea=:faceArea,faceRockTypes=:faceRockTypes,benchRL=:benchRL,benchHeightWidth=:benchHeightWidth,benchAngle=:benchAngle,dipDirectionAngle=:dipDirectionAngle,thicknessOfOre=:thicknessOfOre,thicknessOfOverburden=:thicknessOfOverburden,thicknessOfInterBurden=:thicknessOfInterBurden,observedGradeOfOre=:observedGradeOfOre,sampleCollected=:sampleCollected,actualGradOfOre=:actualGradOfOre,weathering=:weathering,rockStrength=:rockStrength,waterCondition=:waterCondition,typeOfGeologicalStructures=:typeOfGeologicalStructures,typeOfFaults=:typeOfFaults,notes=:notes,geologistSign=:geologistSign,clientsGeologistSign=:clientsGeologistSign,image=:image WHERE uid=:uid")
    fun updateOCReport(ocDate: String?, mappingSheetNo: String?, minesSiteName: String?, pitName: String?, pitLocation: String?, shiftInChargeName: String?, geologistName: String?, shift: String?, faceLocation: String?, faceLength: String?, faceArea: String?, faceRockTypes: String?, benchRL: String?, benchHeightWidth: String?, benchAngle: String?, dipDirectionAngle: String?, thicknessOfOre: String?, thicknessOfOverburden: String?, thicknessOfInterBurden: String?, observedGradeOfOre: String?, sampleCollected: String?, actualGradOfOre: String?, weathering: String?, rockStrength: String?, waterCondition: String?, typeOfGeologicalStructures: String?, typeOfFaults: String?, notes: String?, geologistSign: Bitmap?, clientsGeologistSign: Bitmap?, image: Bitmap?, uid: Int?)

    @Query("SELECT * FROM opencastmappingreport  WHERE userId =:userId")
    fun geAllOpenCastMappingReport(userId: String?): List<OpenCastMappingReport>

    @Query("SELECT * FROM opencastmappingreport   WHERE userId =:userId ORDER BY uid ASC")
    fun geAllOpenCastMappingReportASC(userId: String?): LiveData<List<OpenCastMappingReport>>

    @Query("SELECT * FROM opencastmappingreport   WHERE userId =:userId ORDER BY uid DESC")
    fun geAllOpenCastMappingReport1(userId: String?): LiveData<List<OpenCastMappingReport>>

    @Query("DELETE FROM OpenCastMappingReport  WHERE userId =:userId")
    fun deleteOpenCastMappingReport(userId: String?)

    @Query("DELETE FROM OpenCastMappingReport  WHERE uid =:uid")
    fun deleteOpenCastMappingReportByUid(uid: Int?)

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
    fun geAllTypeOfFaults(): List<TypeOfFaults>

    @Query("DELETE FROM typeoffaults")
    fun deleteTypeOfFaults() // ORDER BY uid ASC

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeologist(geologist: Geologist?)

    @Query("SELECT * FROM Geologist")
    fun geAllGeologist(): List<Geologist>

    @Query("DELETE FROM geologist")
    fun deleteGeologist() // ORDER BY uid ASC
}