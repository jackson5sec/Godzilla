/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import org.mozilla.javascript.IdScriptableObject;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Undefined;
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
/*     */ public abstract class NativeArrayBufferView
/*     */   extends IdScriptableObject
/*     */ {
/*     */   private static final long serialVersionUID = 6884475582973958419L;
/*     */   protected final NativeArrayBuffer arrayBuffer;
/*     */   protected final int offset;
/*     */   protected final int byteLength;
/*     */   private static final int Id_buffer = 1;
/*     */   private static final int Id_byteOffset = 2;
/*     */   private static final int Id_byteLength = 3;
/*     */   private static final int MAX_INSTANCE_ID = 3;
/*     */   
/*     */   public NativeArrayBufferView() {
/*  32 */     this.arrayBuffer = NativeArrayBuffer.EMPTY_BUFFER;
/*  33 */     this.offset = 0;
/*  34 */     this.byteLength = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NativeArrayBufferView(NativeArrayBuffer ab, int offset, int byteLength) {
/*  39 */     this.offset = offset;
/*  40 */     this.byteLength = byteLength;
/*  41 */     this.arrayBuffer = ab;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeArrayBuffer getBuffer() {
/*  48 */     return this.arrayBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getByteOffset() {
/*  55 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getByteLength() {
/*  62 */     return this.byteLength;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean isArg(Object[] args, int i) {
/*  67 */     return (args.length > i && !Undefined.instance.equals(args[i]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxInstanceId() {
/*  75 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/*  81 */     switch (id) { case 1:
/*  82 */         return "buffer";
/*  83 */       case 2: return "byteOffset";
/*  84 */       case 3: return "byteLength"; }
/*  85 */      return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/*  92 */     switch (id) {
/*     */       case 1:
/*  94 */         return this.arrayBuffer;
/*     */       case 2:
/*  96 */         return ScriptRuntime.wrapInt(this.offset);
/*     */       case 3:
/*  98 */         return ScriptRuntime.wrapInt(this.byteLength);
/*     */     } 
/* 100 */     return super.getInstanceIdValue(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/* 111 */     int id = 0; String X = null;
/* 112 */     int s_length = s.length();
/* 113 */     if (s_length == 6) { X = "buffer"; id = 1; }
/* 114 */     else if (s_length == 10)
/* 115 */     { int c = s.charAt(4);
/* 116 */       if (c == 76) { X = "byteLength"; id = 3; }
/* 117 */       else if (c == 79) { X = "byteOffset"; id = 2; }
/*     */        }
/* 119 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 123 */     if (id == 0) {
/* 124 */       return super.findInstanceIdInfo(s);
/*     */     }
/* 126 */     return instanceIdInfo(5, id);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeArrayBufferView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */