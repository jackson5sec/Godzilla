/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
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
/*     */ 
/*     */ @Beta
/*     */ public final class Funnels
/*     */ {
/*     */   public static Funnel<byte[]> byteArrayFunnel() {
/*  36 */     return ByteArrayFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ByteArrayFunnel implements Funnel<byte[]> {
/*  40 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(byte[] from, PrimitiveSink into) {
/*  44 */       into.putBytes(from);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  49 */       return "Funnels.byteArrayFunnel()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Funnel<CharSequence> unencodedCharsFunnel() {
/*  61 */     return UnencodedCharsFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum UnencodedCharsFunnel implements Funnel<CharSequence> {
/*  65 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(CharSequence from, PrimitiveSink into) {
/*  69 */       into.putUnencodedChars(from);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  74 */       return "Funnels.unencodedCharsFunnel()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Funnel<CharSequence> stringFunnel(Charset charset) {
/*  85 */     return new StringCharsetFunnel(charset);
/*     */   }
/*     */   
/*     */   private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
/*     */     private final Charset charset;
/*     */     
/*     */     StringCharsetFunnel(Charset charset) {
/*  92 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public void funnel(CharSequence from, PrimitiveSink into) {
/*  97 */       into.putString(from, this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 102 */       return "Funnels.stringFunnel(" + this.charset.name() + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 107 */       if (o instanceof StringCharsetFunnel) {
/* 108 */         StringCharsetFunnel funnel = (StringCharsetFunnel)o;
/* 109 */         return this.charset.equals(funnel.charset);
/*     */       } 
/* 111 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 116 */       return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 120 */       return new SerializedForm(this.charset);
/*     */     }
/*     */     
/*     */     private static class SerializedForm implements Serializable {
/*     */       private final String charsetCanonicalName;
/*     */       
/*     */       SerializedForm(Charset charset) {
/* 127 */         this.charsetCanonicalName = charset.name();
/*     */       }
/*     */       private static final long serialVersionUID = 0L;
/*     */       private Object readResolve() {
/* 131 */         return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Funnel<Integer> integerFunnel() {
/* 144 */     return IntegerFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IntegerFunnel implements Funnel<Integer> {
/* 148 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(Integer from, PrimitiveSink into) {
/* 152 */       into.putInt(from.intValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 157 */       return "Funnels.integerFunnel()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel) {
/* 168 */     return new SequentialFunnel<>(elementFunnel);
/*     */   }
/*     */   
/*     */   private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
/*     */     private final Funnel<E> elementFunnel;
/*     */     
/*     */     SequentialFunnel(Funnel<E> elementFunnel) {
/* 175 */       this.elementFunnel = (Funnel<E>)Preconditions.checkNotNull(elementFunnel);
/*     */     }
/*     */ 
/*     */     
/*     */     public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
/* 180 */       for (E e : from) {
/* 181 */         this.elementFunnel.funnel(e, into);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 187 */       return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 192 */       if (o instanceof SequentialFunnel) {
/* 193 */         SequentialFunnel<?> funnel = (SequentialFunnel)o;
/* 194 */         return this.elementFunnel.equals(funnel.elementFunnel);
/*     */       } 
/* 196 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 201 */       return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Funnel<Long> longFunnel() {
/* 211 */     return LongFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LongFunnel implements Funnel<Long> {
/* 215 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void funnel(Long from, PrimitiveSink into) {
/* 219 */       into.putLong(from.longValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 224 */       return "Funnels.longFunnel()";
/*     */     }
/*     */   }
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
/*     */   public static OutputStream asOutputStream(PrimitiveSink sink) {
/* 239 */     return new SinkAsStream(sink);
/*     */   }
/*     */   
/*     */   private static class SinkAsStream extends OutputStream {
/*     */     final PrimitiveSink sink;
/*     */     
/*     */     SinkAsStream(PrimitiveSink sink) {
/* 246 */       this.sink = (PrimitiveSink)Preconditions.checkNotNull(sink);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/* 251 */       this.sink.putByte((byte)b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes) {
/* 256 */       this.sink.putBytes(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes, int off, int len) {
/* 261 */       this.sink.putBytes(bytes, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 266 */       return "Funnels.asOutputStream(" + this.sink + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\Funnels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */