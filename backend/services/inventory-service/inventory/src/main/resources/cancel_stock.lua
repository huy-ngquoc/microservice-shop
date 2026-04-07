-- KEYS[1]: inventory:variant:{variantId}
-- ARGV[1]: số lượng muốn mua (amount)

-- 1. Lấy giá trị thô từ Redis
local quantity_raw = redis.call('HGET', KEYS[1], 'quantity')
local reserved_raw = redis.call('HGET', KEYS[1], 'reservedQuantity')
local amount = tonumber(ARGV[1])

-- 2. Kiểm tra nếu Key/Field không tồn tại (Chưa load từ DB lên Redis)
if not quantity_raw then
    return -1
end

-- 3. Chuyển đổi sang kiểu số và kiểm tra tính hợp lệ (Tránh lỗi compare with nil)
local quantity = tonumber(quantity_raw)
local reserved = tonumber(reserved_raw) or 0 -- Nếu reservedQuantity chưa có thì mặc định là 0

if quantity == nil then
    return -2 -- Lỗi dữ liệu không phải là số hợp lệ
end

-- 4. Kiểm tra xem tồn kho khả dụng (quantity) có đủ không
if reserved < amount then
    return 0 
end

-- 5. Thực hiện trừ quantity và cộng vào reservedQuantity
-- HINCRBY sẽ tự động xử lý phép cộng/trừ số nguyên
redis.call('HINCRBY', KEYS[1], 'quantity', amount)
redis.call('HINCRBY', KEYS[1], 'reservedQuantity', -amount)

-- 6. Trả về 1 nếu thành công
return 1