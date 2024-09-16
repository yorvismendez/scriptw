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
@CrossOrigin(origins = "*", maxAge = 3600)//seguridad
@RequestMapping(path = "/api/globalstatus")//simplifica la ruta de los end points hijos
public class GlobalStatusRest {

    @Autowired
    private GlobalStatusService globalStatusService;
    
    @Autowired
    private DestinoService destinoService;
    
    @GetMapping("/consultar")//consulta el status por farmacia
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalStatus> consultarporcodigo(GlobalStatus globalStatus) {

        var farmacia = globalStatusService.findByCodigo(globalStatus.getCodigo()).orElse(null);

        //fecha y hora sin formato
        var time = new Date();

        farmacia.setUltmConexion(time);
        //actualizamos la fecha 
        globalStatusService.save(farmacia);

        return ResponseEntity.ok(farmacia);

    }
    
    @GetMapping("/consultall")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<GlobalStatus>> consuAll(){
        
        return ResponseEntity.ok(globalStatusService.findAll());
    }
    

    @PostMapping("/generar")//end ponit que recibe la respuesta de la consulta y crea el excel
    @PreAuthorize("hasRole('ADMIN')")
    public int respuesta(@RequestParam(value = "iddestino") int iddestino, int idfarm,@RequestBody String respuestajson) throws Exception {

        //combertimos el json en string ya que asi lo trabaja mejor la libreria de apose
        String json = respuestajson;
        
        //combertimos la varible iddestino que es int a string para poder usarla en el nombre del archivo
        String iddestinostr = Integer.toString(iddestino);

        //este codigo crea un archivo json con la variable del json
        // Crear un objeto FileWriter con el nombre del archivo

        /*FileWriter file = new FileWriter("data/" + iddestinostr + ".json");

        // Escribir el contenido de la variable json en el archivo
        file.write(data);

        // Cerrar el objeto FileWriter
        file.close();*/
        // Lea el archivo JSON de origen
        //String str = new String(Files.readAllBytes(Paths.get("data/" + iddestinostr + ".json")));

        // Crear objeto de libro de trabajo vacío
        Workbook workbook = new Workbook();
        // Obtenga las celdas de la primera hoja de trabajo llamando al método get
        Cells cells = workbook.getWorksheets().get(0).getCells();
        // Establezca JsonLayoutOptions que represente las opciones del tipo de diseño json.
        JsonLayoutOptions importOptions = new JsonLayoutOptions();
        // Invoque este método setConvertNumericOrDate para establecer un valor que indique si la cadena en json se convierte en numérica o en fecha.
        importOptions.setConvertNumericOrDate(true);
        // Llame al método setArrayAsTable y establezca su valor si desea procesar Array como tabla.
        importOptions.setArrayAsTable(true);
        // El método setIgnoreArrayTitle indica si ignorar el título si la matriz es una propiedad del objeto.
        importOptions.setIgnoreArrayTitle(true);
        // Invoque el método setIgnoreObjectTitle para ignorar el título si el objeto es una propiedad del objeto.
        importOptions.setIgnoreObjectTitle(true);
        // Llame a este método importData para convertir JSON en una cadena
        JsonUtility.importData(json, cells, 0, 0, importOptions);     
        // Guarde el libro de trabajo usando las opciones de guardado.
        //desarrollo
        workbook.save("C:/angular/script_wizard/src/assets/data/" + iddestinostr + ".xlsx");
        
        //produccion COBECA IIS
        //workbook.save("P:/assets/data/" + iddestinostr + ".xlsx");
        
                
        //marcamos el destino como ejecutado
        globalStatusService.updateDstatus(0, iddestino);     
        
        //Extraemos el idfarmacia que recibe el endpoint
        Integer idfarmacia = idfarm;
        //creamos un objeto de tipo farmacia
        Farmacias farmacias = new Farmacias();
        //le aplicamoe el id al objeto
        farmacias.setIdfarmacia(idfarmacia);
        
        //consultamos el prime registro con el id farmacia y en esatus pendiente, esto para validar si hay alguna otro destino pendiente por ejecutar 
        Optional<Destino> destinoe = destinoService.findFirstByIdFarmaciaAndStatus(farmacias, 1);
        
        //validamos si hay algun otro destino (ejecucion) pendiente
        if(destinoe.isPresent()){//si hay algo mas pendiente
            
            GlobalStatus globalStatus = new GlobalStatus();
            
            //estraemos el codigo de la farmacia
            int codigo = destinoe.get().getIdFarmacia().getCodigo();
            
            //le aplicamos el codigo
            globalStatus.setCodigo(codigo);
            
            //consultamos ese global estatus para obtener el objeto 
            globalStatus = globalStatusService.findByCodigo(globalStatus.getCodigo()).orElse(null);
            
            
            //creamos la variable integer con el iddestino pendiente
            Integer iddestinop = destinoe.get().getIddestino();
            //creamos un objeto de tipo destino para aplicarle el id pendiente
            Destino destin = new Destino();
            //le agregamos el iddespito pendiente al objeto que acabamos de crear
            destin.setIddestino(iddestinop);
            
            //le aplicamos el iddestino del destino pendiente por ejecutar
            globalStatus.setIdDestino(destin);            
            
            //actualizamos el iddestino al que esta pendiente
            globalStatusService.save(globalStatus);
            
        }
        else{//no hay nada mas pendiente
            globalStatusService.updateGstatus(0, iddestino);
        }      
        
        return iddestino;
    }
    
