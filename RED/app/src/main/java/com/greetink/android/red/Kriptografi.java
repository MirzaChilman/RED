package com.greetink.android.red;

import java.io.UnsupportedEncodingException;

public class Kriptografi {

    //Metode Kriptografi Klasik
    public String toCryptText(String text) {
        if (!text.isEmpty()) {
            text = text.toUpperCase();
            String resultText = "";
            for (int i = 0; i < text.length(); i++) {
                int cipher = text.charAt(i);
                if (cipher >= 65 && cipher <= 90) {
                    resultText += (char) cipher;
                }
            }
            text = resultText;
        }
        return text;
    }

    public String encryptCaesarCipher(String plainText, int key) {
        if (!plainText.isEmpty() || key != 0) {
            key %= 26;
            String encryptResult = "";
            for (int i = 0; i < plainText.length(); i++) {
                int cipher = plainText.charAt(i);
                cipher += key;
                if (cipher > 90) {
                    cipher -= 26;
                }
                encryptResult += (char) cipher;
            }
            plainText = encryptResult;
        }
        return plainText;
    }

    public String decryptCaesarCipher(String cipherText, int key) {
        if (!cipherText.isEmpty() || key != 0) {
            key %= 26;
            String decryptResult = "";
            for (int i = 0; i < cipherText.length(); i++) {
                int cipher = cipherText.charAt(i);
                cipher -= key;
                if (cipher < 65) {
                    cipher += 26;
                }
                decryptResult += (char) cipher;
            }
            cipherText = decryptResult;
        }
        return cipherText;
    }

    public String encryptVigenere(String plainText, String key) {
        if (!plainText.isEmpty() && !key.isEmpty()) {
            String encryptResult = "";
            for (int i = 0, j = 0; i < plainText.length(); i++, j++) {
                String cipher = "";
                cipher += plainText.charAt(i);
                int keyCipher = ((int) key.charAt(j)) - 65;
                encryptResult += encryptCaesarCipher(cipher, keyCipher);
                if (j == key.length() - 1) {
                    j = -1;
                }
            }
            plainText = encryptResult;
        }
        return plainText;
    }

    public String decryptVigenere(String cipherText, String key) {
        if (!cipherText.isEmpty() && !key.isEmpty()) {
            String encryptResult = "";
            for (int i = 0, j = 0; i < cipherText.length(); i++, j++) {
                String cipher = "";
                cipher += cipherText.charAt(i);
                int keyCipher = ((int) key.charAt(j)) - 65;
                encryptResult += decryptCaesarCipher(cipher, keyCipher);
                if (j == key.length() - 1) {
                    j = -1;
                }
            }
            cipherText = encryptResult;
        }
        return cipherText;
    }
    //End

    //Metode Kriptografi Modern
    public String charToBit(String text, int textLong) throws UnsupportedEncodingException {
        String binary = "";
        if (!text.isEmpty()) {
            byte[] bitArray = text.getBytes("UTF-8");
            for (byte b : bitArray) {
                String binaryStream = Integer.toBinaryString(b);
                while (binaryStream.length() < textLong) {
                    binaryStream = "0" + binaryStream;
                }
                binary += binaryStream;
            }
        }
        return binary;
    }

    public String bitToChar(String text) throws UnsupportedEncodingException {
        String charText = "";
        if (!text.isEmpty()) {
            int charBinary = Integer.parseInt(text, 2);
            charText += (char) charBinary;
        }
        return charText;
    }

    public String blockPadding(String text, int blockLength) {
        String textStream = text;
        while (textStream.length() < blockLength) {
            textStream = "0" + textStream;
        }
        return textStream;
    }

    public String shiftRightBlock(String text) {
        String result = "";
        result += text.charAt(text.length() - 1);
        for (int i = 0; i < text.length() - 1; i++) {
            result += text.charAt(i);
        }
        return result;
    }

    public String shiftLeftBlock(String text) {
        String result = "";
        result += text.charAt(0);
        for (int i = text.length() - 1; i > 0; i--) {
            result = text.charAt(i) + result;
        }
        return result;
    }

    public String createString8Bit(String text, int block) {
        StringBuilder sb = new StringBuilder(text);
        String box = "";
        int checkString = 0;
        for (int i = text.length() - 1; i > text.length() - 1 - block; i--) {
            box = text.charAt(i) + box;
            sb.deleteCharAt(i);
        }
        StringBuilder sb2 = new StringBuilder(sb.toString());
        while (sb2.length() % 8 != 0) {
            sb2.append("0");
            checkString++;
        }
        for (int j = block - checkString; j < block; j++) {
            sb.append(box.charAt(j));
        }
        return sb.toString();
    }

