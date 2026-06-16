const fs = require('fs')
const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell,
        HeadingLevel, AlignmentType, BorderStyle, WidthType, ShadingType,
        LevelFormat, PageBreak, TableOfContents, Header, Footer, PageNumber } = require('docx')

const border = { style: BorderStyle.SINGLE, size: 1, color: "999999" }
const borders = { top: border, bottom: border, left: border, right: border }
const cm = { top: 60, bottom: 60, left: 100, right: 100 }
const W = 9360 // content width for A4 with 2.54cm margins

const hCell = (t, w) => new TableCell({ borders, width: { size: w, type: WidthType.DXA }, shading: { fill: "E8F4F0", type: ShadingType.CLEAR }, margins: cm, children: [p(t, true)] })
const cell = (t, w, align) => new TableCell({ borders, width: { size: w, type: WidthType.DXA }, margins: cm, children: [p(t, false, align)] })
const p = (text, bold, align) => new Paragraph({ alignment: align || AlignmentType.LEFT, children: [new TextRun({ text, bold, font: "宋体", size: 21 })] })

function tbl(rows) { return new Table({ width: { size: W, type: WidthType.DXA }, columnWidths: rows[0].map(r => r.width?.size || 1000), rows: rows.map(r => new TableRow({ children: r })) }) }

function bullet(text) { return new Paragraph({ numbering: { reference: "bullets", level: 0 }, children: [new TextRun({ text, font: "宋体", size: 21 })] }) }

function heading(text) { return new Paragraph({ heading: HeadingLevel.HEADING_1, spacing: { before: 300, after: 120 }, children: [new TextRun({ text, bold: true, font: "黑体", size: 28 })] }) }

function subheading(text) { return new Paragraph({ heading: HeadingLevel.HEADING_2, spacing: { before: 200, after: 80 }, children: [new TextRun({ text, bold: true, font: "宋体", size: 24 })] }) }

function body(text) { return new Paragraph({ spacing: { after: 60, line: 360 }, children: [new TextRun({ text, font: "宋体", size: 21 })] }) }

function screenshot(desc) { return new Paragraph({ alignment: AlignmentType.CENTER, spacing: { before: 80, after: 120 }, children: [new TextRun({ text: `【这里插入截图：${desc}】`, italics: true, font: "宋体", size: 18, color: "E74C3C" })] }) }

function code(text) { return new Paragraph({ spacing: { after: 30 }, children: [new TextRun({ text, font: "Courier New", size: 18 })] }) }

