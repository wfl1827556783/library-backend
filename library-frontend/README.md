# 图书管理系统前端

基于 Vue 3 + Element Plus 开发的图书管理系统前端项目。

## 技术栈

- **Vue 3**: 渐进式 JavaScript 框架
- **Vite**: 下一代前端构建工具
- **Vue Router**: 官方路由管理器
- **Pinia**: 状态管理
- **Element Plus**: Vue 3 UI 组件库
- **Axios**: HTTP 客户端

## 功能特性

- ✅ 用户登录/注册
- ✅ 图书管理（CRUD、搜索、分类筛选）
- ✅ 分类管理（CRUD）
- ✅ 借阅管理（借阅、归还、查询）
- ✅ 用户管理（管理员功能）
- ✅ 数据统计（首页仪表盘）
- ✅ 响应式布局
- ✅ 路由守卫
- ✅ 统一错误处理

## 快速开始

### 1. 安装依赖

```bash
cd library-frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问 `http://localhost:3000`

### 3. 构建生产版本

```bash
npm run build
```

构建产物在 `dist` 目录。

### 4. 预览生产构建

```bash
npm run preview
```

## 项目结构

```
library-frontend/
├── src/
│   ├── api/           # API 接口
│   │   ├── user.js
│   │   ├── book.js
│   │   ├── category.js
│   │   └── borrow.js
│   ├── assets/        # 静态资源
│   ├── components/    # 公共组件
│   ├── layout/        # 布局组件
│   │   └── Index.vue
│   ├── router/        # 路由配置
│   │   └── index.js
│   ├── store/         # 状态管理
│   │   └── user.js
│   ├── utils/         # 工具函数
│   │   └── request.js
│   ├── views/         # 页面组件
│   │   ├── Login.vue
│   │   ├── Register.vue
│   │   ├── Dashboard.vue
│   │   ├── Books.vue
│   │   ├── Categories.vue
│   │   ├── Borrows.vue
│   │   └── Users.vue
│   ├── App.vue
│   └── main.js
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

## 页面说明

### 登录/注册
- 用户登录和注册功能
- 表单验证

### 首页（仪表盘）
- 数据统计卡片
- 最近借阅记录
- 热门图书

### 图书管理
- 图书列表展示
- 添加/编辑/删除图书
- 按分类筛选
- 按书名搜索

### 分类管理
- 分类列表展示
- 添加/编辑/删除分类

### 借阅管理
- 借阅记录列表
- 借阅图书
- 归还图书
- 状态筛选

### 用户管理（管理员）
- 用户列表展示
- 添加/编辑/删除用户
- 角色管理

## 配置说明

### API 代理

在 `vite.config.js` 中配置了 API 代理：

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

### 路由守卫

在 `src/router/index.js` 中配置了路由守卫：
- 需要登录的页面会自动跳转到登录页
- 需要管理员权限的页面会进行权限检查

## 开发说明

### 添加新页面

1. 在 `src/views/` 中创建页面组件
2. 在 `src/router/index.js` 中添加路由配置
3. 在 `src/api/` 中添加对应的 API 接口

### 状态管理

使用 Pinia 进行状态管理，当前有：
- `user` store: 用户信息和登录状态

### API 调用

所有 API 调用都通过 `src/utils/request.js` 封装的 axios 实例，已配置：
- 请求拦截器（添加 token）
- 响应拦截器（统一错误处理）

## 注意事项

1. 确保后端服务运行在 `http://localhost:8080`
2. 登录后 token 存储在 localStorage
3. 管理员功能需要用户角色为 `admin`
4. 所有时间显示为本地时间格式

## 浏览器支持

现代浏览器和最新版本的 Chrome、Firefox、Safari、Edge。

## License

MIT



