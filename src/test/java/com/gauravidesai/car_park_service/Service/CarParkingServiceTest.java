package com.gauravidesai.car_park_service.Service;

import com.gauravidesai.car_park_service.dto.BillResponse;
import com.gauravidesai.car_park_service.dto.OccupiedParkingResponse;
import com.gauravidesai.car_park_service.exception.ParkingFullException;
import com.gauravidesai.car_park_service.exception.VehicleNotFoundException;
import com.gauravidesai.car_park_service.model.ParkingRecord;
import com.gauravidesai.car_park_service.model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CarParkingServiceTest {

    @Autowired
    private CarParkingService carParkingService;

    @BeforeEach
    void resetParkingLot() {
        carParkingService.clearAll();
    }

    @Test
    void shouldMapVehicleTypeCorrectly() {
        assertEquals(VehicleType.SMALL, VehicleType.fromCode(1));
    }

    @Test
    void shouldThrowExceptionForInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> VehicleType.fromCode(4));
    }

    @Test
    void shouldAllocateFirstSpace() {
        ParkingRecord record = carParkingService.parkVehicle("AB1CD2", 1);
        assertEquals(1, record.spaceNumber());
    }

    @Test
    void shouldAllocateNextSpace() {
        carParkingService.parkVehicle("AB1CD2", 1);
        ParkingRecord record2 = carParkingService.parkVehicle("EF3GH4", 2);
        assertEquals(2, record2.spaceNumber());
    }

    @Test
    void shouldThrowParkingFullException() {
        int capacity = carParkingService.getCapacity();
        for (int i = 0; i < capacity; i++) {
            carParkingService.parkVehicle("CAR" + i, 1);
        }
        assertThrows(ParkingFullException.class,
                () -> carParkingService.parkVehicle("OVERFLOW", 1));
    }

    @Test
    void shouldNotAllowDuplicateParking() {
        carParkingService.parkVehicle("AB1CD2", 1);
        assertThrows(IllegalArgumentException.class,
                () -> carParkingService.parkVehicle("AB1CD2", 1));
    }

    @Test
    void shouldExitVehicleAndFreeSpace() {
        int capacity = carParkingService.getCapacity();
        carParkingService.parkVehicle("AB1CD2", 1);

        BillResponse bill = carParkingService.exitVehicle("AB1CD2");

        assertEquals("AB1CD2", bill.vehicleReg());
        assertEquals(capacity, carParkingService.getStatus().availableSpaces());
    }

    @Test
    void shouldThrowVehicleNotFoundException() {
        assertThrows(VehicleNotFoundException.class,
                () -> carParkingService.exitVehicle("UNKNOWN"));
    }

    @Test
    void shouldCalculateBillCorrectly() throws Exception {
        carParkingService.parkVehicle("AB1CD2", 2);

        Field field = carParkingService.getClass().getDeclaredField("parkedVehicles");
        field.setAccessible(true);
        Map<String, ParkingRecord> map = (Map<String, ParkingRecord>) field.get(carParkingService);

        map.put("AB1CD2", new ParkingRecord("AB1CD2", 1,
                LocalDateTime.now().minusMinutes(10), VehicleType.MEDIUM));

        BillResponse bill = carParkingService.exitVehicle("AB1CD2");

        assertEquals(4.0, bill.vehicleCharge(), 0.1);
    }

    @Test
    void shouldReturnCorrectStatus() {
        int capacity = carParkingService.getCapacity();
        carParkingService.parkVehicle("AB1CD2", 1);

        OccupiedParkingResponse status = carParkingService.getStatus();

        assertEquals(capacity - 1, status.availableSpaces());
        assertEquals(1, status.occupiedSpaces());
    }

    @Test
    void shouldReuseFreedSpaceFirst() {
        ParkingRecord r1 = carParkingService.parkVehicle("A", 1);
        carParkingService.parkVehicle("B", 1);

        carParkingService.exitVehicle("A");

        ParkingRecord r3 = carParkingService.parkVehicle("C", 1);

        assertEquals(r1.spaceNumber(), r3.spaceNumber());
    }
}