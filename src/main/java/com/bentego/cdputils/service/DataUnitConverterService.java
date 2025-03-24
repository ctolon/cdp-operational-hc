package com.bentego.cdputils.service;

import com.bentego.cdputils.commands.CDPClusterHealthcheck;
import com.bentego.cdputils.enums.DataUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

// ex.: DataUnitConverterService.convert(10, DataUnit.MEGABYTE, DataUnit.GIGABYTE)

public class DataUnitConverterService {

    Logger logger = LoggerFactory.getLogger(DataUnitConverterService.class);

    public static double convertFromDouble(double value, DataUnit fromUnit, DataUnit toUnit) {
        if (fromUnit == null || toUnit == null) {
            throw new IllegalArgumentException("geçersiz birim: " + fromUnit + " veya " + toUnit);
        }
        double bytes = value * fromUnit.toBytes();
        return bytes / toUnit.toBytes();
    }

    public static BigDecimal convertFromBigDecimal(BigDecimal value, DataUnit fromUnit, DataUnit toUnit) {
        if (fromUnit == null || toUnit == null) {
            throw new IllegalArgumentException("Geçersiz birim: " + fromUnit + " veya " + toUnit);
        }

        BigDecimal bytes = value.multiply(BigDecimal.valueOf(fromUnit.toBytes()));
        return bytes.divide(BigDecimal.valueOf(toUnit.toBytes()), 10, RoundingMode.HALF_UP);
    }
}
