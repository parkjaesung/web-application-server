package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import util.HttpRequestUtils.Pair;

public class HttpRequestUtilsTest {
	@Test
	public void parseQueryString() {
		String queryString = "userId=javajigi&password=password2";
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
		assertThat(parameters.get("userId"), is("javajigi"));
		assertThat(parameters.get("password"), is("password2"));
	}
	
	@Test
	public void parseQueryString_invalid() {
		String queryString = "userId=javajigi&password";
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
		assertThat(parameters.get("userId"), is("javajigi"));
		assertThat(parameters.get("password"), is(nullValue()));
	}
	
	@Test
	public void getKeyValue() throws Exception {
		Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi");
		assertThat(pair, is(new Pair("userId", "javajigi")));
	}
	
	@Test
	public void getKeyValue_invalid() throws Exception {
		Pair pair = HttpRequestUtils.getKeyValue("userId");
		assertThat(pair, is(nullValue()));
	}
}
