/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.beans.Encoder;
/*     */ import java.beans.Expression;
/*     */ import java.beans.PersistenceDelegate;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
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
/*     */ public class BoundSize
/*     */   implements Serializable
/*     */ {
/*  51 */   public static final BoundSize NULL_SIZE = new BoundSize(null, null);
/*  52 */   public static final BoundSize ZERO_PIXEL = new BoundSize(UnitValue.ZERO, "0px");
/*     */   
/*     */   private final transient UnitValue min;
/*     */   
/*     */   private final transient UnitValue pref;
/*     */   
/*     */   private final transient UnitValue max;
/*     */   
/*     */   private final transient boolean gapPush;
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public BoundSize(UnitValue minMaxPref, String createString) {
/*  65 */     this(minMaxPref, minMaxPref, minMaxPref, createString);
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
/*     */   public BoundSize(UnitValue min, UnitValue preferred, UnitValue max, String createString) {
/*  77 */     this(min, preferred, max, false, createString);
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
/*     */   public BoundSize(UnitValue min, UnitValue preferred, UnitValue max, boolean gapPush, String createString) {
/*  90 */     this.min = min;
/*  91 */     this.pref = preferred;
/*  92 */     this.max = max;
/*  93 */     this.gapPush = gapPush;
/*     */     
/*  95 */     LayoutUtil.putCCString(this, createString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UnitValue getMin() {
/* 103 */     return this.min;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UnitValue getPreferred() {
/* 111 */     return this.pref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UnitValue getMax() {
/* 119 */     return this.max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getGapPush() {
/* 127 */     return this.gapPush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnset() {
/* 136 */     return (this == ZERO_PIXEL || (this.pref == null && this.min == null && this.max == null && !this.gapPush));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int constrain(int size, float refValue, ContainerWrapper parent) {
/* 147 */     if (this.max != null)
/* 148 */       size = Math.min(size, this.max.getPixels(refValue, parent, parent)); 
/* 149 */     if (this.min != null)
/* 150 */       size = Math.max(size, this.min.getPixels(refValue, parent, parent)); 
/* 151 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final UnitValue getSize(int sizeType) {
/* 160 */     switch (sizeType) {
/*     */       case 0:
/* 162 */         return this.min;
/*     */       case 1:
/* 164 */         return this.pref;
/*     */       case 2:
/* 166 */         return this.max;
/*     */     } 
/* 168 */     throw new IllegalArgumentException("Unknown size: " + sizeType);
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
/*     */   final int[] getPixelSizes(float refSize, ContainerWrapper parent, ComponentWrapper comp) {
/* 182 */     return new int[] { (this.min != null) ? this.min
/* 183 */         .getPixels(refSize, parent, comp) : 0, (this.pref != null) ? this.pref
/* 184 */         .getPixels(refSize, parent, comp) : 0, (this.max != null) ? this.max
/* 185 */         .getPixels(refSize, parent, comp) : 2097051 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getConstraintString() {
/* 194 */     String cs = LayoutUtil.getCCString(this);
/* 195 */     if (cs != null) {
/* 196 */       return cs;
/*     */     }
/* 198 */     if (this.min == this.pref && this.pref == this.max) {
/* 199 */       return (this.min != null) ? (this.min.getConstraintString() + "!") : "null";
/*     */     }
/* 201 */     StringBuilder sb = new StringBuilder(16);
/*     */     
/* 203 */     if (this.min != null) {
/* 204 */       sb.append(this.min.getConstraintString()).append(':');
/*     */     }
/* 206 */     if (this.pref != null) {
/* 207 */       if (this.min == null && this.max != null)
/* 208 */         sb.append(":"); 
/* 209 */       sb.append(this.pref.getConstraintString());
/* 210 */     } else if (this.min != null) {
/* 211 */       sb.append('n');
/*     */     } 
/*     */     
/* 214 */     if (this.max != null) {
/* 215 */       sb.append((sb.length() == 0) ? "::" : ":").append(this.max.getConstraintString());
/*     */     }
/* 217 */     if (this.gapPush) {
/* 218 */       if (sb.length() > 0)
/* 219 */         sb.append(':'); 
/* 220 */       sb.append("push");
/*     */     } 
/*     */     
/* 223 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   void checkNotLinked() {
/* 228 */     if (isLinked()) {
/* 229 */       throw new IllegalArgumentException("Size may not contain links");
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isLinked() {
/* 234 */     return ((this.min != null && this.min.isLinkedDeep()) || (this.pref != null && this.pref.isLinkedDeep()) || (this.max != null && this.max.isLinkedDeep()));
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isAbsolute() {
/* 239 */     return ((this.min == null || this.min.isAbsoluteDeep()) && (this.pref == null || this.pref.isAbsoluteDeep()) && (this.max == null || this.max.isAbsoluteDeep()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 244 */     return "BoundSize{min=" + this.min + ", pref=" + this.pref + ", max=" + this.max + ", gapPush=" + this.gapPush + '}';
/*     */   }
/*     */   
/*     */   static {
/* 248 */     if (LayoutUtil.HAS_BEANS) {
/* 249 */       LayoutUtil.setDelegate(BoundSize.class, new PersistenceDelegate()
/*     */           {
/*     */             protected Expression instantiate(Object oldInstance, Encoder out)
/*     */             {
/* 253 */               BoundSize bs = (BoundSize)oldInstance;
/*     */               
/* 255 */               return new Expression(oldInstance, BoundSize.class, "new", new Object[] { bs
/* 256 */                     .getMin(), bs.getPreferred(), bs.getMax(), Boolean.valueOf(bs.getGapPush()), bs.getConstraintString() });
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readResolve() throws ObjectStreamException {
/* 276 */     return LayoutUtil.getSerializedObject(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 281 */     if (getClass() == BoundSize.class) {
/* 282 */       LayoutUtil.writeAsXML(out, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 287 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\BoundSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */