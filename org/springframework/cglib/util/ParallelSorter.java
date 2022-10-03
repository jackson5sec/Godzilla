/*     */ package org.springframework.cglib.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.ClassesKey;
/*     */ import org.springframework.cglib.core.ReflectUtils;
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
/*     */ public abstract class ParallelSorter
/*     */   extends SorterTemplate
/*     */ {
/*     */   protected Object[] a;
/*     */   private Comparer comparer;
/*     */   
/*     */   public abstract ParallelSorter newInstance(Object[] paramArrayOfObject);
/*     */   
/*     */   public static ParallelSorter create(Object[] arrays) {
/*  66 */     Generator gen = new Generator();
/*  67 */     gen.setArrays(arrays);
/*  68 */     return gen.create();
/*     */   }
/*     */   
/*     */   private int len() {
/*  72 */     return ((Object[])this.a[0]).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void quickSort(int index) {
/*  80 */     quickSort(index, 0, len(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void quickSort(int index, int lo, int hi) {
/*  90 */     quickSort(index, lo, hi, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void quickSort(int index, Comparator cmp) {
/*  99 */     quickSort(index, 0, len(), cmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void quickSort(int index, int lo, int hi, Comparator cmp) {
/* 110 */     chooseComparer(index, cmp);
/* 111 */     quickSort(lo, hi - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeSort(int index) {
/* 118 */     mergeSort(index, 0, len(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeSort(int index, int lo, int hi) {
/* 128 */     mergeSort(index, lo, hi, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeSort(int index, Comparator cmp) {
/* 138 */     mergeSort(index, 0, len(), cmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeSort(int index, int lo, int hi, Comparator cmp) {
/* 149 */     chooseComparer(index, cmp);
/* 150 */     mergeSort(lo, hi - 1);
/*     */   }
/*     */   
/*     */   private void chooseComparer(int index, Comparator cmp) {
/* 154 */     Object array = this.a[index];
/* 155 */     Class<?> type = array.getClass().getComponentType();
/* 156 */     if (type.equals(int.class)) {
/* 157 */       this.comparer = new IntComparer((int[])array);
/* 158 */     } else if (type.equals(long.class)) {
/* 159 */       this.comparer = new LongComparer((long[])array);
/* 160 */     } else if (type.equals(double.class)) {
/* 161 */       this.comparer = new DoubleComparer((double[])array);
/* 162 */     } else if (type.equals(float.class)) {
/* 163 */       this.comparer = new FloatComparer((float[])array);
/* 164 */     } else if (type.equals(short.class)) {
/* 165 */       this.comparer = new ShortComparer((short[])array);
/* 166 */     } else if (type.equals(byte.class)) {
/* 167 */       this.comparer = new ByteComparer((byte[])array);
/* 168 */     } else if (cmp != null) {
/* 169 */       this.comparer = new ComparatorComparer((Object[])array, cmp);
/*     */     } else {
/* 171 */       this.comparer = new ObjectComparer((Object[])array);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int compare(int i, int j) {
/* 176 */     return this.comparer.compare(i, j);
/*     */   }
/*     */   
/*     */   static interface Comparer {
/*     */     int compare(int param1Int1, int param1Int2);
/*     */   }
/*     */   
/*     */   static class ComparatorComparer implements Comparer {
/*     */     private Object[] a;
/*     */     private Comparator cmp;
/*     */     
/*     */     public ComparatorComparer(Object[] a, Comparator cmp) {
/* 188 */       this.a = a;
/* 189 */       this.cmp = cmp;
/*     */     }
/*     */     
/*     */     public int compare(int i, int j) {
/* 193 */       return this.cmp.compare(this.a[i], this.a[j]);
/*     */     } }
/*     */   
/*     */   static class ObjectComparer implements Comparer { private Object[] a;
/*     */     
/*     */     public ObjectComparer(Object[] a) {
/* 199 */       this.a = a;
/*     */     } public int compare(int i, int j) {
/* 201 */       return ((Comparable<Object>)this.a[i]).compareTo(this.a[j]);
/*     */     } }
/*     */ 
/*     */   
/*     */   static class IntComparer implements Comparer { private int[] a;
/*     */     
/* 207 */     public IntComparer(int[] a) { this.a = a; } public int compare(int i, int j) {
/* 208 */       return this.a[i] - this.a[j];
/*     */     } }
/*     */   static class LongComparer implements Comparer { private long[] a;
/*     */     
/*     */     public LongComparer(long[] a) {
/* 213 */       this.a = a;
/*     */     } public int compare(int i, int j) {
/* 215 */       long vi = this.a[i];
/* 216 */       long vj = this.a[j];
/* 217 */       return (vi == vj) ? 0 : ((vi > vj) ? 1 : -1);
/*     */     } }
/*     */   
/*     */   static class FloatComparer implements Comparer { private float[] a;
/*     */     
/*     */     public FloatComparer(float[] a) {
/* 223 */       this.a = a;
/*     */     } public int compare(int i, int j) {
/* 225 */       float vi = this.a[i];
/* 226 */       float vj = this.a[j];
/* 227 */       return (vi == vj) ? 0 : ((vi > vj) ? 1 : -1);
/*     */     } }
/*     */   
/*     */   static class DoubleComparer implements Comparer { private double[] a;
/*     */     
/*     */     public DoubleComparer(double[] a) {
/* 233 */       this.a = a;
/*     */     } public int compare(int i, int j) {
/* 235 */       double vi = this.a[i];
/* 236 */       double vj = this.a[j];
/* 237 */       return (vi == vj) ? 0 : ((vi > vj) ? 1 : -1);
/*     */     } }
/*     */   
/*     */   static class ShortComparer implements Comparer {
/*     */     private short[] a;
/*     */     
/* 243 */     public ShortComparer(short[] a) { this.a = a; } public int compare(int i, int j) {
/* 244 */       return this.a[i] - this.a[j];
/*     */     }
/*     */   }
/*     */   static class ByteComparer implements Comparer { private byte[] a;
/*     */     
/* 249 */     public ByteComparer(byte[] a) { this.a = a; } public int compare(int i, int j) {
/* 250 */       return this.a[i] - this.a[j];
/*     */     } }
/*     */   
/*     */   public static class Generator extends AbstractClassGenerator {
/* 254 */     private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(ParallelSorter.class.getName());
/*     */     
/*     */     private Object[] arrays;
/*     */     
/*     */     public Generator() {
/* 259 */       super(SOURCE);
/*     */     }
/*     */     
/*     */     protected ClassLoader getDefaultClassLoader() {
/* 263 */       return null;
/*     */     }
/*     */     
/*     */     public void setArrays(Object[] arrays) {
/* 267 */       this.arrays = arrays;
/*     */     }
/*     */     
/*     */     public ParallelSorter create() {
/* 271 */       return (ParallelSorter)create(ClassesKey.create(this.arrays));
/*     */     }
/*     */     
/*     */     public void generateClass(ClassVisitor v) throws Exception {
/* 275 */       if (this.arrays.length == 0) {
/* 276 */         throw new IllegalArgumentException("No arrays specified to sort");
/*     */       }
/* 278 */       for (int i = 0; i < this.arrays.length; i++) {
/* 279 */         if (!this.arrays[i].getClass().isArray()) {
/* 280 */           throw new IllegalArgumentException(this.arrays[i].getClass() + " is not an array");
/*     */         }
/*     */       } 
/* 283 */       new ParallelSorterEmitter(v, getClassName(), this.arrays);
/*     */     }
/*     */     
/*     */     protected Object firstInstance(Class type) {
/* 287 */       return ((ParallelSorter)ReflectUtils.newInstance(type)).newInstance(this.arrays);
/*     */     }
/*     */     
/*     */     protected Object nextInstance(Object instance) {
/* 291 */       return ((ParallelSorter)instance).newInstance(this.arrays);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cgli\\util\ParallelSorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */