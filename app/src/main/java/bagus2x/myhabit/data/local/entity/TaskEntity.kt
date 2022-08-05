package bagus2x.myhabit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["task_id"],
            childColumns = ["parent_task_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val taskId: Long = 0,
    @ColumnInfo(
        name = "parent_task_id",
        index = true
    )
    val parentTaskId: Long?,
    val title: String,
    val description: String,
    val due: Long?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    val hierarchy: String
) {

    companion object {
        private const val DELIMITER = "/"

        fun decodeHierarchy(hierarchy: String): Set<Long> {
            if (hierarchy.isBlank()) {
                return emptySet()
            }
            return hierarchy.split(DELIMITER).map { it.toLong() }.toSet()
        }

        fun encodeHierarchy(hierarchy: Set<Long>): String {
            return hierarchy.joinToString(DELIMITER)
        }
    }
}
