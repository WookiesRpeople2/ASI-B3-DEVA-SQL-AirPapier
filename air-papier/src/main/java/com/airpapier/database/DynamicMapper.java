package com.airpapier.database;

import org.jooq.Record;
import org.jooq.RecordMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DynamicMapper {
    public static <T, R extends Record> RecordMapper<R,T> makeMapper(Class<T> classInstance) {
        return record -> {
            try {
                Method builderMethod = classInstance.getMethod("builder");
                Object builder = builderMethod.invoke(null);
                Field[] fields = classInstance.getDeclaredFields();

                for (Field field : fields) {
                    try {
                        String fieldName = field.getName();
                        Class<?> fieldType = field.getType();

                        Object value = record.get(fieldName, fieldType);

                        if (value != null) {
                            String builderMethodName = fieldName;

                            try {
                                Method setterMethod = builder.getClass().getMethod(builderMethodName, fieldType);
                                setterMethod.invoke(builder, value);
                            } catch (NoSuchMethodException e) {
                                builderMethodName = toCamelCase(fieldName);
                                Method setterMethod = builder.getClass().getMethod(builderMethodName, fieldType);
                                setterMethod.invoke(builder, value);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Warning: Could not map field '" + field.getName() + "': " + e.getMessage());
                    }
                }

                Method buildMethod = builder.getClass().getMethod("build");
                Object builtObject = buildMethod.invoke(builder);
                return classInstance.cast(builtObject);

            } catch (Exception e) {
                throw new RuntimeException("Failed to map record to " + classInstance.getSimpleName(), e);
            }
        };
    }

    private static String toCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char ch : str.toCharArray()) {
            if (ch == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(ch));
                    capitalizeNext = false;
                } else {
                    result.append(ch);
                }
            }
        }

        return result.toString();
    }
}
