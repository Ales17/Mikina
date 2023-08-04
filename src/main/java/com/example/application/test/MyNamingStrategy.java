package com.example.application.test;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

import java.io.Serial;

@Component
public class MyNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    @Serial
    private static final long serialVersionUID = 1383021413247872469L;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // Convert all table names to lowercase
        String tableName = name.getText().toLowerCase();
        return Identifier.toIdentifier(tableName);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        // Convert column names to lowercase with underscores
        String columnName = name.getText();
        columnName = columnName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        return Identifier.toIdentifier(columnName);
    }
}
