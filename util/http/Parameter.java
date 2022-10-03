/*     */ package util.http;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Parameter
/*     */   implements Serializable
/*     */ {
/*     */   public static final byte NEXT_PARAMETER = 3;
/*     */   public static final byte NEXT_VALUE = 2;
/*  19 */   protected HashMap<String, byte[]> hashMap = (HashMap)new HashMap<>();
/*     */ 
/*     */   
/*     */   public String getParameterString(String key) {
/*  23 */     byte[] retByteArray = getParameterByteArray(key);
/*  24 */     if (retByteArray != null) {
/*  25 */       return new String(retByteArray);
/*     */     }
/*  27 */     return null;
/*     */   }
/*     */   
/*     */   public byte[] getParameterByteArray(String key) {
/*  31 */     byte[] retByteArray = this.hashMap.get(key);
/*  32 */     return retByteArray;
/*     */   }
/*     */   public Parameter addParameterString(String key, String value) {
/*  35 */     addParameterByteArray(key, value.getBytes());
/*  36 */     return this;
/*     */   }
/*     */   public synchronized Parameter addParameterByteArray(String key, byte[] value) {
/*  39 */     this.hashMap.put(key, value);
/*  40 */     return this;
/*     */   }
/*     */   public byte[] remove(String key) {
/*  43 */     byte[] ret = this.hashMap.remove(key);
/*  44 */     return ret;
/*     */   }
/*     */   public byte[] get(String key) {
/*  47 */     return getParameterByteArray(key);
/*     */   }
/*     */   public Parameter add(String key, String value) {
/*  50 */     addParameterString(key, value);
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public Parameter add(String key, byte[] value) {
/*  55 */     addParameterByteArray(key, value);
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public long getSize() {
/*  60 */     return this.hashMap.size();
/*     */   }
/*     */   
/*     */   public int len() {
/*  64 */     AtomicInteger len = new AtomicInteger();
/*  65 */     this.hashMap.forEach((k, v) -> {
/*     */           len.addAndGet(k.length());
/*     */           len.addAndGet(1);
/*     */           len.addAndGet(4);
/*     */           len.addAndGet(v.length);
/*     */         });
/*  71 */     return len.get();
/*     */   }
/*     */   
/*     */   public static Parameter unSerialize(byte[] parameterByte) {
/*  75 */     return unSerialize(new ByteArrayInputStream(parameterByte));
/*     */   }
/*     */   public static Parameter unSerialize(InputStream inputStream) {
/*  78 */     Parameter resParameter = new Parameter();
/*  79 */     ByteArrayOutputStream stringBuffer = new ByteArrayOutputStream();
/*  80 */     String key = null;
/*  81 */     byte[] lenBytes = new byte[4];
/*  82 */     byte[] data = null;
/*     */     try {
/*     */       while (true) {
/*  85 */         byte tmpByte = (byte)inputStream.read();
/*  86 */         if (tmpByte == -1) {
/*     */           break;
/*     */         }
/*  89 */         if (tmpByte == 2) {
/*  90 */           key = stringBuffer.toString();
/*     */           
/*  92 */           inputStream.read(lenBytes);
/*     */           
/*  94 */           int len = functions.bytesToInt(lenBytes);
/*     */           
/*  96 */           data = new byte[len];
/*     */           
/*  98 */           int readOneLen = 0;
/*     */           
/* 100 */           while ((readOneLen += inputStream.read(data, readOneLen, data.length - readOneLen)) < data.length);
/*     */           
/* 102 */           resParameter.addParameterByteArray(key, data);
/*     */           
/* 104 */           stringBuffer.reset(); continue;
/*     */         } 
/* 106 */         if (tmpByte > 32 && tmpByte <= 126) {
/* 107 */           stringBuffer.write(tmpByte);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 114 */       stringBuffer.close();
/* 115 */       inputStream.close();
/* 116 */     } catch (Exception e) {
/* 117 */       e.printStackTrace();
/*     */     } 
/* 119 */     if (resParameter.hashMap.size() > 0) {
/* 120 */       return resParameter;
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public byte[] serialize() {
/* 126 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream(len());
/* 127 */     serialize(outputStream);
/* 128 */     return outputStream.toByteArray();
/*     */   }
/*     */   
/*     */   public void serialize(ByteArrayOutputStream outputStream) {
/* 132 */     Iterator<String> keys = this.hashMap.keySet().iterator();
/*     */ 
/*     */     
/* 135 */     byte[] value = null;
/*     */     
/* 137 */     while (keys.hasNext()) {
/*     */       
/*     */       try {
/* 140 */         String key = keys.next();
/*     */         
/* 142 */         value = this.hashMap.get(key);
/*     */         
/* 144 */         outputStream.write(key.getBytes());
/* 145 */         outputStream.write(2);
/* 146 */         outputStream.write(functions.intToBytes(value.length));
/* 147 */         outputStream.write(value);
/*     */       }
/* 149 */       catch (Exception e) {
/* 150 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\http\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */