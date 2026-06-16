const fs = require('fs')
const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell,
        HeadingLevel, AlignmentType, BorderStyle, WidthType, ShadingType,
        LevelFormat, PageBreak } = require('docx')

const border = { style: BorderStyle.SINGLE, size: 1, color: "CCCCCC" }
const borders = { top: border, bottom: border, left: border, right: border }
const cellMargins = { top: 60, bottom: 60, left: 100, right: 100 }

function headerCell(text, width) {
  return new TableCell({
    borders, width: { size: width, type: WidthType.DXA },
    shading: { fill: "E8F4F0", type: ShadingType.CLEAR },
    margins: cellMargins,
    children: [new Paragraph({ children: [new TextRun({ text, bold: true, font: "Arial", size: 20 })] })]
  })
}

function cell(text, width) {
  return new TableCell({
    borders, width: { size: width, type: WidthType.DXA },
    margins: cellMargins,
    children: [new Paragraph({ children: [new TextRun({ text, font: "Arial", size: 20 })] })]
  })
}

const API_TABLE_WIDTH = 9360
const API_COLUMNS = [800, 2600, 1100, 2200, 2660]

async function main() {
  const doc = new Document({
    styles: {
      default: { document: { run: { font: "Arial", size: 22 } } },
      paragraphStyles: [
        { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
          run: { size: 36, bold: true, font: "Arial", color: "2C3E50" },
          paragraph: { spacing: { before: 360, after: 180 }, outlineLevel: 0 } },
        { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
          run: { size: 28, bold: true, font: "Arial", color: "34495E" },
          paragraph: { spacing: { before: 240, after: 120 }, outlineLevel: 1 } },
        { id: "Heading3", name: "Heading 3", basedOn: "Normal", next: "Normal", quickFormat: true,
          run: { size: 24, bold: true, font: "Arial" },
          paragraph: { spacing: { before: 180, after: 80 }, outlineLevel: 2 } },
      ]
    },
    numbering: {
      config: [{
        reference: "bullets",
        levels: [{ level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT,
          style: { paragraph: { indent: { left: 720, hanging: 360 } } } }]
      }]
    },
    sections: [
      // ===== 封面 =====
      {
        children: [
          new Paragraph({ spacing: { before: 2400 }, children: [] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 120 },
            children: [new TextRun({ text: "TeaCraft Studio", bold: true, size: 56, font: "Arial", color: "27AE60" })] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 80 },
            children: [new TextRun({ text: "茶饮外卖点餐系统", size: 32, font: "Arial", color: "7F8C8D" })] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 200 },
            children: [new TextRun({ text: "项目技术文档", size: 24, font: "Arial", color: "95A5A6" })] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 60 },
            children: [new TextRun({ text: "版本 1.0  |  2026年6月", size: 20, font: "Arial", color: "BDC3C7" })] }),
        ]
      },
      // ===== 正文 =====
      {
        children: [
          // 1. 项目概述
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("1. 项目概述")] }),
          new Paragraph({ spacing: { after: 80 }, children: [new TextRun("TeaCraft Studio 是一个练手型外卖点餐系统（MVP），以茶饮店为业务场景，覆盖用户端微信小程序和商家端 Web 后台。项目采用前后端分离架构，通过 RESTful API 通信，实现从注册登录、商品浏览、规格选择、购物车管理、下单结算到订单管理的完整交易闭环。")] }),

          // 2. 技术栈
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("2. 技术栈")] }),
          new Table({
            width: { size: API_TABLE_WIDTH, type: WidthType.DXA },
            columnWidths: [2000, 7360],
            rows: [
              new TableRow({ children: [headerCell("层级", 2000), headerCell("技术选型", 7360)] }),
              new TableRow({ children: [cell("后端框架", 2000), cell("Spring Boot 3.0.2 + JDK 17 + Maven", 7360)] }),
              new TableRow({ children: [cell("数据库", 2000), cell("MySQL 8.0 + MyBatis Plus 3.5.3.1", 7360)] }),
              new TableRow({ children: [cell("认证", 2000), cell("JWT（jjwt 0.11.5），客户端存储，不存数据库", 7360)] }),
              new TableRow({ children: [cell("用户端", 2000), cell("UniApp (Vue 3) → 微信小程序 + uni-ui 组件库", 7360)] }),
              new TableRow({ children: [cell("商家端", 2000), cell("Vue 3 + Vite + Element Plus + Axios + Pinia", 7360)] }),
              new TableRow({ children: [cell("架构模式", 2000), cell("单体应用，前后端分离，RESTful API", 7360)] }),
            ]
          }),

          // 3. 系统架构
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("3. 系统架构")] }),
          new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun("3.1 项目结构")] }),
          new Paragraph({ spacing: { after: 60 }, children: [new TextRun("teacraft-studio/")] }),
          ...[
            "├── docs/                    # 项目文档与数据库脚本",
            "│   ├── PROJECT_SPEC.md       # 需求规格说明书",
            "│   ├── database.sql           # MySQL 建库建表脚本（8张表）",
            "│   └── AI_RULES.md.md         # AI 编码规则与约束",
            "├── backend/                  # Spring Boot 后端",
            "│   └── src/main/java/com/leoyoung/backend/",
            "│       ├── common/            # Result、ResultCode、异常处理",
            "│       ├── config/            # MyBatis Plus、跨域、拦截器",
            "│       ├── interceptor/       # JWT 登录拦截器",
            "│       ├── util/              # JWT 工具类",
            "│       ├── entity/            # 数据库实体（8张表）",
            "│       ├── mapper/            # MyBatis Plus Mapper",
            "│       ├── dto/               # 请求/响应 DTO",
            "│       ├── service/           # 业务逻辑层",
            "│       └── controller/        # REST 控制器（9个）",
            "├── frontend-admin/            # 商家端 Vue 3 后台",
            "└── frontend-user/             # 用户端 UniApp 小程序",
          ].map(s => new Paragraph({ spacing: { after: 40 }, children: [new TextRun({ text: s, font: "Courier New", size: 18 })] })),

          // 4. 数据库设计
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("4. 数据库设计")] }),
          new Paragraph({ spacing: { after: 80 }, children: [new TextRun("数据库名：teacraft_db，字符集 utf8mb4，共 8 张核心业务表。")] }),
          new Table({
            width: { size: API_TABLE_WIDTH, type: WidthType.DXA },
            columnWidths: [1600, 1600, 6160],
            rows: [
              new TableRow({ children: [headerCell("表名", 1600), headerCell("实体类", 1600), headerCell("说明", 6160)] }),
              new TableRow({ children: [cell("user", 1600), cell("User", 1600), cell("C端用户：id, nickname, phone, password(MD5), avatar_url", 6160)] }),
              new TableRow({ children: [cell("admin", 1600), cell("Admin", 1600), cell("管理员：id, username, password(MD5)", 6160)] }),
              new TableRow({ children: [cell("category", 1600), cell("Category", 1600), cell("商品分类：7个固定分类（推荐/冰淇淋/咖啡/果茶/奶茶/冰沙/其他）", 6160)] }),
              new TableRow({ children: [cell("product", 1600), cell("Product", 1600), cell("商品：名称、描述、图片URL、价格、库存、上架状态", 6160)] }),
              new TableRow({ children: [cell("cart_item", 1600), cell("CartItem", 1600), cell("购物车：用户+商品+数量+规格（甜度/温度/杯型/加料）", 6160)] }),
              new TableRow({ children: [cell("address", 1600), cell("Address", 1600), cell("收货地址：收货人、手机号、详细地址、是否默认", 6160)] }),
              new TableRow({ children: [cell("order_info", 1600), cell("OrderInfo", 1600), cell("订单主表：订单号、用户、总价、用餐方式、状态", 6160)] }),
              new TableRow({ children: [cell("order_item", 1600), cell("OrderItem", 1600), cell("订单明细：商品快照（名称/价格/规格）、数量、小计", 6160)] }),
            ]
          }),

          // 5. API 接口清单
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("5. API 接口清单")] }),
          new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun("5.1 用户端接口")] }),
          new Table({
            width: { size: API_TABLE_WIDTH, type: WidthType.DXA },
            columnWidths: API_COLUMNS,
            rows: [
              new TableRow({ children: [headerCell("方法", 800), headerCell("路径", 2600), headerCell("认证", 1100), headerCell("说明", 2200), headerCell("返回", 2660)] }),
              ...apiRow("POST", "/api/user/register", "否", "用户注册", "Result<Void>"),
              ...apiRow("POST", "/api/user/login", "否", "用户登录", "Result<LoginResponse>"),
              ...apiRow("GET", "/api/categories", "是", "获取分类列表", "Result<List<Category>>"),
              ...apiRow("GET", "/api/products?categoryId=", "是", "按分类查商品", "Result<List<Product>>"),
              ...apiRow("GET", "/api/products/{id}", "是", "商品详情", "Result<Product>"),
              ...apiRow("POST", "/api/cart/add", "是", "加入购物车", "Result<Void>"),
              ...apiRow("GET", "/api/cart/list", "是", "查看购物车", "Result<List<CartItemResp>>"),
              ...apiRow("PUT", "/api/cart/{id}", "是", "修改数量", "Result<Void>"),
              ...apiRow("DELETE", "/api/cart/{id}", "是", "删除条目", "Result<Void>"),
              ...apiRow("GET", "/api/address/list", "是", "地址列表", "Result<List<Address>>"),
              ...apiRow("POST", "/api/address/save", "是", "新增/编辑地址", "Result<Void>"),
              ...apiRow("DELETE", "/api/address/{id}", "是", "删除地址", "Result<Void>"),
              ...apiRow("POST", "/api/order/create", "是", "创建订单", "Result<Void>"),
              ...apiRow("GET", "/api/order/list", "是", "订单列表", "Result<List<OrderInfo>>"),
              ...apiRow("GET", "/api/order/{id}", "是", "订单详情", "Result<OrderResponse>"),
            ]
          }),
          new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun("5.2 商家端接口")] }),
          new Table({
            width: { size: API_TABLE_WIDTH, type: WidthType.DXA },
            columnWidths: API_COLUMNS,
            rows: [
              new TableRow({ children: [headerCell("方法", 800), headerCell("路径", 2600), headerCell("认证", 1100), headerCell("说明", 2200), headerCell("返回", 2660)] }),
              ...apiRow("POST", "/api/admin/login", "否", "管理员登录", "Result<AdminLoginResponse>"),
              ...apiRow("GET", "/api/admin/products", "是", "商品列表(分页+搜索)", "Result<Page<Product>>"),
              ...apiRow("POST", "/api/admin/products", "是", "新增/编辑商品", "Result<Void>"),
              ...apiRow("DELETE", "/api/admin/products/{id}", "是", "删除商品", "Result<Void>"),
              ...apiRow("POST", "/api/admin/upload", "是", "上传商品图片", "Result<String>"),
              ...apiRow("GET", "/api/admin/orders/pending", "是", "待处理订单", "Result<List<OrderInfo>>"),
              ...apiRow("PUT", "/api/admin/orders/{id}/complete", "是", "完成订单", "Result<Void>"),
              ...apiRow("PUT", "/api/admin/orders/{id}/cancel", "是", "取消订单(恢复库存)", "Result<Void>"),
              ...apiRow("GET", "/api/admin/orders/history", "是", "历史订单", "Result<List<OrderInfo>>"),
            ]
          }),

          // 6. 功能清单
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("6. MVP 功能清单")] }),
          new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun("6.1 用户端（微信小程序）")] }),
          ...bulletItems([
            "登录/注册：手机号 + 密码 + JWT Token",
            "首页：轮播 Banner + 自提/外卖入口 + 品牌展示",
            "点餐：左侧固定分类 + 右侧商品列表，点击进入详情",
            "商品详情：大图 + 描述 + 规格选择（甜度/温度/杯型/加料）+ 立即购买/加入购物车",
            "购物车：浮动栏显示总价与数量 + 结算页可删除商品",
            "下单结算：切换自提/外卖 + 选择地址 + 实时计算总价",
            "订单列表：按时间排序 + 状态标签（制作中/已完成/已取消）",
            "订单详情：商品快照明细 + 再来一单",
            "地址管理：增删改查 + 默认地址切换",
            "个人中心：头像/昵称/手机号 + 退出登录",
          ]),
          new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun("6.2 商家端（Web 后台）")] }),
          ...bulletItems([
            "管理员登录：用户名 + 密码 + JWT Token",
            "库存管理：商品列表（分页+搜索）+ 新增/编辑弹窗 + 图片上传 + 删除",
            "订单管理：待处理订单列表 + 完成按钮 + 取消按钮（二次确认+恢复库存）",
            "历史订单：已完成/已取消订单查看",
          ]),

          // 7. 业务规则
          new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun("7. 核心业务规则")] }),
          ...bulletItems([
            "订单状态流转：下单 → making（制作中）→ finished（已完成）/ cancelled（已取消）",
            "库存扣减：下单时在同一事务中扣减库存，取消时恢复",
            "加入购物车校验：检查商品是否上架、库存是否充足",
            "购物车同规格合并：同商品+同规格再次加入时累加数量，而非新增条目",
            "订单明细快照：保存下单时的商品名称、价格、规格，不受后续商品修改影响",
            "下单清空购物车：订单创建成功后自动清空当前用户购物车",
            "推荐分类限制：最多 10 个商品",
            "支付模拟：点击确认下单即视为支付成功，不接入真实支付",
            "JWT 认证：登录签发，客户端保存，请求头携带，不存数据库，不实现 Refresh Token",
          ]),

          new Paragraph({ spacing: { before: 400 }, children: [] }),
          new Paragraph({ alignment: AlignmentType.CENTER,
            children: [new TextRun({ text: "--- 文档结束 ---", size: 20, color: "BDC3C7" })] }),
        ]
      }
    ]
  })

  const buffer = await Packer.toBuffer(doc)
  fs.writeFileSync('E:/teacraft-studio/docs/TeaCraft_Studio_项目文档.docx', buffer)
  console.log('Word 文档已生成: docs/TeaCraft_Studio_项目文档.docx')
}

function apiRow(method, path, auth, desc, returns) {
  return [
    new TableRow({ children: [
      cell(method, 800), cell(path, 2600), cell(auth, 1100), cell(desc, 2200), cell(returns, 2660)
    ]})
  ]
}

function bulletItems(items) {
  return items.map(text =>
    new Paragraph({
      numbering: { reference: "bullets", level: 0 },
      children: [new TextRun({ text, font: "Arial", size: 22 })]
    })
  )
}

main().catch(console.error)
