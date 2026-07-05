# 🎓 校园信息交流系统（CollegeInfoGap）

## 📌 项目简介
本系统是一个基于 Android + Java Socket + MySQL 的校园信息交流平台，支持用户注册登录、帖子发布、评论点赞收藏、在线聊天以及AI辅助功能。

---

## 🚀 技术栈
- Android（RecyclerView + Material Design）
- Java（Socket网络通信）
- MySQL（数据库）
- Ollama 本地大模型（AI功能）
- TCP Socket通信

---

## ✨ 主要功能

### 👤 用户模块
- 注册 / 登录
- 用户信息管理

### 📝 帖子模块
- 发布帖子
- 查看帖子列表
- 帖子详情

### 💬 互动模块
- 评论功能
- 点赞功能（防重复）
- 收藏功能（防重复）

### 📡 聊天模块
- 在线聊天室
- 实时消息同步
- 消息存入数据库

### 🤖 AI模块
- 标题生成
- 内容总结
- 标签生成（本地大模型）

---

## 🗄️ 数据库设计
系统包含以下表：
- user（用户表）
- post（帖子表）
- comment（评论表）
- likes（点赞表）
- favorite（收藏表）
- message（聊天记录表）

---

## 🏗️ 系统架构
Android客户端 ←→ Socket服务器 ←→ MySQL数据库

---

## 📦 运行方式

### 1️⃣ 启动服务器
运行 Server 项目中的 main 方法

### 2️⃣ 启动数据库
导入 database.sql 到 MySQL

### 3️⃣ 启动客户端
使用 Android Studio 运行 app

---

## 👨‍💻 作者
学生软件开发大作业
