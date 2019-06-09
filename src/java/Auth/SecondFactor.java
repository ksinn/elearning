/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auth;

import DAO.DBConnect;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;
import org.apache.commons.codec.binary.Base32;

/**
 *
 * @author ksinn
 */
public class SecondFactor {

    // taken from Google pam docs - we probably don't need to mess with these
    final static int secretSize = 10;
    final static int numOfScratchCodes = 5;
    final static int scratchCodeSize = 8;

    int window_size = 0;  // default 3 - max 17 (from google docs)

    /*public void setWindowSize(int s) {
  if( s >= 1 && s <= 17 )
   window_size = s;
 }*/
    protected static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[19] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

    static public Secret get2factor(int user) throws SQLException, NamingException {

        Connection conn = null;
        try {
            Secret key = new Secret();
            conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from users_key where users=?;");
            stmt.setInt(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                key.Secret = rs.getString("secret_key");
                key.Type = rs.getString("secret_type");
            } else {
                key = null;
            }
            return key;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    static public boolean put2factor(int user, Secret key) throws SQLException, NamingException {

        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement("insert into users_key (users, secret_key, secret_type) values (?, ?, ?)");
            stmt.setInt(1, user);
            stmt.setString(2, key.Secret);
            stmt.setString(3, key.Type);
            return 1 == stmt.executeUpdate();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static String generateSecretKey(int id) {
        // Allocating the buffer
        byte[] buffer = new byte[secretSize + numOfScratchCodes * scratchCodeSize];

        // Filling the buffer with random numbers.
        // Notice: you want to reuse the same random generator
        // while generating larger random number sequences.
        new Random(id).nextBytes(buffer);

        // Getting the key and converting it to Base32
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, secretSize);
        byte[] bEncodedKey = codec.encode(secretKey);
        String encodedKey = new String(bEncodedKey);
        return encodedKey;
    }

}
