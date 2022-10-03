/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import com.kitfox.svg.SVGElementException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrackManager
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 0L;
/*     */   
/*     */   static class TrackKey
/*     */   {
/*     */     String name;
/*     */     int type;
/*     */     
/*     */     TrackKey(AnimationElement base) {
/*  64 */       this(base.getAttribName(), base.getAttribType());
/*     */     }
/*     */ 
/*     */     
/*     */     TrackKey(String name, int type) {
/*  69 */       this.name = name;
/*  70 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  76 */       int hash = (this.name == null) ? 0 : this.name.hashCode();
/*  77 */       hash = hash * 97 + this.type;
/*  78 */       return hash;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  84 */       if (!(obj instanceof TrackKey)) return false; 
/*  85 */       TrackKey key = (TrackKey)obj;
/*  86 */       return (key.type == this.type && key.name.equals(this.name));
/*     */     }
/*     */   }
/*     */   
/*  90 */   HashMap<TrackKey, TrackBase> tracks = new HashMap<TrackKey, TrackBase>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTrackElement(AnimationElement element) throws SVGElementException {
/* 102 */     TrackKey key = new TrackKey(element);
/*     */     
/* 104 */     TrackBase track = this.tracks.get(key);
/*     */     
/* 106 */     if (track == null) {
/*     */ 
/*     */       
/* 109 */       if (element instanceof Animate) {
/*     */         
/* 111 */         switch (((Animate)element).getDataType()) {
/*     */           
/*     */           case 0:
/* 114 */             track = new TrackDouble(element);
/*     */             break;
/*     */           case 1:
/* 117 */             track = new TrackColor(element);
/*     */             break;
/*     */           case 2:
/* 120 */             track = new TrackPath(element);
/*     */             break;
/*     */           default:
/* 123 */             throw new RuntimeException("");
/*     */         } 
/*     */       
/* 126 */       } else if (element instanceof AnimateColor) {
/*     */         
/* 128 */         track = new TrackColor(element);
/*     */       }
/* 130 */       else if (element instanceof AnimateTransform || element instanceof AnimateMotion) {
/*     */         
/* 132 */         track = new TrackTransform(element);
/*     */       } 
/*     */       
/* 135 */       this.tracks.put(key, track);
/*     */     } 
/*     */     
/* 138 */     track.addElement(element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TrackBase getTrack(String name, int type) {
/* 144 */     if (type == 2) {
/*     */       
/* 146 */       TrackBase trackBase = getTrack(name, 0);
/* 147 */       if (trackBase != null) return trackBase; 
/* 148 */       trackBase = getTrack(name, 1);
/* 149 */       if (trackBase != null) return trackBase; 
/* 150 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 154 */     TrackKey key = new TrackKey(name, type);
/* 155 */     TrackBase t = this.tracks.get(key);
/* 156 */     if (t != null) return t;
/*     */ 
/*     */     
/* 159 */     key = new TrackKey(name, 2);
/* 160 */     return this.tracks.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumTracks() {
/* 165 */     return this.tracks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<TrackBase> iterator() {
/* 170 */     return this.tracks.values().iterator();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TrackManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */