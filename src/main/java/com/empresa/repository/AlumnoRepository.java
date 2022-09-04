package com.empresa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.empresa.entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

	
	//JPQL:Jakarta Persistence Query Language
	//Es un SQL aplicado a las clases
	//NO se usa * para obetener todas las columnas
	//Si existe error en la sintaxis no levanta el proyecto
	//Las entidades son siempre en mayúscula
	
	@Query("select x from Alumno x where x.dni = ?1")
	public List<Alumno> listaPorDni(String dni);

	@Query("select x from Alumno x where x.correo = ?1")
	public List<Alumno> listaPorCorreo(String correo);
}
