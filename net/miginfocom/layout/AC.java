/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.util.ArrayList;
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
/*     */ public final class AC
/*     */   implements Externalizable
/*     */ {
/*  51 */   private final ArrayList<DimConstraint> cList = new ArrayList<>(1);
/*     */   
/*  53 */   private transient int curIx = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AC() {
/*  60 */     this.cList.add(new DimConstraint());
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
/*     */   public final DimConstraint[] getConstaints() {
/*  72 */     return this.cList.<DimConstraint>toArray(new DimConstraint[this.cList.size()]);
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
/*     */   public final void setConstaints(DimConstraint[] constr) {
/*  84 */     if (constr == null || constr.length < 1) {
/*  85 */       constr = new DimConstraint[] { new DimConstraint() };
/*     */     }
/*  87 */     this.cList.clear();
/*  88 */     this.cList.ensureCapacity(constr.length);
/*  89 */     for (DimConstraint c : constr) {
/*  90 */       this.cList.add(c);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount() {
/*  98 */     return this.cList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC count(int size) {
/* 108 */     makeSize(size);
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC noGrid() {
/* 120 */     return noGrid(new int[] { this.curIx });
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
/*     */   public final AC noGrid(int... indexes) {
/* 132 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 133 */       int ix = indexes[i];
/* 134 */       makeSize(ix);
/* 135 */       ((DimConstraint)this.cList.get(ix)).setNoGrid(true);
/*     */     } 
/* 137 */     return this;
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
/*     */   public final AC index(int i) {
/* 152 */     makeSize(i);
/* 153 */     this.curIx = i;
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC fill() {
/* 164 */     return fill(new int[] { this.curIx });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC fill(int... indexes) {
/* 175 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 176 */       int ix = indexes[i];
/* 177 */       makeSize(ix);
/* 178 */       ((DimConstraint)this.cList.get(ix)).setFill(true);
/*     */     } 
/* 180 */     return this;
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
/*     */   public final AC sizeGroup() {
/* 224 */     return sizeGroup("", new int[] { this.curIx });
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
/*     */   public final AC sizeGroup(String s) {
/* 236 */     return sizeGroup(s, new int[] { this.curIx });
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
/*     */   public final AC sizeGroup(String s, int... indexes) {
/* 249 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 250 */       int ix = indexes[i];
/* 251 */       makeSize(ix);
/* 252 */       ((DimConstraint)this.cList.get(ix)).setSizeGroup(s);
/*     */     } 
/* 254 */     return this;
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
/*     */   public final AC size(String s) {
/* 266 */     return size(s, new int[] { this.curIx });
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
/*     */   public final AC size(String size, int... indexes) {
/* 279 */     BoundSize bs = ConstraintParser.parseBoundSize(size, false, true);
/* 280 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 281 */       int ix = indexes[i];
/* 282 */       makeSize(ix);
/* 283 */       ((DimConstraint)this.cList.get(ix)).setSize(bs);
/*     */     } 
/* 285 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC gap() {
/* 296 */     this.curIx++;
/* 297 */     makeSize(this.curIx);
/* 298 */     return this;
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
/*     */   public final AC gap(String size) {
/* 310 */     return gap(size, new int[] { this.curIx++ });
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
/*     */   public final AC gap(String size, int... indexes) {
/* 323 */     BoundSize bsa = (size != null) ? ConstraintParser.parseBoundSize(size, true, true) : null;
/*     */     
/* 325 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 326 */       int ix = indexes[i];
/* 327 */       makeSize(ix + 1);
/* 328 */       if (bsa != null)
/* 329 */         ((DimConstraint)this.cList.get(ix)).setGapAfter(bsa); 
/*     */     } 
/* 331 */     return this;
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
/*     */   public final AC align(String side) {
/* 344 */     return align(side, new int[] { this.curIx });
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
/*     */   public final AC align(String side, int... indexes) {
/* 358 */     UnitValue al = ConstraintParser.parseAlignKeywords(side, true);
/* 359 */     if (al == null) {
/* 360 */       al = ConstraintParser.parseAlignKeywords(side, false);
/*     */     }
/* 362 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 363 */       int ix = indexes[i];
/* 364 */       makeSize(ix);
/* 365 */       ((DimConstraint)this.cList.get(ix)).setAlign(al);
/*     */     } 
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC growPrio(int p) {
/* 378 */     return growPrio(p, new int[] { this.curIx });
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
/*     */   public final AC growPrio(int p, int... indexes) {
/* 390 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 391 */       int ix = indexes[i];
/* 392 */       makeSize(ix);
/* 393 */       ((DimConstraint)this.cList.get(ix)).setGrowPriority(p);
/*     */     } 
/* 395 */     return this;
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
/*     */   public final AC grow() {
/* 408 */     return grow(100.0F, new int[] { this.curIx });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC grow(float w) {
/* 419 */     return grow(w, new int[] { this.curIx });
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
/*     */   public final AC grow(float w, int... indexes) {
/* 431 */     Float gw = new Float(w);
/* 432 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 433 */       int ix = indexes[i];
/* 434 */       makeSize(ix);
/* 435 */       ((DimConstraint)this.cList.get(ix)).setGrow(gw);
/*     */     } 
/* 437 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AC shrinkPrio(int p) {
/* 448 */     return shrinkPrio(p, new int[] { this.curIx });
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
/*     */   public final AC shrinkPrio(int p, int... indexes) {
/* 460 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 461 */       int ix = indexes[i];
/* 462 */       makeSize(ix);
/* 463 */       ((DimConstraint)this.cList.get(ix)).setShrinkPriority(p);
/*     */     } 
/* 465 */     return this;
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
/*     */   public final AC shrink() {
/* 478 */     return shrink(100.0F, new int[] { this.curIx });
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
/*     */   public final AC shrink(float w) {
/* 490 */     return shrink(w, new int[] { this.curIx });
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
/*     */   public final AC shrink(float w, int... indexes) {
/* 503 */     Float sw = new Float(w);
/* 504 */     for (int i = indexes.length - 1; i >= 0; i--) {
/* 505 */       int ix = indexes[i];
/* 506 */       makeSize(ix);
/* 507 */       ((DimConstraint)this.cList.get(ix)).setShrink(sw);
/*     */     } 
/* 509 */     return this;
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
/*     */   public final AC shrinkWeight(float w) {
/* 521 */     return shrink(w);
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
/*     */   public final AC shrinkWeight(float w, int... indexes) {
/* 534 */     return shrink(w, indexes);
/*     */   }
/*     */ 
/*     */   
/*     */   private void makeSize(int sz) {
/* 539 */     if (this.cList.size() <= sz) {
/* 540 */       this.cList.ensureCapacity(sz);
/* 541 */       for (int i = this.cList.size(); i <= sz; i++) {
/* 542 */         this.cList.add(new DimConstraint());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() throws ObjectStreamException {
/* 552 */     return LayoutUtil.getSerializedObject(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 558 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 564 */     if (getClass() == AC.class)
/* 565 */       LayoutUtil.writeAsXML(out, this); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\AC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */