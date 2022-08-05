package bagus2x.myhabit.data

import bagus2x.myhabit.data.local.entity.HabitEntity
import bagus2x.myhabit.data.local.entity.HabitWithHistories
import bagus2x.myhabit.data.local.entity.HistoryEntity
import bagus2x.myhabit.data.local.entity.TaskEntity
import bagus2x.myhabit.domain.model.Habit
import bagus2x.myhabit.domain.model.History
import bagus2x.myhabit.domain.model.Task
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun HabitEntity.toModel(): Habit {
    return Habit(
        habitId = habitId,
        title = title,
        description = description,
        priority = Habit.Priority.valueOf(priority),
        focusTimes = focusTimes.milliseconds,
        dayOfWeek = DayOfWeek.of(dayOfWeek),
        createdAt = createdAt.toLocalDateTime()
    )
}

fun HistoryEntity.toModel(): History {
    return History(
        historyId = habitId,
        remainingTimes = remainingTimes.milliseconds,
        createdAt = createdAt.toLocalDateTime()
    )
}

fun HabitWithHistories.toModel(): Habit.HabitWithHistories {
    return Habit.HabitWithHistories(
        habit = habit.toModel(),
        histories = histories.map { it.toModel() }
    )
}

private fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun TaskEntity.toModel(): Task {
    return Task(
        taskId = taskId,
        parentTaskId = parentTaskId,
        title = title,
        description = description,
        due = due?.toLocalDateTime(),
        createdAt = createdAt.toLocalDateTime(),
        hierarchy = TaskEntity.decodeHierarchy(hierarchy)
    )
}
