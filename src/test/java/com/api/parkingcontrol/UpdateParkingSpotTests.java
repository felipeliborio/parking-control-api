package com.api.parkingcontrol;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UpdateParkingSpotTests {

    @Mock
    ParkingSpotService parkingSpotService;

    @InjectMocks
    ParkingSpotController parkingSpotController;

    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ParkingSpotModel parkingSpotModel;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(parkingSpotController).build();

        parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setId(UUID.randomUUID());
        parkingSpotModel.setParkingSpotNumber("123");
        parkingSpotModel.setApartment("456");
        parkingSpotModel.setBlock("A");
        parkingSpotModel.setLicensePlateCar("ABC1234");
        parkingSpotModel.setBrandCar("brandone");
        parkingSpotModel.setModelCar("car");
        parkingSpotModel.setColorCar("color");
        parkingSpotModel.setResponsibleName("responsible");
    }

    @Test
    @DisplayName("Teste de atualização de vaga de estacionamento com dados completos")
    void testUpdateParkingSpot_ReturnsOk() throws Exception {
        Mockito.when(parkingSpotService.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(parkingSpotModel));

        ParkingSpotDto updatedParkingSpot = new ParkingSpotDto();
        updatedParkingSpot.setParkingSpotNumber("456");
        updatedParkingSpot.setApartment("789");
        updatedParkingSpot.setBlock("B");
        updatedParkingSpot.setLicensePlateCar("DEF5678");
        updatedParkingSpot.setBrandCar("brandoner");
        updatedParkingSpot.setModelCar("carer");
        updatedParkingSpot.setColorCar("colorer");
        updatedParkingSpot.setResponsibleName("responsibleer");

        BeanUtils.copyProperties(updatedParkingSpot, parkingSpotModel);
        Mockito.when(parkingSpotService.save(ArgumentMatchers.any(ParkingSpotModel.class)))
            .thenReturn(parkingSpotModel);

        ResponseEntity<Object> response = parkingSpotController.updateParkingSpot(parkingSpotModel.getId(), updatedParkingSpot);
        
        assertEquals(200, response.getStatusCodeValue());

        var rp = (ParkingSpotModel) response.getBody();

        assertArrayEquals(
            new String[] { "456", "789", "B", "DEF5678", "brandoner" }, 
            new String[]{rp.getParkingSpotNumber(), rp.getApartment(), rp.getBlock(), rp.getLicensePlateCar(), rp.getBrandCar()}
        );

        assertNotEquals("car", rp.getModelCar());
        assertNotEquals("color", rp.getColorCar());
        assertNotEquals("responsible", rp.getResponsibleName());
    }

    @Test
    @DisplayName("Teste de atualização de vaga de estacionamento com dados incompletos")
    void testUpdateParkingSpot_BadRequestWhenIncomplete() throws Exception {
        UUID id = UUID.randomUUID();

        ParkingSpotModel updatedParkingSpot = new ParkingSpotModel();
        updatedParkingSpot.setId(id);
        updatedParkingSpot.setParkingSpotNumber("456");
        updatedParkingSpot.setApartment("789");
        updatedParkingSpot.setBlock("B");
        updatedParkingSpot.setLicensePlateCar("DEF-5678");

        mockMvc.perform(put("/parking-spot/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedParkingSpot)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Teste de atualização de vaga de estacionamento com id inexistente")
    void testUpdateParkingSpot_ReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        ParkingSpotModel updatedParkingSpot = new ParkingSpotModel();
        updatedParkingSpot.setParkingSpotNumber("456");
        updatedParkingSpot.setApartment("789");
        updatedParkingSpot.setBlock("B");
        updatedParkingSpot.setLicensePlateCar("DEF5678");
        updatedParkingSpot.setBrandCar("brandoner");
        updatedParkingSpot.setModelCar("carer");
        updatedParkingSpot.setColorCar("colorer");
        updatedParkingSpot.setResponsibleName("responsibleer");

        mockMvc.perform(put("/parking-spot/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedParkingSpot)))
            .andExpect(status().isNotFound());
    }
}
