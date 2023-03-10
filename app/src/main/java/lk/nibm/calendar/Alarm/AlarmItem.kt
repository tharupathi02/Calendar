package com.leoxtech.alarmscheduler

import java.time.LocalDateTime
import java.time.Month

data class AlarmItem(
    val title: String,
    val description: String,
    val date: String,
    val month: String,
    val year: String,
    val hour: String,
    val minute: String
)
