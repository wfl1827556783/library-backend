# 图书管理系统 API 文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8

## 统一响应格式

所有API响应都使用统一的 `Result` 格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code`: 200 表示成功，其他值表示失败
- `message`: 响应消息
- `data`: 响应数据

## API 接口列表

### 1. 健康检查

**GET** `/api/health`

检查服务是否正常运行

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": "OK"
}
```

---

### 2. 用户管理

#### 2.1 用户注册

**POST** `/api/users/register`

**请求体：**
```json
{
  "username": "testuser",
  "password": "123456",
  "role": "user"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "role": "user"
  }
}
```

#### 2.2 用户登录

**POST** `/api/users/login`

**请求体：**
```json
{
  "username": "testuser",
  "password": "123456"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "role": "user"
  }
}
```

#### 2.3 获取所有用户

**GET** `/api/users`

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "username": "testuser",
      "role": "user"
    }
  ]
}
```

#### 2.4 根据ID获取用户

**GET** `/api/users/{id}`

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "role": "user"
  }
}
```

#### 2.5 更新用户

**PUT** `/api/users/{id}`

**请求体：**
```json
{
  "username": "testuser",
  "password": "newpassword",
  "role": "admin"
}
```

#### 2.6 删除用户

**DELETE** `/api/users/{id}`

---

### 3. 分类管理

#### 3.1 添加分类

**POST** `/api/categories`

**请求体：**
```json
{
  "name": "计算机科学"
}
```

#### 3.2 获取所有分类

**GET** `/api/categories`

#### 3.3 根据ID获取分类

**GET** `/api/categories/{id}`

#### 3.4 更新分类

**PUT** `/api/categories/{id}`

**请求体：**
```json
{
  "name": "计算机科学（更新）"
}
```

#### 3.5 删除分类

**DELETE** `/api/categories/{id}`

---

### 4. 图书管理

#### 4.1 添加图书

**POST** `/api/books`

**请求体：**
```json
{
  "title": "Java编程思想",
  "author": "Bruce Eckel",
  "isbn": "978-7-111-21382-6",
  "description": "Java经典教材",
  "stock": 10,
  "price": 89.00,
  "categoryId": 1
}
```

#### 4.2 获取所有图书

**GET** `/api/books`

**查询参数：**
- `categoryId` (可选): 按分类筛选
- `keyword` (可选): 按书名关键词搜索

**示例：**
- `/api/books?categoryId=1`
- `/api/books?keyword=Java`

#### 4.3 根据ID获取图书

**GET** `/api/books/{id}`

#### 4.4 更新图书

**PUT** `/api/books/{id}`

**请求体：**
```json
{
  "title": "Java编程思想（第5版）",
  "author": "Bruce Eckel",
  "stock": 15,
  "price": 99.00,
  "categoryId": 1
}
```

#### 4.5 删除图书

**DELETE** `/api/books/{id}`

---

### 5. 借阅管理

#### 5.1 借阅图书

**POST** `/api/borrows`

**请求体：**
```json
{
  "userId": 1,
  "bookId": 1
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "username": "testuser",
    "bookId": 1,
    "bookTitle": "Java编程思想",
    "borrowTime": "2025-11-23T16:00:00",
    "returnTime": null,
    "returned": false
  }
}
```

#### 5.2 归还图书

**PUT** `/api/borrows/{id}/return`

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "username": "testuser",
    "bookId": 1,
    "bookTitle": "Java编程思想",
    "borrowTime": "2025-11-23T16:00:00",
    "returnTime": "2025-11-23T17:00:00",
    "returned": true
  }
}
```

#### 5.3 获取用户的所有借阅记录

**GET** `/api/borrows/user/{userId}`

#### 5.4 根据ID获取借阅记录

**GET** `/api/borrows/{id}`

#### 5.5 获取所有借阅记录

**GET** `/api/borrows`

---

## 错误码说明

- `200`: 成功
- `400`: 请求参数错误
- `500`: 服务器内部错误

## 注意事项

1. 所有时间格式为 ISO 8601 格式：`yyyy-MM-ddTHH:mm:ss`
2. 密码在响应中不会返回
3. 所有接口都支持 CORS 跨域请求
4. 用户名和分类名称具有唯一性约束
5. 借阅图书时会自动减少库存，归还时会自动增加库存



