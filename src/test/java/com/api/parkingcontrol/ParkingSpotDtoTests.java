package com.api.parkingcontrol;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.parkingcontrol.dtos.ParkingSpotDto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.DisplayName;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDtoTests {

    @DisplayName("Teste de criação de um objeto ParkingSpotDto")
    @ParameterizedTest
    @ValueSource(strings = {"ABC1234", "XYZ9876"})
    void testCreateParkingSpotDto(String licensePlate) {
        assumeTrue(licensePlate.matches("[A-Z]{3}[0-9]{4}"));
        String parkingSpotNumber = "1";
        String apartment = "101";
        String block = "A";
        
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setLicensePlateCar(licensePlate);
        parkingSpotDto.setParkingSpotNumber(parkingSpotNumber);
        parkingSpotDto.setApartment(apartment);
        parkingSpotDto.setBlock(block);

        assertTrue(licensePlate.equals(parkingSpotDto.getLicensePlateCar()));
        assertTrue(parkingSpotNumber.equals(parkingSpotDto.getParkingSpotNumber()));
        assertTrue(apartment.equals(parkingSpotDto.getApartment()));
        assertTrue(block.equals(parkingSpotDto.getBlock()));
    }

    @DisplayName("Deve lançar uma exceção quando a placa do carro for inválida")
    @ParameterizedTest
    @ValueSource(strings = {"abc12345", "1234ABCD4"})
    void testIsInvalidLicensePlate(String licensePlate) {
        assumeFalse(licensePlate.matches("[A-Z]{3}[0-9]{4}"));
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        assertThrows(Exception.class, () -> parkingSpotDto.setLicensePlateCar(licensePlate));
    }
}
