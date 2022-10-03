/*     */ package org.springframework.objenesis.instantiator.basic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.Serializable;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*     */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Instantiator(Typology.SERIALIZATION)
/*     */ public class ObjectInputStreamInstantiator<T>
/*     */   implements ObjectInstantiator<T>
/*     */ {
/*     */   private final ObjectInputStream inputStream;
/*     */   
/*     */   private static class MockStream
/*     */     extends InputStream
/*     */   {
/*     */     private int pointer;
/*     */     private byte[] data;
/*     */     private int sequence;
/*  50 */     private static final int[] NEXT = new int[] { 1, 2, 2 };
/*     */     
/*     */     private final byte[][] buffers;
/*     */     private static byte[] HEADER;
/*     */     private static byte[] REPEATING_DATA;
/*     */     
/*     */     static {
/*  57 */       initialize();
/*     */     }
/*     */     
/*     */     private static void initialize() {
/*     */       try {
/*  62 */         ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
/*  63 */         DataOutputStream dout = new DataOutputStream(byteOut);
/*  64 */         dout.writeShort(-21267);
/*  65 */         dout.writeShort(5);
/*  66 */         HEADER = byteOut.toByteArray();
/*     */         
/*  68 */         byteOut = new ByteArrayOutputStream();
/*  69 */         dout = new DataOutputStream(byteOut);
/*     */         
/*  71 */         dout.writeByte(115);
/*  72 */         dout.writeByte(113);
/*  73 */         dout.writeInt(8257536);
/*  74 */         REPEATING_DATA = byteOut.toByteArray();
/*     */       }
/*  76 */       catch (IOException e) {
/*  77 */         throw new Error("IOException: " + e.getMessage());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public MockStream(Class<?> clazz) {
/*  83 */       this.pointer = 0;
/*  84 */       this.sequence = 0;
/*  85 */       this.data = HEADER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  96 */       ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
/*  97 */       DataOutputStream dout = new DataOutputStream(byteOut);
/*     */       try {
/*  99 */         dout.writeByte(115);
/* 100 */         dout.writeByte(114);
/* 101 */         dout.writeUTF(clazz.getName());
/* 102 */         dout.writeLong(ObjectStreamClass.lookup(clazz).getSerialVersionUID());
/* 103 */         dout.writeByte(2);
/* 104 */         dout.writeShort(0);
/* 105 */         dout.writeByte(120);
/* 106 */         dout.writeByte(112);
/*     */       }
/* 108 */       catch (IOException e) {
/* 109 */         throw new Error("IOException: " + e.getMessage());
/*     */       } 
/* 111 */       byte[] firstData = byteOut.toByteArray();
/* 112 */       this.buffers = new byte[][] { HEADER, firstData, REPEATING_DATA };
/*     */     }
/*     */     
/*     */     private void advanceBuffer() {
/* 116 */       this.pointer = 0;
/* 117 */       this.sequence = NEXT[this.sequence];
/* 118 */       this.data = this.buffers[this.sequence];
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 123 */       int result = this.data[this.pointer++];
/* 124 */       if (this.pointer >= this.data.length) {
/* 125 */         advanceBuffer();
/*     */       }
/*     */       
/* 128 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() {
/* 133 */       return Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) {
/* 138 */       int left = len;
/* 139 */       int remaining = this.data.length - this.pointer;
/*     */       
/* 141 */       while (remaining <= left) {
/* 142 */         System.arraycopy(this.data, this.pointer, b, off, remaining);
/* 143 */         off += remaining;
/* 144 */         left -= remaining;
/* 145 */         advanceBuffer();
/* 146 */         remaining = this.data.length - this.pointer;
/*     */       } 
/* 148 */       if (left > 0) {
/* 149 */         System.arraycopy(this.data, this.pointer, b, off, left);
/* 150 */         this.pointer += left;
/*     */       } 
/*     */       
/* 153 */       return len;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectInputStreamInstantiator(Class<T> clazz) {
/* 160 */     if (Serializable.class.isAssignableFrom(clazz)) {
/*     */       try {
/* 162 */         this.inputStream = new ObjectInputStream(new MockStream(clazz));
/*     */       }
/* 164 */       catch (IOException e) {
/* 165 */         throw new Error("IOException: " + e.getMessage());
/*     */       } 
/*     */     } else {
/*     */       
/* 169 */       throw new ObjenesisException(new NotSerializableException(clazz + " not serializable"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public T newInstance() {
/*     */     try {
/* 176 */       return (T)this.inputStream.readObject();
/*     */     }
/* 178 */     catch (ClassNotFoundException e) {
/* 179 */       throw new Error("ClassNotFoundException: " + e.getMessage());
/*     */     }
/* 181 */     catch (Exception e) {
/* 182 */       throw new ObjenesisException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\basic\ObjectInputStreamInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */