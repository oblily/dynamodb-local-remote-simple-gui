package dynamodbclient.dynamodbclientwebgui.utils;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public final class ItemUtil {
    private ItemUtil() {
    }
    public static <T> Map<String, T> toSimpleMapValue(
        Map<String, AttributeValue> values) {
        if (values == null) {
            return null;
        }

        Map<String, T> result = new LinkedHashMap<String, T>(values.size());
        for (Map.Entry<String, AttributeValue> entry : values.entrySet()) {
            T t = toSimpleValue(entry.getValue());
            result.put(entry.getKey(), t);
        }
        return result;
    }

    /**
     * Converts a low-level <code>AttributeValue</code> into a simple value,
     * which can be one of the followings:
     *
     * <ul>
     * <li>String</li>
     * <li>Set&lt;String></li>
     * <li>Number (including any subtypes and primitive types)</li>
     * <li>Set&lt;Number></li>
     * <li>byte[]</li>
     * <li>Set&lt;byte[]></li>
     * <li>ByteBuffer</li>
     * <li>Set&lt;ByteBuffer></li>
     * <li>Boolean or boolean</li>
     * <li>null</li>
     * <li>Map&lt;String,T>, where T can be any type on this list but must not
     * induce any circular reference</li>
     * <li>List&lt;T>, where T can be any type on this list but must not induce
     * any circular reference</li>
     * </ul>
     *
     * @throws IllegalArgumentException
     *             if an empty <code>AttributeValue</code> value is specified
     */
    public static <T> T toSimpleValue(AttributeValue value) {
        if (value == null) {
            return null;
        }
        if (Boolean.TRUE.equals(value.nul())) {
            return null;
        } else if (Boolean.FALSE.equals(value.nul())) {
            throw new UnsupportedOperationException("False-NULL is not supported in DynamoDB");
        } else if (value.bool() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) value.bool();
            return t;
        } else if (value.s() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) value.s();
            return t;
        } else if (value.n() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) new BigDecimal(value.n());
            return t;
        } else if (value.b() != null) {
            // TODO
            // @SuppressWarnings("unchecked")
            // T t = (T) copyAllBytesFrom(value.b());
            //return t;
            return null;
        } else if (value.ss() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) new LinkedHashSet<String>(value.ss());
            return t;
        } else if (value.ns() != null) {
            Set<BigDecimal> set = new LinkedHashSet<BigDecimal>(value.ns().size());
            for (String s : value.ns()) {
                set.add(new BigDecimal(s));
            }
            @SuppressWarnings("unchecked")
            T t = (T) set;
            return t;
        } else if (value.bs() != null) {
            Set<byte[]> set = new LinkedHashSet<byte[]>(value.bs().size());
            // TODO
            // for (ByteBuffer bb : value.bs()) {
            //     set.add(copyAllBytesFrom(bb));
            // }
            @SuppressWarnings("unchecked")
            T t = (T) set;
            return t;
        } else if (value.l() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) toSimpleList(value.l());
            return t;
        } else if (value.m() != null) {
            @SuppressWarnings("unchecked")
            T t = (T) toSimpleMapValue(value.m());
            return t;
        } else {
            throw new IllegalArgumentException(
                "Attribute value must not be empty: " + value);
        }
    }

    /**
     * Converts a list of low-level <code>AttributeValue</code> into a list of
     * simple values. Each value in the returned list can be one of the
     * followings:
     *
     * <ul>
     * <li>String</li>
     * <li>Set&lt;String></li>
     * <li>Number (including any subtypes and primitive types)</li>
     * <li>Set&lt;Number></li>
     * <li>byte[]</li>
     * <li>Set&lt;byte[]></li>
     * <li>ByteBuffer</li>
     * <li>Set&lt;ByteBuffer></li>
     * <li>Boolean or boolean</li>
     * <li>null</li>
     * <li>Map&lt;String,T>, where T can be any type on this list but must not
     * induce any circular reference</li>
     * <li>List&lt;T>, where T can be any type on this list but must not induce
     * any circular reference</li>
     * </ul>
     */
    public static List<Object> toSimpleList(List<AttributeValue> attrValues) {
        if (attrValues == null)
            return null;
        List<Object> result = new ArrayList<Object>(attrValues.size());
        for (AttributeValue attrValue : attrValues) {
            Object value = toSimpleValue(attrValue);
            result.add(value);
        }
        return result;
    }

    /**
     * Returns a copy of all the bytes from the given <code>ByteBuffer</code>,
     * from the beginning to the buffer's limit; or null if the input is null.
     * <p>
     * The internal states of the given byte buffer will be restored when this
     * method completes execution.
     * <p>
     * When handling <code>ByteBuffer</code> from user's input, it's typical to
     * call the {@link #copyBytesFrom(ByteBuffer)} instead of
     * {@link #copyAllBytesFrom(ByteBuffer)} so as to account for the position
     * of the input <code>ByteBuffer</code>. The opposite is typically true,
     * however, when handling <code>ByteBuffer</code> from withint the
     * unmarshallers of the low-level clients.
     */
    public static byte[] copyAllBytesFrom(ByteBuffer bb) {
        if (bb == null) {
            return null;
        }

        if (bb.hasArray()) {
            return Arrays.copyOfRange(
                    bb.array(),
                    bb.arrayOffset(),
                    bb.arrayOffset() + bb.limit());
        }

        ByteBuffer copy = bb.asReadOnlyBuffer();
        copy.rewind();

        byte[] dst = new byte[copy.remaining()];
        copy.get(dst);
        return dst;
    }
}