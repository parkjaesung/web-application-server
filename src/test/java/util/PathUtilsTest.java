package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathUtilsTest {
	private static final Logger log = LoggerFactory.getLogger(PathUtilsTest.class);

	@Test
	public void getCurrentPath() {
		log.debug("CurrentPath : {}", PathUtils.getCurrentPath());
	}

}
