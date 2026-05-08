import requests
import random
import string
import time
import os

# Cấu hình URL
PRODUCT_API = 'http://localhost:8070/products'
INVENTORY_API = 'http://localhost:8090/inventory/'
LOG_FILE = 'created_variant_ids.txt'

def random_string(length=4):
    letters = string.ascii_uppercase + string.digits
    return ''.join(random.choice(letters) for i in range(length))

def generate_fashion_name():
    types = ["Áo thun", "Áo sơ mi", "Quần Jean", "Quần Tây", "Chân váy", "Đầm", "Áo khoác", "Áo Hoodie", "Quần Short"]
    materials = ["Cotton", "Denim", "Lụa Silk", "Vải Linen", "Kaki", "Len", "Poly", "Voan"]
    styles = ["Slimfit", "Oversize", "Vintage", "Basic", "Hàn Quốc", "Streetwear", "Office", "Premium"]
    colors = ["Đen", "Trắng", "Xanh Navy", "Xám Ghi", "Be", "Pastel", "Họa tiết"]

    return f"{random.choice(types)} {random.choice(materials)} {random.choice(styles)} - {random.choice(colors)} {random_string()}"

def save_id_to_file(variant_id):
    with open(LOG_FILE, 'a') as f:
        f.write(f"{variant_id}\n")

def create_inventory(variant_id):
    if not variant_id:
        print("   [!] Lỗi: variant_id bị trống, không thể gọi Inventory")
        return False

    inventory_data = {
        "variantId": variant_id,
        "quantity": random.randint(50, 500)
    }
    
    headers = {
        'Content-Type': 'application/json',
        'X-User-Id': 'admin-system',
        'X-User-Roles': 'ADMIN'
    }

    try:
        response = requests.post(INVENTORY_API, json=inventory_data, headers=headers)
        
        if response.status_code in [200, 201]:
            print(f"   -> Đã tạo kho cho: {variant_id}")
            save_id_to_file(variant_id)
            return True
        else:
            # Dòng này sẽ cho bạn biết tại sao Inventory thất bại (400? 500?)
            print(f"   [!] Lỗi Inventory API: Code {response.status_code} - Message: {response.text}")
            return False
    except Exception as e:
        print(f"   [!] Lỗi kết nối Inventory: {e}")
        return False

def create_product():
    product_name = generate_fashion_name()
    product_options = ["Size", "Màu sắc"]
    trait_values = [
        random.choice(["S", "M", "L", "XL"]),
        random.choice(["Đen", "Trắng", "Xanh Navy"])
    ]

    payload = {
        "name": product_name,
        "categoryId": "9d6211a9-8d03-49fe-97f4-f679e4fd1f43",
        "brandId": "9449baf4-cc75-449e-a4a0-847a473fc277",
        "options": product_options,
        "variants": [{
            "price": random.randint(10, 25) * 20000,
            "traits": trait_values,
            "targets": ["Male", "Apple"] # Test nhanh với giá trị cố định
        }]
    }

    try:
        response = requests.post(PRODUCT_API, json=payload)
        if response.status_code in [200, 201]:
            data = response.json()
            
            # DEBUG: In thử data ra nếu bạn không thấy variant_id
            # print(f"DEBUG Response: {data}") 

            variants = data.get("variants", [])
            if variants and len(variants) > 0:
                variant_id = variants[0].get("id")
                print(f"Thành công: [{product_name}] - ID: {variant_id}")
                # Gọi hàm tạo inventory
                create_inventory(variant_id)
            else:
                print(f"   [!] Cảnh báo: Product tạo xong nhưng Response không có variants/id")
        else:
            print(f"Lỗi Product API: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Có lỗi xảy ra: {e}")
    product_name = generate_fashion_name()
    
    # Định nghĩa options trước
    product_options = ["Size", "Màu sắc"]
    
    # Tạo traits tương ứng với số lượng options
    # Phần tử 1 cho Size, phần tử 2 cho Màu sắc
    trait_values = [
        random.choice(["S", "M", "L", "XL", "XXL"]),
        random.choice(["Đen", "Trắng", "Xanh Navy", "Xám Ghi", "Đỏ Đô", "Vàng Cát"])
    ]

    payload = {
        "name": product_name,
        "categoryId": "9d6211a9-8d03-49fe-97f4-f679e4fd1f43",
        "brandId": "9449baf4-cc75-449e-a4a0-847a473fc277",
        "options": product_options,
        "variants": [
            {
                "price": random.randint(10, 25) * 20000,
                "traits": trait_values, # Số phần tử traits luôn bằng số phần tử options
                "targets": [
                    random.choice(["Male", "Female"]),
                    random.choice(["Apple", "Inverted_triangle", "Hourglass", "Rectangle", "Pear"])
                ]
            }
        ]
    }

    try:
        response = requests.post(PRODUCT_API, json=payload)
        if response.status_code in [200, 201]:
            data = response.json()
            variants = data.get("variants", [])
            if variants:
                variant_id = variants[0].get("id")
                print(f"Thành công: [{product_name}] - Traits: {trait_values}")
                create_inventory(variant_id)
        else:
            print(f"Lỗi Product API: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Có lỗi xảy ra: {e}")

if __name__ == "__main__":
    count = 10000
    print(f"Bắt đầu seeding {count} sản phẩm...")
    
    if os.path.exists(LOG_FILE):
        print(f"Lưu ý: Tiếp tục ghi vào file {LOG_FILE}")

    for i in range(count):
        create_product()
        # Để tốc độ nhanh hơn, mình để sleep thấp
        time.sleep(0.1) 
        
    print(f"--- HOÀN THÀNH. ID lưu tại: {LOG_FILE} ---")