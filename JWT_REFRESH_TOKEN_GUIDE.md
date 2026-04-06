# JWT Guide (Don Gian Cho Nguoi Moi)

## 1. JWT la gi?

- JWT la token server ky de xac thuc user.
- Client gui token qua header:

```http
Authorization: Bearer <access_token>
```

## 2. Access token

- Access token:
  - Han ngan (mac dinh 15 phut).
  - Dung de goi API can dang nhap.

## 3. Luong auth hien tai trong du an

1. Dang ky (`POST /api/auth/register`)

- Tao user moi.
- Tao cart mac dinh cho user.

2. Dang nhap (`POST /api/auth/login`)

- Kiem tra email/password.
- Tra ve `accessToken`.

3. Goi API can dang nhap

- Gui `Authorization: Bearer <accessToken>`.

4. Access token het han

- API se tra `401 Unauthorized`.
- Ban dang nhap lai de lay access token moi.

## 4. Danh sach endpoint auth

- `POST /api/auth/register`
- `POST /api/auth/login`

## 5. cURL test nhanh

### Dang ky

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"fullName\":\"Nguyen Van A\",\"email\":\"a@example.com\",\"password\":\"123456\",\"phone\":\"0900000000\"}"
```

### Dang nhap

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"a@example.com\",\"password\":\"123456\"}"
```

### Goi API can login

```bash
curl http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <access_token>"
```

## 6. Luu y

- Nho doi `app.jwt.secret` cho moi truong production.
- Luon dung HTTPS o production.
