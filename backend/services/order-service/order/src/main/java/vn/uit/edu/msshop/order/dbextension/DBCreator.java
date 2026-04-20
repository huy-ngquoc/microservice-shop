package vn.uit.edu.msshop.order.dbextension;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.in.web.request.CreateInventoryRequest;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfo;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfoRepository;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;


//@Component
public class DBCreator {
    private final VariantInfoRepository variantInfoRepo;
    private final InventoryChecker inventoryCreator;

    public DBCreator(VariantInfoRepository variantInfoRepo, InventoryChecker inventoryCreator) {
        this.variantInfoRepo = variantInfoRepo;
        this.inventoryCreator = inventoryCreator;
    }

    
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        String idFilePath = "generated_variant_ids.txt";
        
        
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(idFilePath)))) {
            Random rand = new Random();

            for (int i = 0; i < 200; i++) {
                try {
                    List<VariantInfo> variantInfos = new ArrayList<>();
                    List<CreateInventoryRequest> requests = new ArrayList<>();

                    for (int j = 0; j < 500; j++) {
                        VariantInfo info = generateRandomVariantInfo(rand);
                        variantInfos.add(info);
                        requests.add(new CreateInventoryRequest(info.getVariantId(), 1000000));
                        
                        writer.println(info.getVariantId().toString());
                    }

                    variantInfoRepo.saveAll(variantInfos);
                    inventoryCreator.create(requests);
                    
                    
                    writer.flush(); 
                    System.out.println("Success, batch " + (i + 1) + "/200");
                } catch (Exception e) {
                    System.err.println("Error at batch " + (i + 1));
                    e.printStackTrace();
                }
            }
            System.out.println("--- DONE! Đã lưu 100,000 IDs vào file: " + idFilePath + " ---");
        } catch (Exception e) {
            System.err.println("Không thể mở file để ghi ID");
            e.printStackTrace();
        }
    }

    private VariantInfo generateRandomVariantInfo(Random rand) {
        return new VariantInfo(UUID.randomUUID(), UUID.randomUUID(), "", rand.nextInt(10000, 1000000), new ArrayList<>(), "");
    }
}