    //-->Referensi dari Stackoverflow
    private static boolean bitOf(char in) {
        return (in == '1');
    }

    private static char charOf(boolean in) {
        return (in) ? '1' : '0';
    }
    //-->

    //Enkripsi Cipher Block Chaining
    public String cbcEncryption(String text, String key, int block, int shift) throws UnsupportedEncodingException {
        String result = text;
        if (block > 1 && !key.isEmpty()) {
            String arrayOfBitText = charToBit(text, 8);
            String arrayOfBitKey = charToBit(key, 8);
            String iValue = blockPadding("", block);
            String bPlain = "";
            String bitKey = "";
            StringBuilder bPlainEx = new StringBuilder();
            String bPlainT = "";
            String bPlainShift = "";
            int m = 0;
            if (!text.isEmpty()) {
                for (int i = 0; i < arrayOfBitText.length(); i++) {
                    bPlain = "";
                    bitKey = "";
                    bPlainEx.setLength(0);
                    for (int j = i; j < block + i; j++) {
                        try {
                            bPlain += arrayOfBitText.charAt(j);
                        } catch (Exception e) {
                            bPlain = "0" + bPlain;
                        }
                        try {
                            bitKey += arrayOfBitKey.charAt(m);
                        } catch (Exception e) {
                            m = 0;
                            bitKey += arrayOfBitKey.charAt(m);
                        }
                        m++;
                    }
                    for (int k = 0; k < block; k++) {
                        bPlainEx.append(charOf(bitOf(bPlain.charAt(k)) ^ bitOf(iValue.charAt(k)) ^ bitOf(bitKey.charAt(k))));
                    }
                    i += block - 1;
                    bPlainShift = bPlainEx.toString();
                    for (int l = 0; l < shift; l++) {
                        bPlainShift = shiftRightBlock(bPlainShift);
                    }
                    iValue = bPlainShift;
                    bPlainT += bitToChar(bPlainShift);
                }
                result = bPlainT;
            }
        }
        if (block >= 8) {
            result = "Block can't be more than 7";
        }
        return result;
    }

    //Dekripsi Cipher Block Chaining
    public String cbcDecryption(String text, String key, int block, int shift) throws UnsupportedEncodingException {
        String result = text;
        if (block > 1 && !key.isEmpty()) {
            String arrayOfBitCipher = charToBit(text, block);
            String arrayOfBitKey = charToBit(key, 8);
            String resultByte = "";
            String resultBox = "";
            result = "";
            String bCipher = "";
            String bitKey = "";
            String iValue = "";
            String bCipherT = "";
            int q = 0;
            while (arrayOfBitCipher.length() >= arrayOfBitKey.length()) {
                arrayOfBitKey += arrayOfBitKey.charAt(q);
                q++;
            }
            StringBuilder bCipherEx = new StringBuilder();
            for (int i = arrayOfBitCipher.length() - 1; i > 0; i--) {
                bCipher = "";
                bitKey = "";
                iValue = "";
                bCipherEx.setLength(0);
                for (int j = i; j > i - block; j--) {
                    bCipher = arrayOfBitCipher.charAt(j) + bCipher;
                    try {
                        iValue = arrayOfBitCipher.charAt(j - block) + iValue;
                    } catch (StringIndexOutOfBoundsException e) {
                        iValue += "0";
                    }
                    bitKey = arrayOfBitKey.charAt(j) + bitKey;
                }
                for (int l = 0; l < shift; l++) {
                    bCipher = shiftLeftBlock(bCipher);
                }
                for (int k = 0; k < block; k++) {
                    bCipherEx.append(charOf(bitOf(bCipher.charAt(k)) ^ bitOf(bitKey.charAt(k)) ^ bitOf(iValue.charAt(k))));
                }
                bCipherT = bCipherEx.toString() + bCipherT;
                i = i - block + 1;
            }
            resultByte = createString8Bit(bCipherT, block);
            for (int y = 0; y < resultByte.length(); ) {
                resultBox = "";
                for (int z = 0; z < 8; z++) {
                    resultBox += resultByte.charAt(y);
                    y++;
                }
                result += bitToChar(resultBox);
            }
        }
        if (block >= 8) {
            result = "Block can't be more than 7";
        }
        return result;
    }
}
