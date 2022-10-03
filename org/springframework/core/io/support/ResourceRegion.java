/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceRegion
/*    */ {
/*    */   private final Resource resource;
/*    */   private final long position;
/*    */   private final long count;
/*    */   
/*    */   public ResourceRegion(Resource resource, long position, long count) {
/* 47 */     Assert.notNull(resource, "Resource must not be null");
/* 48 */     Assert.isTrue((position >= 0L), "'position' must be larger than or equal to 0");
/* 49 */     Assert.isTrue((count >= 0L), "'count' must be larger than or equal to 0");
/* 50 */     this.resource = resource;
/* 51 */     this.position = position;
/* 52 */     this.count = count;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Resource getResource() {
/* 60 */     return this.resource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getPosition() {
/* 67 */     return this.position;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getCount() {
/* 74 */     return this.count;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\ResourceRegion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */