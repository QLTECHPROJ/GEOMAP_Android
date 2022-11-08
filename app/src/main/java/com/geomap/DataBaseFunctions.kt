package com.geomap

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.geomap.roomDataBase.*

class DataBaseFunctions {
    companion object {
        var DB: GeoMapDatabase? = null
        private fun getDataBase(ctx: Context?): GeoMapDatabase? {
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

        private fun saveUGReport(obj: UnderGroundMappingReport, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertUGReport(obj)
            }
        }

        private fun deleteUGReport(ctx: Context) {
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

        private fun saveOCReport(obj: OpenCastMappingReport, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertOCReport(obj)
            }
        }

        private fun deleteOCReport(ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.deleteAllOpenCastMappingReport()
            }
        }

        fun callGetAttributesObserver(ctx: Context): List<GetAttributes> {
            DB = getDataBase(ctx)
            var list = ArrayList<GetAttributes>()
            DB?.taskDao()?.geAllGetAttributes()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<GetAttributes>
                DB!!.taskDao().geAllGetAttributes().removeObserver {}
            }
            return list
        }

        private fun saveGetAttributes(obj: GetAttributes, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertGetAttributes(obj)
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

        private fun saveSampleCollected(obj: SampleCollected, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertSampleCollected(obj)
            }
        }

        fun callWeatheringObserver(ctx: Context): List<Weathering> {
            DB = getDataBase(ctx)
            var list = ArrayList<Weathering>()
            DB?.taskDao()?.geAllWeathering()?.observe(ctx as LifecycleOwner) { lists ->
                list = lists as ArrayList<Weathering>
                DB!!.taskDao().geAllWeathering().removeObserver {}
            }
            return list
        }

        private fun saveWeathering(obj: Weathering, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertWeathering(obj)
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

        private fun saveRockStrength(obj: RockStrength, ctx: Context) {
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

        private fun saveWaterCondition(obj: WaterCondition, ctx: Context) {
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

        private fun saveTypeOfGeologicalStructures(obj: TypeOfGeologicalStructures, ctx: Context) {
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

        private fun saveTypeOfFaults(obj: TypeOfFaults, ctx: Context) {
            DB = getDataBase(ctx)
            GeoMapDatabase.databaseWriteExecutor.execute {
                DB!!.taskDao()?.insertTypeOfFaults(obj)
            }
        }
    }

}