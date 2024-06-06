package com.capgemini.wsb.fitnesstracker.training.internal;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
record DateDto(@JsonFormat(pattern = "yyyy-MM-dd HH:mm a z") Date date) {
}
