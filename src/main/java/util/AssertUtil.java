package util;

public class AssertUtil {
    static public void Assert(boolean condition, String desc) throws Exception {
        if(!condition) throw new Exception(desc);
    }
}
