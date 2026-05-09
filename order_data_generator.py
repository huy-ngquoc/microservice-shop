import json
import random

# Danh sách dữ liệu bạn cung cấp
inventory_data = [
    {"variantId": "b906f290-6084-4282-b9b5-dd8850740a57", "stock": 330},
    {"variantId": "75067c37-48e8-4639-bc1b-39a0e0e014c5", "stock": 473},
    {"variantId": "b1c20993-748f-4554-a330-4af6d6f1192b", "stock": 207},
    {"variantId": "bf607c96-a6e9-46de-a4d3-21db848c297d", "stock": 224},
    {"variantId": "321d8fa1-d5ab-46be-b0bd-dc3f099a20a6", "stock": 369},
    {"variantId": "41203849-8f62-4fdc-be6a-04fa9d37170a", "stock": 500},
    {"variantId": "f9c15476-e704-49bf-92cc-bc0c00ce95de", "stock": 424},
    {"variantId": "b125d11d-59ec-419c-9e54-4338c7c7049c", "stock": 373},
    {"variantId": "032fe477-5c3b-4cc8-82e6-40ba5de9c5ab", "stock": 140},
    {"variantId": "8ce5c3e1-4a2a-461e-9b23-63f0612d922f", "stock": 70}
]

variant_ids = [item['variantId'] for item in inventory_data]
FILENAME = "order_details.csv"

def generate_order_line():
    # Chọn ngẫu nhiên từ 1 đến 3 item cho một đơn hàng
    num_items = random.randint(1, 3)
    selected_variants = random.sample(inventory_data, num_items)
    
    order_items = []
    for item in selected_variants:
        # 10% tỷ lệ đặt hàng vượt quá tồn kho để test lỗi hết hàng
        if random.random() < 0.1:
            qty = random.randint(1000, 2000)
        else:
            qty = random.randint(1, 5)
            
        order_items.append({
            "variantId": item['variantId'],
            "quantity": qty
        })
    
    # Format đúng chuẩn JSON string để JMeter đọc (bao trong dấu nháy kép)
    json_str = json.dumps(order_items)
    # Escape dấu nháy kép để CSV không bị lệch cột nếu cần
    escaped_json = json_str.replace('"', '""')
    return f'"{escaped_json}"'

# Ghi ra file 500 dòng
with open(FILENAME, "w", encoding="utf-8") as f:
    # Header cho JMeter dễ quản lý
    f.write("orderDetailJson\n")
    for _ in range(500):
        f.write(generate_order_line() + "\n")

print(f"Đã tạo xong file {FILENAME} với 500 dòng dữ liệu!")