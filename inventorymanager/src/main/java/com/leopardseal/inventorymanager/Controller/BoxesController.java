package com.leopardseal.inventorymanager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;
import com.leopardseal.inventorymanager.service.AzureBlobService;
import com.leopardseal.inventorymanager.entity.Box;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class BoxesController{  

    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private AuthService authService;

    @Autowired
    private BoxesRepository boxesRepository;


    @Autowired
    BoxSizesRepository boxSizesRepository;

    @GetMapping("/get_boxes/{org_id}")
    public ResponseEntity<Iterable<Box>> getBoxes(@PathVariable("org_id") Long orgId){
        if(authService.checkAuth(orgId)){
            List<Box> boxes = boxesRepository.findAllByOrgId(orgId);
            return new ResponseEntity<Iterable<Box>>(boxes, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_box/{box_id}")
    public ResponseEntity<Box> getBoxById(@PathVariable("box_id") Long boxId) {
        Optional<Box> box = boxesRepository.findById(boxId);
        if (box.get() != null) {
            if(authService.checkAuth(box.get().getOrgId())){
                return ResponseEntity.ok(box.get());
            }else{
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get_boxes_by_location_id/{org_id}/{location_id}")
    public ResponseEntity<Iterable<Box>> getBoxesByLocationId(@PathVariable("org_id") Long orgId, @PathVariable("location_id") Long locationId){
        if(authService.checkAuth(orgId)){
            List<Box> boxes = boxesRepository.findAllByOrgIdAndLocationId(orgId, locationId);
            return new ResponseEntity<Iterable<Box>>(boxes, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @PostMapping("/update_box/{img_changed}")
    public ResponseEntity<SaveResponse> updateBox(@RequestBody Box box, @PathVariable("img_changed") Boolean imageChanged) {
        if(authService.checkAuth(box.getOrgId())){
            try {
                String imgUrl = null;
                boxesRepository.save(box);
                if(imageChanged){
                    if(box.getImageUrl() == null){
                        imgUrl = azureBlobService.uploadImageWithSas(box.getId(), "box", 1L);
                    }else{
                        Long vers = azureBlobService.extractVersion(box.getImageUrl()) + 1L;
                        imgUrl = azureBlobService.uploadImageWithSas(box.getId(), "box", vers);
                    }
                    box.setImageUrl(imgUrl.split("\\?")[0]);
                    box = boxesRepository.save(box);
                }
                
                if (box != null && box.getId() != null) {
                    return new ResponseEntity<SaveResponse>(new SaveResponse(box.getId(), imgUrl), HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}
