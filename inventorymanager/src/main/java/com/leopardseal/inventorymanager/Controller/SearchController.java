package com.leopardseal.inventorymanager.controller;


@RestController
public class SearchController{
    
    @Autowired
    private AuthService authService;

    @Autowired
    private ItemsRepository itemsRepository;
    
    @Autowired
    private BoxesRepository boxesRepository;
    
    @Autowired
    private LocationsRepository locationsRepository;

    @GetMapping("/search/{org_id}/{query}")
    public ResponseEntity<SearchResponse> search(@PathVariable("org_id") Long orgId, @PathVariable("query") String query){
        if(authService.checkAuth(orgId)){
            
            List<Items> items = itemsRepository.findAllItemsByQuery(orgId, query);
            List<BoxesResponse> boxes = boxesRepository.findAllItemsByQuery(orgId, query);
            List<Locations> locations = locationsRepository.findAllItemsByQuery(orgId, query);

            SearchResponse response = new SearchResponse(items.size(), boxes.size(), locations.size(), items, boxes, locations);

            return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
        }else{
            return ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
