package com.example.pathfinder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pathfinder.data.dao.JobDao
import com.example.pathfinder.data.model.JobEntity

@Database(entities = [JobEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration từ phiên bản 1 lên phiên bản 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Cập nhật schema cơ sở dữ liệu (ví dụ: thêm cột cvUrl)
                database.execSQL("ALTER TABLE jobs ADD COLUMN cvUrl TEXT")
                database.execSQL("ALTER TABLE jobs ADD COLUMN isCvSubmitted INTEGER DEFAULT 0")
            }
        }

        // Lấy instance của database, nếu chưa có thì tạo mới
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pathfinder_database"
                )
                    .addMigrations(MIGRATION_1_2)  // Thêm migration tại đây
                    .fallbackToDestructiveMigration() // Khi không tìm thấy migration, Room sẽ xóa cơ sở dữ liệu cũ
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}