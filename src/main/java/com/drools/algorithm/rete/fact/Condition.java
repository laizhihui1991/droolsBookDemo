package com.drools.algorithm.rete.fact;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Condition {
    private static final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>();
    private String attribute;
    private Object value;
    private String operator;

    public Condition(String attribute, Object value, String operator) {
        this.attribute = attribute;
        this.value = value;
        this.operator = operator;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean evaluate(Object fact) {
        try {
            Field field = getField(fact.getClass(), attribute);
            if (field == null) {
                return false;
            }
            field.setAccessible(true);
            Object factValue = field.get(fact);
            if (factValue == null) {
                return false;
            }
            // 处理不同的数据类型和操作符
            if (factValue instanceof Integer && value instanceof Integer) {
                int factIntValue = (int) factValue;
                int valueInt = (int) value;
                switch (operator) {
                    case "==":
                        return factIntValue == valueInt;
                    case ">":
                        return factIntValue > valueInt;
                    case ">=":
                        return factIntValue >= valueInt;
                    case "<":
                        return factIntValue < valueInt;
                    case "<=":
                        return factIntValue <= valueInt;
                }
            } else if (factValue instanceof Double && value instanceof Double) {
                double factDoubleValue = (double) factValue;
                double valueDouble = (double) value;
                switch (operator) {
                    case "==":
                        return factDoubleValue == valueDouble;
                    case ">":
                        return factDoubleValue > valueDouble;
                    case ">=":
                        return factDoubleValue >= valueDouble;
                    case "<":
                        return factDoubleValue < valueDouble;
                    case "<=":
                        return factDoubleValue <= valueDouble;
                }
            } else if (factValue instanceof String && value instanceof String) {
                String factStringValue = (String) factValue;
                String valueString = (String) value;
                if (operator.equals("==")) {
                    return factStringValue.equals(valueString);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private Field getField(Class<?> clazz, String fieldName) {
        Map<String, Field> fields = fieldCache.get(clazz);
        if (fields == null) {
            fields = new HashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                fields.put(field.getName(), field);
            }
            fieldCache.put(clazz, fields);
        }
        return fields.get(fieldName);
    }
}