async function main() {
  const doc = new Document({
    styles: {
      default: { document: { run: { font: "宋体", size: 21 } } },
      paragraphStyles: [
        { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true, run: { size: 28, bold: true, font: "黑体" }, paragraph: { spacing: { before: 300, after: 120 }, outlineLevel: 0 } },
        { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true, run: { size: 24, bold: true, font: "宋体" }, paragraph: { spacing: { before: 200, after: 80 }, outlineLevel: 1 } },
      ]
    },
    numbering: { config: [{ reference: "bullets", levels: [{ level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 720, hanging: 360 } } } }] }] },
    sections: [
      // ==================== 封面 ====================
      {
        children: [
          new Paragraph({ spacing: { before: 1200 }, children: [] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 60 }, children: [new TextRun({ text: "企业级综合项目实践", bold: true, size: 44, font: "黑体" })] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 60 }, children: [new TextRun({ text: "课程设计报告", bold: true, size: 44, font: "黑体" })] }),
          new Paragraph({ spacing: { before: 400 }, children: [] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 60 }, children: [new TextRun({ text: "项目名称：TeaCraft Studio 茶饮外卖点餐系统", bold: true, size: 28, font: "宋体" })] }),
          new Paragraph({ spacing: { before: 400 }, children: [] }),
          ...[
            ["学    院：", "计算机与人工智能学院"],
            ["姓    名：", "___________"],
            ["学    号：", "___________"],
            ["指导老师：", "___________"],
            ["时    间：", "2026年6月"],
          ].map(([label, val]) => new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 80 }, children: [new TextRun({ text: `${label}  ${val}`, size: 24, font: "宋体" })] })),
        ]
      },
      // ==================== 目录 ====================
      {
        children: [
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun({ text: "目  录", bold: true, font: "黑体", size: 28 })] }),
          new TableOfContents("目录", { hyperlink: true, headingStyleRange: "1-2" }),
        ]
      },
      // ==================== 正文 ====================
      {
        children: [
          // ========== 一、设计要求 ==========
          heading("一、设计要求"),
          subheading("1.1 问题概述"),
          body("TeaCraft Studio 是一款面向茶饮店的外卖点餐系统，旨在为顾客提供便捷的在线点餐体验，同时为商家提供高效的后台管理工具。系统分为用户端微信小程序和商家端 Web 后台两个前端，通过 Spring Boot 后端统一提供 RESTful API 服务。"),
          body("传统茶饮店点餐存在排队等候时间长、人工记账易出错、库存管理混乱等问题。本系统通过信息化手段解决以上痛点，实现从商品浏览、规格定制、购物车管理、下单结算到订单处理、库存管控的完整业务闭环。"),
          subheading("1.2 功能要求"),
          body("根据 MVP（最小可行产品）原则，系统本期必须实现以下核心功能："),
          body("【用户端小程序】", true),
          ...[
            "用户注册与登录：支持手机号+密码注册，JWT Token 认证",
            "首页展示：轮播 Banner 广告 + 自提/外卖快捷入口 + 品牌展示",
            "分类点餐：左侧固定分类导航 + 右侧商品列表，支持分类切换",
            "商品详情：商品大图、简介、价格，甜度/温度/杯型/加料等规格选择，数量增减，加入购物车与立即购买",
            "购物车管理：浮动购物车栏显示总价与数量，结算页支持删除商品",
            "下单结算：切换自提/外卖用餐方式，选择/管理收货地址，实时计算总价",
            "订单管理：订单列表按时间排序显示状态，订单详情查看商品明细，支持再来一单",
            "地址管理：收货地址的增删改查，支持设置默认地址",
            "个人中心：显示头像、昵称、手机号，退出登录",
          ].map(bullet),
          body("【商家端 Web 后台】", true),
          ...[
            "管理员登录：用户名+密码，JWT Token 认证",
            "库存管理：商品列表（分页+关键词搜索），新增/编辑商品（含图片上传），删除商品",
            "订单管理：待处理订单列表，完成订单（更新状态），取消订单（二次确认+自动恢复库存）",
            "历史订单：查看已完成和已取消的订单记录",
          ].map(bullet),
          screenshot("首页轮播图与整体界面"),

          // ========== 二、设计简介 ==========
          new Paragraph({ children: [new PageBreak()] }),
          heading("二、设计简介"),
          subheading("2.1 功能简介"),
          body("本系统采用前后端分离的单体架构。后端基于 Spring Boot 3.0.2 框架，使用 MyBatis Plus 作为 ORM 工具操作 MySQL 8.0 数据库，通过 JWT 实现无状态认证。前端用户端使用 UniApp (Vue 3) 开发并编译为微信小程序，商家端使用 Vue 3 + Vite + Element Plus 构建 Web 后台。前后端之间通过 HTTP/RESTful API 以 JSON 格式交换数据。"),
          body("系统整体功能架构如下图所示："),
          screenshot("系统功能架构图"),
          subheading("2.2 存储结构设计"),
          body("数据库采用 MySQL 8.0，数据库名为 teacraft_db，字符集 utf8mb4。共设计 8 张核心业务表："),
          tbl([
            [hCell("序号", 600), hCell("表名", 1400), hCell("实体类", 1600), hCell("说明", 5760)],
            [cell("1", 600), cell("user", 1400), cell("User", 1600), cell("C端用户表：id, nickname, phone, password(MD5), avatar_url, created_at, updated_at", 5760)],
            [cell("2", 600), cell("admin", 1400), cell("Admin", 1600), cell("管理员表：id, username, password(MD5), created_at, updated_at", 5760)],
            [cell("3", 600), cell("category", 1400), cell("Category", 1600), cell("商品分类表：id, name, sort_order（7个固定分类：推荐/冰淇淋/咖啡/果茶/奶茶/冰沙/其他）", 5760)],
            [cell("4", 600), cell("product", 1400), cell("Product", 1600), cell("商品表：id, category_id, name, description, image_url, price, stock, is_on_sale", 5760)],
            [cell("5", 600), cell("cart_item", 1400), cell("CartItem", 1600), cell("购物车表：id, user_id, product_id, quantity, sweetness, temperature, cup_size, add_ons", 5760)],
            [cell("6", 600), cell("address", 1400), cell("Address", 1600), cell("地址表：id, user_id, receiver_name, receiver_phone, detail_address, is_default", 5760)],
            [cell("7", 600), cell("order_info", 1400), cell("OrderInfo", 1600), cell("订单主表：id, order_no, user_id, total_amount, meal_type, address_id, status, paid_at", 5760)],
            [cell("8", 600), cell("order_item", 1400), cell("OrderItem", 1600), cell("订单明细表：id, order_id, product_id, product_name(快照), price(快照), quantity, subtotal", 5760)],
          ]),
          body(""),
          body("数据库 ER 图如下所示："),
          screenshot("数据库 ER 图"),
          body("订单明细表保存商品名称、价格、规格等快照数据，即使后续商品被修改或删除，历史订单的显示也不会受影响，这是电商系统的标准设计实践。"),

          // ========== 三、模块介绍 ==========
          new Paragraph({ children: [new PageBreak()] }),
          heading("三、模块介绍"),
          subheading("3.1 模块划分"),
          body("系统按业务领域纵向切分为 6 大模块，后端整体采用 Controller → Service → Mapper 三层架构："),
          tbl([
            [hCell("模块", 1200), hCell("后端服务", 2660), hCell("用户端页面", 2660), hCell("商家端页面", 2860)],
            [cell("用户认证", 1200), cell("UserController\nAdminController", 2660), cell("login, register", 2660), cell("LoginView", 2860)],
            [cell("商品管理", 1200), cell("CategoryController\nProductController\nProductAdminController\nUploadController", 2660), cell("index(首页)\norder(点餐)\ndetail(详情)", 2660), cell("ProductManage\n(库存管理)", 2860)],
            [cell("购物车", 1200), cell("CartItemController", 2660), cell("checkout(结算)", 2660), cell("—", 2860)],
            [cell("地址管理", 1200), cell("AddressController", 2660), cell("mine(我的)\naddress(地址)", 2660), cell("—", 2860)],
            [cell("订单处理", 1200), cell("OrderController\nOrderAdminController", 2660), cell("orders(订单列表)\norder-detail(详情)\ncheckout(下单)", 2660), cell("OrderManage\nOrderHistory", 2860)],
          ]),
          body(""),
          subheading("3.2 系统子程序及功能设计"),
          body("各模块核心功能及关键实现说明："),
          body(""),
          body("【用户认证模块】", true),
          body("注册时对密码进行 MD5 加密存储，登录时校验加密后的密码。后端签发 JWT Token，客户端保存于本地 Storage，后续请求通过 Authorization 头部携带。JwtInterceptor 拦截 /api/** 路径，排除 login 和 register 接口。JWT 不在数据库中存储，不实现 Refresh Token，符合 MVP 简化原则。"),
          body(""),
          body("【商品管理模块】", true),
          body("商品支持按分类浏览（仅显示已上架），详情页可查看大图、描述、价格。规格选项（甜度、温度、杯型、加料）为固定选项设计，不采用动态扩展表。商家端支持商品图片上传（MultipartFile → target/classes/static/ 目录 + src 目录备份），新增和编辑复用同一表单，根据 id 是否为空判断操作类型。推荐分类限制最多 10 个商品。"),
          body(""),
          body("【购物车模块】", true),
          body("购物车数据持久化在 cart_item 表中。同商品+同规格再次加入时自动累加数量而非新增条目。加入购物车前校验商品是否上架及库存是否充足。结算页支持删除单个商品条目。订单创建成功后自动清空购物车。"),
          body(""),
          body("【订单处理模块】", true),
          body("这是系统最复杂的业务模块，涉及双表写入、库存扣减、事务控制。创建订单时：遍历购物车 → 校验库存 → 扣减库存 → 生成订单主记录和明细快照 → 清空购物车，全部在 @Transactional 注解保护下执行，任一步失败则整体回滚。商家取消订单时同样在事务中恢复库存。订单状态仅三种：making（制作中）→ finished（已完成）/ cancelled（已取消）"),
          body(""),
          body("【地址管理模块】", true),
          body("用户可管理多个收货地址。设为默认地址时自动将其他地址取消默认，确保只有一个默认地址。结算页选择外卖配送时显示地址列表供选择。"),
          screenshot("系统模块架构图"),

          // ========== 四、详细设计 ==========
          new Paragraph({ children: [new PageBreak()] }),
          heading("四、详细设计"),
          subheading("4.1 后端架构设计"),
          body("后端项目结构如下："),
          code("com.leoyoung.backend/"),
          code("├── BackendApplication.java          # Spring Boot 启动类"),
          code("├── common/                          # 通用模块"),
          code("│   ├── Result.java                  # 统一响应体 {code, message, data}"),
          code("│   ├── ResultCode.java              # 状态码枚举（200/400/401/404/500 + 业务码）"),
          code("│   ├── exception/"),
          code("│   │   └── BusinessException.java   # 自定义业务异常"),
          code("│   └── handler/"),
          code("│       └── GlobalExceptionHandler.java # 全局异常拦截（业务异常/参数校验/兜底）"),
          code("├── config/                          # 配置类"),
          code("│   ├── MyBatisPlusConfig.java       # 分页插件 + MapperScan"),
          code("│   └── WebConfig.java               # 跨域配置 + 拦截器注册"),
          code("├── interceptor/"),
          code("│   └── JwtInterceptor.java          # JWT 登录拦截器"),
          code("├── util/"),
          code("│   └── JwtUtils.java                # JWT 签发/解析/校验"),
          code("├── entity/        # 8个数据库实体类"),
          code("├── mapper/        # 8个 MyBatis Plus Mapper 接口"),
          code("├── dto/           # 12个请求/响应 DTO"),
          code("├── service/       # 6个业务接口 + 6个实现类"),
          code("└── controller/    # 9个 REST 控制器"),
          body(""),
          subheading("4.2 API 接口设计"),
          body("系统统一返回格式为 {code, message, data}。code=200 表示成功，其他值表示对应的错误类型。"),
          body("用户端主要 API 接口："),
          tbl([
            [hCell("方法", 700), hCell("接口路径", 2400), hCell("说明", 2200), hCell("认证", 700), hCell("返回数据", 3360)],
            [cell("POST", 700), cell("/api/user/register", 2400), cell("用户注册", 2200), cell("否", 700), cell("Result<Void>", 3360)],
            [cell("POST", 700), cell("/api/user/login", 2400), cell("用户登录", 2200), cell("否", 700), cell("Result<LoginResponse>（token+userId+nickname）", 3360)],
            [cell("GET", 700), cell("/api/categories", 2400), cell("获取分类列表", 2200), cell("是", 700), cell("Result<List<Category>>", 3360)],
            [cell("GET", 700), cell("/api/products?categoryId=", 2400), cell("按分类查商品", 2200), cell("是", 700), cell("Result<List<Product>>", 3360)],
            [cell("GET", 700), cell("/api/products/{id}", 2400), cell("商品详情", 2200), cell("是", 700), cell("Result<Product>", 3360)],
            [cell("POST", 700), cell("/api/cart/add", 2400), cell("加入购物车", 2200), cell("是", 700), cell("Result<Void>", 3360)],
            [cell("GET", 700), cell("/api/cart/list", 2400), cell("查看购物车", 2200), cell("是", 700), cell("Result<List<CartItemResponse>>", 3360)],
            [cell("PUT", 700), cell("/api/cart/{id}", 2400), cell("修改数量", 2200), cell("是", 700), cell("Result<Void>", 3360)],
            [cell("DELETE", 700), cell("/api/cart/{id}", 2400), cell("删除条目", 2200), cell("是", 700), cell("Result<Void>", 3360)],
            [cell("POST", 700), cell("/api/order/create", 2400), cell("创建订单", 2200), cell("是", 700), cell("Result<Void>（扣库存+清购物车）", 3360)],
            [cell("GET", 700), cell("/api/order/list", 2400), cell("订单列表", 2200), cell("是", 700), cell("Result<List<OrderInfo>>", 3360)],
            [cell("GET", 700), cell("/api/order/{id}", 2400), cell("订单详情(含明细)", 2200), cell("是", 700), cell("Result<OrderResponse>", 3360)],
            [cell("GET", 700), cell("/api/address/list", 2400), cell("地址列表", 2200), cell("是", 700), cell("Result<List<Address>>", 3360)],
            [cell("POST", 700), cell("/api/address/save", 2400), cell("新增/编辑地址", 2200), cell("是", 700), cell("Result<Void>", 3360)],
            [cell("DELETE", 700), cell("/api/address/{id}", 2400), cell("删除地址", 2200), cell("是", 700), cell("Result<Void>", 3360)],
          ]),
          body(""),
          body("商家端主要 API 接口："),
          tbl([
            [hCell("方法", 700), hCell("接口路径", 2800), hCell("说明", 2600), hCell("认证", 700), hCell("返回数据", 2560)],
            [cell("POST", 700), cell("/api/admin/login", 2800), cell("管理员登录", 2600), cell("否", 700), cell("Result<AdminLoginResponse>", 2560)],
            [cell("GET", 700), cell("/api/admin/products", 2800), cell("商品列表(分页+搜索)", 2600), cell("是", 700), cell("Result<Page<Product>>", 2560)],
            [cell("POST", 700), cell("/api/admin/products", 2800), cell("新增/编辑商品", 2600), cell("是", 700), cell("Result<Void>", 2560)],
            [cell("DELETE", 700), cell("/api/admin/products/{id}", 2800), cell("删除商品", 2600), cell("是", 700), cell("Result<Void>", 2560)],
            [cell("POST", 700), cell("/api/admin/upload", 2800), cell("上传商品图片", 2600), cell("是", 700), cell("Result<String>（图片URL）", 2560)],
            [cell("GET", 700), cell("/api/admin/orders/pending", 2800), cell("待处理订单", 2600), cell("是", 700), cell("Result<List<OrderInfo>>", 2560)],
            [cell("PUT", 700), cell("/api/admin/orders/{id}/complete", 2800), cell("完成订单", 2600), cell("是", 700), cell("Result<Void>", 2560)],
            [cell("PUT", 700), cell("/api/admin/orders/{id}/cancel", 2800), cell("取消订单(恢复库存)", 2600), cell("是", 700), cell("Result<Void>", 2560)],
            [cell("GET", 700), cell("/api/admin/orders/history", 2800), cell("历史订单", 2600), cell("是", 700), cell("Result<List<OrderInfo>>", 2560)],
          ]),
          body(""),
          subheading("4.3 用户端页面设计"),
          body("用户端（微信小程序）共 10 个页面，底部 4 个 Tab（首页/点餐/订单/我的），以 pages.json 中的 pages 数组第一项（pages/login/login）为启动页。"),
          body("页面结构及跳转关系："),
          code("login（登录）→ register（注册）→ login（返回登录）"),
          code("login → index（首页 Tab）"),
          code("order（点餐 Tab）→ detail（商品详情）→ checkout（结算）"),
          code("orders（订单 Tab）→ order-detail（订单详情）"),
          code("mine（我的 Tab）→ address（地址管理）"),
          screenshot("小程序页面结构图"),
          body("关键技术点："),
          ...[
            "使用 uni-ui 官方组件库（uni-swiper、uni-card 等），不引入第三方 UI 框架",
            "封装 src/utils/request.js 统一管理 HTTP 请求，自动携带 JWT Token、401 拦截跳转登录",
            "src/api/ 目录按模块封装接口调用（user.js / product.js / cart.js / order.js / address.js）",
            "onShow 生命周期刷新购物车浮层和订单列表，确保数据实时性",
            "规格选项采用固定数组定义，不动态查询数据库",
          ].map(bullet),

          subheading("4.4 商家端页面设计"),
          body("商家端（Vue 3 Web 后台）共 5 个页面：LoginView（登录）→ HomeView（主框架：左侧导航 + 右侧内容区）→ ProductManage（库存管理）/ OrderManage（订单管理）/ OrderHistory（历史订单）。"),
          body("关键技术点："),
          ...[
            "Element Plus 组件库：el-table、el-dialog、el-upload、el-pagination、el-menu 等",
            "Axios 封装 src/utils/request.js：统一 baseURL、自动携带 Token、响应拦截处理 401",
            "Vue Router 路由守卫：未登录自动跳转 /login，已登录访问 /login 自动跳转 /",
            "商品图片上传：el-upload 组件 → POST /api/admin/upload → 后端保存并返回 URL",
            "订单取消二次确认：ElMessageBox.confirm 弹窗确认后执行",
            "库存管理搜索防抖 + 分页加载",
          ].map(bullet),
          screenshot("商家端界面截图"),

          subheading("4.5 关键代码示例"),
          body("【统一响应体 Result.java】"),
          code("public class Result<T> {"),
          code("    private int code;      // 状态码（200=成功）"),
          code("    private String message; // 提示消息"),
          code("    private T data;        // 携带数据"),
          code("    public static <T> Result<T> success(T data) { ... }"),
          code("    public static <T> Result<T> error(ResultCode code) { ... }"),
          code("}"),
          body(""),
          body("【订单创建事务处理 OrderServiceImpl.create()】"),
          code("@Transactional  // 任一失败则全部回滚"),
          code("public void create(Long userId, OrderCreateRequest req) {"),
          code("    // 1. 查询购物车，为空则抛异常"),
          code("    // 2. 遍历购物车：校验库存 → 扣减 → 构建订单明细快照"),
          code("    // 3. 生成订单号，计算总价，创建 order_info"),
          code("    // 4. 批量插入 order_item"),
          code("    // 5. 清空购物车"),
          code("}"),
          body(""),
          body("【JWT 拦截器 JwtInterceptor.preHandle()】"),
          code("public boolean preHandle(HttpServletRequest req, ...) {"),
          code("    String token = req.getHeader(\"Authorization\");"),
          code("    // 去掉 \"Bearer \" 前缀，校验有效性"),
          code("    // 有效：注入 userId + userType → request.setAttribute"),
          code("    // 无效：返回 401"),
          code("}"),

          // ========== 五、测试分析 ==========
          new Paragraph({ children: [new PageBreak()] }),
          heading("五、测试分析"),
          subheading("5.1 测试环境"),
          body("后端测试环境：Windows 11 + IntelliJ IDEA + Spring Boot 3.0.2 + MySQL 8.0，使用 curl 命令行工具进行接口测试。"),
          body("前端测试环境：微信开发者工具（用户端小程序）+ Chrome 浏览器（商家端 Web），配合 Postman 进行辅助调试。"),
          subheading("5.2 功能测试"),
          body("以下列出核心功能测试用例及结果："),
          tbl([
            [hCell("编号", 600), hCell("测试场景", 2000), hCell("预期结果", 3160), hCell("实际结果", 1600), hCell("状态", 2000)],
            [cell("T01", 600), cell("用户注册", 2000), cell("新手机号注册成功，返回 code=200", 3160), cell("注册成功", 1600), cell("✅ 通过", 2000)],
            [cell("T02", 600), cell("重复手机号注册", 2000), cell("返回 code=1001 用户已存在", 3160), cell("提示已存在", 1600), cell("✅ 通过", 2000)],
            [cell("T03", 600), cell("用户登录", 2000), cell("正确凭证返回 token+用户信息", 3160), cell("返回正确", 1600), cell("✅ 通过", 2000)],
            [cell("T04", 600), cell("错误密码登录", 2000), cell("返回 code=1003 密码错误", 3160), cell("提示错误", 1600), cell("✅ 通过", 2000)],
            [cell("T05", 600), cell("无 Token 访问", 2000), cell("返回 401 未授权", 3160), cell("401拦截", 1600), cell("✅ 通过", 2000)],
            [cell("T06", 600), cell("加入购物车", 2000), cell("商品成功加入，同规格累加数量", 3160), cell("累加正常", 1600), cell("✅ 通过", 2000)],
            [cell("T07", 600), cell("库存不足加购", 2000), cell("返回 code=2001 库存不足", 3160), cell("提示不足", 1600), cell("✅ 通过", 2000)],
            [cell("T08", 600), cell("创建订单", 2000), cell("库存扣减、购物车清空、订单生成", 3160), cell("全部正确", 1600), cell("✅ 通过", 2000)],
            [cell("T09", 600), cell("商家取消订单", 2000), cell("订单状态变为 cancelled，库存恢复", 3160), cell("恢复正确", 1600), cell("✅ 通过", 2000)],
            [cell("T10", 600), cell("再来一单", 2000), cell("订单商品重新加入购物车", 3160), cell("加购成功", 1600), cell("✅ 通过", 2000)],
            [cell("T11", 600), cell("商家商品CRUD", 2000), cell("新增/编辑/删除/搜索/分页正常", 3160), cell("功能正常", 1600), cell("✅ 通过", 2000)],
            [cell("T12", 600), cell("图片上传", 2000), cell("上传成功并返回可访问URL", 3160), cell("显示正常", 1600), cell("✅ 通过", 2000)],
          ]),
          body(""),
          screenshot("测试结果截图"),
          subheading("5.3 测试结论"),
          body("经过 12 项核心功能测试，所有用例全部通过。系统在正常场景下功能完整，异常场景下错误提示友好，库存扣减/恢复逻辑正确，事务回滚机制有效。前后端联调无 404/500 系统性错误。系统已具备完整的 MVP 闭环能力。"),

          // ========== 六、总结 ==========
          new Paragraph({ children: [new PageBreak()] }),
          heading("六、总结"),
          subheading("6.1 项目成果"),
          body("本项目成功完成了一个茶饮外卖点餐系统的 MVP 版本开发，实现了："),
          ...[
            "后端 Spring Boot 项目：8 张数据表、6 大业务模块、24 个 API 接口、JWT 认证体系",
            "用户端 UniApp 微信小程序：10 个页面、4 个 Tab、完整交易闭环",
            "商家端 Vue 3 Web 后台：5 个页面、侧栏导航、商品/订单管理",
            "代码总量约 4000+ 行（Java ~2000 行 + Vue ~1500 行 + UniApp ~1000 行）",
            "文档产出：需求规格说明书、数据库 SQL 脚本、AI 编码规则、Word 实验报告",
          ].map(bullet),
          subheading("6.2 学习收获"),
          ...[
            "掌握了 Spring Boot + MyBatis Plus 后端开发的标准分层架构",
            "理解了 JWT 无状态认证机制及其在前后端分离项目中的实际应用",
            "熟悉了 Vue 3 Composition API + Element Plus 的企业级后台开发模式",
            "学习了 UniApp 跨端开发框架，了解微信小程序的页面生命周期和组件体系",
            "实践了 RESTful API 设计、统一异常处理、事务控制等后端最佳实践",
            "体验了完整项目从需求分析、数据库设计到编码实现、联调测试的全流程",
          ].map(bullet),
          subheading("6.3 不足与展望"),
          body("由于采用 MVP 策略，以下功能未在本期实现，可作为后续迭代方向："),
          ...[
            "真实微信支付/支付宝支付接入",
            "第三方登录（微信授权登录）",
            "配送追踪与地图定位",
            "用户评价与评分系统",
            "优惠券/营销活动模块",
            "消息推送（订单状态变更通知）",
            "数据统计看板（销售额、商品排行等）",
            "多门店/多角色权限管理",
          ].map(bullet),
          body("这些功能涉及第三方 SDK 集成、消息队列、复杂权限模型等高级技术，适合在掌握 MVP 基础后逐步深入学习。"),

          // ========== 七、参考资料 ==========
          new Paragraph({ children: [new PageBreak()] }),
          heading("七、参考资料"),
          ...[
            "[1] Spring Boot 官方文档. https://docs.spring.io/spring-boot/docs/3.0.2/reference/html/",
            "[2] MyBatis Plus 官方文档. https://baomidou.com/",
            "[3] JJWT 官方文档. https://github.com/jwtk/jjwt",
            "[4] Vue 3 官方文档. https://cn.vuejs.org/",
            "[5] Element Plus 官方文档. https://element-plus.org/",
            "[6] UniApp 官方文档. https://uniapp.dcloud.net.cn/",
            "[7] 微信小程序开发文档. https://developers.weixin.qq.com/miniprogram/dev/framework/",
            "[8] MySQL 8.0 参考手册. https://dev.mysql.com/doc/refman/8.0/en/",
            "[9] Atlassian. \"Monoliths vs Microservices\". https://www.atlassian.com/microservices",
            "[10] Eric Evans. 《领域驱动设计》. 人民邮电出版社, 2016.",
          ].map(t => new Paragraph({ spacing: { after: 40 }, children: [new TextRun({ text: t, font: "宋体", size: 21 })] })),
        ]
      }
    ]
  })

  const buffer = await Packer.toBuffer(doc)
  fs.writeFileSync('E:/teacraft-studio/word/TeaCraft_Studio_实验报告.docx', buffer)
  console.log('报告已生成: word/TeaCraft_Studio_实验报告.docx')
  console.log('文件中共有 ' + String(buffer.length) + ' 字节')
}

main().catch(e => { console.error(e); process.exit(1) })
