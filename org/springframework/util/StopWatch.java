/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StopWatch
/*     */ {
/*     */   private final String id;
/*     */   private boolean keepTaskList = true;
/*  58 */   private final List<TaskInfo> taskList = new ArrayList<>(1);
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTimeNanos;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String currentTaskName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TaskInfo lastTaskInfo;
/*     */ 
/*     */   
/*     */   private int taskCount;
/*     */ 
/*     */   
/*     */   private long totalTimeNanos;
/*     */ 
/*     */ 
/*     */   
/*     */   public StopWatch() {
/*  81 */     this("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StopWatch(String id) {
/*  92 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 103 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepTaskList(boolean keepTaskList) {
/* 114 */     this.keepTaskList = keepTaskList;
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
/*     */   public void start() throws IllegalStateException {
/* 126 */     start("");
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
/*     */   public void start(String taskName) throws IllegalStateException {
/* 138 */     if (this.currentTaskName != null) {
/* 139 */       throw new IllegalStateException("Can't start StopWatch: it's already running");
/*     */     }
/* 141 */     this.currentTaskName = taskName;
/* 142 */     this.startTimeNanos = System.nanoTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws IllegalStateException {
/* 153 */     if (this.currentTaskName == null) {
/* 154 */       throw new IllegalStateException("Can't stop StopWatch: it's not running");
/*     */     }
/* 156 */     long lastTime = System.nanoTime() - this.startTimeNanos;
/* 157 */     this.totalTimeNanos += lastTime;
/* 158 */     this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
/* 159 */     if (this.keepTaskList) {
/* 160 */       this.taskList.add(this.lastTaskInfo);
/*     */     }
/* 162 */     this.taskCount++;
/* 163 */     this.currentTaskName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 171 */     return (this.currentTaskName != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String currentTaskName() {
/* 181 */     return this.currentTaskName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastTaskTimeNanos() throws IllegalStateException {
/* 190 */     if (this.lastTaskInfo == null) {
/* 191 */       throw new IllegalStateException("No tasks run: can't get last task interval");
/*     */     }
/* 193 */     return this.lastTaskInfo.getTimeNanos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastTaskTimeMillis() throws IllegalStateException {
/* 201 */     if (this.lastTaskInfo == null) {
/* 202 */       throw new IllegalStateException("No tasks run: can't get last task interval");
/*     */     }
/* 204 */     return this.lastTaskInfo.getTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastTaskName() throws IllegalStateException {
/* 211 */     if (this.lastTaskInfo == null) {
/* 212 */       throw new IllegalStateException("No tasks run: can't get last task name");
/*     */     }
/* 214 */     return this.lastTaskInfo.getTaskName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskInfo getLastTaskInfo() throws IllegalStateException {
/* 221 */     if (this.lastTaskInfo == null) {
/* 222 */       throw new IllegalStateException("No tasks run: can't get last task info");
/*     */     }
/* 224 */     return this.lastTaskInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalTimeNanos() {
/* 235 */     return this.totalTimeNanos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalTimeMillis() {
/* 244 */     return nanosToMillis(this.totalTimeNanos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTotalTimeSeconds() {
/* 253 */     return nanosToSeconds(this.totalTimeNanos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTaskCount() {
/* 260 */     return this.taskCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskInfo[] getTaskInfo() {
/* 267 */     if (!this.keepTaskList) {
/* 268 */       throw new UnsupportedOperationException("Task info is not being kept!");
/*     */     }
/* 270 */     return this.taskList.<TaskInfo>toArray(new TaskInfo[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String shortSummary() {
/* 278 */     return "StopWatch '" + getId() + "': running time = " + getTotalTimeNanos() + " ns";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prettyPrint() {
/* 287 */     StringBuilder sb = new StringBuilder(shortSummary());
/* 288 */     sb.append('\n');
/* 289 */     if (!this.keepTaskList) {
/* 290 */       sb.append("No task info kept");
/*     */     } else {
/*     */       
/* 293 */       sb.append("---------------------------------------------\n");
/* 294 */       sb.append("ns         %     Task name\n");
/* 295 */       sb.append("---------------------------------------------\n");
/* 296 */       NumberFormat nf = NumberFormat.getNumberInstance();
/* 297 */       nf.setMinimumIntegerDigits(9);
/* 298 */       nf.setGroupingUsed(false);
/* 299 */       NumberFormat pf = NumberFormat.getPercentInstance();
/* 300 */       pf.setMinimumIntegerDigits(3);
/* 301 */       pf.setGroupingUsed(false);
/* 302 */       for (TaskInfo task : getTaskInfo()) {
/* 303 */         sb.append(nf.format(task.getTimeNanos())).append("  ");
/* 304 */         sb.append(pf.format(task.getTimeNanos() / getTotalTimeNanos())).append("  ");
/* 305 */         sb.append(task.getTaskName()).append('\n');
/*     */       } 
/*     */     } 
/* 308 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 318 */     StringBuilder sb = new StringBuilder(shortSummary());
/* 319 */     if (this.keepTaskList) {
/* 320 */       for (TaskInfo task : getTaskInfo()) {
/* 321 */         sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
/* 322 */         long percent = Math.round(100.0D * task.getTimeNanos() / getTotalTimeNanos());
/* 323 */         sb.append(" = ").append(percent).append('%');
/*     */       } 
/*     */     } else {
/*     */       
/* 327 */       sb.append("; no task info kept");
/*     */     } 
/* 329 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static long nanosToMillis(long duration) {
/* 334 */     return TimeUnit.NANOSECONDS.toMillis(duration);
/*     */   }
/*     */   
/*     */   private static double nanosToSeconds(long duration) {
/* 338 */     return duration / 1.0E9D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class TaskInfo
/*     */   {
/*     */     private final String taskName;
/*     */ 
/*     */     
/*     */     private final long timeNanos;
/*     */ 
/*     */     
/*     */     TaskInfo(String taskName, long timeNanos) {
/* 352 */       this.taskName = taskName;
/* 353 */       this.timeNanos = timeNanos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getTaskName() {
/* 360 */       return this.taskName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getTimeNanos() {
/* 370 */       return this.timeNanos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getTimeMillis() {
/* 379 */       return StopWatch.nanosToMillis(this.timeNanos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getTimeSeconds() {
/* 388 */       return StopWatch.nanosToSeconds(this.timeNanos);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\StopWatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */