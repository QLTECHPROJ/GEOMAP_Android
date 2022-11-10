package com.geomap

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.geomap.roomDataBase.*
import com.geomap.userModule.models.UserCommonDataModel

class DataBaseFunctions {
    companion object {
        var DB: GeoMapDatabase? = null
        fun getDataBase(ctx: Context?): GeoMapDatabase? {
            DB = Room.databaseBuilder(ctx!!, GeoMapDatabase::class.java, "GeoMap_database").build()
            return DB
        }

        fun callUGReportObserver(ctx: Context): List<UnderGroundMappingReport> {
            DB = getDataBase(ctx)
            var list = ArrayList<UnderGroundMappingReport>()
            DB?.taskDao()?.geAllUnderGroundMappingReport()?.observe(
                ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<UnderGroundMappingReport>
                DB!!.taskDao().geAllUnderGroundMappingReport().removeObserver {}
            }
            return list
        }

        fun saveUGReport(obj: UnderGroundMappingReport, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertUGReport(obj)
            }
        }

        fun deleteUGReport(ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.deleteAllUnderGroundMappingReport()
            }
        }

        fun callOcReportObserver(ctx: Context): List<OpenCastMappingReport> {
            DB = getDataBase(ctx)
            var list = ArrayList<OpenCastMappingReport>()
            DB?.taskDao()?.geAllOpenCastMappingReport()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<OpenCastMappingReport>
                DB!!.taskDao().geAllOpenCastMappingReport().removeObserver {}
            }
            return list
        }

        fun saveOCReport(obj: OpenCastMappingReport, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertOCReport(obj)
            }
        }

        fun deleteOCReport(ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.deleteAllOpenCastMappingReport()
            }
        }

        fun callAttributeDataObserver(ctx: Context): List<AttributeData> {
            DB = getDataBase(ctx)
            var list = ArrayList<AttributeData>()
            DB?.taskDao()?.geAllAttributeData()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<AttributeData>
                DB!!.taskDao().geAllAttributeData().removeObserver {}
            }
            return list
        }



        fun saveAttributeData(obj: AttributeData, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertAttributeData(obj)
            }
        }

        fun callNosObserver(ctx: Context,attributeId:String): List<Nos> {
            DB = getDataBase(ctx)
            var list = ArrayList<Nos>()
            DB?.taskDao()?.geAllNos(attributeId)?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<Nos>
            }
            return list
        }

        fun saveNos(nos: Nos, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertNos(nos)
            }
        }

        fun callSampleCollectedObserver(ctx: Context): List<SampleCollected> {
            DB = getDataBase(ctx)
            var list = ArrayList<SampleCollected>()
            DB?.taskDao()?.geAllSampleCollected()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<SampleCollected>
                DB!!.taskDao().geAllSampleCollected().removeObserver {}
            }
            return list
        }

        fun saveSampleCollected(obj: SampleCollected, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertSampleCollected(obj)
            }
        }

        fun callWeatheringDataObserver(ctx: Context): List<WeatheringData> {
            DB = getDataBase(ctx)
            var list = ArrayList<WeatheringData>()
            DB?.taskDao()?.geAllWeatheringData()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<WeatheringData>
                DB!!.taskDao().geAllWeatheringData().removeObserver {}
            }
            return list
        }
     //   UndergroundReport: [ { "name": "test","shift": "test shift","mappedBy": "test map","scale": "2*3","location": "test loc","venieLoad": "test load","xCordinate": "85","yCordinate": "88","zCordinate": "88","comment": "test conten","mapSerialNo": "123","ugDate": "1-Nov-2022","leftImage": "bfvbdf","roofImage": "nuvfxll","rightImage": "nvxvull","faceImage": "nvxvull","userId": "1","attribute": "[ {"undergroundId": 1,"name": "test","nose": "test nose",  "properties": "dssdv"},{"undergroundId": 1,"name": "test","nose": "test nose",  "properties": "dssdv"},{"undergroundId": 1,"name": "test","nose": "test nose",  "properties": "dssdv"}]" }]
      //  OpenCastReport:[{"minesSiteName": "test name","mappingSheetNo": "12312","pitName": "9797","pitLoaction": "gkgk","shiftInchargeName": "gkjggkjg","geologistName": "jgjgkjgkjg","mappingParameter": "jgjkgkjgjkgjk","faceLocation": "gjkgkjgjg","faceLength": "gjhgg","faceArea": "gjhk","faceRockType": "kjgjgjgjg","benchRl": "jkgjgjkgkjgkj","benchHeightWidth": "gjkgkjgjgj","benchAngle": "gkjgjk","thicknessOfOre": "jgjkgj","thicknessOfOverburdan": "gkjgkjgkjgkjgjgjg","thicknessOfInterburden": "jg","observedGradeOfOre": "jgkjgkjgkjg","sampleColledted": "jhg","actualGradeOfOre": "jghkj","weathring": "jhgjk","rockStregth": "jgkjkkggg","waterCondition": "5+5","typeOfGeologist": "564","typeOfFaults": "456564","shift": "Day Shift","ocDate": "1-Nov-2022","userId": "1","dipDirectionAndAngle": "mbmbm","geologistSign": "mnbnmnbmnbm","clientsGeologistSign": "" }]

        fun saveWeatheringData(obj: WeatheringData, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertWeatheringData(obj)
            }
        }

        fun callRockStrengthObserver(ctx: Context): List<RockStrength> {
            DB = getDataBase(ctx)
            var list = ArrayList<RockStrength>()
            DB?.taskDao()?.geAllRockStrength()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<RockStrength>
                DB!!.taskDao().geAllRockStrength().removeObserver {}
            }
            return list
        }


        fun saveRockStrength(obj: RockStrength, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertRockStrength(obj)
            }
        }

        fun callWaterConditionObserver(ctx: Context): List<WaterCondition> {
            DB = getDataBase(ctx)
            var list = ArrayList<WaterCondition>()
            DB?.taskDao()?.geAllWaterCondition()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<WaterCondition>
                DB!!.taskDao().geAllWaterCondition().removeObserver {}
            }
            return list
        }

        fun saveWaterCondition(obj: WaterCondition, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertWaterCondition(obj)
            }
        }

        fun callTypeOfGeologicalStructuresObserver(ctx: Context): List<TypeOfGeologicalStructures> {
            DB = getDataBase(ctx)
            var list = ArrayList<TypeOfGeologicalStructures>()
            DB?.taskDao()?.geAllTypeOfGeologicalStructures()?.observe(
                ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<TypeOfGeologicalStructures>
                DB!!.taskDao().geAllTypeOfGeologicalStructures().removeObserver {}
            }
            return list
        }

        fun saveTypeOfGeologicalStructures(obj: TypeOfGeologicalStructures, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertTypeOfGeologicalStructures(obj)
            }
        }

        fun callTypeOfFaultsObserver(ctx: Context): List<TypeOfFaults> {
            DB = getDataBase(ctx)
            var list = ArrayList<TypeOfFaults>()
            DB?.taskDao()?.geAllTypeOfFaults()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<TypeOfFaults>
                DB!!.taskDao().geAllTypeOfFaults().removeObserver {}
            }
            return list
        }

        fun saveTypeOfFaults(obj: TypeOfFaults, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertTypeOfFaults(obj)
            }
        }

        fun callLocalDBGetAndInsertFunction(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            val listTypeOfFaults = callTypeOfFaultsObserver(ctx)
            if (listTypeOfFaults.isEmpty()) {
                for (i in responseData.typeOfFaults!!.indices) {
                    val obj = TypeOfFaults()
                    obj.iD = responseData.typeOfFaults!![i].id
                    obj.name = responseData.typeOfFaults!![i].name
                    obj.createDate = responseData.typeOfFaults!![i].createdAt
                    obj.updateDate = responseData.typeOfFaults!![i].updatedAt
                    saveTypeOfFaults(obj, ctx)
                    Log.e("savetypeOfFaults", "true")
                }
            } else {
                for (i in responseData.typeOfFaults!!.indices) {
                    val obj = TypeOfFaults()
                    obj.iD = responseData.typeOfFaults!![i].id
                    obj.name = responseData.typeOfFaults!![i].name
                    obj.createDate = responseData.typeOfFaults!![i].createdAt
                    obj.updateDate = responseData.typeOfFaults!![i].updatedAt
                    if (!listTypeOfFaults.contains(obj)) {
                        saveTypeOfFaults(obj, ctx)

                        Log.e("savetypeOfFaults", "true   " + obj.name)
                    }
                }
            }
            val listRockStrength = callRockStrengthObserver(ctx)
            if (listRockStrength.isEmpty()) {
                for (i in responseData.rockStrength!!.indices) {
                    val obj = RockStrength()
                    obj.iD = responseData.rockStrength!![i].id
                    obj.name = responseData.rockStrength!![i].name
                    obj.createDate = responseData.rockStrength!![i].createdAt
                    obj.updateDate = responseData.rockStrength!![i].updatedAt
                    saveRockStrength(obj, ctx)
                    Log.e("saveRockStrength", "true")
                }
            } else {
                for (i in responseData.rockStrength!!.indices) {
                    val obj = RockStrength()
                    obj.iD = responseData.rockStrength!![i].id
                    obj.name = responseData.rockStrength!![i].name
                    obj.createDate = responseData.rockStrength!![i].createdAt
                    obj.updateDate = responseData.rockStrength!![i].updatedAt

                    if (!listRockStrength.contains(obj)) {
                        saveRockStrength(obj, ctx)
                        Log.e("saveRockStrength", "true   " + obj.name)
                    }
                }
            }

            val listSampleCollected = callSampleCollectedObserver(ctx)
            if (listSampleCollected.isEmpty()) {
                for (i in responseData.sampleCollected!!.indices) {
                    val obj = SampleCollected()
                    obj.iD = responseData.sampleCollected!![i].id
                    obj.name = responseData.sampleCollected!![i].name
                    obj.createDate = responseData.sampleCollected!![i].createdAt
                    obj.updateDate = responseData.sampleCollected!![i].updatedAt
                    saveSampleCollected(obj, ctx)
                    Log.e("saveSampleCollected", "true")
                }
            } else {
                for (i in responseData.sampleCollected!!.indices) {
                    val obj = SampleCollected()
                    obj.iD = responseData.sampleCollected!![i].id
                    obj.name = responseData.sampleCollected!![i].name
                    obj.createDate = responseData.sampleCollected!![i].createdAt
                    obj.updateDate = responseData.sampleCollected!![i].updatedAt

                    if (!listSampleCollected.contains(obj)) {
                        saveSampleCollected(obj, ctx)
                        Log.e("saveSampleCollected", "true   " + obj.name)
                    }
                }
            }
            val listTypeOfGeologicalStructures = callTypeOfGeologicalStructuresObserver(ctx)
            if (listTypeOfGeologicalStructures.isEmpty()) {
                for (i in responseData.typeOfGeologicalStructures!!.indices) {
                    val obj = TypeOfGeologicalStructures()
                    obj.iD = responseData.typeOfGeologicalStructures!![i].id
                    obj.name = responseData.typeOfGeologicalStructures!![i].name
                    obj.createDate = responseData.typeOfGeologicalStructures!![i].createdAt
                    obj.updateDate = responseData.typeOfGeologicalStructures!![i].updatedAt
                    saveTypeOfGeologicalStructures(obj, ctx)
                    Log.e("saveTypeOfGeologicalStructures", "true")
                }
            } else {
                for (i in responseData.typeOfGeologicalStructures!!.indices) {
                    val obj = TypeOfGeologicalStructures()
                    obj.iD = responseData.typeOfGeologicalStructures!![i].id
                    obj.name = responseData.typeOfGeologicalStructures!![i].name
                    obj.createDate = responseData.typeOfGeologicalStructures!![i].createdAt
                    obj.updateDate = responseData.typeOfGeologicalStructures!![i].updatedAt

                    if (!listTypeOfGeologicalStructures.contains(obj)) {
                        saveTypeOfGeologicalStructures(obj, ctx)
                        Log.e("saveTypeOfGeologicalStructures", "true   " + obj.name)
                    }
                }
            }
            val listWaterCondition = callWaterConditionObserver(ctx)
            if (listWaterCondition.isEmpty()) {
                for (i in responseData.waterCondition!!.indices) {
                    val obj = WaterCondition()
                    obj.iD = responseData.waterCondition!![i].id
                    obj.name = responseData.waterCondition!![i].name
                    obj.createDate = responseData.waterCondition!![i].createdAt
                    obj.updateDate = responseData.waterCondition!![i].updatedAt
                    saveWaterCondition(obj, ctx)
                    Log.e("saveWaterCondition", "true")
                }
            } else {
                for (i in responseData.waterCondition!!.indices) {
                    val obj = WaterCondition()
                    obj.iD = responseData.waterCondition!![i].id
                    obj.name = responseData.waterCondition!![i].name
                    obj.createDate = responseData.waterCondition!![i].createdAt
                    obj.updateDate = responseData.waterCondition!![i].updatedAt

                    if (!listWaterCondition.contains(obj)) {
                        saveWaterCondition(obj, ctx)
                        Log.e("saveWaterCondition", "true   " + obj.name)
                    }
                }
            }
            val listWeatheringData = callWeatheringDataObserver(ctx)
            if (listWeatheringData.isEmpty()) {
                for (i in responseData.weatheringData!!.indices) {
                    val obj = WeatheringData()
                    obj.iD = responseData.weatheringData!![i].id
                    obj.name = responseData.weatheringData!![i].name
                    obj.createDate = responseData.weatheringData!![i].createdAt
                    obj.updateDate = responseData.weatheringData!![i].updatedAt
                    saveWeatheringData(obj, ctx)
                    Log.e("saveweatheringData", "true")
                }
            } else {
                for (i in responseData.weatheringData!!.indices) {
                    val obj = WeatheringData()
                    obj.iD = responseData.weatheringData!![i].id
                    obj.name = responseData.weatheringData!![i].name
                    obj.createDate = responseData.weatheringData!![i].createdAt
                    obj.updateDate = responseData.weatheringData!![i].updatedAt

                    if (!listWeatheringData.contains(obj)) {
                        saveWeatheringData(obj, ctx)
                        Log.e("saveweatheringData", "true   " + obj.name)
                    }
                }
            }

            val listAttributeData = callAttributeDataObserver(ctx)
            if (listAttributeData.isEmpty()) {
                for (i in responseData.attributeData!!.indices) {
                    val obj = AttributeData()
                    obj.iD = responseData.attributeData!![i].id
                    obj.name = responseData.attributeData!![i].name
                    obj.createDate = responseData.attributeData!![i].createdAt
                    obj.updateDate = responseData.attributeData!![i].updatedAt
                    saveAttributeData(obj, ctx)
                    Log.e("saveAttributeData", "true")
                    for (j in responseData.attributeData!![i].nos!!.indices) {
                        val objNos = Nos()
                        objNos.iD = responseData.attributeData!![i].nos!![j].id
                        objNos.name = responseData.attributeData!![i].nos!![j].name
                        objNos.attributeId = responseData.attributeData!![i].nos!![j].attributeId
                        objNos.createDate = responseData.attributeData!![i].nos!![j].createdAt
                        objNos.updateDate = responseData.attributeData!![i].nos!![j].updatedAt
                        saveNos(objNos, ctx)
                        Log.e("saveNosData", "true")
                    }

                }
            } else {
                for (i in responseData.attributeData!!.indices) {
                    val obj = AttributeData()
                    obj.iD = responseData.attributeData!![i].id
                    obj.name = responseData.attributeData!![i].name
                    obj.createDate = responseData.attributeData!![i].createdAt
                    obj.updateDate = responseData.attributeData!![i].updatedAt
                    if (!listAttributeData.contains(obj)) {
                        saveAttributeData(obj, ctx)
                        Log.e("saveAttributeData", "true   " + obj.name)
                        for (j in responseData.attributeData!![i].nos!!.indices) {
                            val objNos = Nos()
                            objNos.iD = responseData.attributeData!![i].nos!![j].id
                            objNos.name = responseData.attributeData!![i].nos!![j].name
                            objNos.attributeId = responseData.attributeData!![i].nos!![j].attributeId
                            objNos.createDate = responseData.attributeData!![i].nos!![j].createdAt
                            objNos.updateDate = responseData.attributeData!![i].nos!![j].updatedAt
                            saveNos(objNos, ctx)
                            Log.e("saveNosData", "true")
                        }
                    }
                }
            }
        }
    }

}