    @GetMapping("/errorejec")//marca como error la ejecucion
    @PreAuthorize("hasRole('ADMIN')")
    public void errorEjec(Integer iddestino, int idfarm ) {
        
        globalStatusService.updateDstatus(2, iddestino);
        
        //Extraemos el idfarmacia que recibe el endpoint
        Integer idfarmacia = idfarm;
        //creamos un objeto de tipo farmacia
        Farmacias farmacias = new Farmacias();
        //le aplicamoe el id al objeto
        farmacias.setIdfarmacia(idfarmacia);
        
        //consultamos el prime registro con el id farmacia y en esatus pendiente, esto para validar si hay alguna otro destino pendiente por ejecutar 
        Optional<Destino> destinoe = destinoService.findFirstByIdFarmaciaAndStatus(farmacias, 1);
        
        //validamos si hay algun otro destino (ejecucion) pendiente
        if(destinoe.isPresent()){//si hay algo mas pendiente
            
            GlobalStatus globalStatus = new GlobalStatus();
            
            //estraemos el codigo de la farmacia
            int codigo = destinoe.get().getIdFarmacia().getCodigo();
            
            //le aplicamos el codigo
            globalStatus.setCodigo(codigo);
            
            //consultamos ese global estatus para obtener el objeto 
            globalStatus = globalStatusService.findByCodigo(globalStatus.getCodigo()).orElse(null);
            
            
            //creamos la variable integer con el iddestino pendiente
            Integer iddestinop = destinoe.get().getIddestino();
            //creamos un objeto de tipo destino para aplicarle el id pendiente
            Destino destin = new Destino();
            //le agregamos el iddespito pendiente al objeto que acabamos de crear
            destin.setIddestino(iddestinop);
            
            //le aplicamos el iddestino del destino pendiente por ejecutar
            globalStatus.setIdDestino(destin);            
            
            //actualizamos el iddestino al que esta pendiente
            globalStatusService.save(globalStatus);
            
        }
        else{//no hay nada mas pendiente
            globalStatusService.updateGstatus(0, iddestino);
        } 
    }
    
    
    //endpoint para validar si esta arriba el api
    @GetMapping("/test")
    public ResponseEntity testapi(){                
        String jsonEnString = "{\"estatus\": \"ok\"}";
        return ResponseEntity.ok(jsonEnString);
    }

}
