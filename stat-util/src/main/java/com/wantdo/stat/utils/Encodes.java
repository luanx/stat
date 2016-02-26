package com.wantdo.stat.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class Encodes {

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
