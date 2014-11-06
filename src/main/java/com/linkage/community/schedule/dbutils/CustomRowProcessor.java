package com.linkage.community.schedule.dbutils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;

public class CustomRowProcessor extends BasicRowProcessor {
	public CustomRowProcessor() {
		super();
	}

	public CustomRowProcessor(BeanProcessor convert) {
		super(convert);
	}

	@Override
	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		for (int i = 1; i <= cols; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(i);
			}
			if ("TINYINT".equalsIgnoreCase(rsmd.getColumnTypeName(i))
					|| "TINYINT UNSIGNED".equalsIgnoreCase(rsmd
							.getColumnTypeName(i))) {
				result.put(columnName, String.valueOf(rs.getInt(i)));
			} else {
				result.put(columnName, rs.getObject(i).toString());
			}
		}

		return result;
	}

	private static class CaseInsensitiveHashMap extends
			LinkedHashMap<String, Object> {
		/**
		 * The internal mapping from lowercase keys to the real keys.
		 * 
		 * <p>
		 * Any query operation using the key ({@link #get(Object)},
		 * {@link #containsKey(Object)}) is done in three steps:
		 * <ul>
		 * <li>convert the parameter key to lower case</li>
		 * <li>get the actual key that corresponds to the lower case key</li>
		 * <li>query the map with the actual key</li>
		 * </ul>
		 * </p>
		 */
		private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

		/**
		 * Required for serialization support.
		 * 
		 * @see java.io.Serializable
		 */
		private static final long serialVersionUID = -2848100435296897392L;

		/** {@inheritDoc} */
		@Override
		public boolean containsKey(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(
					Locale.ENGLISH));
			return super.containsKey(realKey);
			// Possible optimisation here:
			// Since the lowerCaseMap contains a mapping for all the keys,
			// we could just do this:
			// return lowerCaseMap.containsKey(key.toString().toLowerCase());
		}

		/** {@inheritDoc} */
		@Override
		public Object get(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(
					Locale.ENGLISH));
			return super.get(realKey);
		}

		/** {@inheritDoc} */
		@Override
		public Object put(String key, Object value) {
			/*
			 * In order to keep the map and lowerCaseMap synchronized, we have
			 * to remove the old mapping before putting the new one. Indeed,
			 * oldKey and key are not necessaliry equals. (That's why we call
			 * super.remove(oldKey) and not just super.put(key, value))
			 */
			Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH),
					key);
			Object oldValue = super.remove(oldKey);
			super.put(key, value);
			return oldValue;
		}

		/** {@inheritDoc} */
		@Override
		public void putAll(Map<? extends String, ?> m) {
			for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				this.put(key, value);
			}
		}

		/** {@inheritDoc} */
		@Override
		public Object remove(Object key) {
			Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(
					Locale.ENGLISH));
			return super.remove(realKey);
		}
	}
}
