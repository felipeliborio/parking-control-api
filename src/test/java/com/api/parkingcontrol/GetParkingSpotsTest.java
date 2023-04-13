package com.api.parkingcontrol;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Tag("Listagens")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class GetParkingSpotsTest {

    private UUID spotTwoId = UUID.fromString("4b4fb3d3-e86c-4de1-b36d-c9d3a3a0a3c7");

    @Mock
    private ParkingSpotService parkingSpotService;

    private ParkingSpotController parkingSpotController;
    List<ParkingSpotModel> parkingSpots = new ArrayList<>();

    @BeforeAll
    void setUpAll() {
        ParkingSpotModel parkingSpot1 = new ParkingSpotModel();
        parkingSpot1.setId(UUID.fromString("8573d670-08b4-4db3-af2d-f88b2a9e10c6"));
        parkingSpot1.setApartment("A101");
        parkingSpot1.setBlock("Block A");
        parkingSpot1.setLicensePlateCar("XYZ1234");
        parkingSpot1.setParkingSpotNumber("A101-01");

        parkingSpots.add(parkingSpot1);

        ParkingSpotModel parkingSpot2 = new ParkingSpotModel();
        parkingSpot2.setId(spotTwoId);
        parkingSpot2.setApartment("A201");
        parkingSpot2.setBlock("Block A");
        parkingSpot2.setLicensePlateCar("ABC5678");
        parkingSpot2.setParkingSpotNumber("A201-01");
        
        parkingSpots.add(parkingSpot2);
    }

    @BeforeEach
    void setUp() {
        parkingSpotController = new ParkingSpotController(parkingSpotService);
    }

    @Test
    @DisplayName("Deve retornar todos os estacionamentos cadastrados.")
    void testGetAllParkingSpots() {

        Pageable pageable = Pageable.unpaged();
        Page<ParkingSpotModel> parkingSpotPage = new PageImpl<>(parkingSpots);

        when(parkingSpotService.findAll(pageable)).thenReturn(parkingSpotPage);

        ResponseEntity<Page<ParkingSpotModel>> response = parkingSpotController.getAllParkingSpots(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parkingSpots, response.getBody().getContent());
        assertEquals(parkingSpots.size(), response.getBody().getContent().size());
    }

    @Test
    @DisplayName("Deve retornar o estacionamento quando encontrado.")
    void getOneParkingSpot_ReturnsParkingSpot_WhenFound() {
        ParkingSpotModel expectedParkingSpot = parkingSpots.get(0);
        when(parkingSpotService.findById(spotTwoId))
                .thenReturn(Optional.of(expectedParkingSpot));

        ResponseEntity<Object> responseEntity =
                parkingSpotController.getOneParkingSpot(spotTwoId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedParkingSpot, responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve retornar 404 quando não encontrar o estacionamento.")
    void getOneParkingSpot_Returns404_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(parkingSpotService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = parkingSpotController.getOneParkingSpot(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @DisplayName("Buscar um com repetição e parâmetros")
    @RepeatedTest(value = 100, name = "{displayName} - {currentRepetition}/{totalRepetitions}")
    @ParameterizedTest
    @MethodSource("provideLicensePlates")
    void testFindAllWithRepetition(RepetitionInfo repetitionInfo,String licensePlate) {
        
        int expectedSize = parkingSpots.size();
        Page<ParkingSpotModel> parkingSpotPage = new PageImpl<>(parkingSpots);
        
        Pageable pageable = Pageable.unpaged();
        when(parkingSpotService.findAll(pageable)).thenReturn(parkingSpotPage);

        ResponseEntity<Page<ParkingSpotModel>> response = parkingSpotController.getAllParkingSpots(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parkingSpots, response.getBody().getContent());
        assertEquals(parkingSpots.size(), response.getBody().getContent().size());

        assertNotNull(parkingSpotPage);
        assertEquals(expectedSize, parkingSpotPage.getSize());
        assertEquals(100, repetitionInfo.getTotalRepetitions());
    }

    private Collection<String> provideLicensePlates() {
        return Arrays.asList(
            parkingSpots.get(0).getLicensePlateCar(),
            parkingSpots.get(1).getLicensePlateCar()
        );
    }
}
