/**
 * 
 */
package com.ui.operation.crud;

import java.util.Date;

/**
 * 
 */
public class Etudiant {
	private static int nextId = 1; // Variable static pour incrémenter l'identifiant des etudiants ajoutés
    private int id;
    private String firstname;
    private String lastname;
    private Date birthday;


	/**
	 * 
	 */
// CONTRUCTEUR SUPER CLASS
	public Etudiant() {
		// TODO Auto-generated constructor stub
		this.id = nextId++; // Affectation de l'ID et incrémentation de nextId pour le prochain étudiant
	}
	
	  
    
/**
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param birthday
	 */
	public Etudiant(int id, String firstname, String lastname, Date birthday) {
		super();
		this.id = id; // Affectation de l'ID fourni
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		
		
        // S'assurer que nextId est mis à jour pour éviter les conflits d'ID
        if (id >= nextId) {
            nextId = id + 1;
        }else {
            nextId++; // Incrémente uniquement si l'ID fourni est plus petit que nextId
        }
	}


	
	/**
 * @return the nextId
 */
public static int getNextId() {
	return nextId;
}



/**
 * @param nextId the nextId to set
 */
public static void setNextId(int nextId) {
	Etudiant.nextId = nextId;
}



// **********************************************************************************************************
// **************************************__GETTERS & SETTERS__***************************************************
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


}
