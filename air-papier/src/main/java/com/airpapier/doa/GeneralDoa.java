package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.database.DynamicMapper;
import org.jooq.*;
import org.jooq.Record;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


public class GeneralDoa<T> {
    private final DSLContext dsl;
    private final String tableName;
    private final Class<T> entity;
    private final Function<Record, T> mapper;

    public GeneralDoa(String tableName, Class<T> entity) {
        this.dsl = DataBaseConnection.getDsl();
        this.tableName = tableName;
        this.entity = entity;
        this.mapper = DynamicMapper.makeMapper(entity);
    }

    public List<T> getAll() {
        return dsl.select()
                .from(tableName)
                .fetch()
                .map(mapper::apply);
    }

    public T getById(String id) {
        Record record = dsl.select()
                .from(tableName)
                .where(field("id").eq(id))
                .fetchOne();

        return record != null ? mapper.apply(record) : null;
    }

    public void create(T object) {
        try {
            InsertQuery<?> query = dsl.insertQuery(table(tableName));

            for (Field field : entity.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null && !field.getName().equals("id")) {
                    query.addValue(field(field.getName()), value);
                }
            }

            query.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create record: " + e.getMessage(), e);
        }
    }

    public void update(Object id, T object) {
        try {
            UpdateQuery<?> query = dsl.updateQuery(table(tableName));

            for (Field field : entity.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null && !field.getName().equals("id")) {
                    query.addValue(field(field.getName()), value);
                }
            }

            query.addConditions(field("id").eq(id));
            query.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update record: " + e.getMessage(), e);
        }
    }

    public void delete(String id) {
        dsl.deleteFrom(table(tableName))
                .where(field("id").eq(id))
                .execute();
    }
}
