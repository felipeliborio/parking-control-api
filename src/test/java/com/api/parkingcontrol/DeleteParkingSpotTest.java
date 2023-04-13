package com.api.parkingcontrol;

import com.api.parkingcontrol.controllers.ParkingSpotController;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;

@Tag("ignore")
@ExtendWith(MockitoExtension.class)
public class DeleteParkingSpotTest {

    @Mock
    ParkingSpotService parkingSpotService;

    @InjectMocks
    ParkingSpotController parkingSpotController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(parkingSpotController).build();
    }

    @Disabled
    @Test
    void deleteParkingSpot_whenParkingSpotExists_shouldDeleteAndReturnNoContent() throws Exception {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setId(UUID.randomUUID());
        parkingSpotModel.setLicensePlateCar("ABC1233");
        parkingSpotModel.setApartment("123");
        parkingSpotModel.setBlock("A");
        parkingSpotModel.setParkingSpotNumber("1");
        parkingSpotModel.setBrandCar("brand");
        parkingSpotModel.setModelCar("car");
        parkingSpotModel.setColorCar("color");
        parkingSpotModel.setResponsibleName("responsible");


        when(parkingSpotService.save(any(ParkingSpotModel.class))).thenReturn(parkingSpotModel);

        mockMvc.perform(MockMvcRequestBuilders.delete("/parking-spot/{id}", parkingSpotModel.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(parkingSpotService, times(1)).delete(parkingSpotModel);
    }
}
