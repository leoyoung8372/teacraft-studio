# AI_RULES.md

# AI 编码规则

## 项目定位
不追求企业级设计，只追求完整闭环和可运行。

## 固定技术栈
后端：

spring boot 3.0.2，

JDK17，

MySQL8.0，

JWT。

用户端（微信小程序）：

微信开发者工具，

uniapp，

vue3

商家端（web）：

vue3

vite

axios

element plus

等等。

## 固定架构
- 一个后端
- 两个前端
  - frontend-user：用户端小程序
  - frontend-admin：商家端后台
  - backend：后端接口服务

## 必须遵守的约束
- 不使用 Redis

- 不使用微服务

- 不使用消息队列

- 不使用分布式事务

- 不接真实第三方支付

- 不做复杂安全体系

- 不做多门店、多角色、优惠券、配送、实时通知等扩展功能

  

## 开发原则
- 优先实现最小可运行闭环
- 优先简单方案，不主动引入复杂架构
- 功能不足时先补基础，不先做优化
- 代码风格统一，优先使用 MyBatis Plus + Service + Controller 的常规分层
- 用户端与商家端都通过后端 REST API 交互
- 需求文档以 PROJECT_SPEC.md 为唯一依据

## 后端建议
- Controller / Service / Mapper 分层
- 返回统一结构，如 Result<T>
- 登录先用简单 token 方案
- 业务校验优先放在 Service 层
- 输入校验可以先轻量处理，不强依赖复杂 validation 设计

## 前端建议
- 用户端使用 UniApp 页面和 tabBar
- 商家端使用 Vue 3 + 路由 + 组件化
- 状态管理保持简单，不强行上复杂全局状态体系

## 认证与登录规范

- 用户端和商家端均采用简单 JWT 登录认证。
- 用户登录成功后，由后端签发 JWT 并返回给客户端。
- 客户端（UniApp / Vue）负责本地保存 JWT，并在后续请求中通过 Authorization 请求头携带。
- JWT 不在数据库中存储，不设计 user.token 或 admin.token 字段。
- 不实现 Refresh Token、Redis 黑名单、单点登录、多设备管理等复杂认证机制。
- JWT 校验采用 Spring Boot 拦截器或过滤器进行简单验证即可。

## 数据库设计原则

- 数据库设计以 database.sql 为唯一依据。
- AI 不得擅自新增业务表或修改核心字段命名。
- 不设计 user.token、admin.token 等数据库 Token 字段。
- 订单明细表保存商品名称、价格、规格等快照信息，避免历史订单受商品修改影响。
- 购物车采用 cart_item 表持久化存储。

## 登录认证规范

- 用户端和商家端均采用简单 JWT 登录认证。
- 登录成功后由后端签发 JWT 并返回客户端。
- 客户端负责本地保存 JWT，并通过 Authorization 请求头携带。
- JWT 不在数据库中存储，不设计 user.token 或 admin.token 字段。
- 不实现 Refresh Token、Redis 黑名单、单点登录、多设备管理等复杂认证机制。

## 数据库设计规范

- 数据库结构以 database.sql 为唯一依据。
- 本项目共使用 8 张核心业务表：user、admin、address、category、product、cart_item、order_info、order_item。
- 购物车采用 cart_item 表持久化存储，不使用前端本地 Storage 作为唯一数据来源。
- 订单明细必须保存商品名称、价格、规格等快照数据。
- AI 不得自行新增 user_token、shopping_cart、orders_v2 等额外业务表。

## 最终明确

1. 用户端和商家端采用简单 JWT 登录认证。
2. JWT 仅由后端签发并由客户端保存，不在数据库存储。
3. 购物车采用 cart_item 表持久化存储。
4. 数据库结构以 database.sql 为唯一标准，AI 不得自行新增核心业务表。