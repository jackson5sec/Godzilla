/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.core.NestedIOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.ClassMetadata;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SimpleMetadataReader
/*    */   implements MetadataReader
/*    */ {
/*    */   private static final int PARSING_OPTIONS = 7;
/*    */   private final Resource resource;
/*    */   private final AnnotationMetadata annotationMetadata;
/*    */   
/*    */   SimpleMetadataReader(Resource resource, @Nullable ClassLoader classLoader) throws IOException {
/* 48 */     SimpleAnnotationMetadataReadingVisitor visitor = new SimpleAnnotationMetadataReadingVisitor(classLoader);
/* 49 */     getClassReader(resource).accept(visitor, 7);
/* 50 */     this.resource = resource;
/* 51 */     this.annotationMetadata = visitor.getMetadata();
/*    */   }
/*    */   
/*    */   private static ClassReader getClassReader(Resource resource) throws IOException {
/* 55 */     InputStream is = resource.getInputStream(); Throwable throwable = null;
/*    */     
/* 57 */     try { return new ClassReader(is); }
/*    */     
/* 59 */     catch (IllegalArgumentException ex)
/* 60 */     { throw new NestedIOException("ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: " + resource, ex); }
/*    */     catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/*    */     finally
/* 63 */     { if (is != null) if (throwable != null) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  } else { is.close(); }
/*    */           }
/*    */   
/*    */   }
/*    */   
/*    */   public Resource getResource() {
/* 69 */     return this.resource;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassMetadata getClassMetadata() {
/* 74 */     return (ClassMetadata)this.annotationMetadata;
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationMetadata getAnnotationMetadata() {
/* 79 */     return this.annotationMetadata;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\SimpleMetadataReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */