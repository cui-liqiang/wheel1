package core;

import static util.AssertUtil.Assert;

public class ValueParamDesc implements ParamDesc {
    private String value;

    public ValueParamDesc(String value) {
        this.value = value;
    }

    @Override
    public Object getValue(Class parameterType) throws Exception {
        Assert(parameterType.isPrimitive(), "Cannot convert a string value(\"" + value + "\") to type: " + parameterType.getName());

        if(parameterType.equals(int.class)) {
            return Integer.valueOf(value);
        } else if(parameterType.equals(byte.class)) {
            return Byte.valueOf(value);
        } else if(parameterType.equals(short.class)) {
            return Short.valueOf(value);
        } else if(parameterType.equals(long.class)) {
            return Long.valueOf(value);
        } else if(parameterType.equals(float.class)) {
            return Float.valueOf(value);
        } else if(parameterType.equals(double.class)) {
            return Double.valueOf(value);
        } else if(parameterType.equals(char.class)) {
            if(value.length() != 1) throw new Exception(
                    "Parameter with type char/Character cannot accept \"" + value + "\" as value, cause it's more than one character");
            return Character.valueOf(value.charAt(0));
        }

        return null;
    }
}
