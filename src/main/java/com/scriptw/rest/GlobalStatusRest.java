/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.rest;

import com.aspose.cells.Cells;
import com.aspose.cells.JsonLayoutOptions;
import com.aspose.cells.JsonUtility;
import com.aspose.cells.Workbook;
import com.scriptw.model.Destino;
import com.scriptw.model.Farmacias;
import com.scriptw.model.GlobalStatus;
import com.scriptw.service.DestinoService;
import com.scriptw.service.GlobalStatusService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author USRSIS0173
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api/globalstatus")
public class GlobalStatusRest {

    @Autowired
    private GlobalStatusService globalStatusService;
    
    @Autowired
    private DestinoService destinoService;
    
    @GetMapping("/consultar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalStatus> consultarporcodigo(GlobalStatus globalStatus) {

        var farmacia = globalStatusService.findByCodigo(globalStatus.getCodigo()).orElse(null);
        var time = new Date();
        farmacia.setUltmConexion(time);
        globalStatusService.save(farmacia);

        return ResponseEntity.ok(farmacia);

    }
    
    @GetMapping("/consultall")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<GlobalStatus>> consuAll(){
        
        return ResponseEntity.ok(globalStatusService.findAll());
    }
    

    @PostMapping("/generar")
    @PreAuthorize("hasRole('ADMIN')")
    public int respuesta(@RequestParam(value = "iddestino") int iddestino, int idfarm,@RequestBody String respuestajson) throws Exception {

        String json = respuestajson;
        
        String iddestinostr = Integer.toString(iddestino);


        Workbook workbook = new Workbook();
        Cells cells = workbook.getWorksheets().get(0).getCells();
        JsonLayoutOptions importOptions = new JsonLayoutOptions();
        importOptions.setConvertNumericOrDate(true);
        importOptions.setArrayAsTable(true);
        importOptions.setIgnoreArrayTitle(true);
        importOptions.setIgnoreObjectTitle(true);
        JsonUtility.importData(json, cells, 0, 0, importOptions);
        workbook.save("C:/angular/script_wizard/src/assets/data/" + iddestinostr + ".xlsx");
        
      
        globalStatusService.updateDstatus(0, iddestino);     
        
        Integer idfarmacia = idfarm;
        Farmacias farmacias = new Farmacias();
        farmacias.setIdfarmacia(idfarmacia);
        
        Optional<Destino> destinoe = destinoService.findFirstByIdFarmaciaAndStatus(farmacias, 1);
        
        if(destinoe.isPresent()){
            
            GlobalStatus globalStatus = new GlobalStatus();
            int codigo = destinoe.get().getIdFarmacia().getCodigo();
            
            globalStatus.setCodigo(codigo);
             
            globalStatus = globalStatusService.findByCodigo(globalStatus.getCodigo()).orElse(null);
            
            Integer iddestinop = destinoe.get().getIddestino();
            Destino destin = new Destino();
            destin.setIddestino(iddestinop);
            
            globalStatus.setIdDestino(destin);            
            
            globalStatusService.save(globalStatus);
            
        }
        else{
            globalStatusService.updateGstatus(0, iddestino);
        }      
        
        return iddestino;
    }
    
    @GetMapping("/errorejec")
    @PreAuthorize("hasRole('ADMIN')")
    public void errorEjec(Integer iddestino, int idfarm ) {
        
        globalStatusService.updateDstatus(2, iddestino);
        
        
        Integer idfarmacia = idfarm;
        Farmacias farmacias = new Farmacias();
        farmacias.setIdfarmacia(idfarmacia);
        
        Optional<Destino> destinoe = destinoService.findFirstByIdFarmaciaAndStatus(farmacias, 1);
        
        if(destinoe.isPresent()){
            
            GlobalStatus globalStatus = new GlobalStatus();
            
            int codigo = destinoe.get().getIdFarmacia().getCodigo();
            
            globalStatus.setCodigo(codigo);
 
            globalStatus = globalStatusService.findByCodigo(globalStatus.getCodigo()).orElse(null);

            Integer iddestinop = destinoe.get().getIddestino();
            Destino destin = new Destino();
            destin.setIddestino(iddestinop);
            globalStatus.setIdDestino(destin);
            globalStatusService.save(globalStatus);
            
        }
        else{
            globalStatusService.updateGstatus(0, iddestino);
        } 
    }
    
    
    @GetMapping("/test")
    public ResponseEntity testapi(){                
        String jsonEnString = "{\"estatus\": \"ok\"}";
        return ResponseEntity.ok(jsonEnString);
    }

}
