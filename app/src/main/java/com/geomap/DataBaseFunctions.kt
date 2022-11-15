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
        fun callUGReportObserver(ctx: Context) {
            DB = getDataBase(ctx)
            var list = ArrayList<UnderGroundMappingReport>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllUnderGroundMappingReport() as ArrayList<UnderGroundMappingReport>

                Log.e("List UnderGroundMappingReport", "true" + gson.toJson(list).toString())
            } as (List<UnderGroundMappingReport>)
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
                DB!!.taskDao().deleteUnderGroundMappingReport()
            }
        }

        fun callOcReportObserver(ctx: Context) {
            DB = getDataBase(ctx)
            var list = ArrayList<OpenCastMappingReport>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllOpenCastMappingReport() as ArrayList<OpenCastMappingReport>

                Log.e("List OpenCastMappingReport", "true" + gson.toJson(list).toString())
            }
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
                DB!!.taskDao().deleteOpenCastMappingReport()
            }
        }

        fun callTypeOfFaultsObserver(ctx: Context, responseData: UserCommonDataModel.ResponseData) {
            DB = getDataBase(ctx)
            var list: ArrayList<TypeOfFaults>
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB?.taskDao()?.geAllTypeOfFaults() as ArrayList<TypeOfFaults>
                callTypeOfFaultsSave(list, responseData, ctx)
            }
        }

        fun saveTypeOfFaults(obj: TypeOfFaults, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertTypeOfFaults(obj)
            }
        }

        fun callRockStrengthObserver(responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            DB = getDataBase(ctx)
            var list: ArrayList<RockStrength>
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllRockStrength() as ArrayList<RockStrength>
                callRockStrengthSave(list, responseData, ctx)
            }
        }

        fun saveRockStrength(obj: RockStrength, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertRockStrength(obj)
            }
        }

        fun callSampleCollectedObserver(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)
            var list: ArrayList<SampleCollected>
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB?.taskDao()?.geAllSampleCollected() as ArrayList<SampleCollected>
                Log.e("List SampleCollected", "true" + gson.toJson(list).toString())
                callSampleCollectedSave(list, responseData, ctx)
            }
        }

        fun saveSampleCollected(obj: SampleCollected, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertSampleCollected(obj)
            }
        }

        fun callTypeOfGeologicalStructuresObserver(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)
            var list: ArrayList<TypeOfGeologicalStructures>
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllTypeOfGeologicalStructures() as ArrayList<TypeOfGeologicalStructures>
                Log.e("List TypeOfGeologicalStructures", "true" + gson.toJson(list).toString())
                callTypeOfGeologicalStructuresSave(list, responseData, ctx)
            }
        }

        fun saveTypeOfGeologicalStructures(obj: TypeOfGeologicalStructures, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertTypeOfGeologicalStructures(obj)
            }
        }

        fun callWaterConditionObserver(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)
            var list: ArrayList<WaterCondition>
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllWaterCondition() as ArrayList<WaterCondition>
                Log.e("List WaterCondition", "true" + gson.toJson(list).toString())
                callWaterConditionSave(list, responseData, ctx)
            }
        }

        fun saveWaterCondition(obj: WaterCondition, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertWaterCondition(obj)
            }
        }

        fun callWeatheringDataObserver(responseData: UserCommonDataModel.ResponseData,
            ctx: Context): ArrayList<WeatheringData> {
            DB = getDataBase(ctx)
            var list = ArrayList<WeatheringData>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllWeatheringData() as ArrayList<WeatheringData>
                Log.e("List WeatheringData", "true" + gson.toJson(list).toString())
                callWeatheringDataSave(list, responseData, ctx)
            }
            return list
        }

        fun saveWeatheringData(obj: WeatheringData, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertWeatheringData(obj)
            }
        }

        fun callAttributeDataObserver(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)
            var list: ArrayList<AttributeData>
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllAttributeData() as ArrayList<AttributeData>
                Log.e("List AttributeData", "true" + gson.toJson(list).toString())
                callAttributeDataSave(list, responseData, ctx)
            }
        }

        fun saveAttributeData(obj: AttributeData, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertAttributeData(obj)
            }
        }

        fun callNosObserver(ctx: Context, attributeId: Int) {
            DB = getDataBase(ctx)
            var list = ArrayList<Nos>()
            GeoMapDatabase.databaseWriteExecutor.execute {
                list = DB.taskDao().geAllNos(attributeId) as ArrayList<Nos>

                Log.e("List Nos", "true" + gson.toJson(list).toString())
            }
        }

        fun saveNos(nos: Nos, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao().insertNos(nos)
            }
        }

        fun callTypeOfFaultsSave(list: ArrayList<TypeOfFaults>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteTypeOfFaults()
                }
            }
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
        }

        fun callRockStrengthSave(list: ArrayList<RockStrength>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteRockStrength()
                }
            }
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
        }

        fun callSampleCollectedSave(list: ArrayList<SampleCollected>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteSampleCollected()
                }
            }
            for (i in responseData.sampleCollected!!.indices) {
                val obj = SampleCollected()
                obj.iD = responseData.sampleCollected!![i].id
                obj.name = responseData.sampleCollected!![i].name
                obj.createDate = responseData.sampleCollected!![i].createdAt
                obj.updateDate = responseData.sampleCollected!![i].updatedAt
                saveSampleCollected(obj, ctx)
                Log.e("saveSampleCollected", "true")
                if (i == responseData.sampleCollected!!.size - 1) {
                    callTypeOfGeologicalStructuresObserver(responseData, ctx)
                }
            }
        }

        fun callTypeOfGeologicalStructuresSave(list: ArrayList<TypeOfGeologicalStructures>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteTypeOfGeologicalStructures()
                }
            }
            for (i in responseData.typeOfGeologicalStructures!!.indices) {
                val obj = TypeOfGeologicalStructures()
                obj.iD = responseData.typeOfGeologicalStructures!![i].id
                obj.name = responseData.typeOfGeologicalStructures!![i].name
                obj.createDate = responseData.typeOfGeologicalStructures!![i].createdAt
                obj.updateDate = responseData.typeOfGeologicalStructures!![i].updatedAt
                saveTypeOfGeologicalStructures(obj, ctx)
                Log.e("saveTypeOfGeologicalStructures", "true")
                if (i == responseData.typeOfGeologicalStructures!!.size - 1) {
                    callWaterConditionObserver(responseData, ctx)
                }
            }
        }

        fun callWaterConditionSave(list: ArrayList<WaterCondition>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteWaterCondition()
                }
            }
            for (i in responseData.waterCondition!!.indices) {
                val obj = WaterCondition()
                obj.iD = responseData.waterCondition!![i].id
                obj.name = responseData.waterCondition!![i].name
                obj.createDate = responseData.waterCondition!![i].createdAt
                obj.updateDate = responseData.waterCondition!![i].updatedAt
                saveWaterCondition(obj, ctx)
                Log.e("saveWaterCondition", "true")
                if (i == responseData.waterCondition!!.size - 1) {
                    callWeatheringDataObserver(responseData, ctx)
                }
            }
        }

        fun callWeatheringDataSave(list: ArrayList<WeatheringData>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteWeatheringData()
                }
            }
            for (i in responseData.weatheringData!!.indices) {
                val obj = WeatheringData()
                obj.iD = responseData.weatheringData!![i].id
                obj.name = responseData.weatheringData!![i].name
                obj.createDate = responseData.weatheringData!![i].createdAt
                obj.updateDate = responseData.weatheringData!![i].updatedAt
                saveWeatheringData(obj, ctx)
                Log.e("saveweatheringData", "true")
                if (i == responseData.weatheringData!!.size - 1) {
                    callAttributeDataObserver(responseData, ctx)
                }
            }
        }

        fun callAttributeDataSave(list: ArrayList<AttributeData>,
            responseData: UserCommonDataModel.ResponseData, ctx: Context) {
            if (list.size != 0) {
                DB = getDataBase(ctx)
                GeoMapDatabase.databaseWriteExecutor.execute {
                    DB!!.taskDao().deleteAttributeData()
                }
            }
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
        }

        fun callLocalDBGetAndInsertFunction(responseData: UserCommonDataModel.ResponseData,
            ctx: Context) {
            DB = getDataBase(ctx)
            callTypeOfFaultsObserver(ctx, responseData)
        }
    }
}