/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ import javassist.bytecode.annotation.MemberValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationDefaultAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "AnnotationDefault";
/*     */   
/*     */   public AnnotationDefaultAttribute(ConstPool cp, byte[] info) {
/*  81 */     super(cp, "AnnotationDefault", info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationDefaultAttribute(ConstPool cp) {
/*  92 */     this(cp, new byte[] { 0, 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationDefaultAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 101 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 109 */     AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, classnames);
/*     */     
/*     */     try {
/* 112 */       copier.memberValue(0);
/* 113 */       return new AnnotationDefaultAttribute(newCp, copier.close());
/*     */     }
/* 115 */     catch (Exception e) {
/* 116 */       throw new RuntimeException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberValue getDefaultValue() {
/*     */     try {
/* 126 */       return (new AnnotationsAttribute.Parser(this.info, this.constPool))
/* 127 */         .parseMemberValue();
/*     */     }
/* 129 */     catch (Exception e) {
/* 130 */       throw new RuntimeException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultValue(MemberValue value) {
/* 141 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 142 */     AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
/*     */     try {
/* 144 */       value.write(writer);
/* 145 */       writer.close();
/*     */     }
/* 147 */     catch (IOException e) {
/* 148 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 151 */     set(output.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return getDefaultValue().toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\AnnotationDefaultAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */