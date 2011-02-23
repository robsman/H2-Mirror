/*
 * Copyright 2004-2011 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: James Moger
 */
package org.h2.jaqu;

import org.h2.jaqu.TableDefinition.FieldDefinition;
import org.h2.jaqu.TableInspector.ColumnInspector;
import org.h2.jaqu.util.StringUtils;

/**
 * A Validation Remark is a result of running a model validation.
 * <p>
 * Each remark has a level, associated component (schema, table, column, index),
 * and a message.
 *
 */
public class Validation {

    private int todoReviewWholeClass;

    public static Validation CONSIDER(String table, String type, String message) {
        return new Validation(Level.CONSIDER, table, type, message);
    }

    public static Validation CONSIDER(String table, ColumnInspector col, String message) {
        return new Validation(Level.CONSIDER, table, col, message);
    }

    public static Validation WARN(String table, ColumnInspector col, String message) {
        return new Validation(Level.WARN, table, col, message);
    }

    public static Validation WARN(String table, String type, String message) {
        return new Validation(Level.WARN, table, type,  message);
    }

    public static Validation ERROR(String table, ColumnInspector col, String message) {
        return new Validation(Level.ERROR, table, col, message);
    }

    public static Validation ERROR(String table, String type, String message) {
        return new Validation(Level.ERROR, table, type,  message);
    }

    public static Validation ERROR(String table, FieldDefinition field, String message) {
        return new Validation(Level.ERROR, table, field, message);
    }

    public static enum Level {
        CONSIDER, WARN, ERROR;
    }

    Level level;
    String table;
    String fieldType;
    String fieldName;
    String message;

    private Validation(Level level, String table, String type, String message) {
        this.level = level;
        this.table = table;
        this.fieldType = type;
        this.fieldName = "";
        this.message = message;
    }

    private Validation(Level level, String table, FieldDefinition field, String message) {
        this.level = level;
        this.table = table;
        this.fieldType = field.dataType;
        this.fieldName = field.columnName;
        this.message = message;
    }

    private Validation(Level level, String table, ColumnInspector col, String message) {
        this.level = level;
        this.table = table;
        this.fieldType = col.type;
        this.fieldName = col.name;
        this.message = message;
    }

    public Validation throwError(boolean throwOnError) {
        if (throwOnError && isError())
            throw new RuntimeException(toString());
        return this;
    }

    public boolean isError() {
        return level.equals(Level.ERROR);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.pad(level.name(), 9, " ", true));
        sb.append(StringUtils.pad(table, 25, " ", true));
        sb.append(StringUtils.pad(fieldName, 20, " ", true));
        sb.append(' ');
        sb.append(message);
        return sb.toString();
    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();
        sb.append(level.name()).append(',');
        sb.append(table).append(',');
        sb.append(fieldType).append(',');
        sb.append(fieldName).append(',');
        sb.append(message);
        return sb.toString();
    }
}
