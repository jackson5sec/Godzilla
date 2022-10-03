/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ final class ByteStream
/*     */   extends OutputStream
/*     */ {
/*     */   private byte[] buf;
/*     */   private int count;
/*     */   
/*     */   public ByteStream() {
/*  26 */     this(32);
/*     */   }
/*     */   public ByteStream(int size) {
/*  29 */     this.buf = new byte[size];
/*  30 */     this.count = 0;
/*     */   }
/*     */   
/*  33 */   public int getPos() { return this.count; } public int size() {
/*  34 */     return this.count;
/*     */   }
/*     */   public void writeBlank(int len) {
/*  37 */     enlarge(len);
/*  38 */     this.count += len;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] data) {
/*  43 */     write(data, 0, data.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] data, int off, int len) {
/*  48 */     enlarge(len);
/*  49 */     System.arraycopy(data, off, this.buf, this.count, len);
/*  50 */     this.count += len;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) {
/*  55 */     enlarge(1);
/*  56 */     int oldCount = this.count;
/*  57 */     this.buf[oldCount] = (byte)b;
/*  58 */     this.count = oldCount + 1;
/*     */   }
/*     */   
/*     */   public void writeShort(int s) {
/*  62 */     enlarge(2);
/*  63 */     int oldCount = this.count;
/*  64 */     this.buf[oldCount] = (byte)(s >>> 8);
/*  65 */     this.buf[oldCount + 1] = (byte)s;
/*  66 */     this.count = oldCount + 2;
/*     */   }
/*     */   
/*     */   public void writeInt(int i) {
/*  70 */     enlarge(4);
/*  71 */     int oldCount = this.count;
/*  72 */     this.buf[oldCount] = (byte)(i >>> 24);
/*  73 */     this.buf[oldCount + 1] = (byte)(i >>> 16);
/*  74 */     this.buf[oldCount + 2] = (byte)(i >>> 8);
/*  75 */     this.buf[oldCount + 3] = (byte)i;
/*  76 */     this.count = oldCount + 4;
/*     */   }
/*     */   
/*     */   public void writeLong(long i) {
/*  80 */     enlarge(8);
/*  81 */     int oldCount = this.count;
/*  82 */     this.buf[oldCount] = (byte)(int)(i >>> 56L);
/*  83 */     this.buf[oldCount + 1] = (byte)(int)(i >>> 48L);
/*  84 */     this.buf[oldCount + 2] = (byte)(int)(i >>> 40L);
/*  85 */     this.buf[oldCount + 3] = (byte)(int)(i >>> 32L);
/*  86 */     this.buf[oldCount + 4] = (byte)(int)(i >>> 24L);
/*  87 */     this.buf[oldCount + 5] = (byte)(int)(i >>> 16L);
/*  88 */     this.buf[oldCount + 6] = (byte)(int)(i >>> 8L);
/*  89 */     this.buf[oldCount + 7] = (byte)(int)i;
/*  90 */     this.count = oldCount + 8;
/*     */   }
/*     */   
/*     */   public void writeFloat(float v) {
/*  94 */     writeInt(Float.floatToIntBits(v));
/*     */   }
/*     */   
/*     */   public void writeDouble(double v) {
/*  98 */     writeLong(Double.doubleToLongBits(v));
/*     */   }
/*     */   
/*     */   public void writeUTF(String s) {
/* 102 */     int sLen = s.length();
/* 103 */     int pos = this.count;
/* 104 */     enlarge(sLen + 2);
/*     */     
/* 106 */     byte[] buffer = this.buf;
/* 107 */     buffer[pos++] = (byte)(sLen >>> 8);
/* 108 */     buffer[pos++] = (byte)sLen;
/* 109 */     for (int i = 0; i < sLen; i++) {
/* 110 */       char c = s.charAt(i);
/* 111 */       if ('\001' <= c && c <= '') {
/* 112 */         buffer[pos++] = (byte)c;
/*     */       } else {
/* 114 */         writeUTF2(s, sLen, i);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 119 */     this.count = pos;
/*     */   }
/*     */   
/*     */   private void writeUTF2(String s, int sLen, int offset) {
/* 123 */     int size = sLen;
/* 124 */     for (int i = offset; i < sLen; i++) {
/* 125 */       int c = s.charAt(i);
/* 126 */       if (c > 2047) {
/* 127 */         size += 2;
/* 128 */       } else if (c == 0 || c > 127) {
/* 129 */         size++;
/*     */       } 
/*     */     } 
/* 132 */     if (size > 65535) {
/* 133 */       throw new RuntimeException("encoded string too long: " + sLen + size + " bytes");
/*     */     }
/*     */     
/* 136 */     enlarge(size + 2);
/* 137 */     int pos = this.count;
/* 138 */     byte[] buffer = this.buf;
/* 139 */     buffer[pos] = (byte)(size >>> 8);
/* 140 */     buffer[pos + 1] = (byte)size;
/* 141 */     pos += 2 + offset;
/* 142 */     for (int j = offset; j < sLen; j++) {
/* 143 */       int c = s.charAt(j);
/* 144 */       if (1 <= c && c <= 127) {
/* 145 */         buffer[pos++] = (byte)c;
/* 146 */       } else if (c > 2047) {
/* 147 */         buffer[pos] = (byte)(0xE0 | c >> 12 & 0xF);
/* 148 */         buffer[pos + 1] = (byte)(0x80 | c >> 6 & 0x3F);
/* 149 */         buffer[pos + 2] = (byte)(0x80 | c & 0x3F);
/* 150 */         pos += 3;
/*     */       } else {
/*     */         
/* 153 */         buffer[pos] = (byte)(0xC0 | c >> 6 & 0x1F);
/* 154 */         buffer[pos + 1] = (byte)(0x80 | c & 0x3F);
/* 155 */         pos += 2;
/*     */       } 
/*     */     } 
/*     */     
/* 159 */     this.count = pos;
/*     */   }
/*     */   
/*     */   public void write(int pos, int value) {
/* 163 */     this.buf[pos] = (byte)value;
/*     */   }
/*     */   
/*     */   public void writeShort(int pos, int value) {
/* 167 */     this.buf[pos] = (byte)(value >>> 8);
/* 168 */     this.buf[pos + 1] = (byte)value;
/*     */   }
/*     */   
/*     */   public void writeInt(int pos, int value) {
/* 172 */     this.buf[pos] = (byte)(value >>> 24);
/* 173 */     this.buf[pos + 1] = (byte)(value >>> 16);
/* 174 */     this.buf[pos + 2] = (byte)(value >>> 8);
/* 175 */     this.buf[pos + 3] = (byte)value;
/*     */   }
/*     */   
/*     */   public byte[] toByteArray() {
/* 179 */     byte[] buf2 = new byte[this.count];
/* 180 */     System.arraycopy(this.buf, 0, buf2, 0, this.count);
/* 181 */     return buf2;
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 185 */     out.write(this.buf, 0, this.count);
/*     */   }
/*     */   
/*     */   public void enlarge(int delta) {
/* 189 */     int newCount = this.count + delta;
/* 190 */     if (newCount > this.buf.length) {
/* 191 */       int newLen = this.buf.length << 1;
/* 192 */       byte[] newBuf = new byte[(newLen > newCount) ? newLen : newCount];
/* 193 */       System.arraycopy(this.buf, 0, newBuf, 0, this.count);
/* 194 */       this.buf = newBuf;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ByteStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */