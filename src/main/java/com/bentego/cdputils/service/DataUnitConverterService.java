package com.bentego.cdputils.service;

import com.bentego.cdputils.enums.DataUnit;
import org.springframework.stereotype.Service;

// ex.: DataUnitConverterService.convert(10, DataUnit.MEGABYTE, DataUnit.GIGABYTE)

public class DataUnitConverterService {
    public static double convert(double value, DataUnit fromUnit, DataUnit toUnit) {
        if (fromUnit == null || toUnit == null) {
            throw new IllegalArgumentException("geçersiz birim: " + fromUnit + " veya " + toUnit);
        }
        double bytes = value * fromUnit.toBytes();
        return bytes / toUnit.toBytes();
    }
}
