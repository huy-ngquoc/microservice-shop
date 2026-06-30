import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class JmeterDatagenerator {
    public static void main(String[] args) {
        String idInputFile = "generated_variant_ids.txt"; // File chứa 100k ID
        String csvOutputFile = "order_data.csv";         // File payload nạp vào JMeter
        int totalRows = 20000;                           // Số lượng đơn hàng cần tạo cho JMeter
        Random random = new Random();

        try {
            // 1. Đọc tất cả variantId vào bộ nhớ
            System.out.println("Đang nạp ID từ file " + idInputFile + "...");
            List<String> variantIds = Files.readAllLines(Paths.get(idInputFile));
            
            if (variantIds.isEmpty()) {
                System.err.println("Lỗi: File ID trống rỗng!");
                return;
            }
            System.out.println("Đã nạp " + variantIds.size() + " ID.");

            // 2. Mở file CSV để ghi payload
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvOutputFile))) {
                writer.write("payload");
                writer.newLine();

                for (int i = 0; i < totalRows; i++) {
                    // Giả lập mỗi đơn hàng có từ 1 đến 3 món đồ
                    int itemsCount = random.nextInt(3) + 1;
                    StringBuilder json = new StringBuilder("[");

                    for (int j = 0; j < itemsCount; j++) {
                        // Lấy ngẫu nhiên 1 ID từ danh sách đã nạp
                        String randomId = variantIds.get(random.nextInt(variantIds.size()));
                        int quantity = random.nextInt(5) + 1;

                        // LƯU Ý: Vì variantId giờ là UUID (String), nên cần bọc trong dấu ngoặc kép ""
                        json.append(String.format("{\"variantId\": \"%s\", \"quantity\": %d}", randomId, quantity));
                        
                        if (j < itemsCount - 1) json.append(",");
                    }
                    json.append("]");

                    // Escape JSON để JMeter CSV Data Set đọc đúng
                    // Format: "[[{""variantId"": ""uuid"", ""quantity"": 1}]]"
                    String escapedJson = "\"" + json.toString().replace("\"", "\"\"") + "\"";
                    
                    writer.write(escapedJson);
                    writer.newLine();

                    if (i % 5000 == 0 && i > 0) {
                        System.out.println("Đã tạo được " + i + " đơn hàng...");
                    }
                }
                System.out.println("--- THÀNH CÔNG! ---");
                System.out.println("File nạp JMeter: " + csvOutputFile);
            }

        } catch (IOException e) {
            System.err.println("Lỗi đọc/ghi file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}