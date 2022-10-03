/*     */ package org.springframework.core.metrics.jfr;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.springframework.core.metrics.StartupStep;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FlightRecorderStartupStep
/*     */   implements StartupStep
/*     */ {
/*     */   private final FlightRecorderStartupEvent event;
/*  38 */   private final FlightRecorderTags tags = new FlightRecorderTags();
/*     */ 
/*     */   
/*     */   private final Consumer<FlightRecorderStartupStep> recordingCallback;
/*     */ 
/*     */ 
/*     */   
/*     */   public FlightRecorderStartupStep(long id, String name, long parentId, Consumer<FlightRecorderStartupStep> recordingCallback) {
/*  46 */     this.event = new FlightRecorderStartupEvent(id, name, parentId);
/*  47 */     this.event.begin();
/*  48 */     this.recordingCallback = recordingCallback;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  54 */     return this.event.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  59 */     return this.event.eventId;
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getParentId() {
/*  64 */     return Long.valueOf(this.event.parentId);
/*     */   }
/*     */ 
/*     */   
/*     */   public StartupStep tag(String key, String value) {
/*  69 */     this.tags.add(key, value);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StartupStep tag(String key, Supplier<String> value) {
/*  75 */     this.tags.add(key, value.get());
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StartupStep.Tags getTags() {
/*  81 */     return this.tags;
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  86 */     this.event.end();
/*  87 */     if (this.event.shouldCommit()) {
/*  88 */       StringBuilder builder = new StringBuilder();
/*  89 */       this.tags.forEach(tag -> builder.append(tag.getKey()).append('=').append(tag.getValue()).append(','));
/*     */ 
/*     */       
/*  92 */       this.event.setTags(builder.toString());
/*     */     } 
/*  94 */     this.event.commit();
/*  95 */     this.recordingCallback.accept(this);
/*     */   }
/*     */   
/*     */   protected FlightRecorderStartupEvent getEvent() {
/*  99 */     return this.event;
/*     */   }
/*     */   
/*     */   static class FlightRecorderTags
/*     */     implements StartupStep.Tags
/*     */   {
/* 105 */     private StartupStep.Tag[] tags = new StartupStep.Tag[0];
/*     */     
/*     */     public void add(String key, String value) {
/* 108 */       StartupStep.Tag[] newTags = new StartupStep.Tag[this.tags.length + 1];
/* 109 */       System.arraycopy(this.tags, 0, newTags, 0, this.tags.length);
/* 110 */       newTags[newTags.length - 1] = new FlightRecorderStartupStep.FlightRecorderTag(key, value);
/* 111 */       this.tags = newTags;
/*     */     }
/*     */     
/*     */     public void add(String key, Supplier<String> value) {
/* 115 */       add(key, value.get());
/*     */     }
/*     */ 
/*     */     
/*     */     @NotNull
/*     */     public Iterator<StartupStep.Tag> iterator() {
/* 121 */       return new TagsIterator();
/*     */     }
/*     */     
/*     */     private class TagsIterator
/*     */       implements Iterator<StartupStep.Tag> {
/* 126 */       private int idx = 0;
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/* 130 */         return (this.idx < FlightRecorderStartupStep.FlightRecorderTags.this.tags.length);
/*     */       }
/*     */ 
/*     */       
/*     */       public StartupStep.Tag next() {
/* 135 */         return FlightRecorderStartupStep.FlightRecorderTags.this.tags[this.idx++];
/*     */       }
/*     */ 
/*     */       
/*     */       public void remove() {
/* 140 */         throw new UnsupportedOperationException("tags are append only");
/*     */       }
/*     */       
/*     */       private TagsIterator() {}
/*     */     }
/*     */   }
/*     */   
/*     */   static class FlightRecorderTag
/*     */     implements StartupStep.Tag {
/*     */     private final String key;
/*     */     private final String value;
/*     */     
/*     */     public FlightRecorderTag(String key, String value) {
/* 153 */       this.key = key;
/* 154 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 159 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 164 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\metrics\jfr\FlightRecorderStartupStep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */