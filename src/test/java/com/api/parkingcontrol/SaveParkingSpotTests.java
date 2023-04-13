package com.api.parkingcontrol;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ParkingSpotController.class)
@ExtendWith(MockitoExtension.class)
public class SaveParkingSpotTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingSpotService parkingSpotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar criado quando salvar um novo estacionamento")
    public void saveParkingSpot_shouldReturnCreated() throws Exception {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setLicensePlateCar("ABC1233");
        parkingSpotModel.setApartment("123");
        parkingSpotModel.setBlock("A");
        parkingSpotModel.setParkingSpotNumber("1");
        parkingSpotModel.setBrandCar("brand");
        parkingSpotModel.setModelCar("car");
        parkingSpotModel.setColorCar("color");
        parkingSpotModel.setResponsibleName("responsible");


        when(parkingSpotService.save(any(ParkingSpotModel.class))).thenReturn(parkingSpotModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/parking-spot")
                .content(objectMapper.writeValueAsString(parkingSpotModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @DisplayName("Deve retornar bad request quando salvar um novo estacionamento com placa de carro inválida")
    public void saveParkingSpot_shouldReturnBadRequest() throws Exception {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setLicensePlateCar("ABC1233");
        parkingSpotModel.setApartment("123");
        parkingSpotModel.setBlock("A");
        parkingSpotModel.setParkingSpotNumber("1");
        parkingSpotModel.setBrandCar("brand");
        parkingSpotModel.setModelCar("car");
        parkingSpotModel.setResponsibleName("responsible");


        when(parkingSpotService.save(any(ParkingSpotModel.class))).thenReturn(parkingSpotModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/parking-spot")
                .content(objectMapper.writeValueAsString(parkingSpotModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
