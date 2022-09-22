package com.empresa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.entity.Alumno;
import com.empresa.service.AlumnoService;

@RestController
@RequestMapping("/rest/alumno")
public class AlumnoController {

	@Autowired
	private AlumnoService service;

	@GetMapping
	@ResponseBody
	public ResponseEntity<List<Alumno>> listaAlumno(){
		List<Alumno> lista = service.listaAlumno();
		return ResponseEntity.ok(lista);
	}
	 
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> inserta(@Valid @RequestBody Alumno obj, Errors errors){
		HashMap<String, Object> salida = new HashMap<>();
		
		List<ObjectError> lstErrors =  errors.getAllErrors();
		List<String> lstMensajes = new ArrayList<String>();
		for (ObjectError objectError : lstErrors) {
			objectError.getDefaultMessage();
			lstMensajes.add(objectError.getDefaultMessage());
		}
		if (!CollectionUtils.isEmpty(lstMensajes)) {
			return ResponseEntity.ok(salida);
		}
		
		List<Alumno> lstAlumno = service.listaAlumnoPorDni(obj.getDni());
		if (!CollectionUtils.isEmpty(lstAlumno)) {
			salida.put("mensaje", "Ya existe un alumno con DNI ==> " + obj.getDni());
			return ResponseEntity.ok(salida);
		}
		lstAlumno = service.listaAlumnoPorCorreo(obj.getCorreo());
		if (!CollectionUtils.isEmpty(lstAlumno)) {
			salida.put("mensaje", "Ya existe un alumno con correo ==> " + obj.getCorreo());
			return ResponseEntity.ok(salida);
		}
		Alumno objSalida = service.insertaActualizaAlumno(obj);
		if (objSalida == null) {
			salida.put("mensaje", "Error en el registro");
		}else {
			salida.put("mensaje", "Se registró el alumno ==> " + objSalida.getIdAlumno());
		}
		return ResponseEntity.ok(salida);
	}
	
	@PutMapping
	@ResponseBody
	public ResponseEntity<?> actualiza(@RequestBody Alumno obj){
		HashMap<String, Object> salida = new HashMap<>();
		Optional<Alumno> optAlumno = service.buscaAlumno(obj.getIdAlumno());		
		if (optAlumno.isPresent()) {
			Alumno objSalida = service.insertaActualizaAlumno(obj);
			if (objSalida == null) {
				salida.put("mensaje", "Error al actualizar");
			}else {
				salida.put("mensaje", "Se actualizó el alumno ==> " + objSalida.getIdAlumno());
			}
		}else {
			salida.put("mensaje", "No existe el alumno de ID ==> " + obj.getIdAlumno());
		}
		return ResponseEntity.ok(salida);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> elimina(@PathVariable("id") int id){
		HashMap<String, Object> salida = new HashMap<>();
		Optional<Alumno> optAlumno = service.buscaAlumno(id);		
		if (optAlumno.isPresent()) {
			service.eliminaAlumno(id);
			salida.put("mensaje", "Se eliminó el alumno de ID ==> " + id);

		}else {
			salida.put("mensaje", "No existe el alumno de ID ==> " + id);
		}
		return ResponseEntity.ok(salida);
	}
	
	
}





