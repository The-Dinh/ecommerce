# Checkout Reading Guide

Doc nay giup ban doc code checkout theo dung thu tu, tu de den kho.

## Thu tu de doc
1. `src/main/java/com/ecommerce/controller/OrderController.java`
- Xem endpoint `POST /api/orders/me` nhan request checkout.

2. `src/main/java/com/ecommerce/dto/CheckoutRequest.java`
- Day la du lieu dau vao khi checkout (dia chi giao hang, phuong thuc thanh toan).

3. `src/main/java/com/ecommerce/service/OrderService.java`
- Xem contract ham `createMyOrder`.

4. `src/main/java/com/ecommerce/service/impl/OrderServiceImpl.java`
- Day la file quan trong nhat.
- Theo doi ham `createMyOrder` theo thu tu:
  - Lay user va cart
  - Validate cart khong rong
  - Validate ton kho
  - Tao `Order` + danh sach `OrderItem`
  - Tru ton kho `Product`
  - Save order
  - Clear cart
- Tat ca nam trong `@Transactional` de dam bao tinh toan ven.

5. `src/main/java/com/ecommerce/entity/Order.java`
- Cau truc bang `orders` va quan he voi `order_items`.

6. `src/main/java/com/ecommerce/entity/OrderItem.java`
- Cau truc tung dong san pham trong don hang.

7. `src/main/java/com/ecommerce/entity/Cart.java`
8. `src/main/java/com/ecommerce/entity/CartItem.java`
- Hieu gio hang duoc doc va clear the nao.

9. `src/main/java/com/ecommerce/entity/Product.java`
- Hieu truong `stockQuantity`, `status` duoc cap nhat khi checkout.

10. `src/main/java/com/ecommerce/repository/CartRepository.java`
11. `src/main/java/com/ecommerce/repository/CartItemRepository.java`
12. `src/main/java/com/ecommerce/repository/OrderRepository.java`
- Hieu cac thao tac DB ma service dang goi.

## Luong checkout tom tat (1 transaction)
1. User goi `POST /api/orders/me`.
2. Service lay cart cua user.
3. Kiem tra tung item co du ton kho va san pham dang `ACTIVE`.
4. Tao order + order items.
5. Tru ton kho tung product.
6. Luu order.
7. Xoa tat ca cart items (clear cart).
8. Tra ve `OrderDTO`.

Neu bat ky buoc nao loi, transaction rollback va DB khong bi cap nhat nua chung.
