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
import com.leopardseal.inventorymanager.entity.Boxes;
import com.leopardseal.inventorymanager.entity.dto.BoxesResponse;
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
    public ResponseEntity<Iterable<BoxesResponse>> getBoxes(@PathVariable("org_id") Long orgId){
        if(authService.checkAuth(orgId)){
            List<BoxesResponse> boxes = boxesRepository.findAllBoxesByOrgId(orgId);
            return new ResponseEntity<Iterable<BoxesResponse>>(boxes, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_box/{box_id}")
    public ResponseEntity<BoxesResponse> getBoxById(@PathVariable("box_id") Long boxId) {
        Optional<BoxesResponse> box = boxesRepository.findBoxById(boxId);
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
    public ResponseEntity<Iterable<BoxesResponse>> getBoxesByLocationId(@PathVariable("org_id") Long orgId, @PathVariable("location_id") Long locationId){
        if(authService.checkAuth(orgId)){
            List<BoxesResponse> boxes = boxesRepository.findAllBoxesByOrgIdAndLocationId(orgId, locationId);
            return new ResponseEntity<Iterable<BoxesResponse>>(boxes, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @PostMapping("/update_box/{img_changed}")
    public ResponseEntity<SaveResponse> updateBox(@RequestBody BoxesResponse box, @PathVariable("img_changed") Boolean imageChanged) {
        if(authService.checkAuth(box.getOrgId())){
            try {
                String imgUrl = null;
                Long sizeId = boxSizesRepository.findBySize(box.getSize()).get().getId();
                Boxes updatedBox = new Boxes(box.getId(), box.getName(), box.getOrgId(), box.getBarcode(), box.getLocationId(), sizeId, box.getImageUrl());
                boxesRepository.save(updatedBox);
                if(imageChanged){
                    if(updatedBox.getImageUrl() == null){
                        imgUrl = azureBlobService.uploadImageWithSas(updatedBox.getId(), "box", 1L);
                    }else{
                        Long vers = azureBlobService.extractVersion(updatedBox.getImageUrl()) + 1L;
                        imgUrl = azureBlobService.uploadImageWithSas(updatedBox.getId(), "box", vers);
                    }
                    updatedBox.setImageUrl(imgUrl.split("\\?")[0]);
                    updatedBox = boxesRepository.save(updatedBox);
                }
                
                if (updatedBox != null && updatedBox.getId() != null) {
                    return new ResponseEntity<SaveResponse>(new SaveResponse(updatedBox.getId(), imgUrl), HttpStatus.OK);
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
