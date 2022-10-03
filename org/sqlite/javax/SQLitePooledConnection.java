/*     */ package org.sqlite.javax;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.sql.ConnectionEvent;
/*     */ import javax.sql.ConnectionEventListener;
/*     */ import javax.sql.PooledConnection;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.jdbc4.JDBC4PooledConnection;
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
/*     */ public class SQLitePooledConnection
/*     */   extends JDBC4PooledConnection
/*     */ {
/*     */   protected SQLiteConnection physicalConn;
/*     */   protected volatile Connection handleConn;
/*  57 */   protected List<ConnectionEventListener> listeners = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SQLitePooledConnection(SQLiteConnection physicalConn) {
/*  64 */     this.physicalConn = physicalConn;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLiteConnection getPhysicalConn() {
/*  69 */     return this.physicalConn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/*  75 */     if (this.handleConn != null) {
/*  76 */       this.listeners.clear();
/*  77 */       this.handleConn.close();
/*     */     } 
/*     */     
/*  80 */     if (this.physicalConn != null) {
/*     */       try {
/*  82 */         this.physicalConn.close();
/*     */       } finally {
/*  84 */         this.physicalConn = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*  93 */     if (this.handleConn != null) {
/*  94 */       this.handleConn.close();
/*     */     }
/*  96 */     this.handleConn = (Connection)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Connection.class }, new InvocationHandler()
/*     */         {
/*     */           boolean isClosed;
/*     */           
/*     */           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*     */             try {
/* 102 */               String name = method.getName();
/* 103 */               if ("close".equals(name)) {
/* 104 */                 ConnectionEvent event = new ConnectionEvent((PooledConnection)SQLitePooledConnection.this);
/*     */                 
/* 106 */                 for (int i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; i--) {
/* 107 */                   ((ConnectionEventListener)SQLitePooledConnection.this.listeners.get(i)).connectionClosed(event);
/*     */                 }
/*     */                 
/* 110 */                 if (!SQLitePooledConnection.this.physicalConn.getAutoCommit()) {
/* 111 */                   SQLitePooledConnection.this.physicalConn.rollback();
/*     */                 }
/* 113 */                 SQLitePooledConnection.this.physicalConn.setAutoCommit(true);
/* 114 */                 this.isClosed = true;
/*     */                 
/* 116 */                 return null;
/*     */               } 
/* 118 */               if ("isClosed".equals(name)) {
/* 119 */                 if (!this.isClosed) {
/* 120 */                   this.isClosed = ((Boolean)method.invoke(SQLitePooledConnection.this.physicalConn, args)).booleanValue();
/*     */                 }
/* 122 */                 return Boolean.valueOf(this.isClosed);
/*     */               } 
/*     */               
/* 125 */               if (this.isClosed) {
/* 126 */                 throw new SQLException("Connection is closed");
/*     */               }
/*     */               
/* 129 */               return method.invoke(SQLitePooledConnection.this.physicalConn, args);
/*     */             }
/* 131 */             catch (SQLException e) {
/* 132 */               if ("database connection closed".equals(e.getMessage())) {
/* 133 */                 ConnectionEvent event = new ConnectionEvent((PooledConnection)SQLitePooledConnection.this, e);
/*     */                 
/* 135 */                 for (int i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; i--) {
/* 136 */                   ((ConnectionEventListener)SQLitePooledConnection.this.listeners.get(i)).connectionErrorOccurred(event);
/*     */                 }
/*     */               } 
/*     */               
/* 140 */               throw e;
/*     */             }
/* 142 */             catch (InvocationTargetException ex) {
/* 143 */               throw ex.getCause();
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 148 */     return this.handleConn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConnectionEventListener(ConnectionEventListener listener) {
/* 155 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeConnectionEventListener(ConnectionEventListener listener) {
/* 162 */     this.listeners.remove(listener);
/*     */   }
/*     */   
/*     */   public List<ConnectionEventListener> getListeners() {
/* 166 */     return this.listeners;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\javax\SQLitePooledConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */