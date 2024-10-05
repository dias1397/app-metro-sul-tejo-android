package com.diasjoao.metrosultejo.helpers;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import com.diasjoao.metrosultejo.util.DateHelper;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class DateHelperTest {

    private Context context;

    @Before
    public void setup() {
    }

    @Test
    public void whenDateIsAHoliday_thenShouldReturnTrue() {
        assertTrue(DateHelper.isHoliday(context, LocalDate.of(2022, 10, 5)));
    }

}
