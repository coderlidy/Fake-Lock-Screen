# 伪锁屏 🔒

[English](README.md) | [中文](README_zh.md)

一款创新的安卓应用，通过无障碍服务提供快速便捷的伪锁屏功能。

## 功能特点

- 🎯 支持拖拽的悬浮伪锁屏按钮
- 🔓 任意位置五击快速解除伪锁屏
- 🛡️ 无需root权限
- 🎨 简洁直观的用户界面
- 📱 适配各种屏幕尺寸和系统版本

## 创新亮点

- 采用无障碍服务代替系统级权限
- 实现创新的快速解除伪锁屏机制
- 提供流畅的用户体验，最小化系统资源占用

## 安装说明

### 系统要求

- Android 6.0 (API 23) 或更高版本
- 最少10MB存储空间
- 无需root权限

### 安装步骤

1. 下载并安装APK
2. 打开应用，按照屏幕提示操作
3. 在提示时启用无障碍服务
4. 悬浮伪锁屏按钮将出现在屏幕上

## 使用说明

### 基本操作

1. **伪锁屏**：点击悬浮伪锁屏按钮
2. **解除伪锁屏**：在黑色遮罩上快速点击5次任意位置
3. **移动伪锁屏按钮**：按住悬浮按钮拖动到任意位置
4. **停用服务**：进入系统设置 > 无障碍 > 伪锁屏

### 高级功能

- 基于无障碍事件的自动伪锁屏
- 悬浮按钮位置记忆
- 全局遮罩支持

## 技术详情

### 架构

```kotlin
// 主要组件
├── MainActivity              // 主界面与服务管理
├── LockScreenAccessibilityService  // 核心伪锁屏功能
├── FloatButtonView          // 可拖动悬浮按钮
└── Resources               // 布局和配置文件
```

### 实现亮点

- 利用Android无障碍框架
- 实现自定义悬浮按钮视图
- 使用Kotlin协程处理异步操作
- Material Design 3组件

## 参与贡献

欢迎贡献代码！请遵循以下步骤：

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

本项目采用MIT许可证 - 详见 [LICENSE](LICENSE) 文件。