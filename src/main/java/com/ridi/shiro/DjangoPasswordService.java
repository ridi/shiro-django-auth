package com.ridi.data;

import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class DjangoPasswordService implements PasswordService {
    private static final Logger log = LoggerFactory.getLogger(DjangoPasswordService.class);

    public String encryptPassword(Object plaintextPassword) {
        return "";
    }

    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        ByteSource plaintextBytes = ByteSource.Util.bytes(submittedPlaintext);
        if (encrypted == null || encrypted.length() == 0) {
            return plaintextBytes == null || plaintextBytes.isEmpty();
        } else {
            if (plaintextBytes == null || plaintextBytes.isEmpty()) {
                return false;
            }
        }
        String plaintext = new String(plaintextBytes.getBytes());
        String[] tokens = encrypted.split("\\$");
        int iterations = Integer.parseInt(tokens[1]);
        byte[] salt = tokens[2].getBytes();
        String hash = tokens[3];

        KeySpec spec = new PBEKeySpec(plaintext.toCharArray(), salt, iterations, 256);
        try {
            String algorithmName = getAlgorithmFullName(tokens[0]);
            SecretKeyFactory f = SecretKeyFactory.getInstance(algorithmName);
            return hash.equals(Base64.encodeToString(f.generateSecret(spec).getEncoded()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private String getAlgorithmFullName(String token) throws Exception {
        if (token.equals("pbkdf2_sha256")) {
            return "PBKDF2WithHmacSHA256";
        }
        throw new Exception("algorithm " + token + " is not supported");
    }
}
