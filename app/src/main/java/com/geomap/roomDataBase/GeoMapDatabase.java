package com.geomap.roomDataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UnderGroundMappingReport.class, OpenCastMappingReport.class, GetAttributes.class, SampleCollected.class, Weathering.class, RockStrength.class, WaterCondition.class, TypeOfGeologicalStructures.class, TypeOfFaults.class}, version = 1, exportSchema = false)
public abstract class GeoMapDatabase extends RoomDatabase {

    private static volatile GeoMapDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract GeoMapDetailsDao taskDao();
}
