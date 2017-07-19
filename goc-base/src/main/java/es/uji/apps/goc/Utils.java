package es.uji.apps.goc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils
{
    public static String getHash(byte[] file)
            throws NoSuchAlgorithmException, IOException
    {
        InputStream input = new ByteArrayInputStream(file);

        MessageDigest algorithm = MessageDigest.getInstance("SHA1");
        DigestInputStream digestInput = new DigestInputStream(input, algorithm);

        while (digestInput.read() != -1)
        {
        }

        byte[] hash = algorithm.digest();

        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes)
    {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
