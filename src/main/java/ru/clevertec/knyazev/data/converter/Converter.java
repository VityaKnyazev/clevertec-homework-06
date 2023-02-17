package ru.clevertec.knyazev.data.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.util.Settings;

/**
 *
 * represents mechanism of converting data input
 *
 */
public interface Converter<T, U> {

	/**
	 * 
	 * @param inputData for converting
	 * @return  converted input data in type T
	 * @throws ConverterException when error on converting input data
	 */
	T convert(U inputData)  throws ConverterException;
	
	default Float convertToFloat(String val) throws NumberFormatException {
		return Float.parseFloat(val);		
	}
	
	default Long convertToLong(String val) throws NumberFormatException {
		return Long.parseLong(val);		
	}
	
	default BigDecimal convertToBigDecimal(String val) throws NumberFormatException {
		BigDecimal convertedVal = BigDecimal.valueOf(convertToFloat(val));
		convertedVal = convertedVal.setScale(Settings.QUANTITY_SCALE_VALUE, RoundingMode.HALF_UP);
		return convertedVal;
	}
	
}
