/*    */ package org.springframework.core.metrics.jfr;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Description;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.Label;
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
/*    */ @Category({"Spring Application"})
/*    */ @Label("Startup Step")
/*    */ @Description("Spring Application Startup")
/*    */ class FlightRecorderStartupEvent
/*    */   extends Event
/*    */ {
/*    */   public final long eventId;
/*    */   public final long parentId;
/*    */   @Label("Name")
/*    */   public final String name;
/*    */   @Label("Tags")
/* 46 */   String tags = "";
/*    */ 
/*    */   
/*    */   public FlightRecorderStartupEvent(long eventId, String name, long parentId) {
/* 50 */     this.name = name;
/* 51 */     this.eventId = eventId;
/* 52 */     this.parentId = parentId;
/*    */   }
/*    */   
/*    */   public void setTags(String tags) {
/* 56 */     this.tags = tags;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\metrics\jfr\FlightRecorderStartupEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */