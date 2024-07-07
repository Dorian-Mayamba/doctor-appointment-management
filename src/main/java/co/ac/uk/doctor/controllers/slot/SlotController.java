package co.ac.uk.doctor.controllers.slot;


import co.ac.uk.doctor.entities.Doctor;
import co.ac.uk.doctor.entities.Slot;
import co.ac.uk.doctor.requests.slot.AddSlotRequest;
import co.ac.uk.doctor.requests.slot.UpdateSlotRequest;
import co.ac.uk.doctor.responses.slot.AddSlotResponse;
import co.ac.uk.doctor.responses.slot.GetSlotResponse;
import co.ac.uk.doctor.responses.slot.RemoveSlotResponse;
import co.ac.uk.doctor.responses.slot.UpdateSlotResponse;
import co.ac.uk.doctor.services.DoctorService;
import co.ac.uk.doctor.services.SlotService;
import co.ac.uk.doctor.utils.EntityToSerializerConverter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SlotController {
    private final SlotService slotService;
    private final DoctorService doctorService;

    @Autowired
    public SlotController(SlotService slotService, DoctorService doctorService){
        this.slotService = slotService;
        this.doctorService = doctorService;
    }

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<GetSlotResponse> getDoctorSlots(@PathVariable("doctorId") Long doctorId){
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        assert doctor != null : "Doctor cannot be null";
        GetSlotResponse response = GetSlotResponse
                .builder()
                .slots(EntityToSerializerConverter.toSlotSerializer(slotService.getDoctorSlots(doctor)))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/slots/{doctorId}/create")
    public ResponseEntity<AddSlotResponse> addDoctorSlot(@PathVariable("doctorId") Long doctorId,
                                                         @RequestBody AddSlotRequest addSlotRequest,
                                                         HttpServletRequest request){
        Doctor doctor = (Doctor) doctorService.loadUserById(doctorId);
        assert doctor != null : "Doctor cannot be null";
        Slot slot = slotService.addDoctorSlot(doctor,addSlotRequest.getMonth(),addSlotRequest
                .getDate(), addSlotRequest.getTime());
        assert slot != null : "Slot cannot be null";
        AddSlotResponse response = AddSlotResponse
                .builder()
                .message(String.format("The slot of date %s at %s has been saved",
                        slot.getDate(), slot.getTime()))
                .slot(EntityToSerializerConverter.toSlotSerializer(slot))
                .build();
        URI uri = URI.create(request.getRequestURI());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/slots/{slotId}/update")
    public ResponseEntity<UpdateSlotResponse> updateDoctorSlot(@PathVariable("slotId") Long slotId
    , @RequestBody UpdateSlotRequest updateSlotRequest){
        Slot slot = slotService.updateDoctorSlot(slotId,updateSlotRequest.getDate(),updateSlotRequest
                .getTime());
        assert slot != null : "Slot cannot be null";
        UpdateSlotResponse response = new UpdateSlotResponse();
        response.setMessage("Your changes have been saved");
        response.setSlot(EntityToSerializerConverter.toSlotSerializer(slot));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/slots/{slotId}/delete")
    public ResponseEntity<RemoveSlotResponse> removeDoctorSlot(@PathVariable("slotId")
                                                                   Long slotId){
        Slot slot = slotService.removeDoctorSlot(slotId);
        assert slot != null : "Slot cannot be null";
        RemoveSlotResponse response = RemoveSlotResponse
                .builder()
                .message(String.format("The slot of date %s at %s has been removed",
                        slot.getDate(), slot.getTime()))
                .build();
        return ResponseEntity.ok(response);
    }
}
