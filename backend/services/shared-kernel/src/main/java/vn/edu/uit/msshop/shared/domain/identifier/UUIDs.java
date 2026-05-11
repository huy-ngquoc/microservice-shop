package vn.edu.uit.msshop.shared.domain.identifier;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

public final class UUIDs {
    private static final TimeBasedEpochGenerator GENERATOR = Generators.timeBasedEpochGenerator();

    private UUIDs() {
    }

    public static UUID newId() {
        return GENERATOR.generate();
    }
}
