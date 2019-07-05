package baselibrary.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author yangming
 * Aes 加密
 */
public class AesUtils {

    private static IvParameterSpec iv;
    /**
     * 算法/模式/补码方式
     */
    private static String CipherType = "AES/CBC/PKCS5Padding";

    static {
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        iv = new IvParameterSpec("0102030405060708".getBytes());
    }

    /**
     * 加密
     */
    public synchronized static String encrypt(String sSrc, String sKey) {
        // 判断Key、待加密字符串是否为空
        if (TextUtils.isEmpty(sSrc) || TextUtils.isEmpty(sKey)) {
            System.out.print("Key为空null");
            return null;
        }

        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        try {
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CipherType);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    /**
     * 解密
     */
    public synchronized static String decrypt(String sSrc, String sKey) {
        try {
            // 判断Key、待解密字符串是否为空
            if (TextUtils.isEmpty(sSrc) || TextUtils.isEmpty(sKey)) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CipherType);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用base64解密
            byte[] encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, "UTF-8");
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * base64 编码
     */
    public static String base64Encoder(byte[] bytes) {
        if (bytes != null) {
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        }
        return "";
    }

    /**
     * base64 解码
     */
    public static byte[] base64Decoder(String data) {
        if (!TextUtils.isEmpty(data)) {
            return Base64.decode(data, Base64.NO_WRAP);
        }
        return null;
    }

}
