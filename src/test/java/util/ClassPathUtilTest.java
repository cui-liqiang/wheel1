package util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class ClassPathUtilTest {
    @Test
    public void should_find_all_class_in_package() throws Exception {
        List<String> classNames = ClassPathUtil.getClassNamesInPackage("testpackage");
        List<String> expectedClassNames = new ArrayList<String>();
        expectedClassNames.add("testpackage.Demo1");
        expectedClassNames.add("testpackage.nested.Demo2");

        assertTrue(classNames.containsAll(expectedClassNames));
    }
}
