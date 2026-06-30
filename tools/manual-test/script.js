const PRODUCT_API = 'http://localhost:8070/products';
const INVENTORY_API = 'http://localhost:8090/inventories';

const getRandomElement = (arr) => arr[Math.floor(Math.random() * arr.length)];
const randomString = (len = 5) => Math.random().toString(36).substring(2, 2 + len).toUpperCase();

// Hàm tạo tên sản phẩm thời trang thực tế
function generateFashionName() {
    const types = ["Áo thun", "Áo sơ mi", "Quần Jean", "Quần Tây", "Chân váy", "Đầm", "Áo khoác", "Áo Hoodie", "Quần Short"];
    const materials = ["Cotton", "Denim", "Lụa Silk", "Vải Linen", "Kaki", "Len", "Poly", "Voan"];
    const styles = ["Slimfit", "Oversize", "Vintage", "Basic", "Hàn Quốc", "Streetwear", "Office", "Premium"];
    const colors = ["Đen", "Trắng", "Xanh Navy", "Xám Ghi", "Be", "Pastel", "Họa tiết"];

    const type = getRandomElement(types);
    const material = getRandomElement(materials);
    const style = getRandomElement(styles);
    const color = getRandomElement(colors);

    return `${type} ${material} ${style} - ${color} ${randomString(4)}`;
}

// Hàm tạo Inventory
async function createInventory(variantId) {
    const inventoryData = {
        variantId: variantId,
        quantity: Math.floor(Math.random() * 500) + 50 // Số lượng ngẫu nhiên từ 50-550
    };

    try {
        const response = await fetch(INVENTORY_API, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'X-User-Id': 'admin-system',
                'X-User-Roles': 'ADMIN'
            },
            body: JSON.stringify(inventoryData)
        });
        if (response.ok) console.log(`   -> Đã cập nhật kho cho: ${variantId}`);
    } catch (error) {
        console.error("Lỗi Inventory:", error);
    }
}

// Hàm tạo Product
async function createProduct() {
    const productName = generateFashionName();
    const payload = {
        name: productName,
        categoryId: "9d6211a9-8d03-49fe-97f4-f679e4fd1f43",
        brandId: "9449baf4-cc75-449e-a4a0-847a473fc277",
        options: ["Size", "Màu sắc"],
        variants: [{
            price: (Math.floor(Math.random() * 15) + 10) * 20000, // Giá từ 200k - 500k
            traits: [getRandomElement(["S", "M", "L", "XL"])],
            targets: [
                getRandomElement(["Male", "Female"]),
                getRandomElement(["Apple", "Inverted_triangle", "Hourglass", "Rectangle", "Pear"])
            ]
        }]
    };

    try {
        const response = await fetch(PRODUCT_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const data = await response.json();
        const newVariantId = data?.variants?.[0]?.id;

        if (newVariantId) {
            console.log(`Thành công: [${productName}] | ID: ${newVariantId}`);
            await createInventory(newVariantId);
        }
    } catch (error) {
        console.error('Lỗi Product:', error);
    }
}

// Vòng lặp chính
let count = 10000;
const timer = setInterval(async () => {
    if (count <= 0) {
        console.log("--- HOÀN THÀNH ---");
        clearInterval(timer);
        return;
    }
    
    await createProduct();
    count--;
}, 2000);