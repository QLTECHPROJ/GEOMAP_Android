package com.geomap

import android.content.Context
import android.util.Log
import com.geomap.GeoMapApp.DB
import com.geomap.GeoMapApp.getDataBase
import com.geomap.roomDataBase.*
import com.geomap.userModule.models.UserCommonDataModel
import com.google.gson.Gson

class DataBaseFunctions {
    companion object {
        val gson = Gson()
        fun callUGReportObserver(ctx: Context): ArrayList<UnderGroundMappingReport> {
            DB = getDataBase(ctx)
            var list = ArrayList<UnderGroundMappingReport>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllUnderGroundMappingReport() as ArrayList<UnderGroundMappingReport>

                Log.e("List UnderGroundMappingReport", "true" + gson.toJson(list).toString())
            } as (List<UnderGroundMappingReport>)
            return list
        }

        fun saveUGReport(obj: UnderGroundMappingReport, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertUGReport(obj)
            }
        }

        fun deleteUGReport(ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().deleteAllUnderGroundMappingReport()
            }
        }

        fun callOcReportObserver(ctx: Context): ArrayList<OpenCastMappingReport> {
            DB = getDataBase(ctx)
            var list = ArrayList<OpenCastMappingReport>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllOpenCastMappingReport() as ArrayList<OpenCastMappingReport>

                Log.e("List OpenCastMappingReport", "true" + gson.toJson(list).toString())
            }
            return list
        }

        fun saveOCReport(obj: OpenCastMappingReport, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertOCReport(obj)
            }
        }

        fun deleteOCReport(ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().deleteAllOpenCastMappingReport()
            }
        }

        fun callAttributeDataObserver(ctx: Context): ArrayList<AttributeData> {
            DB = getDataBase(ctx)
            var list = ArrayList<AttributeData>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllAttributeData() as ArrayList<AttributeData>

                Log.e("List AttributeData", "true" + gson.toJson(list).toString())
            }
            return list
        }

        fun saveAttributeData(obj: AttributeData, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertAttributeData(obj)
            }
        }

        fun callNosObserver(ctx: Context, attributeId: Int): ArrayList<Nos> {
            DB = getDataBase(ctx)
            var list = ArrayList<Nos>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllNos(attributeId) as ArrayList<Nos>

                Log.e("List Nos", "true" + gson.toJson(list).toString())
            }
            return list
        }

        fun saveNos(nos: Nos, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertNos(nos)
            }
        }

        fun callSampleCollectedObserver(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)
            var list = ArrayList<SampleCollected>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB?.taskDao()?.geAllSampleCollected() as ArrayList<SampleCollected>
                callSampleCollectedSave(list, responseData, ctx)
            }
        }

        fun saveSampleCollected(obj: SampleCollected, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertSampleCollected(obj)
            }
        }

        fun callWeatheringDataObserver(ctx: Context): ArrayList<WeatheringData> {
            DB = getDataBase(ctx)
            var list = ArrayList<WeatheringData>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllWeatheringData() as ArrayList<WeatheringData>

                Log.e("List WeatheringData", "true" + gson.toJson(list).toString())
            }
            return list
        }

        fun saveWeatheringData(obj: WeatheringData, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertWeatheringData(obj)
            }
        }

        fun callRockStrengthObserver(responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            DB = getDataBase(ctx)
            var listRockStrength: ArrayList<RockStrength>
            GeoMapDatabase.databaseWriteExecutor.execute {
                listRockStrength = DB.taskDao().geAllRockStrength() as ArrayList<RockStrength>
                callRockStrengthSave(listRockStrength, responseData, ctx)
            }
        }

        fun saveRockStrength(obj: RockStrength, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertRockStrength(obj)
            }
        }

        fun callWaterConditionObserver(ctx: Context): ArrayList<WaterCondition> {
            DB = getDataBase(ctx)
            var list = ArrayList<WaterCondition>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllWaterCondition() as ArrayList<WaterCondition>
                Log.e("List WaterCondition", "true" + gson.toJson(list).toString())
            }
            return list
        }

        fun saveWaterCondition(obj: WaterCondition, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertWaterCondition(obj)
            }
        }

        fun callTypeOfGeologicalStructuresObserver(
            ctx: Context): ArrayList<TypeOfGeologicalStructures> {
            DB = getDataBase(ctx)
            var list = ArrayList<TypeOfGeologicalStructures>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllTypeOfGeologicalStructures() as ArrayList<TypeOfGeologicalStructures>
                Log.e("List TypeOfGeologicalStructures", "true" + gson.toJson(list).toString())
            }
            return list
        }

        fun saveTypeOfGeologicalStructures(obj: TypeOfGeologicalStructures, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertTypeOfGeologicalStructures(obj)
            }
        }

        fun callTypeOfFaultsObserver(ctx: Context, responseData: UserCommonDataModel.ResponseData) {
            DB = getDataBase(ctx)
            var list = ArrayList<TypeOfFaults>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB?.taskDao()?.geAllTypeOfFaults() as ArrayList<TypeOfFaults>
                callTypeOfFaultsSave(list, responseData, ctx)
            }
        }

        fun callTypeOfFaultsSave(listTypeOfFaults: ArrayList<TypeOfFaults>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (listTypeOfFaults.isEmpty()) {
                for (i in responseData.typeOfFaults!!.indices) {
                    val obj = TypeOfFaults()
                    obj.iD = responseData.typeOfFaults!![i].id
                    obj.name = responseData.typeOfFaults!![i].name
                    obj.createDate = responseData.typeOfFaults!![i].createdAt
                    obj.updateDate = responseData.typeOfFaults!![i].updatedAt
                    saveTypeOfFaults(obj, ctx)
                    Log.e("savetypeOfFaults", "true")
                    if (i == responseData.typeOfFaults!!.size - 1) {
                        callRockStrengthObserver(responseData, ctx)
                    }
                }
            } else {
                for (i in responseData.typeOfFaults!!.indices) {
                    val obj = TypeOfFaults()
                    obj.iD = responseData.typeOfFaults!![i].id
                    obj.name = responseData.typeOfFaults!![i].name
                    obj.createDate = responseData.typeOfFaults!![i].createdAt
                    obj.updateDate = responseData.typeOfFaults!![i].updatedAt
                    for (j in listTypeOfFaults.indices) {
                        if (listTypeOfFaults[j].iD == obj.iD) {
                            break
                        } else if (j == listTypeOfFaults.size - 1) {
                            saveTypeOfFaults(obj, ctx)
                            Log.e("savetypeOfFaults", "true   " + obj.name)
                        }
                    }
                    if (i == responseData.typeOfFaults!!.size - 1) {
                        callRockStrengthObserver(responseData, ctx)
                    }
                }
            }
        }

        fun callRockStrengthSave(listRockStrength: ArrayList<RockStrength>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (listRockStrength.isEmpty()) {
                for (i in responseData.rockStrength!!.indices) {
                    val obj = RockStrength()
                    obj.iD = responseData.rockStrength!![i].id
                    obj.name = responseData.rockStrength!![i].name
                    obj.createDate = responseData.rockStrength!![i].createdAt
                    obj.updateDate = responseData.rockStrength!![i].updatedAt
                    saveRockStrength(obj, ctx)
                    Log.e("saveRockStrength", "true")
                    if (i == responseData.rockStrength!!.size - 1) {
                        callSampleCollectedObserver(responseData, ctx)
                    }
                }
            } else {
                for (i in responseData.rockStrength!!.indices) {
                    val obj = RockStrength()
                    obj.iD = responseData.rockStrength!![i].id
                    obj.name = responseData.rockStrength!![i].name
                    obj.createDate = responseData.rockStrength!![i].createdAt
                    obj.updateDate = responseData.rockStrength!![i].updatedAt
                    for (j in listRockStrength.indices) {
                        if (listRockStrength[j].iD == obj.iD) {
                            break
                        } else if (j == listRockStrength.size - 1) {
                            saveRockStrength(obj, ctx)
                            Log.e("saveRockStrength", "true   " + obj.name)
                        }
                    }
                    if (i == responseData.rockStrength!!.size - 1) { //      callRockOb(responseData, ctx)
                    }
                }
            }
        }

        fun callSampleCollectedSave(listSampleCollected: ArrayList<SampleCollected>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (listSampleCollected.isEmpty()) {
                for (i in responseData.sampleCollected!!.indices) {
                    val obj = SampleCollected()
                    obj.iD = responseData.sampleCollected!![i].id
                    obj.name = responseData.sampleCollected!![i].name
                    obj.createDate = responseData.sampleCollected!![i].createdAt
                    obj.updateDate = responseData.sampleCollected!![i].updatedAt
                    saveSampleCollected(obj, ctx)
                    Log.e("saveSampleCollected", "true")
                    if (i == responseData.sampleCollected!!.size - 1) {
                        callSampleCollectedObserver(responseData, ctx)
                    }
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
        }

        fun saveTypeOfFaults(obj: TypeOfFaults, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertTypeOfFaults(obj)
            }
        }

        fun callLocalDBGetAndInsertFunction(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)

            callTypeOfFaultsObserver(ctx, responseData)
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