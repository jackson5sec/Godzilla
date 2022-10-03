/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.Annotation;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterAnnotationsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
/*     */   public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";
/*     */   
/*     */   public ParameterAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
/*  71 */     super(cp, attrname, info);
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
/*     */   public ParameterAnnotationsAttribute(ConstPool cp, String attrname) {
/*  86 */     this(cp, attrname, new byte[] { 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParameterAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  95 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numParameters() {
/* 102 */     return this.info[0] & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 110 */     AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, classnames);
/*     */     try {
/* 112 */       copier.parameters();
/* 113 */       return new ParameterAnnotationsAttribute(newCp, getName(), copier
/* 114 */           .close());
/*     */     }
/* 116 */     catch (Exception e) {
/* 117 */       throw new RuntimeException(e.toString());
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
/*     */   public Annotation[][] getAnnotations() {
/*     */     try {
/* 135 */       return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseParameters();
/*     */     }
/* 137 */     catch (Exception e) {
/* 138 */       throw new RuntimeException(e.toString());
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
/*     */   public void setAnnotations(Annotation[][] params) {
/* 152 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 153 */     AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
/*     */     try {
/* 155 */       writer.numParameters(params.length);
/* 156 */       for (Annotation[] anno : params) {
/* 157 */         writer.numAnnotations(anno.length);
/* 158 */         for (int j = 0; j < anno.length; j++) {
/* 159 */           anno[j].write(writer);
/*     */         }
/*     */       } 
/* 162 */       writer.close();
/*     */     }
/* 164 */     catch (IOException e) {
/* 165 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 168 */     set(output.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/* 177 */     Map<String, String> map = new HashMap<>();
/* 178 */     map.put(oldname, newname);
/* 179 */     renameClass(map);
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(Map<String, String> classnames) {
/* 184 */     AnnotationsAttribute.Renamer renamer = new AnnotationsAttribute.Renamer(this.info, getConstPool(), classnames);
/*     */     try {
/* 186 */       renamer.parameters();
/* 187 */     } catch (Exception e) {
/* 188 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void getRefClasses(Map<String, String> classnames) {
/* 193 */     renameClass(classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 200 */     Annotation[][] aa = getAnnotations();
/* 201 */     StringBuilder sbuf = new StringBuilder();
/* 202 */     for (Annotation[] a : aa) {
/* 203 */       for (Annotation i : a) {
/* 204 */         sbuf.append(i.toString()).append(" ");
/*     */       }
/* 206 */       sbuf.append(", ");
/*     */     } 
/*     */     
/* 209 */     return sbuf.toString().replaceAll(" (?=,)|, $", "");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ParameterAnnotationsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */