/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkage.community.schedule.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * <code>ResultSetHandler</code> implementation that converts one
 * <code>ResultSet</code> column into an Object. This class is thread safe.
 *
 * @param <T> The type of the scalar
 * @see org.apache.commons.dbutils.ResultSetHandler
 */
public class CustomScalarHandler<T> implements ResultSetHandler<T> {

    /**
     * The column number to retrieve.
     */
    private final int columnIndex;

    /**
     * The column name to retrieve.  Either columnName or columnIndex
     * will be used but never both.
     */
    private final String columnName;

    /**
     * Creates a new instance of ScalarHandler.  The first column will
     * be returned from <code>handle()</code>.
     */
    public CustomScalarHandler() {
        this(1, null);
    }

    /**
     * Creates a new instance of ScalarHandler.
     *
     * @param columnIndex The index of the column to retrieve from the
     * <code>ResultSet</code>.
     */
    public CustomScalarHandler(int columnIndex) {
        this(columnIndex, null);
    }

    /**
     * Creates a new instance of ScalarHandler.
     *
     * @param columnName The name of the column to retrieve from the
     * <code>ResultSet</code>.
     */
    public CustomScalarHandler(String columnName) {
        this(1, columnName);
    }

    /** Helper constructor
     * @param columnIndex The index of the column to retrieve from the
     * <code>ResultSet</code>.
     * @param columnName The name of the column to retrieve from the
     * <code>ResultSet</code>.
     */
    private CustomScalarHandler(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    /**
     * Returns one <code>ResultSet</code> column as an object via the
     * <code>ResultSet.getObject()</code> method that performs type
     * conversions.
     * @param rs <code>ResultSet</code> to process.
     * @return The column or <code>null</code> if there are no rows in
     * the <code>ResultSet</code>.
     *
     * @throws SQLException if a database access error occurs
     * @throws ClassCastException if the class datatype does not match the column type
     *
     * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
     */
    // We assume that the user has picked the correct type to match the column
    // so getObject will return the appropriate type and the cast will succeed.
    
    
    
    @SuppressWarnings("unchecked")
    //@Override
    public T handle(ResultSet rs) throws SQLException {
    	Object obj = null;
        if (rs.next()) {
            if (this.columnName == null) {
            	obj = rs.getObject(this.columnIndex);
            	if(obj instanceof Integer)
            		return (T) (obj=rs.getInt(columnIndex));
            	else if(obj instanceof Long)
            		return (T) (obj=(new Long(rs.getLong(columnIndex)).intValue()));
            	else if(obj instanceof Boolean)
            		return (T) (obj=rs.getBoolean(columnIndex));
            	else if(obj instanceof Double)
            		return (T) (obj=rs.getDouble(columnIndex));
            	else if(obj instanceof Float)
            		return (T) (obj=rs.getFloat(columnIndex));
            	else if(obj instanceof Short)
            		return (T) (obj=rs.getShort(columnIndex));
            	else if(obj instanceof Byte)
            		return (T) (obj=rs.getByte(columnIndex));
            	else
                return (T) obj;
            }
            else
            {
            	obj = rs.getObject(this.columnName);
            	if(obj instanceof Integer)
            		return (T) (obj=rs.getInt(columnName));
            	else if(obj instanceof Long)
            		return (T) (obj=rs.getLong(columnName));
            	else if(obj instanceof Boolean)
            		return (T) (obj=rs.getBoolean(columnName));
            	else if(obj instanceof Double)
            		return (T) (obj=rs.getDouble(columnName));
            	else if(obj instanceof Float)
            		return (T) (obj=rs.getFloat(columnName));
            	else if(obj instanceof Short)
            		return (T) (obj=rs.getShort(columnName));
            	else if(obj instanceof Byte)
            		return (T) (obj=rs.getByte(columnName));
            	else
                return (T) obj;
            }
        }
        return null;
    }
}
