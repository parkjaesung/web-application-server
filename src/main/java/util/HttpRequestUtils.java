package util;

import java.util.Map;
import java.util.StringTokenizer;

import com.google.common.collect.Maps;

public class HttpRequestUtils {

	public static Map<String, String> parseQueryString(String queryString) {
		StringTokenizer st = new StringTokenizer(queryString, "&");
		Map<String, String> parameters = Maps.newHashMap();
		while(st.hasMoreTokens()) {
			Pair keyValue = getKeyValue(st.nextToken());
			if (keyValue == null) {
				continue;
			}
			parameters.put(keyValue.getKey(), keyValue.getValue());
		}
		return parameters;
	}
	
	static Pair getKeyValue(String keyValue) {
		StringTokenizer st = new StringTokenizer(keyValue, "=");
		if (st.countTokens() != 2) {
			return null;
		}
		
		return new Pair(st.nextToken(), st.nextToken());
	}
	
	static class Pair {
		String key;
		String value;
		
		Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		String getKey() {
			return key;
		}
		
		String getValue() {
			return value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
}
