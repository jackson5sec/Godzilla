/*     */ package com.jgoodies.forms.layout;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import java.awt.Container;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
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
/*     */ public final class BoundedSize
/*     */   implements Size, Serializable
/*     */ {
/*     */   private final Size basis;
/*     */   private final Size lowerBound;
/*     */   private final Size upperBound;
/*     */   
/*     */   public BoundedSize(Size basis, Size lowerBound, Size upperBound) {
/*  83 */     this.basis = (Size)Preconditions.checkNotNull(basis, "The basis must not be null.");
/*  84 */     this.lowerBound = lowerBound;
/*  85 */     this.upperBound = upperBound;
/*  86 */     if (lowerBound == null && upperBound == null) {
/*  87 */       throw new IllegalArgumentException("A bounded size must have a non-null lower or upper bound.");
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
/*     */   public Size getBasis() {
/* 103 */     return this.basis;
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
/*     */   public Size getLowerBound() {
/* 115 */     return this.lowerBound;
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
/*     */   public Size getUpperBound() {
/* 127 */     return this.upperBound;
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
/*     */   public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
/* 154 */     int size = this.basis.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     if (this.lowerBound != null) {
/* 160 */       size = Math.max(size, this.lowerBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     if (this.upperBound != null) {
/* 168 */       size = Math.min(size, this.upperBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     return size;
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
/*     */   public boolean compressible() {
/* 192 */     return getBasis().compressible();
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
/*     */   public boolean equals(Object object) {
/* 209 */     if (this == object) {
/* 210 */       return true;
/*     */     }
/* 212 */     if (!(object instanceof BoundedSize)) {
/* 213 */       return false;
/*     */     }
/* 215 */     BoundedSize size = (BoundedSize)object;
/* 216 */     return (this.basis.equals(size.basis) && ((this.lowerBound == null && size.lowerBound == null) || (this.lowerBound != null && this.lowerBound.equals(size.lowerBound))) && ((this.upperBound == null && size.upperBound == null) || (this.upperBound != null && this.upperBound.equals(size.upperBound))));
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
/*     */   
/*     */   public int hashCode() {
/* 234 */     int hashValue = this.basis.hashCode();
/* 235 */     if (this.lowerBound != null) {
/* 236 */       hashValue = hashValue * 37 + this.lowerBound.hashCode();
/*     */     }
/* 238 */     if (this.upperBound != null) {
/* 239 */       hashValue = hashValue * 37 + this.upperBound.hashCode();
/*     */     }
/* 241 */     return hashValue;
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
/*     */   public String toString() {
/* 255 */     return encode();
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
/*     */   public String encode() {
/* 268 */     StringBuffer buffer = new StringBuffer("[");
/* 269 */     if (this.lowerBound != null) {
/* 270 */       buffer.append(this.lowerBound.encode());
/* 271 */       buffer.append(',');
/*     */     } 
/* 273 */     buffer.append(this.basis.encode());
/* 274 */     if (this.upperBound != null) {
/* 275 */       buffer.append(',');
/* 276 */       buffer.append(this.upperBound.encode());
/*     */     } 
/* 278 */     buffer.append(']');
/* 279 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\BoundedSize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */