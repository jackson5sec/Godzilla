/*    */ package org.springframework.core.io;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DescriptiveResource
/*    */   extends AbstractResource
/*    */ {
/*    */   private final String description;
/*    */   
/*    */   public DescriptiveResource(@Nullable String description) {
/* 45 */     this.description = (description != null) ? description : "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean exists() {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadable() {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 61 */     throw new FileNotFoundException(
/* 62 */         getDescription() + " cannot be opened because it does not point to a readable resource");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 67 */     return this.description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 76 */     return (this == other || (other instanceof DescriptiveResource && ((DescriptiveResource)other).description
/* 77 */       .equals(this.description)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return this.description.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\DescriptiveResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */