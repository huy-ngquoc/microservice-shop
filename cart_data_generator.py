import json
import random

# Cấu hình tên file
INPUT_FILE = "created_variant_ids.txt"    # File chứa danh sách ID (mỗi dòng 1 ID)
OUTPUT_FILE = "cart_details.csv"
TOTAL_ROWS = 500                # Số dòng muốn tạo ra cho JMeter

def load_variant_ids(file_path):
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            # Đọc từng dòng, bỏ khoảng trắng và dòng trống
            ids = [line.strip() for line in f if line.strip()]
        return ids
    except FileNotFoundError:
        print(f"Lỗi: Không tìm thấy file {file_path}")
        return []

def generate_order_line(variant_ids):
    # Chọn ngẫu nhiên từ 1 đến 3 item cho một đơn hàng
    # Nếu danh sách ID ít hơn 3, lấy tối đa số lượng đang có
    num_items = 1
    selected_variants = random.sample(variant_ids, num_items)
    
    order_items = []
    for vid in selected_variants:
        # Điền số lượng ngẫu nhiên vừa phải (từ 1 đến 10)
        qty = random.randint(1, 10)
            
        order_items.append({
            "variantId": vid,
            "amount": qty
        })
    
    # Chuyển sang JSON string
    json_str = json.dumps(order_items)
    # Escape dấu nháy kép để đúng định dạng CSV (JMeter sẽ đọc được nguyên cụm JSON này)
    escaped_json = json_str.replace('"', '""')
    return f'"{escaped_json}"'

# --- Thực thi chính ---
ids = load_variant_ids(INPUT_FILE)

if not ids:
    print("Không có dữ liệu ID để xử lý. Vui lòng kiểm tra file inventory.txt")
else:
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        # Header để JMeter gọi biến ${orderDetailJson}
        f.write("orderDetailJson\n")
        
        for _ in range(TOTAL_ROWS):
            f.write(generate_order_line(ids) + "\n")

    print(f"Thành công! Đã đọc {len(ids)} ID từ {INPUT_FILE}")
    print(f"Đã tạo xong file {OUTPUT_FILE} với {TOTAL_ROWS} dòng dữ liệu!")