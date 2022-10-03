/*    */ package util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RC4
/*    */ {
/*    */   private static final int SBOX_LENGTH = 256;
/*    */   private static final int KEY_MIN_LENGTH = 5;
/* 18 */   private byte[] key = new byte[255];
/*    */ 
/*    */ 
/*    */   
/* 22 */   private int[] sbox = new int[256];
/*    */   
/*    */   public RC4() {
/* 25 */     reset();
/*    */   }
/*    */   
/*    */   public RC4(String key) throws InvalidKeyException {
/* 29 */     this();
/* 30 */     setKey(key);
/*    */   }
/*    */   
/*    */   private void reset() {
/* 34 */     Arrays.fill(this.key, (byte)0);
/* 35 */     Arrays.fill(this.sbox, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encryptMessage(byte[] data, String key) throws InvalidKeyException {
/* 41 */     reset();
/* 42 */     setKey(key);
/* 43 */     byte[] crypt = crypt(data);
/* 44 */     reset();
/* 45 */     return crypt;
/*    */   }
/*    */   public byte[] decryptMessage(byte[] message, String key) {
/* 48 */     reset();
/* 49 */     setKey(key);
/* 50 */     byte[] msg = crypt(message);
/* 51 */     reset();
/* 52 */     return msg;
/*    */   }
/*    */   
/*    */   public byte[] crypt(byte[] msg) {
/* 56 */     this.sbox = initSBox(this.key);
/* 57 */     byte[] code = new byte[msg.length];
/* 58 */     int i = 0;
/* 59 */     int j = 0;
/* 60 */     for (int n = 0; n < msg.length; n++) {
/* 61 */       i = (i + 1) % 256;
/* 62 */       j = (j + this.sbox[i]) % 256;
/* 63 */       swap(i, j, this.sbox);
/* 64 */       int rand = this.sbox[(this.sbox[i] + this.sbox[j]) % 256];
/* 65 */       code[n] = (byte)(rand ^ msg[n]);
/*    */     } 
/* 67 */     return code;
/*    */   }
/*    */   
/*    */   private int[] initSBox(byte[] key) {
/* 71 */     int[] sbox = new int[256];
/* 72 */     int j = 0;
/*    */     int i;
/* 74 */     for (i = 0; i < 256; i++) {
/* 75 */       sbox[i] = i;
/*    */     }
/*    */     
/* 78 */     for (i = 0; i < 256; i++) {
/* 79 */       j = (j + sbox[i] + key[i % key.length] & 0xFF) % 256;
/* 80 */       swap(i, j, sbox);
/*    */     } 
/* 82 */     return sbox;
/*    */   }
/*    */   
/*    */   private void swap(int i, int j, int[] sbox) {
/* 86 */     int temp = sbox[i];
/* 87 */     sbox[i] = sbox[j];
/* 88 */     sbox[j] = temp;
/*    */   }
/*    */   
/*    */   public void setKey(String key) throws InvalidKeyException {
/* 92 */     if (key.length() < 5 || key.length() >= 256) {
/* 93 */       throw new InvalidKeyException("Key length has to be between 5 and 255");
/*    */     }
/*    */ 
/*    */     
/* 97 */     this.key = key.getBytes();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\RC4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */